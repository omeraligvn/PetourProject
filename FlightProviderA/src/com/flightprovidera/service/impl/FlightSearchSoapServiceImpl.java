package com.flightprovidera.service.impl;

import javax.jws.WebService;

import com.flightprovidera.service.FlightSearchSoapService;
import com.flightprovidera.service.SearchRequest;
import com.flightprovidera.service.SearchResult;
import com.flightprovidera.service.SearchService;
import com.flightprovidera.service.metadata.RequestMetadata;

/**
 * SOAP Web Service uygulaması – mevcut SearchService'i SOAP endpoint olarak sunar.
 * Header: RequestMetadata (opsiyonel)
 * Body: SearchRequest → SearchResult
 */
@WebService(
    endpointInterface = "com.flightprovidera.service.FlightSearchSoapService",
    name = "FlightSearchSoapService",
    targetNamespace = "http://service.flightprovider.com/"
)
public class FlightSearchSoapServiceImpl implements FlightSearchSoapService {

    private final SearchService searchService = new SearchService();

    @Override
    public SearchResult availabilitySearch(SearchRequest searchRequest, RequestMetadata requestMetadata) {
        return searchService.availabilitySearch(searchRequest);
    }
}
