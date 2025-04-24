<script setup>
import { ref, onMounted, computed } from 'vue'
import { useAuth } from '~/composables/useAuth'
import { useRouter } from 'vue-router'
import { useApi } from '~/composables/useApi'
import { useCurrency } from '~/composables/useCurrency'
import { useAuthStore } from '~/stores/auth'

definePageMeta({
  middleware: ['auth'] // Sicherstellen, dass nur eingeloggte User diese Seite sehen
})

const router = useRouter()
const { logout } = useAuth()
const authStore = useAuthStore()
const { endpoints, getAuthHeaders } = useApi()
const { getCurrentSymbol } = useCurrency()

const bookings = ref([])
const cars = ref([]) // Zusätzlicher State für Autos
const loading = ref(true)
const error = ref(null)

// Fetch user bookings
const fetchBookings = async () => {
  try {
    // Überprüfen, ob der Benutzer eingeloggt ist und eine userId hat
    if (!authStore.user?.userId) {
      throw new Error('User is not authenticated')
    }
    
    const response = await fetch(endpoints.bookings.list(), {
      headers: {
        ...getAuthHeaders(),
        'Content-Type': 'application/json'
      }
    })
    
    if (!response.ok) throw new Error('Failed to load bookings')
    bookings.value = await response.json()
    
    // Optionaler Schritt: Fahrzeugdetails abrufen, wenn sie benötigt werden
    if (bookings.value.length > 0) {
      await fetchBookingCars()
    }
  } catch (err) {
    console.error('Error loading bookings:', err)
    error.value = err.message
  } finally {
    loading.value = false
  }
}

// Autos für die Buchungen abrufen (optional)
const fetchBookingCars = async () => {
  try {
    const carIds = [...new Set(bookings.value.map(booking => booking.carId))]
    const carPromises = carIds.map(id => 
      fetch(`${endpoints.cars.getById(id.toString())}`, {
        headers: getAuthHeaders()
      })
      .then(res => res.ok ? res.json() : null)
    )
    
    const carData = await Promise.all(carPromises)
    cars.value = carData.filter(Boolean)
  } catch (err) {
    console.error("Error fetching car details", err)
  }
}

// Finde Auto zu einer Buchung
const getCarForBooking = (booking) => {
  return cars.value.find(car => car.id === booking.carId)
}

// Cancel a booking
const cancelBooking = async (bookingId) => {
  try {
    if (!confirm('Click OK if you are sure you want to cancel this booking')) {
      return
    }
    
    const response = await fetch(endpoints.bookings.cancel(bookingId.toString()), {
      method: 'DELETE',
      headers: {
        ...getAuthHeaders()
      }
    })

    if (!response.ok) throw new Error('Failed to cancel booking')
    
    // Erfolgsmeldung
    alert('This booking has been cancelled')
    
    // Buchungen neu laden
    await fetchBookings()
  } catch (err) {
    console.error('Error cancelling booking:', err)
    error.value = err.message
    alert(`Fehler bei der Stornierung: ${err.message}`)
  }
}

// Handle logout
const handleLogout = async () => {
  try {
    await logout()
    router.push('/login')
  } catch (err) {
    error.value = err.message
  }
}

// Format date
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('de-AT', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

onMounted(() => {
  if (!authStore.user) {
    router.push('/login')
    return
  }
  fetchBookings()
})
</script>

<template>
  <div class="container mx-auto px-4 py-8">
    <div class="flex justify-between items-center mb-8">
      <h1 class="text-3xl font-bold">My Account</h1>
      <UButton
        color="error"
        variant="soft"
        @click="handleLogout"
        size="lg"
        icon="material-symbols:logout"
        class="cursor-pointer"
      >
        Logout
      </UButton>
    </div>

    <!-- User Info -->
    <div class="border border border-gray-100 dark:border-gray-600 shadow-md rounded-lg p-6 mb-8">
      <h2 class="text-2xl font-semibold mb-4">Hello, {{ authStore.user?.firstName }}</h2>
      <div class="space-y-2">
        <p><span class="text-gray-400">Username:</span> {{ authStore.user?.username }}</p>
        <p><span class="text-gray-400">User ID:</span> {{ authStore.user?.userId }}</p>
      </div>
    </div>

    <!-- Bookings -->
    <div class="border border border-gray-100 dark:border-gray-600 shadow-md rounded-lg p-6">
      <h2 class="text-2xl font-semibold mb-4">Your Bookings</h2>

      <div v-if="loading" class="text-center py-8">
        <ULoading />
      </div>

      <div v-else-if="error" class="border-l-4 border-red-500 p-4">
        <div class="flex">
          <div class="flex-shrink-0">
            <i class="i-heroicons-exclamation-circle text-red-500"></i>
          </div>
          <div class="ml-3">
            <p class="text-red-500">{{ error }}</p>
          </div>
        </div>
      </div>

      <div v-else-if="bookings.length === 0" class="text-center py-8">
        <p class="text-gray-400">No bookings found</p>
      </div>

      <div v-else class="space-y-4">
        <div 
          v-for="booking in bookings" 
          :key="booking.id"
          class="rounded-lg p-4 bg-gray-100 dark:bg-zinc-800 shadow"
        >
          <div class="flex justify-between items-start">
            <div>
              <!-- Car Informationen anzeigen, wenn verfügbar -->
              <template v-if="getCarForBooking(booking)">
                <h3 class="text-xl font-semibold">
                  {{ getCarForBooking(booking).year }} {{ getCarForBooking(booking).make }} {{ getCarForBooking(booking).model }}
                </h3>
              </template>
              <template v-else>
                <h3 class="text-xl font-semibold">
                  Car ID: {{ booking.carId }}
                </h3>
              </template>
              <p class="text-gray-500 dark:text-gray-400">
                From {{ formatDate(booking.startDate) }} to {{ formatDate(booking.endDate) }}
              </p>
            </div>
              <span class="text-green-600 dark:text-green-400 font-bold text-right mb-4">
              {{ booking.currency }} {{ booking.totalCost }}
            </span>
          </div>
          
          <!-- Car Details wenn vorhanden -->
          <div v-if="getCarForBooking(booking)" class="mt-2 rounded-md">
            <p class="text-sm text-gray-600 dark:text-gray-200">
              <span class="text-gray-500 dark:text-gray-400">Fuel Type:</span> {{ getCarForBooking(booking).fuelType.charAt(0) + getCarForBooking(booking).fuelType.slice(1).toLowerCase() }} |
              <span class="text-gray-500 dark:text-gray-400">Transmission:</span> {{ getCarForBooking(booking).fuel ? 'Automatic' : 'Manual' }} |
              <span class="text-gray-500 dark:text-gray-400">Pickup Location:</span> {{ getCarForBooking(booking).pickupLocation  }}
            </p>
          </div>
          <UButton
              variant="soft"
              color="error"
              size="sm"
              @click="cancelBooking(booking.id)"
              v-if="new Date(booking.startDate) > new Date()"
              icon="material-symbols:cancel"
              class="cursor-pointer mt-4"
          >
            Cancel Booking
          </UButton>
          <UBadge
              variant="soft"
              class="mt-4"
              v-if="new Date(booking.startDate) <= new Date() && new Date(booking.endDate) >= new Date()">
            Happening Now
          </UBadge>
        </div>
      </div>
    </div>
  </div>
</template>