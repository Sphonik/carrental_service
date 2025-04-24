<template>
  <div class="container mx-auto px-4 py-12 lg:w-1/2">

    <!-- Front page welcome -->
    <div class="text-[45px] mb-18 text-center m-auto">
      <span>Need a car?</span>
      <span class="font-bold"> No problem.</span>
    </div>

    <div class="flex justify-between items-center mb-8">
      <h1 class="text-3xl font-bold">Search Cars</h1>
      <div class="">
        <USelect
          v-model="selectedCurrency"
          :items="currencyOptions"
          placeholder="Select Currency"
          @update:modelValue="updateCurrency"
          size="lg"
        />
      </div>
    </div>

    <!-- Search form -->
    <div class="border border-gray-300 dark:border-gray-700 rounded-lg p-6 shadow-md">
      <form @submit.prevent="redirectToCars" class="space-y-6">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <p class="pb-2">Start Date</p>
            <UInput
              v-model="filters.from"
              type="date"
              label="Start Date"
              required
              :min="new Date().toISOString().split('T')[0]"
              class="w-full"
              size="lg"
            />
          </div>
          <div>
            <p class="pb-2">End Date</p>
            <UInput
              v-model="filters.to"
              type="date"
              label="End Date"
              required
              :min="filters.from || new Date().toISOString().split('T')[0]"
              class="w-full"
              size="lg"
            />
          </div>
        </div>
        <div class="flex justify-center pt-4">
          <UButton 
            type="submit" 
            color="primary"
            size="lg"
            class="cursor-pointer"
          >
            Search Available Cars
          </UButton>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCurrency } from '~/composables/useCurrency'

// Cookie Helper Funktionen
const getCookie = (name) => {
  if (process.client) {
    const cookie = document.cookie.split('; ').find(row => row.startsWith(`${name}=`))
    return cookie ? decodeURIComponent(cookie.split('=')[1]) : null
  }
  return null
}

const router = useRouter()
const { setCurrentCurrency, currencySymbols } = useCurrency()

// Date range filters
const filters = ref({
  from: null,
  to: null
})

// Currency selector mit Standard-Wert aus Cookie oder EUR
const selectedCurrency = ref(getCookie('selectedCurrency') || 'USD')

// Available currencies for the select dropdown
const currencyOptions = computed(() => {
  return Object.keys(currencySymbols).map(code => ({
    label: `${code}`,
    value: code
  }))
})

// Weiterleitung zur cars-Seite mit ausgewählten Filtern
const redirectToCars = () => {
  if (!filters.value.from || !filters.value.to) {
    alert('Bitte wählen Sie Start- und Enddatum aus.')
    return
  }

  router.push({
    path: '/cars',
    query: {
      from: filters.value.from,
      to: filters.value.to,
      currency: selectedCurrency.value
    }
  })
}

// Aktualisiere Währung und speichere in Cookies
const updateCurrency = () => {
  setCurrentCurrency(selectedCurrency.value)
}
</script>