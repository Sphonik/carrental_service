FROM python:3.9-slim

WORKDIR /app

# Python bağımlılıklarını yükle
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Uygulama dosyalarını kopyala
COPY . .

# Portu aç
EXPOSE 80

# Flask/Spyne servisini başlat
CMD ["python", "currency_converter_service.py"]
