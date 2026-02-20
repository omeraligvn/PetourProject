package com.restapi.list.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * SOAP arama sonucu (searchResult) – hasError, flightOptions, errorMessage.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "searchResult")
public class SearchResult {

    private boolean hasError = false;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "flightOptions")
    private List<Flight> flightOptions = new ArrayList<>();

    private String errorMessage;

    public SearchResult() {
    }

    public SearchResult(boolean hasError, List<Flight> flightOptions, String errorMessage) {
        this.hasError = hasError;
        this.flightOptions = flightOptions != null ? flightOptions : new ArrayList<>();
        this.errorMessage = errorMessage;
    }

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
        this.flightOptions = flightOptions != null ? flightOptions : new ArrayList<>();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
