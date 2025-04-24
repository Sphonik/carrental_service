<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Available Vehicles</h1>
    
    <!-- Filter Section -->
    <div class="border border-gray-100 dark:border-gray-600 rounded-lg p-6 mb-8 shadow-md">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-5">
        <div>
          <p class="pb-2">Start Date</p>
          <UInput
              v-model="filters.startDate"
              type="date"
              label="Start Date"
              required
              @change="fetchCars()"
              :min="new Date().toISOString().split('T')[0]"
              :max="filters.endDate"
              class="w-full"
              size="lg"
          />
        </div>
        <div>
          <p class="pb-2">End Date</p>
          <UInput
              v-model="filters.endDate"
              type="date"
              label="End Date"
              required
              @change="fetchCars()"
              :min="filters.startDate || new Date().toISOString().split('T')[0]"
              class="w-full"
              size="lg"
          />
        </div>
      </div>
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-5">
        <USelect
          v-model="filters.location"
          :items="locationOptions"
          placeholder="Pickup Location"
          size="lg"
        />
        <USelect
            v-model="filters.transmission"
            :items="['Manual', 'Automatic']"
            placeholder="Transmission"
            size="lg"
        />
        <USelect
            v-model="filters.fuel"
            :items="['Gas', 'Hybrid']"
            placeholder="Fuel Type"
            size="lg"
        />
        <UInput
          v-model="filters.maxPrice"
          type="number"
          placeholder="Maximum Price per Day"
          size="lg"
        />
      </div>
      <UButton
          variant="outline"
          @click="resetFilters"
          size="lg"
          class="cursor-pointer mr-2"
      >
        Reset Filters
      </UButton>
      <UButton
          variant="soft"
          to="/"
          size="lg"
          class="cursor-pointer"
      >
        New Search
      </UButton>
    </div>

    <div v-if="!carsLoaded && (filters.startDate && filters.endDate)" class="flex items-center">
      <div class="inline-block h-4 w-4 animate-spin rounded-full border-3 border-solid border-current border-e-transparent align-[-0.125em] text-surface motion-reduce:animate-[spin_1.5s_linear_infinite] text-blue-500 dark:text-white mr-3"
           role="status"></div>
      <p class="italic text-gray-500 dark:text-gray-300">Searching for available cars...</p>
    </div>
    <UAlert v-if="carsLoaded && filteredCars.length == 0 && (filters.startDate && filters.endDate)"
            icon="material-symbols:search"
            title="No available cars found"
            description="Adjust filters or search different dates"
            variant="subtle">
    </UAlert>
    <UAlert v-if="!filters.startDate || !filters.endDate"
            icon="material-symbols:search"
            title="Search for cars"
            description="Enter dates above to search available cars"
            variant="subtle">
    </UAlert>

    <!-- Cars Map -->
    <div v-if="carsLoaded && filteredCars.length > 0" :key="filteredCars">
      <GoogleMap
          :cars = "filteredCars"/>
    </div>

    <!-- Cars Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="car in filteredCars" 
           :key="car.id" 
           class="border border-gray-200 dark:border-gray-600 rounded-lg p-6"
      >
        <h3 class="text-xl font-semibold mb-2">{{ car.year }} {{ car.make }} {{ car.model }}</h3>
        <div class="flex justify-between items-center mb-4">
          <div class="mb-4 text-gray-500/75 dark:text-white/65">
            <span>{{ car.fuelType.charAt(0) + car.fuelType.slice(1).toLowerCase() }}</span> |
            <span>{{ car.automatic ? 'Automatic' : 'Manual' }} </span> |
            <span>{{ car.color }}</span>
          </div>
          <span class="text-green-700 dark:text-green-400 font-bold">
            {{ getCurrentSymbol() }}{{ car.pricePerDay }}/day
          </span>
        </div>
        <div class="flex justify-between items-center">
          <span><strong>Pickup Location:</strong><br>{{ car.pickupLocation }}</span>
          <UButton
              v-if="filters.startDate && filters.endDate"
            @click="openBookingModal(car)"
            size="lg"
              class="cursor-pointer"
          >
            Book Now
          </UButton>
          <p v-else class="italic text-gray-500/75 dark:text-white/75 text-sm">Select dates to book</p>
        </div>
      </div>
    </div>

    <!-- Booking Modal -->
    <CarBookingModal
      v-if="selectedCar"
      :isOpen="isModalOpen"
      :car="selectedCar"
      :initial-dates="{
        startDate: route.query.from,
        endDate: route.query.to
      }"
      @close="closeBookingModal"
      @booking-success="fetchCars"
    />
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import { ref, computed, onMounted, watch } from 'vue'
import { useApi } from '~/composables/useApi'
import CarBookingModal from '~/components/CarBookingModal.vue'
import { useCurrency } from '~/composables/useCurrency'

