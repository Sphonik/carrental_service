import carbookingservice.CarBookingServiceApplication;
import carbookingservice.exception.CurrencyConversionException;
import carbookingservice.integration.CurrencyConverterClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.withinPercentage;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
class CurrencyConverterClientIT {

    @Autowired
    private CurrencyConverterClient client;

    @Test
    @DisplayName("100 USD → EUR returns positive value")
    void convertUsdToEur_shouldReturnPositiveAmount() {
        BigDecimal eur = client.convert(BigDecimal.valueOf(100), "EUR");
        assertThat(eur).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("USD → USD is identity")
    void convertUsdToUsd_returnsSameAmount() {
        BigDecimal usd = client.convert(BigDecimal.valueOf(42.50), "USD");
        assertThat(usd).isEqualByComparingTo("42.50");
    }

    @Test
    @DisplayName("Negative amount is rejected with exception")
    void convertNegativeAmount_throwsException() {
        Executable call = () -> client.convert(BigDecimal.valueOf(-1), "EUR");
        assertThrows(CurrencyConversionException.class, call);
    }

    @Test
    @DisplayName("Unsupported currency triggers exception")
    void convertToUnsupportedCurrency_throwsException() {
        Executable call = () -> client.convert(BigDecimal.valueOf(10), "ZZZ");
        assertThrows(CurrencyConversionException.class, call);
    }



    @ParameterizedTest(name = "100 USD → {0} returns positive amount")
    @ValueSource(strings = { "GBP", "JPY", "CHF", "CAD", "AUD" })
    void convertUsdToVariousCurrencies(String currency) {
        BigDecimal result = client.convert(BigDecimal.valueOf(100), currency);
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }

    // ------------------------------------------------------------------
    // 2) CASE INSENSITIVITY
    // ------------------------------------------------------------------
    @ParameterizedTest(name = "Case-insensitive code {0} works")
    @ValueSource(strings = { "eur", "Gbp", "cAd" })
    void currencyCode_isCaseInsensitive(String code) {
        BigDecimal result = client.convert(BigDecimal.valueOf(10), code);
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }

    // ------------------------------------------------------------------
    // 3) LARGE AMOUNT + DIFFERENT TARGETS  — sanity-check magnitude
    // ------------------------------------------------------------------
    @ParameterizedTest(name = "1_000_000 USD → {0} returns positive amount")
    @ValueSource(strings = { "EUR", "JPY", "GBP" /*, …*/ })
    void convertLargeAmount_isPositive(String currency) {
        BigDecimal result = client.convert(BigDecimal.valueOf(1_000_000), currency);
        assertThat(result)
                .as("1 000 000 USD → %s must be > 0", currency)
                .isGreaterThan(BigDecimal.ZERO);
    }


    // ------------------------------------------------------------------
    // 4) NULL PARAMETERS — defensive checks
    // ------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({",EUR", "100,,"})
    void nullArguments_throwException(String amountStr, String currency) {
        BigDecimal amount = amountStr == null || amountStr.isBlank()
                ? null : new BigDecimal(amountStr);
        assertThrows(CurrencyConversionException.class,
                () -> client.convert(amount, currency));
    }
}
