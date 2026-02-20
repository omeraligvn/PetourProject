package com.flightproviderb.service.metadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

import com.flightproviderb.service.LocalDateTimeAdapter;

/**
 * SOAP Header metadata – gelen istek için (requestId, clientId, timestamp).
 */
@XmlRootElement(name = "RequestMetadata", namespace = "http://metadata.service.flightprovider.com/")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestMetadata {

    @XmlElement(name = "RequestId", namespace = "http://metadata.service.flightprovider.com/")
    private String requestId;

    @XmlElement(name = "ClientId", namespace = "http://metadata.service.flightprovider.com/")
    private String clientId;

    @XmlElement(name = "Timestamp", namespace = "http://metadata.service.flightprovider.com/")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime timestamp;

    public RequestMetadata() {
    }

    public RequestMetadata(String requestId, String clientId, LocalDateTime timestamp) {
        this.requestId = requestId;
        this.clientId = clientId;
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
