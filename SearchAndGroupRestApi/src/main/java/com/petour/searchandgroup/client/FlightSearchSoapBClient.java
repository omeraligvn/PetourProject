package com.petour.searchandgroup.client;

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
 * WSDL: http://localhost:8083/flightsearch?wsdl
 */
@Service
public class FlightSearchSoapBClient {

    private static final Logger log = LoggerFactory.getLogger(FlightSearchSoapBClient.class);

    private static final String SOAP_ENVELOPE_NS = "http://schemas.xmlsoap.org/soap/envelope/";

    private final WebServiceTemplate webServiceTemplate;

    @Value("${flightsearch.soap.uri.b:http://localhost:8083/flightsearch}")
    private String soapUri;

    @Value("${flightsearch.soap.namespace:http://service.flightprovider.com/}")
    private String targetNamespace;

    @Value("${flightsearch.soap.operation:availabilitySearch}")
    private String operationName;

    public FlightSearchSoapBClient(@Qualifier("flightSearchSoapBWebServiceTemplate") WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    /**
     * SOAP isteği gönderir ve yanıtı döner.
     *
     * @param soapRequest SOAP body içeriği (operation ve parametreler)
     * @return SOAP yanıtı (XML string)
     */
    public String sendSoapRequest(String soapRequest) {
        String soapEnvelope = buildSoapEnvelope(soapRequest);
        log.debug("SOAP Request to {}: {}", soapUri, soapEnvelope);

        try {
            Source source = new StringSource(soapEnvelope);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            String soapAction = targetNamespace.endsWith("/") ? targetNamespace + operationName : targetNamespace + "/" + operationName;
            webServiceTemplate.sendSourceAndReceiveToResult(soapUri, source, new SoapActionCallback(soapAction), result);
            String response = writer.toString();
            log.debug("SOAP Response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("SOAP request failed: {}", e.getMessage());
            throw new RuntimeException("Flight search SOAP API bağlantı hatası: " + e.getMessage(), e);
        }
    }

    /**
     * Uçuş araması yapar (FlightProviderB JAX-WS formatı: departure, arrival, departureDate).
     */
    public String searchFlights(String origin, String destination, String departureDate) {
        String dateTime = formatDateTime(departureDate);
        String soapBody = String.format("""
                <ns:%s xmlns:ns="%s">
                    <searchRequest>
                        <departure>%s</departure>
                        <arrival>%s</arrival>
                        <departureDate>%s</departureDate>
                    </searchRequest>
                </ns:%s>
                """,
                operationName, targetNamespace,
                escapeXml(origin), escapeXml(destination), dateTime,
                operationName);

        return sendSoapRequest(soapBody);
    }

    private String formatDateTime(String date) {
        if (date == null || date.isBlank()) return "";
        return date.contains("T") ? date : date + "T00:00:00";
    }

    /**
     * Ham SOAP body ile istek gönderir.
     * WSDL'inize uygun XML'i doğrudan geçebilirsiniz.
     * Tam SOAP envelope veya sadece body içeriği kabul edilir.
     */
    public String sendCustomRequest(String soapBody) {
        String cleaned = stripXmlDeclaration(soapBody);
        String bodyContent = extractBodyContent(cleaned);
        return sendSoapRequest(bodyContent);
    }

    /**
     * XML declaration (<?xml ...?>) kaldırır - Body içinde geçersiz olduğu için.
     */
    private String stripXmlDeclaration(String xml) {
        if (xml == null) return "";
        String trimmed = xml.trim();
        if (trimmed.toLowerCase().startsWith("<?xml")) {
            int end = trimmed.indexOf("?>");
            if (end > 0) {
                return trimmed.substring(end + 2).trim();
            }
        }
        return xml;
    }

    /**
     * Tam SOAP envelope gelirse sadece Body içeriğini çıkarır.
     * Aksi halde gelen içeriği olduğu gibi döner.
     */
    private String extractBodyContent(String xml) {
        if (xml == null || xml.isBlank()) return "";
        String trimmed = xml.trim();
        // soap:Body veya Body etiketi ara (namespace ile veya prefix ile)
        int bodyStart = findBodyStart(trimmed);
        if (bodyStart >= 0) {
            int bodyEnd = findBodyEnd(trimmed, bodyStart);
            if (bodyEnd > bodyStart) {
                return trimmed.substring(bodyStart, bodyEnd).trim();
            }
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

    private String buildSoapEnvelope(String bodyContent) {
        return """
                <soap:Envelope xmlns:soap="%s">
                    <soap:Body>
                        %s
                    </soap:Body>
                </soap:Envelope>
                """.formatted(SOAP_ENVELOPE_NS, bodyContent);
    }

    private String escapeXml(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
