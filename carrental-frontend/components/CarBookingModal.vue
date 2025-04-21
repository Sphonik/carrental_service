<template>
  <div v-if="isOpen" class="fixed inset-0 bg-white/75 dark:bg-black/75 flex items-center justify-center z-50">
    <div class="border border-gray-300 dark:border-gray-700 rounded-lg p-6 w-full max-w-lg bg-white dark:bg-black shadow-md">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold">Book Vehicle</h2>
        <UButton
          icon="i-heroicons-x-mark"
          color="gray"
          variant="ghost"
          @click="closeModal"
        />
      </div>

      <div class="mb-6 space-y-2">
        <div class="flex justify-between">
          <span class="text-gray-500/75 dark:text-white/65">Vehicle:</span>
          <span>{{ car.year }} {{ car.make }} {{ car.model }}</span>
        </div>
        <div class="flex justify-between">
          <span class="text-gray-500/75 dark:text-white/65">Price per day:</span>
          <span class="text-green-700 dark:text-green-400">{{ getCurrentSymbol() }}{{ car.pricePerDay }}</span>
        </div>
        
        <!-- Date display when both dates are selected -->
        <template v-if="booking.startDate && booking.endDate">
          <div class="flex justify-between">
            <span class="text-gray-500/75 dark:text-white/65">Start date:</span>
            <span>{{ formatDate(booking.startDate) }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-500/75 dark:text-white/65">End date:</span>
            <span>{{ formatDate(booking.endDate) }}</span>
          </div>
          <div v-if="totalPrice" class="flex justify-between border-t border-gray-300 dark:border-gray-700 pt-2 mt-2">
            <span class="text-gray-500/75 dark:text-white/65">Total price:</span>
            <span class="text-green-700 dark:text-green-400 font-bold">{{ getCurrentSymbol() }}{{ totalPrice }}</span>
          </div>
        </template>
      </div>

      <form @submit.prevent="handleBooking" class="space-y-6">
        <!-- Date inputs only when NOT both dates are selected -->
        <div v-if="!(booking.startDate && booking.endDate)" class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <UInput
            v-model="booking.startDate"
            type="date"
            label="Start date"
            required
            :min="new Date().toISOString().split('T')[0]"
            size="lg"
          />
          <UInput
            v-model="booking.endDate"
            type="date"
            label="End date"
            required
            :min="booking.startDate || new Date().toISOString().split('T')[0]"
            size="lg"
          />
        </div>

        <div v-if="error" 
          class="border-l-4 border-red-500 p-4"
        >
          <div class="flex">
            <div class="flex-shrink-0">
              <i class="i-heroicons-exclamation-circle text-red-500"></i>
            </div>
            <div class="ml-3">
              <p class="text-red-500">{{ error }}</p>
            </div>
          </div>
        </div>

        <div class="flex justify-end space-x-4">
          <UButton 
            type="button" 
            color="gray" 
            variant="soft" 
            @click="closeModal"
            size="lg"
          >
            Cancel
          </UButton>
          <UButton 
            type="submit"
            color="primary"
            :loading="isLoading"
            size="lg"
          >
            {{ isLoading ? 'Booking...' : 'Confirm Booking' }}
          </UButton>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useCurrency } from '~/composables/useCurrency'

const route = useRoute()
const props = defineProps({
  isOpen: Boolean,
  car: Object,
  initialDates: {
    type: Object,
    default: () => ({
      startDate: null,
      endDate: null
    })
  }
})

const emit = defineEmits(['close', 'booking-success'])

// Initialize booking with URL parameters or initialDates
const booking = ref({
  startDate: route.query.from || props.initialDates.startDate || null,
  endDate: route.query.to || props.initialDates.endDate || null
})

const isLoading = ref(false)
const error = ref(null)

// Calculate total price
const totalPrice = computed(() => {
  if (!booking.value.startDate || !booking.value.endDate || !props.car) return null
  
  const start = new Date(booking.value.startDate)
  const end = new Date(booking.value.endDate)
  const days = Math.ceil((end - start) / (1000 * 60 * 60 * 24))
  
  return days > 0 ? (days * props.car.pricePerDay).toFixed(2) : null
})

const closeModal = () => {
  emit('close')
}

const handleBooking = async () => {
  try {
    isLoading.value = true
    error.value = null

    if (!booking.value.startDate || !booking.value.endDate) {
      throw new Error('Please select start and end date')
    }

    const startDate = new Date(booking.value.startDate)
    const endDate = new Date(booking.value.endDate)

    if (startDate >= endDate) {
      throw new Error('End date must be after start date')
    }

    // Simulate API call
    console.log('Booking successful:', booking.value)
    emit('booking-success')
    closeModal()
  } catch (err) {
    error.value = err.message
  } finally {
    isLoading.value = false
  }
}

// Date formatting
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

const { getCurrentSymbol } = useCurrency()
</script>