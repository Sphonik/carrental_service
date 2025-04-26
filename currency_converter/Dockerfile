FROM python:3.9-slim

WORKDIR /app

# Install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy service code and .env file
COPY . .
COPY .env .

#Expose port for SOAP service
EXPOSE 80

# Start service
CMD ["python", "currency_converter_service.py"]
