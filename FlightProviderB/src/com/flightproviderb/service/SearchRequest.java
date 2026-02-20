package com.flightproviderb.service;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;



/**
 * SOAP Body – arama isteği (searchRequest).
 */
@XmlRootElement(name = "searchRequest", namespace = "http://service.flightprovider.com/")
@XmlType(name = "SearchRequest", namespace = "http://service.flightprovider.com/", propOrder = {"departure", "arrival", "departureDate"})
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchRequest {
	@XmlElement(required = true)
	private String departure = "";
	@XmlElement(required = true)
	private String arrival = "";
	@XmlElement(required = true)
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime departureDate;

	// Constructors
	public SearchRequest() {
	}

	public SearchRequest(String origin, String destination, LocalDateTime departureDate) {
		this.departure = origin;
		this.arrival = destination;
		this.departureDate = departureDate;
	}

	// Getters and Setters
	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String origin) {
		this.departure = origin;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String destination) {
		this.arrival = destination;
	}

	public LocalDateTime getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDateTime departureDate) {
		this.departureDate = departureDate;
	}
}
