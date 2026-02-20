package com.restapi.list.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Flight Search SOAP API'ye bağlanan istemci servisi.
 * WSDL: http://localhost:8082/flightsearch?wsdl
 */
@Service
public class FlightSearchSoapAClient {
    private static final Logger log =
            LoggerFactory.getLogger(FlightSearchSoapAClient.class);

    private static final String SOAP_ENV_NS =
            "http://schemas.xmlsoap.org/soap/envelope/";

    private final WebServiceTemplate webServiceTemplate;

    /**
     * WSDL endpoint
     */
    @Value("${flightsearch.soap.uri.a:http://localhost:8082/flightsearch}")
    private String soapUri;

    /**
     * WSDL targetNamespace
     */
    @Value("${flightsearch.soap.namespace:http://service.flightprovider.com/}")
    private String targetNamespace;

    /**
     * WSDL operation name
     */
    @Value("${flightsearch.soap.operation:availabilitySearch}")
    private String operationName;

    public FlightSearchSoapAClient(
            @Qualifier("flightSearchSoapAWebServiceTemplate")
            WebServiceTemplate webServiceTemplate) {

        this.webServiceTemplate = webServiceTemplate;
    }

    /**
     * Flight search SOAP çağrısı (FlightProviderA JAX-WS formatı: searchRequest wrapper).
     */
    public String availabilitySearch(String origin, String destination, String departureDate) {
        String dateTime = formatDateTime(departureDate);
        String body = String.format("""
                <ns:%s xmlns:ns="%s">
                    <searchRequest>
                        <origin>%s</origin>
                        <destination>%s</destination>
                        <departureDate>%s</departureDate>
                    </searchRequest>
                </ns:%s>
                """,
                operationName, targetNamespace,
                escapeXml(origin), escapeXml(destination), dateTime,
                operationName);

        return sendSoapRequest(body);
    }

    /**
     * Controller uyumluluğu için searchFlights alias.
     */
    public String searchFlights(String origin, String destination, String date) {
        return availabilitySearch(origin, destination, date);
    }

    /**
     * Ham SOAP body ile istek (JAX-WS formatında: availabilitySearch + searchRequest).
     */
    public String sendCustomRequest(String soapBody) {
        if (soapBody == null || soapBody.isBlank()) {
            throw new IllegalArgumentException("SOAP body boş olamaz");
        }
        String cleaned = stripXmlDeclaration(soapBody);
        String bodyContent = extractBodyContent(cleaned);
        return sendSoapRequest(bodyContent);
    }

    private String formatDateTime(String date) {
        if (date == null || date.isBlank()) return "";
        return date.contains("T") ? date : date + "T00:00:00";
    }

    private String stripXmlDeclaration(String xml) {
        if (xml == null) return "";
        String trimmed = xml.trim();
        if (trimmed.toLowerCase().startsWith("<?xml")) {
            int end = trimmed.indexOf("?>");
            if (end > 0) return trimmed.substring(end + 2).trim();
        }
        return xml;
    }

    private String extractBodyContent(String xml) {
        if (xml == null || xml.isBlank()) return "";
        String trimmed = xml.trim();
        int bodyStart = findBodyStart(trimmed);
        if (bodyStart >= 0) {
            int bodyEnd = findBodyEnd(trimmed, bodyStart);
            if (bodyEnd > bodyStart) return trimmed.substring(bodyStart, bodyEnd).trim();
        }
        return trimmed;
    }

    private int findBodyStart(String xml) {
        String lower = xml.toLowerCase();
        int idx = lower.indexOf("<soap:body");
        if (idx >= 0) return skipTag(xml, idx);
        idx = lower.indexOf("<body");
        if (idx >= 0) return skipTag(xml, idx);
        return -1;
    }

    private int skipTag(String xml, int tagStart) {
        int close = xml.indexOf('>', tagStart);
        return close > 0 ? close + 1 : -1;
    }

    private int findBodyEnd(String xml, int afterBodyStart) {
        String lower = xml.toLowerCase();
        int idx = lower.indexOf("</soap:body>", afterBodyStart);
        if (idx >= 0) return idx;
        idx = lower.indexOf("</body>", afterBodyStart);
        return idx >= 0 ? idx : -1;
    }

    /**
     * SOAP request gönderir
     */
    private String sendSoapRequest(String bodyContent) {

        String envelope = buildEnvelope(bodyContent);

        log.info("Sending SOAP request to {}", soapUri);
        log.debug("SOAP Request: {}", envelope);

        try {
            Source source = new StringSource(envelope);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            String soapAction = targetNamespace.endsWith("/") ? targetNamespace + operationName : targetNamespace + "/" + operationName;
            webServiceTemplate.sendSourceAndReceiveToResult(
                    soapUri,
                    source,
                    new SoapActionCallback(soapAction),
                    result
            );

            String response = writer.toString();

            log.debug("SOAP Response: {}", response);

            return response;

        } catch (Exception e) {

            log.error("SOAP call failed", e);

            throw new RuntimeException(
                    "SOAP çağrısı başarısız: " + e.getMessage(), e);
        }
    }

    /**
     * SOAP envelope oluşturur
     */
    private String buildEnvelope(String body) {

        return """
                <soapenv:Envelope xmlns:soapenv="%s">
                   <soapenv:Body>
                       %s
                   </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(SOAP_ENV_NS, body);
    }

    /**
     * XML escape
     */
    private String escapeXml(String value) {

        if (value == null)
            return "";

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

}
