package com.petour.searchandgroup.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Uçuş model sınıfı – SOAP flightOptions elemanı.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Flight {

    @JacksonXmlProperty(localName = "flightNo")
    @JsonProperty("flightNo")
    private String flightNo;
    private String origin;
    private String destination;
    private String departureDateTime;
    private String arrivalDateTime;
    private Double price;

    public Flight() {
    }

    public Flight(String flightNo, String origin, String destination,
                  String departureDateTime, String arrivalDateTime, Double price) {
        this.flightNo = flightNo;
        this.origin = origin;
        this.destination = destination;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.price = price;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

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

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
