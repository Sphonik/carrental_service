FROM node:18-alpine

WORKDIR /app

# Bağımlılıkları yükle
COPY package*.json ./
RUN npm install
RUN npm install ipx --legacy-peer-deps

# Uygulama kodunu kopyala ve build et
COPY . .
RUN npm run build

# Portu aç
EXPOSE 3000

# Nuxt server'ı başlat
CMD ["npm", "run", "start"]
