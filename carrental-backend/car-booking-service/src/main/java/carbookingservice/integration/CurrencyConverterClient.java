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
 * gRPC client for the Currency Converter microservice.
 * <p>
 * Provides:
 * <ul>
 *   <li>Basic authentication via metadata header.</li>
 *   <li>Per-request deadlines to avoid stale stub deadlines.</li>
 *   <li>Argument validation with domain-specific exceptions.</li>
 *   <li>Single shared channel with clean shutdown.</li>
 * </ul>
 * </p>
 */
@Component
public class CurrencyConverterClient {

    private static final Metadata.Key<String> AUTH_HEADER =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    private final ManagedChannel channel;
    private final CurrencyConverterGrpc.CurrencyConverterBlockingStub baseStub;
    private final long timeoutMs;

    /**
     * Constructs a CurrencyConverterClient with the specified gRPC connection parameters
     * and basic-auth credentials.
     *
     * @param host      the gRPC server host
     * @param port      the gRPC server port
     * @param user      the username for Basic-Auth
     * @param pwd       the password for Basic-Auth
     * @param timeoutMs the per-call deadline in milliseconds
     */
    public CurrencyConverterClient(
            @Value("${currency.grpc.host}") String host,
            @Value("${currency.grpc.port}") int port,
            @Value("${currency.grpc.username}") String user,
            @Value("${currency.grpc.password}") String pwd,
            @Value("${currency.grpc.timeout-ms:5000}") long timeoutMs) {

        // Initialize the channel with retry enabled
        this.channel = NettyChannelBuilder.forAddress(host, port)
                .enableRetry()
                .maxRetryAttempts(3)
                .usePlaintext()
                .build();

        // Prepare Basic-Auth header
        String token = Base64.getEncoder()
                .encodeToString((user + ":" + pwd)
                        .getBytes(StandardCharsets.UTF_8));
        Metadata meta = new Metadata();
        meta.put(AUTH_HEADER, "Basic " + token);
        ClientInterceptor authInterceptor =
                MetadataUtils.newAttachHeadersInterceptor(meta);

        // Create base stub without deadline
        this.baseStub = CurrencyConverterGrpc
                .newBlockingStub(ClientInterceptors.intercept(channel, authInterceptor));

        this.timeoutMs = timeoutMs;
    }

    /**
     * Converts the given USD amount into the specified ISO currency.
     *
     * @param amountUsd  the amount in USD (must not be null)
     * @param toCurrency the ISO 4217 target currency code (must not be null or blank)
     * @return the converted amount in the target currency
     * @throws CurrencyConversionException if validation fails or the gRPC call errors
     */
    public BigDecimal convert(BigDecimal amountUsd, String toCurrency) {
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

        CurrencyConverterGrpc.CurrencyConverterBlockingStub stub =
                baseStub.withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS);

        try {
            ConversionResponse response = stub.convertCurrency(request);
            return BigDecimal.valueOf(response.getConvertedAmount());
        } catch (StatusRuntimeException e) {
            throw new CurrencyConversionException("gRPC call failed: " + e.getStatus(), e);
        }
    }

    /**
     * Shuts down the gRPC channel to release resources and avoid thread leaks.
     *
     * @throws InterruptedException if the current thread is interrupted while awaiting termination
     */
    @PreDestroy
    public void shutdown() throws InterruptedException {
        channel.shutdownNow().awaitTermination(3, TimeUnit.SECONDS);
    }
}
