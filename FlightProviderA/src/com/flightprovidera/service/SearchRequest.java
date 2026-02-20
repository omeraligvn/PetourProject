package com.flightprovidera.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * SOAP Body – arama isteği.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchRequest", namespace = "http://service.flightprovider.com/",
        propOrder = {"origin", "destination", "departureDate"})
public class SearchRequest {

    @XmlElement(name = "origin", required = true)
    private String origin = "";

    @XmlElement(name = "destination", required = true)
    private String destination = "";

    @XmlElement(name = "departureDate", required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime departureDate;

	// Constructors
	public SearchRequest() {
	}

	public SearchRequest(String origin, String destination, LocalDateTime departureDate) {
		this.origin = origin;
		this.destination = destination;
		this.departureDate = departureDate;
	}

	// Getters and Setters
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public LocalDateTime getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDateTime departureDate) {
		this.departureDate = departureDate;
	}
}
