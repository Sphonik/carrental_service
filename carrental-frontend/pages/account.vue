<script setup>
import { ref, onMounted } from 'vue'
import { useAuth } from '~/composables/useAuth'
import { useRouter } from 'vue-router'
import { useApi } from '~/composables/useApi'
import { useCurrency } from '~/composables/useCurrency'

const router = useRouter()
const { user, logout } = useAuth()
const { endpoints } = useApi()
const { getCurrentSymbol } = useCurrency()

const bookings = ref([])
const loading = ref(true)
const error = ref(null)

// Fetch user bookings
const fetchBookings = async () => {
  try {
    const response = await fetch(endpoints.bookings.list(), {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('auth_token')}`
      }
    })
    if (!response.ok) throw new Error('Failed to load bookings')
    bookings.value = await response.json()
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
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
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

onMounted(() => {
  if (!user.value) {
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
        <p><span class="text-gray-400">Email:</span> {{ user?.email }}</p>
        <p><span class="text-gray-400">Role:</span> {{ user?.role }}</p>
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
              <h3 class="text-xl font-semibold">
                {{ booking.car.make }} {{ booking.car.model }}
              </h3>
              <p class="text-gray-400">
                {{ formatDate(booking.startDate) }} - {{ formatDate(booking.endDate) }}
              </p>
            </div>
            <span class="text-blue-400 font-bold">
              {{ getCurrentSymbol() }}{{ booking.totalPrice }}
            </span>
          </div>
          <div class="mt-2 flex gap-2 text-sm">
            <span class="text-blue-400">{{ booking.car.fuelType }}</span>
            <span class="text-blue-400">{{ booking.car.automatic ? 'Automatic' : 'Manual' }}</span>
            <span class="text-blue-400">{{ booking.car.color }}</span>
          </div>
          <p class="mt-2 text-gray-400">
            Pickup Location: {{ booking.car.pickupLocation }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>