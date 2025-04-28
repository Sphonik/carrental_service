# Base Python image
FROM python:3.9-slim

# Set working directory
WORKDIR /app

# Install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy the entire application
COPY . .

# Expose gRPC port
EXPOSE 50051

# Command to run the gRPC server
CMD ["python", "currency_converter_service.py"]
