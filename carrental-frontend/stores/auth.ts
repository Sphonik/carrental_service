import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  // Daten aus localStorage laden
  const credentials = ref<string | null>(
    process.client ? localStorage.getItem('auth_credentials') : null
  )
  
  const user = ref<any>(
    process.client && localStorage.getItem('auth_user') 
      ? JSON.parse(localStorage.getItem('auth_user')!)
      : null
  )

  const isAuthenticated = computed(() => !!credentials.value)

  function setAuth(username: string, password: string, userId: number) {
    credentials.value = btoa(`${username}:${password}`)
    user.value = { username, userId }
    
    // Im localStorage speichern
    if (process.client) {
      localStorage.setItem('auth_credentials', credentials.value)
      localStorage.setItem('auth_user', JSON.stringify(user.value))
    }
  }

  function updateUser(userData: any) {
    if (user.value) {
      user.value = {
        ...user.value,
        ...userData
      }
      
      if (process.client) {
        localStorage.setItem('auth_user', JSON.stringify(user.value))
      }
    }
  }

  function clearAuth() {
    credentials.value = null
    user.value = null
    
    if (process.client) {
      localStorage.removeItem('auth_credentials')
      localStorage.removeItem('auth_user')
    }
  }

  return { 
    credentials, 
    user, 
    isAuthenticated, 
    setAuth, 
    clearAuth,
    updateUser 
  }
})