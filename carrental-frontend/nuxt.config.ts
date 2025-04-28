import { apiconfig } from './config/api.config';

// Ortamı algıla
const env = process.env.NODE_ENV === 'production' ? 'production' : 'development';

export default defineNuxtConfig({
  compatibilityDate: '2024-11-01',
  ssr: false,
  devtools: { enabled: true },
  css: ['~/assets/css/main.css'],
  modules: [
    '@nuxt/fonts',
    '@nuxt/icon',
    '@nuxt/image',
    '@nuxt/test-utils',
    '@nuxt/ui',
    '@pinia/nuxt',
    '@pinia-plugin-persistedstate/nuxt',
  ],
  runtimeConfig: {
    public: {
      googleMapsApiKey: 'AIzaSyBjQ70oYqgMlSWvF1eA31arh6-FdxaJloY',
      carbookingurl: apiconfig[env].carbookingurl,
      userurl: apiconfig[env].userUrl,
    }
  }
});
