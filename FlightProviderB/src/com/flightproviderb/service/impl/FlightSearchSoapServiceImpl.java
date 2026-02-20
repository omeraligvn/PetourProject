package com.flightproviderb.service.impl;

import javax.jws.WebService;

import com.flightproviderb.service.FlightSearchSoapService;
import com.flightproviderb.service.SearchRequest;
import com.flightproviderb.service.SearchResult;
import com.flightproviderb.service.SearchService;
import com.flightproviderb.service.metadata.RequestMetadata;

/**
 * SOAP Web Service uygulaması – mevcut SearchService'i SOAP endpoint olarak sunar.
 * Header: RequestMetadata (opsiyonel)
 * Body: SearchRequest → SearchResult
 */
@WebService(
    endpointInterface = "com.flightproviderb.service.FlightSearchSoapService",
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
