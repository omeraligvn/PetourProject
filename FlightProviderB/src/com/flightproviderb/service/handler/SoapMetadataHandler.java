package com.flightproviderb.service.handler;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * SOAP Header metadata işleyicisi:
 * - Gelen istekte RequestMetadata okur
 * - Giden yanıtta ResponseMetadata ekler (responseId, correlationId, timestamp)
 */
public class SoapMetadataHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String METADATA_NS = "http://metadata.service.flightprovider.com/";

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        try {
            SOAPMessage msg = context.getMessage();
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

            if (outbound) {
                // Yanıt: ResponseMetadata header ekle
                addResponseMetadata(envelope, context);
            } else {
                // İstek: RequestMetadata header oku (log/validation için)
                readRequestMetadata(envelope, context);
            }
        } catch (Exception e) {
            throw new RuntimeException("SOAP metadata işlenirken hata: " + e.getMessage());
        }
        return true;
    }

    private void readRequestMetadata(SOAPEnvelope envelope, SOAPMessageContext context) throws Exception {
        SOAPHeader header = envelope.getHeader();
        if (header != null) {
            // RequestMetadata varsa context'e kaydet (iş mantığında kullanılabilir)
            var requestIdEl = header.getElementsByTagNameNS(METADATA_NS, "RequestId");
            if (requestIdEl.getLength() > 0) {
                String requestId = requestIdEl.item(0).getTextContent();
                context.put("requestId", requestId);
            }
        }
    }

    private void addResponseMetadata(SOAPEnvelope envelope, SOAPMessageContext context) throws Exception {
        SOAPHeader header = envelope.getHeader();
        if (header == null) {
            header = envelope.addHeader();
        }

        String requestId = (String) context.get("requestId");
        String correlationId = requestId != null ? requestId : UUID.randomUUID().toString();
        String responseId = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        javax.xml.soap.SOAPHeaderElement metaEl = header.addHeaderElement(
                new QName(METADATA_NS, "ResponseMetadata", "meta"));
        metaEl.addNamespaceDeclaration("meta", METADATA_NS);
        metaEl.addChildElement("ResponseId", "meta").addTextNode(responseId);
        metaEl.addChildElement("CorrelationId", "meta").addTextNode(correlationId);
        metaEl.addChildElement("Timestamp", "meta").addTextNode(timestamp);
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }
}