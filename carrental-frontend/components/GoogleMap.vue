<template>
  <h2>{{ props.cars.length }}
    <span v-if="props.cars.length > 1"> cars</span>
    <span v-else>Car</span>
    available in {{ Object.keys(locations).length }}
    <span v-if="Object.keys(locations).length > 1">locations</span>
    <span v-else>location</span></h2>

  <div ref="mapEl" :style="style" class="rounded-lg mt-3 mb-6"></div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const runtimeConfig = useRuntimeConfig()

const props = defineProps({
  center: {
    type: Object,
    default: () => ({ lat: 48.16116000, lng: 16.38233000 }), // Default: Vienna
  },
  zoom: {
    type: Number,
    default: 12,
  },
  cars: {
    type: Array,
    default: () => [],
  },
  style: {
    type: Object,
    default: () => ({ width: '100%', height: '300px' }),
  },
})

// Reactive map elements
const mapEl = ref(null)
const map = ref(null)
const markers = ref([])

console.log("Grouping and counting car locations...")
const locations = await groupLocationsAndCountCars(props.cars);

// Load Google Maps API
function loadGoogleMapsScript(apiKey) {
  return new Promise((resolve, reject) => {
    if (window.google && window.google.maps) {
      resolve(window.google.maps)
      return
    }

    const script = document.createElement('script')
    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places`
    script.async = true
    script.defer = true
    script.onload = () => resolve(window.google.maps)
    script.onerror = reject
    document.head.appendChild(script)
  })
}

// Determine geo coordinates of text based locations
async function geocodeLocations(googleMaps, locations) {
  const geocoder = new googleMaps.Geocoder()

  const geocodePromises = Object.entries(locations).map(([key, value]) => {
    return new Promise((resolve, reject) => {
      geocoder.geocode({ address: key }, (results, status) => {
        if (status === 'OK') {
          let position = {
            lat: results[0].geometry.location.lat(),
            lng: results[0].geometry.location.lng(),
          }
          const markerInfo = {
            position,
            title: key,
            value,
          }
          markers.value.push(markerInfo)
          resolve(markerInfo)
        } else {
          console.warn(`Geocode failed for ${key}: ${status}`)
          resolve(null) // resolve to avoid breaking Promise.all
        }
      })
    })
  })
  await Promise.all(geocodePromises)
}

async function addMarkers(googleMaps, map) {

  const infoWindow = new googleMaps.InfoWindow()
  const bounds = new googleMaps.LatLngBounds();

  if (markers.value.length > 0) {
    markers.value.forEach(location => {
      // Add markers with click event to open InfoWindow
      const gMarker = new googleMaps.Marker({
        position: location.position,
        map: map.value,
        title: location.title || '',
        infoContent: `<h1>${location.title}</h1><p>Cars available: ${location.value}</p>`
      })

      gMarker.addListener('click', () => {
        infoWindow.setContent(gMarker.infoContent || gMarker.title || 'Info')
        infoWindow.open(map.value, gMarker)
      })

      bounds.extend(gMarker.position);
    })
  } else {
    console.log("No markers, length: ", markers.value.length)
  }

  console.log("Adjusting center...")
  map.value.fitBounds(bounds);
}

async function groupLocationsAndCountCars(cars) {
  return cars.reduce((acc, item) => {
    const groupKey = item["pickupLocation"]
    acc[groupKey] = (acc[groupKey] || 0) + 1
    return acc
  }, {})
}

onMounted(async () => {
  const apiKey = runtimeConfig.public.googleMapsApiKey
  const googleMaps = await loadGoogleMapsScript(apiKey)

  if (!mapEl.value) return

  // Initialize the map
  console.log("Initializing map...")
  map.value = new googleMaps.Map(mapEl.value, {
    center: props.center,
    zoom: props.zoom,
  })

  // console.log("Grouping and counting car locations...")
  // const locations = await groupLocationsAndCountCars(props.cars);
  console.log("Awaiting geocode locations...")
  await geocodeLocations(googleMaps, locations)
  console.log("Adding markers...")
  await addMarkers(googleMaps, map)

})
</script>
<style>
.gm-style-iw {
  color: #333333
}

.gm-style-iw p {
  font-size: 1.3em;
}
</style>

