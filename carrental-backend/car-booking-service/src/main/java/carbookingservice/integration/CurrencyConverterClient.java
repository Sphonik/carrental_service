package carbookingservice.integration;

import carbookingservice.exception.CurrencyConversionException;
import currencyconverter.CurrencyConverterGrpc;
import currencyconverter.ConversionRequest;
import currencyconverter.ConversionResponse;
import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
public class CurrencyConverterClient {

    private static final Metadata.Key<String> AUTH_HEADER =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    private final ManagedChannel channel;
    private final CurrencyConverterGrpc.CurrencyConverterBlockingStub stub;

    public CurrencyConverterClient(
            @Value("${currency.grpc.host}") String host,
            @Value("${currency.grpc.port}") int port,
            @Value("${currency.grpc.username}") String user,
            @Value("${currency.grpc.password}") String pwd,
            @Value("${currency.grpc.timeout-ms:5000}") long timeoutMs) {

        /* 1) Grund-Channel */
        this.channel = NettyChannelBuilder.forAddress(host, port)
                .usePlaintext()   // TLS erst aktivieren, wenn dein Server es kann
                .build();

        /* 2) Auth-Header vorbereiten */
        String token = Base64.getEncoder()
                .encodeToString((user + ":" + pwd).getBytes(StandardCharsets.UTF_8));
        Metadata meta = new Metadata();
        meta.put(AUTH_HEADER, "Basic " + token);

        ClientInterceptor auth =
                MetadataUtils.newAttachHeadersInterceptor(meta);

        /* 3) Stub mit Deadline + Header-Interceptor */
        this.stub = CurrencyConverterGrpc.newBlockingStub(
                        ClientInterceptors.intercept(channel, auth))
                .withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS);
    }

    /** Konvertiert **USD → toCurrency**. */
    public BigDecimal convert(BigDecimal amountUsd, String toCurrency) {
        if ("USD".equalsIgnoreCase(toCurrency)) {
            return amountUsd;
        }
        ConversionRequest req = ConversionRequest.newBuilder()
                .setAmount(amountUsd != null ? amountUsd.doubleValue() : 0.0)
                .setToCurrency(toCurrency != null ? toCurrency.toUpperCase() : "USD")
                .build();
        try {
            ConversionResponse rsp = stub.convertCurrency(req);
            return BigDecimal.valueOf(rsp.getConvertedAmount());
        } catch (StatusRuntimeException e) {
            throw new CurrencyConversionException("gRPC call failed: " + e.getStatus(), e);
        }
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        channel.shutdownNow().awaitTermination(3, TimeUnit.SECONDS);
    }
}
