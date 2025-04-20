import logging
import requests
from datetime import datetime, time
from lxml import etree
import pytz
from spyne import Application, rpc, ServiceBase, Float, Unicode
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
from spyne.model.fault import Fault
from wsgiref.simple_server import make_server
from werkzeug.wrappers import Request, Response
from spyne.server.wsgi import WsgiApplication
import base64
import os

logging.basicConfig(level=logging.INFO)

from werkzeug.wrappers import Request, Response
import base64
from dotenv import load_dotenv
load_dotenv()

class BasicAuthMiddleware:
    """
    A simple WSGI middleware to enforce HTTP Basic Authentication.
    Requests without valid credentials are rejected with HTTP 401 Unauthorized.
    """

    def __init__(self, app, username, password):
        """
        Initialize the middleware.

        :param app: The WSGI application to wrap (e.g., Spyne WsgiApplication).
        :param username: The username required for authentication.
        :param password: The password required for authentication.
        """
        self.app = app
        self.username = username
        self.password = password

    def __call__(self, environ, start_response):
        """
        Process each incoming request, enforcing Basic Authentication.

        :param environ: The WSGI environment dictionary containing request data.
        :param start_response: The WSGI callback to start the response.
        """
        request = Request(environ)
        auth = request.headers.get('Authorization')

        if not auth or not self.is_authenticated(auth):
            # Authentication failed: respond with 401 Unauthorized
            res = Response(
                'Unauthorized',
                status=401,
                headers={'WWW-Authenticate': 'Basic realm="Login Required"'}
            )
            return res(environ, start_response)

        # Authentication successful: forward request to wrapped WSGI app
        return self.app(environ, start_response)

    def is_authenticated(self, auth_header):
        """
        Validate the provided Authorization header.

        :param auth_header: The value of the 'Authorization' HTTP header.
        :returns: True if authentication credentials are correct, False otherwise.
        """
        try:
            method, credentials = auth_header.split(' ', 1)
            if method.lower() != 'basic':
                return False

            decoded_credentials = base64.b64decode(credentials).decode('utf-8')
            username, password = decoded_credentials.split(':', 1)

            return username == self.username and password == self.password
        except Exception:
            # Malformed header or decoding error
            return False


class CurrencyConverterService(ServiceBase):
    """
    SOAP-based Currency Converter Service using ECB daily exchange rates.
    Amounts must always be provided in USD.
    """
    # Class attributes for caching
    _cached_rates = None
    _last_update = None
    _timeout = 5
    _update_hour = 16  # ECB rates update time in CET

    @staticmethod
    def _should_update_cache():
        """Determines if the exchange rate cache should be updated."""
        if not CurrencyConverterService._cached_rates or not CurrencyConverterService._last_update:
            return True

        tz = pytz.timezone('Europe/Vienna')
        now = datetime.now(tz)
        last_update = CurrencyConverterService._last_update.astimezone(tz)
        update_time = time(CurrencyConverterService._update_hour, 0)

        # Update conditions:
        if last_update.date() < now.date():
            return True
        if (now.time() >= update_time and last_update.time() < update_time and
            last_update.date() == now.date()):
            return True
        if now.time() >= update_time and last_update.date() < now.date():
            return True

        return False

    @staticmethod
    def get_exchange_rates():
        """Fetches and caches ECB exchange rates."""
        try:
            if CurrencyConverterService._should_update_cache():
                logging.info("Updating exchange rates from ECB.")
                url = 'http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml'
                response = requests.get(url, timeout=CurrencyConverterService._timeout)

                tree = etree.fromstring(response.content, parser=etree.XMLParser(remove_blank_text=True))
                rates = {
                    cube.get('currency'): float(cube.get('rate'))
                    for cube in tree.xpath('//ns:Cube/ns:Cube/ns:Cube',
                                           namespaces={'ns': 'http://www.ecb.int/vocabulary/2002-08-01/eurofxref'})
                }

                CurrencyConverterService._cached_rates = rates
                CurrencyConverterService._last_update = datetime.now(pytz.UTC)
                logging.info("Exchange rates updated successfully at %s UTC", CurrencyConverterService._last_update)

            return CurrencyConverterService._cached_rates

        except requests.RequestException as e:
            logging.exception("Failed to fetch exchange rates.")
            if CurrencyConverterService._cached_rates:
                logging.warning("Using previously cached rates.")
                return CurrencyConverterService._cached_rates
            raise

    @rpc(Float, Unicode, _returns=Float)
    def convert_currency(ctx, amount, to_currency):
        """
        Converts an amount from USD to the specified target currency.

        :param amount: Amount in USD
        :param to_currency: Target currency ISO code (e.g., EUR, GBP)
        :returns: Amount converted to target currency
        :raises Fault: If conversion fails or currency is unsupported
        """
        logging.info("Conversion requested: %.2f USD to %s", amount, to_currency)

        if amount < 0:
            logging.error("Negative amount: %f", amount)
            raise Fault(faultstring="Amount cannot be negative.")

        try:
            rates = CurrencyConverterService.get_exchange_rates()
            to_currency = to_currency.upper()

            if to_currency == 'USD':
                logging.info("No conversion needed, target currency is USD.")
                return amount

            if 'USD' not in rates:
                logging.error("USD rate unavailable.")
                raise Fault(faultstring="USD rate currently unavailable.")

            usd_to_eur = 1 / rates['USD']
            amount_in_eur = amount * usd_to_eur

            if to_currency == 'EUR':
                logging.info("Converted %.2f USD to %.2f EUR", amount, amount_in_eur)
                return amount_in_eur

            if to_currency in rates:
                converted_amount = amount_in_eur * rates[to_currency]
                logging.info("Converted %.2f USD to %.2f %s", amount, converted_amount, to_currency)
                return converted_amount
            else:
                logging.error("Unsupported currency requested: %s", to_currency)
                raise Fault(faultstring=f"Unsupported currency: {to_currency}")

        except requests.RequestException:
            logging.exception("Exchange rates currently unavailable.")
            raise Fault(faultstring="Exchange rates currently unavailable. Please try again later.")

# Spyne Application setup
application = Application(
    [CurrencyConverterService],
    tns='spyne.examples.currency',
    in_protocol=Soap11(validator='lxml'),
    out_protocol=Soap11()
)

# Main method for running the server
if __name__ == '__main__':
    wsgi_app = WsgiApplication(application)

    # Hole Credentials aus Umgebungsvariablen
    username = os.getenv('AUTH_USERNAME')
    password = os.getenv('AUTH_PASSWORD')

    app_with_auth = BasicAuthMiddleware(wsgi_app, username, password)

    server = make_server('0.0.0.0', 80, app_with_auth)
    logging.info("CurrencyConverterService with auth running on port 80...")
    server.serve_forever()

