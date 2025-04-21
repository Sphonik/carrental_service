<template>
  <UApp>
    <div class="flex flex-col min-h-screen">
      <!-- Header -->
      <header class="p-6 bg-blue-500 text-white shadow-md">
        <div class="flex justify-between items-center">
          <h1 class="text-2xl"><a href="/"><UIcon name="material-symbols:directions-car" style="position:relative; top: 5px; font-size: 1.2em"/> Car Rental Service</a></h1>
          <!-- Hamburger Menu Button -->
          <button 
            class="lg:hidden text-white focus:outline-none" 
            @click="menuOpen = !menuOpen"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7" />
            </svg>
          </button>
          <!-- Main Menu -->
          <nav :class="['lg:flex', menuOpen ? 'block' : 'hidden']" class="absolute lg:static top-16 left-0 w-full lg:w-auto bg-blue-500 lg:bg-transparent z-50">
            <ul class="flex flex-col lg:flex-row lg:space-x-4">
              <li><a href="/" class="block px-4 py-2 lg:p-0 text-white no-underline transition-colors duration-300 hover:text-yellow-400 hover:underline">Home</a></li>
              <li><a href="/cars" class="block px-4 py-2 lg:p-0 text-white no-underline transition-colors duration-300 hover:text-yellow-400 hover:underline">Vehicles</a></li>
              <li><a href="/contact" class="block px-4 py-2 lg:p-0 text-white no-underline transition-colors duration-300 hover:text-yellow-400 hover:underline">Contact</a></li>
              <li>
                <a 
                  v-if="user" 
                  href="/account" 
                  class="block px-4 py-2 lg:p-0 text-white no-underline transition-colors duration-300 hover:text-yellow-400 hover:underline"
                >
                  Account
                </a>
                <a 
                  v-else 
                  href="/login" 
                  class="block px-4 py-2 lg:p-0 text-white no-underline transition-colors duration-300 hover:text-yellow-400 hover:underline"
                >
                  Login
                </a>
              </li>
            </ul>
          </nav>
        </div>
      </header>

      <!-- Main Content -->
      <main class="flex-grow m-6">
        <NuxtPage/>
      </main>

      <!-- Footer -->
      <footer class="p-4 text-white text-center">
        <p>&copy; 2025 Car Rental Service. All rights reserved.</p>
      </footer>
    </div>
  </UApp>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuth } from '~/composables/useAuth';

const appConfig = useAppConfig()
const { getUser } = useAuth();
const user = ref(null);
const menuOpen = ref(false);

onMounted(async () => {
  // Nur auf Client-Seite ausführen
  if (process.client) {
    user.value = await getUser();
  }
});
</script>

<style>
</style>
