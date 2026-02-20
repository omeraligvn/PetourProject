package com.flightproviderb.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * SOAP Body – arama sonucu (searchResult).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchResult", namespace = "http://service.flightprovider.com/", propOrder = {"hasError", "flightOptions", "errorMessage"})
public class SearchResult {

    @XmlElement(name = "hasError")
    private boolean hasError = false;

    @XmlElement(name = "flightOptions")
    private List<Flight> flightOptions = new ArrayList<>();

    @XmlElement(name = "errorMessage")
    private String errorMessage;

    // Constructors
    public SearchResult() {
    }

    public SearchResult(boolean hasError, List<Flight> flightOptions, String errorMessage) {
        this.hasError = hasError;
        this.flightOptions = flightOptions;
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public List<Flight> getFlightOptions() {
        return flightOptions;
    }

    public void setFlightOptions(List<Flight> flightOptions) {
        this.flightOptions = flightOptions;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}