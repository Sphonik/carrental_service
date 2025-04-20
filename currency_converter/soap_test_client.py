from zeep import Client
from zeep.transports import Transport
from requests.auth import HTTPBasicAuth
import requests


def test_soap_service():
    wsdl_url = 'http://localhost:8000/?wsdl'

    # Username und Passwort hier festlegen
    username = 'user123'
    password = 'fortnite'

    session = requests.Session()
    session.auth = HTTPBasicAuth(username, password)
    transport = Transport(session=session)
    client = Client(wsdl=wsdl_url, transport=transport)

    try:
        # Test USD to EUR conversion
        result = client.service.convert_currency(100.0, 'EUR')
        print(f'100 USD = {result:.2f} EUR')

    except Exception as e:
        print(f'Error: {str(e)}')


if __name__ == '__main__':
    test_soap_service()
