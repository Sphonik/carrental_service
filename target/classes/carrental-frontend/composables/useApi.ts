import { apiConfig } from '~/config/api.config'

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

  const endpoints = {
    cars: {
      list: (filters?: { from?: string; to?: string; currency?: string }) => {
        const queryParams = buildQueryParams(filters || {})
        return `${getBaseUrl()}/cars${queryParams ? `?${queryParams}` : ''}`
      },
      getById: (id: string) => `${getBaseUrl()}/cars/${id}`,
      book: (id: string) => `${getBaseUrl()}/cars/${id}/book`
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
    getBaseUrl
  }
}