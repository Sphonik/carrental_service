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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * gRPC client for the Currency-Converter micro-service.
 *
 * <ul>
 *   <li>Basic-Auth via metadata header</li>
 *   <li>Deadline is applied <i>per request</i> – avoids stale stub deadlines</li>
 *   <li>Null / blank argument validation → domain-specific exception</li>
 *   <li>Single shared channel; clean shutdown via {@code @PreDestroy}</li>
 * </ul>
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

        /* 1 — Channel */
        this.channel = NettyChannelBuilder.forAddress(host, port)
                .enableRetry()           // resilience
                .maxRetryAttempts(3)
                .usePlaintext()          // TODO: useTransportSecurity() once TLS is enabled
                .build();

        /* 2 — Auth-header interceptor (once) */
        String token = Base64.getEncoder()
                .encodeToString((user + ":" + pwd)
                        .getBytes(StandardCharsets.UTF_8));

        Metadata meta = new Metadata();
        meta.put(AUTH_HEADER, "Basic " + token);

        ClientInterceptor authInterceptor =
                MetadataUtils.newAttachHeadersInterceptor(meta);

        /* 3 — Base stub (no deadline yet) */
        this.baseStub = CurrencyConverterGrpc
                .newBlockingStub(ClientInterceptors.intercept(channel, authInterceptor));

        this.timeoutMs = timeoutMs;
    }

    /**
     * Converts a USD amount into the given ISO currency code.
     *
     * @param amountUsd  amount in USD (must not be {@code null})
     * @param toCurrency ISO 4217 target currency (must not be {@code null} / blank)
     * @return converted amount
     * @throws CurrencyConversionException on network / validation / service errors
     */
    public BigDecimal convert(BigDecimal amountUsd, String toCurrency) {
        // ——— argument validation ———
        if (Objects.isNull(amountUsd) || Objects.isNull(toCurrency) || toCurrency.isBlank()) {
            throw new CurrencyConversionException("Amount and target currency must be specified");
        }
        if ("USD".equalsIgnoreCase(toCurrency)) {
            return amountUsd;
        }

        ConversionRequest request = ConversionRequest.newBuilder()
                .setAmount(amountUsd.doubleValue())
                .setToCurrency(toCurrency.toUpperCase())
                .build();

        /* fresh stub with per-call deadline */
        CurrencyConverterGrpc.CurrencyConverterBlockingStub stub =
                baseStub.withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS);

        try {
            ConversionResponse response = stub.convertCurrency(request);
            return BigDecimal.valueOf(response.getConvertedAmount());
        } catch (StatusRuntimeException e) {
            throw new CurrencyConversionException("gRPC call failed: " + e.getStatus(), e);
        }
    }

    /** Clean shutdown so integration tests don’t leak threads. */
    @PreDestroy
    public void shutdown() throws InterruptedException {
        channel.shutdownNow().awaitTermination(3, TimeUnit.SECONDS);
    }
}
