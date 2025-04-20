## SOAP WEB SERVICE – Currency Converter

This SOAP-based Web Service converts currency amounts from **USD** to another supported currency using the latest European Central Bank (ECB) exchange rates.

**Important:** Due to compatibility issues, this service is specifically tested with **Python 3.10**. Python 3.13 is incompatible, and Python 3.12 is reportedly problematic.

---

### 🔧 Requirements

Install all dependencies with:

```bash
pip install -r requirements.txt
```

### Dependencies (`requirements.txt`):

```
spyne==2.14.0
zeep==4.2.1
requests==2.31.0
lxml==5.1.0
pytz==2024.1
werkzeug==3.0.1
```

---

### 🔐 Authentication

The service is protected by **HTTP Basic Authentication**.

Default credentials (modifiable in `currency_converter_service.py`):

```
Username: user123
Password: pass123
```

---

### 🚀 Run the SOAP Web Service

Start the currency converter service with:

```bash
python currency_converter_service.py
```

- The service listens on **port 8000** for incoming SOAP requests.

---

### 📦 Example SOAP Request

Example request converting **100 USD** to **JPY**:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:spy="spyne.examples.currency">
  <soapenv:Header/>
  <soapenv:Body>
    <spy:convert_currency>
      <spy:amount>100</spy:amount>
      <spy:to_currency>JPY</spy:to_currency>
    </spy:convert_currency>
  </soapenv:Body>
</soapenv:Envelope>
```

**Note:** Include authentication headers (Basic Auth) with each request.

---

### 🧪 Testing with the SOAP Test Client

Run the provided Zeep-based SOAP client (`soap_test_client.py`) with authentication:

```bash
python soap_test_client.py
```

Ensure credentials within `soap_test_client.py` match those configured in the service:

```python
# Example credentials in soap_test_client.py
username = 'user123'
password = 'fortnite'
```

---

### ✅ Service Features

- Converts currency amounts from USD to supported target currencies.
- Daily caching of ECB exchange rates at **16:00 CET**.
- Clear error handling via SOAP Fault responses.
- Extensive logging for debugging and monitoring.

---

### 📁 Project Structure

```
├── currency_converter_service.py   # Main service with authentication
├── soap_test_client.py             # Client to test SOAP requests
├── requirements.txt                # Dependencies
├── README.md                       # This documentation
└── runtime.txt (optional)          # Python version specification (3.10.x recommended)
```

---

### ⚠️ Security Notice

This implementation uses HTTP Basic Authentication. For secure, production-level deployment, ensure the service runs over HTTPS.
