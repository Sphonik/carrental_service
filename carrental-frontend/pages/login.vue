<script setup>
import { ref } from 'vue'
import { useAuth } from '~/composables/useAuth'
import { useRouter } from 'vue-router'

const { login } = useAuth()
const router = useRouter()

const username = ref('') // Changed from email to username
const password = ref('')
const errorMessage = ref('')
const loading = ref(false)

const loginUser = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    await login(username.value, password.value) // Changed from email to username
    router.push('/') // Redirect after successful login
  } catch (err) {
    errorMessage.value = err.message || 'Login failed'
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
        Welcome back
      </h2>
      <p class="text-center text-sm">
        Please sign in to continue
      </p>
    </div>

    <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
      <div class="p-8 border border-gray-700 rounded-lg">
        <form @submit.prevent="loginUser" class="space-y-6">
          <div>
            <UInput
              v-model="username"
              type="text"
              label="Username"
              required
              placeholder="Enter username"
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
              {{ loading ? 'Signing in...' : 'Sign in' }}
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
                Don't have an account?
              </span>
            </div>
          </div>

          <div class="mt-6 text-center">
            <NuxtLink 
              to="/register" 
              class="text-blue-500 hover:text-blue-400"
            >
              Register now
            </NuxtLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
