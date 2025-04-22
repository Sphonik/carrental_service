import { apiConfig } from '~/config/api.config'
import { useAuthStore } from '~/stores/auth'

export function useApi() {
  const environment = process.env.NODE_ENV || 'development'
  const config = apiConfig[environment as keyof typeof apiConfig]

  const getBaseUrl = () => config.baseUrl

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
        return `${getBaseUrl()}/cars/available${queryParams ? `?${queryParams}` : ''}`
      },
      getById: (id: string) => `${getBaseUrl()}/cars/${id}`,
      book: (id: string) => `${getBaseUrl()}/bookings`
    },
    bookings: {
      // Bookings für den aktuellen Benutzer abrufen
      list: () => `${getBaseUrl()}/bookings/user/${useAuthStore().user?.userId}`,
      getById: (id: string) => `${getBaseUrl()}/bookings/${id}`,
      cancel: (id: string) => `${getBaseUrl()}/bookings/${id}`
    },
    auth: {
      login: () => `${getBaseUrl()}/auth/login`,
      register: () => `${getBaseUrl()}/auth/register`,
      logout: () => `${getBaseUrl()}/auth/logout`,
      me: () => `${getBaseUrl()}/auth/me`
    }
  }

  return {
    endpoints,
    getBaseUrl,
    getAuthHeaders
  }
}