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
    if (!confirm('Möchten Sie diese Buchung wirklich stornieren?')) {
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
    alert('Buchung erfolgreich storniert')
    
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
      <h1 class="text-3xl font-bold">Account</h1>
      <UButton
        color="red"
        variant="soft"
        @click="handleLogout"
        size="lg"
      >
        Logout
      </UButton>
    </div>

    <!-- User Info -->
    <div class="border border-gray-700 rounded-lg p-6 mb-8">
      <h2 class="text-2xl font-semibold mb-4">Profile Information</h2>
      <div class="space-y-2">
        <p><span class="text-gray-400">Username:</span> {{ authStore.user?.username }}</p>
        <p><span class="text-gray-400">User ID:</span> {{ authStore.user?.userId }}</p>
      </div>
    </div>

    <!-- Bookings -->
    <div class="border border-gray-700 rounded-lg p-6">
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
          class="border border-gray-700 rounded-lg p-4"
        >
          <div class="flex justify-between items-start">
            <div>
              <!-- Car Informationen anzeigen, wenn verfügbar -->
              <template v-if="getCarForBooking(booking)">
                <h3 class="text-xl font-semibold">
                  {{ getCarForBooking(booking).make }} {{ getCarForBooking(booking).model }}
                </h3>
              </template>
              <template v-else>
                <h3 class="text-xl font-semibold">
                  Car ID: {{ booking.carId }}
                </h3>
              </template>
              <p class="text-gray-400">
                {{ formatDate(booking.startDate) }} - {{ formatDate(booking.endDate) }}
              </p>
            </div>
            <span class="text-blue-400 font-bold">
              {{ booking.currency }} {{ booking.totalCost }}
            </span>
          </div>
          
          <!-- Car Details wenn vorhanden -->
          <div v-if="getCarForBooking(booking)" class="mt-2 bg-gray-800/50 p-2 rounded-md">
            <p class="text-sm text-gray-300">
              <span class="text-gray-400">Fuel Type:</span> {{ getCarForBooking(booking).fuelType  }} |
              <span class="text-gray-400">Transmission:</span> {{ getCarForBooking(booking).fuel ? 'Automatic' : 'Manual' }} | 
              <span class="text-gray-400">Pickup Location:</span> {{ getCarForBooking(booking).pickupLocation  }}
            </p>
          </div>
          
          <!-- Cancel Button -->
          <div class="mt-4 flex justify-end">
            <UButton 
              color="red"
              size="sm"
              @click="cancelBooking(booking.id)"
              v-if="new Date(booking.startDate) > new Date()"
            >
              Cancel Booking
            </UButton>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>