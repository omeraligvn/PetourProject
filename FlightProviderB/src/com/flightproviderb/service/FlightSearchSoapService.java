package com.flightproviderb.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.flightproviderb.service.metadata.RequestMetadata;

/**
 * SOAP Web Service arayüzü – uçuş müsaitlik araması.
 * Header: RequestMetadata (requestId, clientId, timestamp)
 * Body: SearchRequest → SearchResult
 */
@WebService(
    name = "FlightSearchSoapService",
    targetNamespace = "http://service.flightprovider.com/"
)
public interface FlightSearchSoapService {

    @WebMethod(operationName = "availabilitySearch")
    @WebResult(name = "searchResult")
    SearchResult availabilitySearch(
            @WebParam(name = "searchRequest") SearchRequest searchRequest,
            @WebParam(name = "RequestMetadata", header = true, targetNamespace = "http://metadata.service.flightprovider.com/")
            RequestMetadata requestMetadata);
}