definePageMeta({
  middleware: ['auth']
})

const router = useRouter()
const route = useRoute()
const { endpoints, getAuthHeaders } = useApi()
const carsLoaded = ref(false)

const cars = ref([])
const filters = ref({
  location: route.query.location || '',
  maxPrice: route.query.maxPrice || null,
  startDate: route.query.from || null,
  endDate: route.query.to || null,
  currency: route.query.currency || 'USD', // Default currency is USD
  transmission: route.query.transmission || '',
  fuel: route.query.fuel || '',
})

const { getCurrentSymbol, setCurrentCurrency } = useCurrency()

// Currency update handler
const updateCurrency = (currency) => {
  setCurrentCurrency(currency)
}

// Location options computation
const locationOptions = computed(() => {
  if (!cars.value.length) return []
  
  return [...new Set(cars.value.map(car => car.pickupLocation))]
    .filter(Boolean)
    .sort()
    .map(location => ({
      label: location,
      value: location
    }))
})

// Filter cars
const filteredCars = computed(() => {
  return cars.value.filter(car => {

    // Location Filter
    const matchesLocation = !filters.value.location || 
      car.pickupLocation === filters.value.location
    
    // Price Filter
    const maxPrice = filters.value.maxPrice ? Number(filters.value.maxPrice) : null
    const matchesPrice = !maxPrice || car.pricePerDay <= maxPrice

    // Transmission filter
    const transmission = (filters.value.transmission === "Automatic") ? true : false;
    const matchesTransmission = !filters.value.transmission || car.automatic === transmission;

    // Fuel filter
    const matchesFuel = !filters.value.fuel || car.fuelType === filters.value.fuel.toUpperCase();

    carsLoaded.value = true;
    return matchesLocation && matchesPrice && matchesTransmission && matchesFuel;
  })
})

// Reset filters (keeping dates)
const resetFilters = () => {
  filters.value = {
    location: '',
    maxPrice: null,
    startDate: filters.value.startDate,
    endDate: filters.value.endDate,
    currency: route.query.currency
  }
  fetchCars()
}

// API functions
const fetchCars = async () => {
  try {
    const response = await fetch(
      endpoints.cars.list({
        from: filters.value.startDate || '',
        to: filters.value.endDate || '',
        currency: filters.value.currency || 'USD'
      }),
      {
        headers: {
          ...getAuthHeaders(),
          'Content-Type': 'application/json'
        }
      }
    )
    if (!response.ok) throw new Error('Failed to load cars')
    const data = await response.json()
    cars.value = data
  } catch (error) {
    console.error('API Error:', error)
    cars.value = []
  }
}

const bookCar = async (car) => {
  try {
    const response = await fetch(endpoints.cars.book(car.id), {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    })
    if (!response.ok) throw new Error('Booking failed')
    await fetchCars()
  } catch (error) {
    console.error('Booking error:', error)
  }
}

// Modal functions
const selectedCar = ref(null)
const isModalOpen = ref(false)

const openBookingModal = (car) => {
  selectedCar.value = car
  isModalOpen.value = true
}

const closeBookingModal = () => {
  selectedCar.value = null
  isModalOpen.value = false
}

// Initial load
onMounted(() => {
  fetchCars()
})

// Watch query parameters
watch(() => route.query, (newQuery) => {
  filters.value = {
    ...filters.value,
    ...newQuery
  }
}, { deep: true })
</script>