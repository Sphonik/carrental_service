export const apiConfig = {
  development: {
    baseUrl: process.env.API_BASE_URL || 'http://localhost:8080/api/v1'
  },
  production: {
    baseUrl: 'https://carrental-backend-app.azurewebsites.net/api/v1'
  }
}
