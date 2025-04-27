import logging
import requests
from datetime import datetime, time
import grpc
from concurrent import futures
import pytz
from lxml import etree
import os
from dotenv import load_dotenv
import time as time_lib

# Import the generated gRPC modules
from proto import currency_converter_pb2
from proto import currency_converter_pb2_grpc

# Load environment variables
load_dotenv()

# Configure logging
logging.basicConfig(level=logging.INFO)

class CurrencyConverterServicer(currency_converter_pb2_grpc.CurrencyConverterServicer):
    """
    gRPC-based Currency Converter Service using ECB exchange rates.
    Amounts must always be specified in USD.
    """
    # Class attributes for caching
    _cached_rates = None
    _last_update = None
    _timeout = 5
    _update_hour = 16  # ECB update time in CET

    @staticmethod
    def _should_update_cache():
        """Determines if the exchange rate cache should be updated."""
        if not CurrencyConverterServicer._cached_rates or not CurrencyConverterServicer._last_update:
            return True

        tz = pytz.timezone('Europe/Vienna')
        now = datetime.now(tz)
        last_update = CurrencyConverterServicer._last_update.astimezone(tz)
        update_time = time(CurrencyConverterServicer._update_hour, 0)

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
        """Fetches and stores ECB exchange rates."""
        try:
            if CurrencyConverterServicer._should_update_cache():
                logging.info("Updating exchange rates from ECB.")
                url = 'http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml'
                response = requests.get(url, timeout=CurrencyConverterServicer._timeout)

                tree = etree.fromstring(response.content, parser=etree.XMLParser(remove_blank_text=True))
                rates = {
                    cube.get('currency'): float(cube.get('rate'))
                    for cube in tree.xpath('//ns:Cube/ns:Cube/ns:Cube',
                                           namespaces={'ns': 'http://www.ecb.int/vocabulary/2002-08-01/eurofxref'})
                }

                CurrencyConverterServicer._cached_rates = rates
                CurrencyConverterServicer._last_update = datetime.now(pytz.UTC)
                logging.info("Exchange rates successfully updated at %s UTC", CurrencyConverterServicer._last_update)

            return CurrencyConverterServicer._cached_rates

        except requests.RequestException as e:
            logging.exception("Error retrieving exchange rates.")
            if CurrencyConverterServicer._cached_rates:
                logging.warning("Using previously cached rates.")
                return CurrencyConverterServicer._cached_rates
            raise

    def ConvertCurrency(self, request, context):
        """
        Converts an amount from USD to the specified target currency.
        
        :param request: gRPC Request with amount and to_currency
        :param context: gRPC Context
        :returns: ConversionResponse with the converted amount
        """
        logging.info("Conversion requested: %.2f USD to %s", request.amount, request.to_currency)

        if request.amount < 0:
            logging.error("Negative amount: %f", request.amount)
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Amount cannot be negative.")
            return currency_converter_pb2.ConversionResponse()

        try:
            rates = self.get_exchange_rates()
            to_currency = request.to_currency.upper()

            if to_currency == 'USD':
                logging.info("No conversion needed, target currency is USD.")
                return currency_converter_pb2.ConversionResponse(converted_amount=request.amount)

            usd_to_eur = 1 / rates['USD']
            amount_in_eur = request.amount * usd_to_eur

            if to_currency == 'EUR':
                logging.info("Converted %.2f USD to %.2f EUR", request.amount, amount_in_eur)
                return currency_converter_pb2.ConversionResponse(converted_amount=amount_in_eur)

            if to_currency in rates:
                converted_amount = amount_in_eur * rates[to_currency]
                logging.info("Converted %.2f USD to %.2f %s", request.amount, converted_amount, to_currency)
                return currency_converter_pb2.ConversionResponse(converted_amount=converted_amount)
            else:
                logging.error("Unsupported currency requested: %s", to_currency)
                context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
                context.set_details(f"Unsupported currency: {to_currency}")
                return currency_converter_pb2.ConversionResponse()

        except requests.RequestException:
            logging.exception("Exchange rates currently unavailable.")
            context.set_code(grpc.StatusCode.UNAVAILABLE)
            context.set_details("Exchange rates currently unavailable. Please try again later.")
            return currency_converter_pb2.ConversionResponse()

class AuthInterceptor(grpc.ServerInterceptor):
    """
    gRPC Interceptor for Basic Authentication
    """
    def __init__(self, username, password):
        self._username = username
        self._password = password
        
        def deny(_, context):
            context.abort(grpc.StatusCode.UNAUTHENTICATED, 'Authentication required')
        
        self._deny = grpc.unary_unary_rpc_method_handler(deny)
    
    def intercept_service(self, continuation, handler_call_details):
        metadata = dict(handler_call_details.invocation_metadata)
        
        if 'authorization' in metadata:
            auth_header = metadata['authorization']
            try:
                auth_type, auth_value = auth_header.split(' ', 1)
                if auth_type.lower() == 'basic':
                    import base64
                    decoded = base64.b64decode(auth_value).decode('utf-8')
                    username, password = decoded.split(':', 1)
                    
                    if username == self._username and password == self._password:
                        return continuation(handler_call_details)
            except Exception:
                pass
                
        return self._deny

def serve():
    """Starts the gRPC server with authentication"""
    username = os.getenv('AUTH_USERNAME', 'admin')
    password = os.getenv('AUTH_PASSWORD', 'master')
    
    # Create an interceptor for authentication
    auth_interceptor = AuthInterceptor(username, password)
    
    # Create the gRPC server with authentication
    server = grpc.server(
        futures.ThreadPoolExecutor(max_workers=10),
        interceptors=[auth_interceptor]
    )
    
    # Register the service
    currency_converter_pb2_grpc.add_CurrencyConverterServicer_to_server(
        CurrencyConverterServicer(), server
    )
    
    # Bind the server to port 50051
    server.add_insecure_port('[::]:50051')
    server.start()
    
    logging.info("gRPC CurrencyConverterService running on port 50051...")
    
    try:
        while True:
            time_lib.sleep(86400)  # One day in seconds
    except KeyboardInterrupt:
        server.stop(0)

if __name__ == '__main__':
    serve()

