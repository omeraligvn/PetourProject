package com.restapi.list.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * SOAP availabilitySearchResponse – body root elementi.
 */
@JacksonXmlRootElement(localName = "availabilitySearchResponse")
public class AvailabilitySearchResponse {

    @JacksonXmlProperty(localName = "searchResult")
    private SearchResult searchResult;

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }
}
