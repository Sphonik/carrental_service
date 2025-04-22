export default defineNuxtPlugin(async () => {
  // Nur ausführen, wenn im Browser
  if (process.client) {
    const { getUser } = useAuth()
    
    try {
      // Benutzer validieren beim App-Start
      await getUser()
    } catch (error) {
      console.error('Auth initialization error:', error)
    }
  }
})