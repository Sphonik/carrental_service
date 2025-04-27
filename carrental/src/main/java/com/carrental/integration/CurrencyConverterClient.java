package com.carrental.integration;

import com.carrental.exception.CurrencyConversionException;
import jakarta.xml.soap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

/**
 * SOAP client for converting currencies via the Spyne Currency Converter service.
 * <p>
 * Communicates using SAAJ (SOAP with Attachments API for Java) and throws
 * {@link CurrencyConversionException} on errors.
 */
@Component
public class CurrencyConverterClient {

    private static final Logger log = LoggerFactory.getLogger(CurrencyConverterClient.class);

    private final String serviceUrl;
    private final String basicAuthHeader;

    /**
     * Constructs a new client for the Spyne Currency Converter service.
     *
     * @param url the endpoint URL of the SOAP service
     * @param user the username for HTTP Basic authentication
     * @param pwd the password for HTTP Basic authentication
     */
    public CurrencyConverterClient(
            @Value("${currency.soap.url}") String url,
            @Value("${currency.soap.username}") String user,
            @Value("${currency.soap.password}") String pwd) {
        this.serviceUrl = url;
        this.basicAuthHeader = "Basic " +
                Base64.getEncoder()
                        .encodeToString((user + ":" + pwd).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts an amount from USD to the specified target currency.
     *
     * @param amountUsd the amount in USD
     * @param toCurrency the ISO currency code to convert to (e.g. "EUR")
     * @return the converted amount in the target currency
     * @throws CurrencyConversionException if conversion fails or the service is unavailable
     */
    public BigDecimal convert(BigDecimal amountUsd, String toCurrency) {
        if ("USD".equalsIgnoreCase(toCurrency)) {
            return amountUsd;
        }

        try {
            SOAPConnection connection = SOAPConnectionFactory
                    .newInstance()
                    .createConnection();

            SOAPMessage request = buildRequest(amountUsd, toCurrency);
            SOAPMessage response = connection.call(request, serviceUrl);

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

    /**
     * Builds the SOAP request message for currency conversion.
     *
     * @param amountUsd the amount in USD
     * @param toCurrency the target currency code
     * @return the SOAPMessage ready to be sent
     * @throws SOAPException if message construction fails
     */
    private SOAPMessage buildRequest(BigDecimal amountUsd, String toCurrency) throws SOAPException {
        SOAPMessage message = MessageFactory.newInstance().createMessage();
        message.getMimeHeaders().addHeader("Authorization", basicAuthHeader);

        SOAPBody body = message.getSOAPBody();
        SOAPElement convert = body.addChildElement(
                "convert_currency", "tns", "spyne.examples.currency");

        SOAPFactory factory = SOAPFactory.newInstance();
        Name amount = factory.createName("amount", "tns", "spyne.examples.currency");
        Name toCurrencyEl = factory.createName("to_currency", "tns", "spyne.examples.currency");

        convert.addChildElement(amount)
                .addTextNode(amountUsd.toPlainString());
        convert.addChildElement(toCurrencyEl)
                .addTextNode(toCurrency.toUpperCase());

        message.saveChanges();
        return message;
    }

    private static final XPathExpression RESULT_PATH;
    static {
        try {
            XPath x = XPathFactory.newInstance().newXPath();
            RESULT_PATH = x.compile("//*[local-name()='convert_currencyResult']/text()");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the SOAP response to extract the conversion result.
     *
     * @param msg the SOAPMessage returned by the service
     * @return the converted amount as BigDecimal
     * @throws CurrencyConversionException if parsing fails or result is empty
     */
    private BigDecimal parseResponse(SOAPMessage msg) throws SOAPException {
        try {
            String text = (String) RESULT_PATH.evaluate(msg.getSOAPBody(), XPathConstants.STRING);
            if (text == null || text.isBlank()) {
                throw new CurrencyConversionException("SOAP result element empty");
            }
            return new BigDecimal(text);
        } catch (CurrencyConversionException e) {
            throw e;
        } catch (Exception ex) {
            throw new CurrencyConversionException("Unable to parse SOAP result", ex);
        }
    }
}
