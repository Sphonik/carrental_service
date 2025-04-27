import { apiConfig } from '~/config/api.config'
import { useAuthStore } from '~/stores/auth'

export function useApi() {
  const environment = process.env.NODE_ENV || 'development'
  const config = apiConfig[environment as keyof typeof apiConfig]

  const getCarBookingUrl = () => config.carBookingUrl

  const buildQueryParams = (params: Record<string, string | null | undefined>) => {
    const query = new URLSearchParams()
    for (const key in params) {
      if (params[key]) {
        query.append(key, params[key] as string)
      }
    }
    return query.toString()
  }

  // Hilfsfunktion für Authorization Header
  const getAuthHeaders = () => {
    const authStore = useAuthStore()
    if (authStore.credentials) {
      return {
        'Authorization': `Basic ${authStore.credentials}`
      }
    }
    return {}
  }

  const endpoints = {
    cars: {
      list: (filters?: { from?: string; to?: string; currency?: string }) => {
        const queryParams = buildQueryParams(filters || {})
        return `${getCarBookingUrl()}/cars/available${queryParams ? `?${queryParams}` : ''}`
      },
      getById: (id: string) => `${getCarBookingUrl()}/cars/${id}`,
      book: (id: string) => `${getCarBookingUrl()}/bookings`
    },
    bookings: {
      // Bookings für den aktuellen Benutzer abrufen
      list: () => `${getCarBookingUrl()}/bookings/user/${useAuthStore().user?.userId}`,
      getById: (id: string) => `${getCarBookingUrl()}/bookings/${id}`,
      cancel: (id: string) => `${getCarBookingUrl()}/bookings/${id}`
    },
    auth: {
      login: () => `${getCarBookingUrl()}/auth/login`,
      register: () => `${getCarBookingUrl()}/auth/register`,
      logout: () => `${getCarBookingUrl()}/auth/logout`,
      me: () => `${getCarBookingUrl()}/auth/me`
    }
  }

  return {
    endpoints,
    getAuthHeaders
  }
}