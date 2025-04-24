## gRPC WEB SERVICE – Currency Converter

This gRPC-based Web Service converts currency amounts from **USD** to another supported currency using the latest European Central Bank (ECB) exchange rates.

**Important:** This service is tested with **Python 3.10** but should work with other recent Python versions.

---

### 🔧 Requirements

Install all dependencies with:

```bash
pip install -r requirements.txt
```

### Dependencies (`requirements.txt`):

```
grpcio==1.56.0
grpcio-tools==1.56.0
requests==2.31.0
lxml==4.9.3
pytz==2023.3
python-dotenv==1.0.0
```

---

### 🔐 Authentication

The service is protected by **Basic Authentication** implemented via gRPC interceptors.

Set your credentials as environment variables or in a `.env` file:

```
AUTH_USERNAME=user123
AUTH_PASSWORD=pass123
```

---

### 🚀 Run the gRPC Service

First, generate the gRPC code from the proto definition:

```bash
python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. proto/currency_converter.proto
```

Then start the currency converter service:

```bash
python currency_converter_service.py
```

- The service listens on **port 50051** for incoming gRPC requests.

---

### 📦 Example Client Request

Use the provided client to test the service:

```bash
python currency_converter_client.py
```

Or create your own client using the gRPC interface:

```python
import grpc
import base64
from proto import currency_converter_pb2
from proto import currency_converter_pb2_grpc

# Authentication
auth_token = base64.b64encode(f"user123:pass123".encode()).decode()
metadata = [('authorization', f'Basic {auth_token}')]

# Create a gRPC channel
with grpc.insecure_channel('localhost:50051') as channel:
    # Create a stub
    stub = currency_converter_pb2_grpc.CurrencyConverterStub(channel)
    
    # Create a request
    request = currency_converter_pb2.ConversionRequest(
        amount=100.0,
        to_currency='JPY'
    )
    
    # Make the request with authentication
    response = stub.ConvertCurrency(request, metadata=metadata)
    print(f"100 USD = {response.converted_amount} JPY")
```

---

### ✅ Service Features

- Converts currency amounts from USD to supported target currencies
- Daily caching of ECB exchange rates at **16:00 CET**
- Clear error handling via gRPC status codes
- Extensive logging for debugging and monitoring
- Secure authentication via interceptors

---

### 📁 Project Structure

```
currency_converter/
├── proto/
│   └── currency_converter.proto        # Protocol Buffers service definition
├── currency_converter_service.py       # Main service implementation
├── currency_converter_client.py        # Test client
├── requirements.txt                    # Dependencies
├── .env                                # Environment variables (create this file)
└── README.md                           # This documentation
```

---

### ⚠️ Security Notice

This implementation uses Basic Authentication over an insecure gRPC channel. For production environments, consider:

1. Using TLS encryption (gRPC secure channel)
2. Implementing more robust authentication mechanisms like JWT or OAuth
3. Adding rate limiting to prevent abuse

---

### 🔄 Migration from SOAP

This service was migrated from a SOAP-based implementation to gRPC for:

- Better performance with HTTP/2
- Smaller message sizes with Protocol Buffers
- Strong typing and contract-first development
- Bi-directional streaming capabilities
- Native code generation for multiple languages
