# Base Python image
FROM python:3.9-slim

WORKDIR /app

# Install runtime deps + grpcio-tools to compile protos
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt grpcio-tools

# Copy .proto definitions
COPY proto ./proto

# Compile proto → Python modules (both message & gRPC code)
RUN python -m grpc_tools.protoc \
    -I proto \
    --python_out=./proto \
    --grpc_python_out=./proto \
    proto/*.proto

# Copy your service implementation
COPY currency_converter_service.py .

# Ensure Python sees the generated modules
ENV PYTHONPATH="${PYTHONPATH}:/app/proto"

EXPOSE 50051
CMD ["python", "currency_converter_service.py"]
