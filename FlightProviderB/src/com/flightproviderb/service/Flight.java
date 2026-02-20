package com.flightproviderb.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * SOAP Body – uçuş bilgisi (flight).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Flight", namespace = "http://service.flightprovider.com/", propOrder = {"flightNo", "origin", "destination", "departuredatetime", "arrivaldatetime", "price"})
public class Flight {

    @XmlElement(name = "flightNo")
    private String flightNo;

    @XmlElement(name = "origin")
    private String origin;

    @XmlElement(name = "destination")
    private String destination;

    @XmlElement(name = "departureDateTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime departuredatetime;

    @XmlElement(name = "arrivalDateTime")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime arrivaldatetime;

    @XmlElement(name = "price")
    private BigDecimal price;

    /** JAX-WS/JAXB için gerekli varsayılan kurucu */
    public Flight() {
    }

    public Flight(String flightNo, String origin, String destination, LocalDateTime departuredatetime, LocalDateTime arrivaldatetime, BigDecimal price) {
        super();
        this.flightNo = flightNo;
        this.origin = origin;
        this.destination = destination;
        this.departuredatetime = departuredatetime;
        this.arrivaldatetime = arrivaldatetime;
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

    public LocalDateTime getDeparturedatetime() {
        return departuredatetime;
    }

    public void setDeparturedatetime(LocalDateTime departuredatetime) {
        this.departuredatetime = departuredatetime;
    }

    public LocalDateTime getArrivaldatetime() {
        return arrivaldatetime;
    }

    public void setArrivaldatetime(LocalDateTime arrivaldatetime) {
        this.arrivaldatetime = arrivaldatetime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
