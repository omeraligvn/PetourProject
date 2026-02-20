package com.flightproviderb.service.metadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

import com.flightproviderb.service.LocalDateTimeAdapter;

/**
 * SOAP Header metadata – giden yanıt için (responseId, correlationId, timestamp).
 */
@XmlRootElement(name = "ResponseMetadata", namespace = "http://metadata.service.flightprovider.com/")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseMetadata {

    @XmlElement(name = "ResponseId", namespace = "http://metadata.service.flightprovider.com/")
    private String responseId;

    @XmlElement(name = "CorrelationId", namespace = "http://metadata.service.flightprovider.com/")
    private String correlationId;

    @XmlElement(name = "Timestamp", namespace = "http://metadata.service.flightprovider.com/")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime timestamp;

    public ResponseMetadata() {
    }

    public ResponseMetadata(String responseId, String correlationId, LocalDateTime timestamp) {
        this.responseId = responseId;
        this.correlationId = correlationId;
        this.timestamp = timestamp;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
