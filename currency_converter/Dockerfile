FROM python:3.9-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy service code and .env file
COPY . .
#COPY .env .

EXPOSE 50051

CMD ["python", "currency_converter_service.py"]
