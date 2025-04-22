<!-- pages/register.vue -->
<script setup>
import { ref } from 'vue'
import { useAuth } from '~/composables/useAuth'
import { useRouter } from 'vue-router'

const { register } = useAuth()
const router = useRouter()

const firstName = ref('')
const lastName = ref('')
const username = ref('')
const password = ref('')
const errorMessage = ref('')
const loading = ref(false)

const handleRegister = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    await register(firstName.value, lastName.value, username.value, password.value)
    router.push('/login')
  } catch (err) {
    errorMessage.value = err.message || 'Registration failed'
  } finally {
    loading.value = false
  }
}

definePageMeta({
  middleware: ['guest']
})
</script>

<template>
  <div class="min-h-full flex flex-col justify-center py-12 sm:px-6 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-md">
      <h2 class="text-3xl font-bold text-center mb-2">
        Register
      </h2>
      <p class="text-center text-sm">
        Create your account
      </p>
    </div>

    <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
      <div class="p-8 border border-gray-700 rounded-lg">
        <form @submit.prevent="handleRegister" class="space-y-6">
          <div>
            <UInput
              v-model="firstName"
              type="text"
              label="First Name"
              required
              placeholder="John"
              class="block w-full"
              size="lg"
            />
          </div>

          <div>
            <UInput
              v-model="lastName"
              type="text"
              label="Last Name"
              required
              placeholder="Doe"
              class="block w-full"
              size="lg"
            />
          </div>

          <div>
            <UInput
              v-model="username"
              type="text"
              label="Username"
              required
              placeholder="johndoe"
              class="block w-full"
              size="lg"
            />
          </div>

          <div>
            <UInput
              v-model="password"
              type="password"
              label="Password"
              required
              placeholder="••••••••"
              class="block w-full"
              size="lg"
            />
          </div>

          <div v-if="errorMessage" 
            class="border-l-4 border-red-500 p-4"
          >
            <div class="flex">
              <div class="flex-shrink-0">
                <i class="i-heroicons-exclamation-circle text-red-500"></i>
              </div>
              <div class="ml-3">
                <p class="text-sm text-red-500">{{ errorMessage }}</p>
              </div>
            </div>
          </div>

          <div>
            <UButton
              type="submit"
              color="primary"
              :loading="loading"
              block
              size="lg"
            >
              {{ loading ? 'Registering...' : 'Register' }}
            </UButton>
          </div>
        </form>

        <div class="mt-6">
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-700"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2">
                Already have an account?
              </span>
            </div>
          </div>

          <div class="mt-6 text-center">
            <NuxtLink 
              to="/login" 
              class="text-blue-500 hover:text-blue-400"
            >
              Sign in now
            </NuxtLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
input {
  border: 1px solid #ccc;
}
</style>
