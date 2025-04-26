package userservice.integration;

import userservice.exception.CurrencyConversionException;
import jakarta.xml.soap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Minimaler SOAP‑Client für den Spyne‑Currency‑Service.
 *  - verwendet jakarta.xml.soap (SAAJ 3.0.1)
 *  - wirft CurrencyConversionException bei Fehlern
 */
@Component
public class CurrencyConverterClient {

    private static final Logger log = LoggerFactory.getLogger(CurrencyConverterClient.class);

    private final String serviceUrl;
    private final String basicAuthHeader;

    public CurrencyConverterClient(
            @Value("${currency.soap.url}")      String url,
            @Value("${currency.soap.username}") String user,
            @Value("${currency.soap.password}") String pwd) {

        this.serviceUrl       = url;
        this.basicAuthHeader  = "Basic " + Base64.getEncoder()
                .encodeToString((user + ":" + pwd)
                        .getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Konvertiert USD → gewünschte Währung über den SOAP‑Service.
     *
     * @throws CurrencyConversionException bei Verbindungs‑ oder Antwortfehlern
     */
    public BigDecimal convert(BigDecimal amountUsd, String toCurrency) {
        if ("USD".equalsIgnoreCase(toCurrency)) {
            return amountUsd;
        }

        try {
            SOAPConnection connection =
                    SOAPConnectionFactory.newInstance().createConnection();



            SOAPMessage request  = buildRequest(amountUsd, toCurrency);
            SOAPMessage response = connection.call(request, serviceUrl);
            // Debug‑Dump der gesamten Antwort
            ByteArrayOutputStream dump = new ByteArrayOutputStream();
            response.writeTo(dump);
            log.debug("SOAP response:\n{}", dump);

            connection.close();

            return parseResponse(response);

        } catch (Exception ex) {
            log.error("Currency conversion failed ({} USD → {})", amountUsd, toCurrency, ex);
            throw new CurrencyConversionException("Currency conversion failed", ex);
        }
    }



    private SOAPMessage buildRequest(BigDecimal amountUsd, String toCurrency) throws SOAPException {
        SOAPMessage message = MessageFactory.newInstance().createMessage();

        // HTTP Basic Auth
        message.getMimeHeaders().addHeader("Authorization", basicAuthHeader);

        SOAPBody body = message.getSOAPBody();

        // Namespace genau so, wie es der Spyne‑Service nutzt
        SOAPElement convert =
                body.addChildElement("convert_currency", "tns", "spyne.examples.currency");

        SOAPFactory factory = SOAPFactory.newInstance();
        Name amount = factory.createName("amount", "tns", "spyne.examples.currency");
        Name toCurrencyEl = factory.createName("to_currency", "tns", "spyne.examples.currency");

        convert.addChildElement(amount).addTextNode(amountUsd.toPlainString());
        convert.addChildElement(toCurrencyEl).addTextNode(toCurrency.toUpperCase());


        message.saveChanges();
        return message;
    }

    private static final XPathExpression RESULT_PATH;
    static {
        try {
            XPath x = XPathFactory.newInstance().newXPath();
            RESULT_PATH = x.compile("//*[local-name()='convert_currencyResult']/text()");
        } catch (Exception e) {
            throw new RuntimeException(e);      // init‑error → fail fast
        }
    }

    private BigDecimal parseResponse(SOAPMessage msg) throws SOAPException {
        try {
            String text = (String) RESULT_PATH.evaluate(msg.getSOAPBody(), XPathConstants.STRING);
            if (text == null || text.isBlank()) {
                throw new CurrencyConversionException("SOAP result element empty");
            }
            return new BigDecimal(text);
        } catch (CurrencyConversionException e) {
            throw e;  // already domain‑specific
        } catch (Exception ex) {
            throw new CurrencyConversionException("Unable to parse SOAP result", ex);
        }
    }
}
