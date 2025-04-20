import { ref } from 'vue'
import { useApi } from './useApi'

interface User {
  id: number
  email: string
  role: string
}

// Cookie Helper Funktionen
const setCookie = (name: string, value: string, days = 7) => {
  const expires = new Date(Date.now() + days * 864e5).toUTCString()
  document.cookie = `${name}=${encodeURIComponent(value)}; expires=${expires}; path=/; Secure; SameSite=Strict`
}

const getCookie = (name: string): string | null => {
  const cookie = document.cookie.split('; ').find(row => row.startsWith(`${name}=`))
  return cookie ? decodeURIComponent(cookie.split('=')[1]) : null
}

const deleteCookie = (name: string) => {
  document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`
}

export function useAuth() {
  const { endpoints } = useApi()
  const user = ref<User | null>(null)

  const register = async (email: string, password: string, role: string) => {
    try {
      const response = await fetch(endpoints.auth.register(), {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password, role })
      })

      if (!response.ok) {
        throw new Error('Registration failed')
      }

      const data = await response.json()
      return { data }
    } catch (error) {
      console.error('Registration error:', error)
      throw error
    }
  }

  const login = async (email: string, password: string) => {
    try {
      if (!email || !password) {
        throw new Error('Email and password are required')
      }

      const response = await fetch(endpoints.auth.login(), {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
      })

      if (!response.ok) {
        throw new Error('Login failed')
      }

      const data = await response.json()
      setCookie('auth_token', data.token) // Cookie statt localStorage
      user.value = data.user
      return { data }
    } catch (error) {
      console.error('Login error:', error)
      throw error
    }
  }

  const getUser = async () => {
    try {
      if (!process.client) return null
      
      const token = getCookie('auth_token') // Cookie statt localStorage
      if (!token) return null

      const response = await fetch(endpoints.auth.me(), {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })

      if (!response.ok) {
        throw new Error('User not found')
      }

      const data = await response.json()
      user.value = data
      return user.value
    } catch (error) {
      console.error('Error loading user:', error)
      return null
    }
  }

  const logout = async () => {
    try {
      const token = getCookie('auth_token') // Cookie statt localStorage
      await fetch(endpoints.auth.logout(), {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
      deleteCookie('auth_token') // Cookie statt localStorage
      user.value = null
    } catch (error) {
      console.error('Logout error:', error)
      throw error
    }
  }

  return {
    user,
    register,
    login,
    getUser,
    logout
  }
}