

import carbookingservice.CarBookingServiceApplication;
import carbookingservice.integration.CurrencyConverterClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = CarBookingServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = {
                "currency.grpc.host=localhost",
                "currency.grpc.port=50051",
                "currency.grpc.username=admin",
                "currency.grpc.password=master",
                "currency.grpc.timeout-ms=5000"
        }
)
public class CurrencyConverterClientIT {

    @Autowired
    private CurrencyConverterClient client;

    @Test
    void convertUsdToEur_shouldReturnPositiveAmount() {
        BigDecimal result = client.convert(BigDecimal.valueOf(100), "EUR");
        assertThat(result)
                .as("100 USD → EUR must be > 0")
                .isGreaterThan(BigDecimal.ZERO);
    }
}
