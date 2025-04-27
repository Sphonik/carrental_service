package carbookingservice.integration;

import carbookingservice.exception.CurrencyConversionException;
import currencyconverter.ConversionRequest;
import currencyconverter.ConversionResponse;
import currencyconverter.CurrencyConverterGrpc;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * gRPC-Client für den Currency-Converter-Service.<br>
 * • Authentifizierung via Basic-Auth-Header<br>
 * • Deadline wird **pro Aufruf** gesetzt – kein „abgelaufener“ Stub mehr
 */
@Component
public class CurrencyConverterClient {

    private static final Metadata.Key<String> AUTH_HEADER =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    private final ManagedChannel channel;
    private final CurrencyConverterGrpc.CurrencyConverterBlockingStub baseStub;
    private final long timeoutMs;

    public CurrencyConverterClient(
            @Value("${currency.grpc.host}") String host,
            @Value("${currency.grpc.port}") int port,
            @Value("${currency.grpc.username}") String user,
            @Value("${currency.grpc.password}") String pwd,
            @Value("${currency.grpc.timeout-ms:5000}") long timeoutMs) {

        /* 1) Channel aufbauen */
        this.channel = NettyChannelBuilder.forAddress(host, port)
                .enableRetry()            // optional, aber empfehlenswert
                .maxRetryAttempts(3)
                .usePlaintext()           // TODO: useTransportSecurity()
                .build();

        /* 2) Auth-Header vorbereiten */
        String token = Base64.getEncoder()
                .encodeToString((user + ":" + pwd)
                        .getBytes(StandardCharsets.UTF_8));

        Metadata meta = new Metadata();
        meta.put(AUTH_HEADER, "Basic " + token);

        ClientInterceptor authInterceptor =
                MetadataUtils.newAttachHeadersInterceptor(meta);

        /* 3) Basis-Stub mit fest verdrahtetem Interceptor */
        this.baseStub = CurrencyConverterGrpc
                .newBlockingStub(ClientInterceptors.intercept(channel, authInterceptor));

        this.timeoutMs = timeoutMs;
    }

    /**
     * Konvertiert einen USD-Betrag in die gewünschte Zielwährung.
     *
     * @throws CurrencyConversionException bei Netzwerk- oder Serverfehlern
     */
    public BigDecimal convert(BigDecimal amountUsd, String toCurrency) {
        if ("USD".equalsIgnoreCase(toCurrency)) {
            return amountUsd;
        }

        ConversionRequest request = ConversionRequest.newBuilder()
                .setAmount(amountUsd.doubleValue())
                .setToCurrency(toCurrency.toUpperCase())
                .build();

        /* 4) Deadline relativ pro Aufruf – verhindert „sofort abgelaufen“ */
        CurrencyConverterGrpc.CurrencyConverterBlockingStub stub =
                baseStub.withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS);

        try {
            ConversionResponse response = stub.convertCurrency(request);
            return BigDecimal.valueOf(response.getConvertedAmount());
        } catch (StatusRuntimeException e) {
            throw new CurrencyConversionException("Currency conversion failed: " + e.getStatus(), e);
        }
    }

    /** Channel sauber schließen (Spring ruft diese Methode beim Shutdown auf). */
    @PreDestroy
    public void shutdown() throws InterruptedException {
        channel.shutdownNow().awaitTermination(3, TimeUnit.SECONDS);
    }
}
