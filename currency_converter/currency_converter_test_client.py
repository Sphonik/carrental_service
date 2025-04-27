import grpc
import logging
import os
from dotenv import load_dotenv

# Import die generierten gRPC-Module
from proto import currency_converter_pb2
from proto import currency_converter_pb2_grpc

# Lade Umgebungsvariablen
load_dotenv()

def run():
    # Authentifizierungsdaten
    username = 'admin'
    password = 'master'
    
    # Erstelle Basic Auth-Token
    import base64
    auth_token = base64.b64encode(f"{username}:{password}".encode()).decode()
    
    # Erstelle Metadaten mit Auth-Token
    metadata = [('authorization', f'Basic {auth_token}')]
    
    # Erstelle einen sicheren Channel zum Server
    with grpc.insecure_channel('localhost:50051') as channel:
        # Erstelle einen Stub (Client)
        stub = currency_converter_pb2_grpc.CurrencyConverterStub(channel)
        
        # Erstelle eine Anfrage
        request = currency_converter_pb2.ConversionRequest(
            amount=100.0,
            to_currency='EUR'
        )
        
        try:
            # Stelle die Anfrage mit Metadaten
            response = stub.ConvertCurrency(request, metadata=metadata)
            print(f"100 USD = {response.converted_amount} EUR")
        except grpc.RpcError as e:
            print(f"Fehler: {e.code()}: {e.details()}")

if __name__ == '__main__':
    logging.basicConfig(level=logging.INFO)
    run()