<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Contact Us</h1>

    <!-- Contact Information -->
    <div class="border border-gray-700 rounded-lg p-6">
      <h2 class="text-2xl font-semibold mb-4">Our Contact Information</h2>
      
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <!-- Email -->
        <div class="p-4 border border-gray-700 rounded-lg">
          <div class="flex items-center mb-3">
            <i class="i-heroicons-envelope text-2xl text-blue-400 mr-2"></i>
            <h3 class="text-xl font-semibold">Email</h3>
          </div>
          <p class="text-gray-400">support@carrental.com</p>
          <p class="text-gray-400">info@carrental.com</p>
        </div>

        <!-- Phone -->
        <div class="p-4 border border-gray-700 rounded-lg">
          <div class="flex items-center mb-3">
            <i class="i-heroicons-phone text-2xl text-blue-400 mr-2"></i>
            <h3 class="text-xl font-semibold">Phone</h3>
          </div>
          <p class="text-gray-400">+1 234 567 890</p>
          <p class="text-gray-400">+1 234 567 891</p>
        </div>

        <!-- Address -->
        <div class="p-4 border border-gray-700 rounded-lg">
          <div class="flex items-center mb-3">
            <i class="i-heroicons-map-pin text-2xl text-blue-400 mr-2"></i>
            <h3 class="text-xl font-semibold">Address</h3>
          </div>
          <p class="text-gray-400">123 Car Rental Street</p>
          <p class="text-gray-400">City, Country</p>
        </div>
      </div>

      <!-- Business Hours -->
      <div class="mt-8 p-4 border border-gray-700 rounded-lg">
        <div class="flex items-center mb-3">
          <i class="i-heroicons-clock text-2xl text-blue-400 mr-2"></i>
          <h3 class="text-xl font-semibold">Business Hours</h3>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <h4 class="font-semibold mb-2">Weekdays</h4>
            <p class="text-gray-400">Monday - Friday: 8:00 AM - 6:00 PM</p>
          </div>
          <div>
            <h4 class="font-semibold mb-2">Weekend</h4>
            <p class="text-gray-400">Saturday: 9:00 AM - 4:00 PM</p>
            <p class="text-gray-400">Sunday: Closed</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>