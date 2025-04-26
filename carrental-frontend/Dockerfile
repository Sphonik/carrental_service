FROM node:18-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install --legacy-peer-deps

COPY . .

# ✨ BURASI YENİ EKLENECEK: Nuxt için execute permission veriyoruz
RUN chmod +x node_modules/.bin/nuxt

RUN npm run build

EXPOSE 3000

CMD ["npm", "run", "start"]
