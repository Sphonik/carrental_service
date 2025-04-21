<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Available Vehicles</h1>
    
    <!-- Filter Section -->
    <div class="border border-gray-700 rounded-lg p-6 mb-8">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <USelect
          v-model="filters.location"
          :items="locationOptions"
          placeholder="Pickup Location"
          size="lg"
        />
        <UInput
          v-model="filters.maxPrice"
          type="number"
          placeholder="Maximum Price per Day"
          size="lg"
        />
        <UButton
          color="gray"
          variant="soft"
          @click="resetFilters"
          size="lg"
        >
          Reset Filters
        </UButton>
      </div>
    </div>

    <!-- Cars Map -->
    <div v-if="filteredCars.length > 0" :key="filteredCars">
      <GoogleMap
          :cars = "filteredCars"/>
    </div>

    <!-- Cars Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="car in filteredCars" 
           :key="car.id" 
           class="border border-gray-700 rounded-lg p-6"
      >
        <h3 class="text-xl font-semibold mb-2">{{ car.make }} {{ car.model }}</h3>
        <div class="flex justify-between items-center mb-4">
          <span class="text-blue-400">{{ car.year }}</span>
          <span class="text-blue-400 font-bold">
            {{ getCurrentSymbol() }}{{ car.pricePerDay }}/day
          </span>
        </div>
        <div class="flex flex-wrap gap-2 mb-4">
          <span class="text-blue-400">{{ car.fuelType }}</span>
          <span class="text-blue-400">{{ car.automatic ? 'Automatic' : 'Manual' }}</span>
          <span class="text-blue-400">{{ car.color }}</span>
        </div>
        <div class="flex justify-between items-center">
          <span class="text-blue-400">{{ car.pickupLocation }}</span>
          <UButton 
            color="blue"
            @click="openBookingModal(car)"
            size="lg"
          >
            Book Now
          </UButton>
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

const router = useRouter()
const route = useRoute()
const { endpoints } = useApi()

const cars = ref([])
const filters = ref({
  location: route.query.location || '',
  maxPrice: route.query.maxPrice || null,
  startDate: route.query.from || null,
  endDate: route.query.to || null,
  currency: route.query.currency || 'USD' // Default currency is USD
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
    
    return matchesLocation && matchesPrice
  })
})

// Reset filters
const resetFilters = () => {
  filters.value = {
    location: '',
    maxPrice: null,
    startDate: null,
    endDate: null,
    currency: 'USD'
  }
  router.push({ query: {} })
}

// API functions
const fetchCars = async () => {
  try {
    const response = await fetch(
      endpoints.cars.list({
        from: filters.value.startDate || '',
        to: filters.value.endDate || '',
        currency: filters.value.currency || 'USD'
      })
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