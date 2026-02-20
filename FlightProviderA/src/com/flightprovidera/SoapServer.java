package com.flightprovidera;

import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;
import java.util.ArrayList;
import java.util.List;

import com.flightprovidera.service.handler.SoapMetadataHandler;
import com.flightprovidera.service.impl.FlightSearchSoapServiceImpl;

/**
 * SOAP servisini yayınlar. Header metadata (RequestMetadata/ResponseMetadata) handler ile işlenir.
 * WSDL: http://localhost:8082/flightsearch?wsdl
 */
public class SoapServer {

    public static final String BASE_URL = "http://localhost:8082";
    public static final String SERVICE_PATH = "/flightsearch";

    public static void main(String[] args) {
        String address = BASE_URL + SERVICE_PATH;
        FlightSearchSoapServiceImpl implementor = new FlightSearchSoapServiceImpl();
        Endpoint endpoint = Endpoint.create(implementor);

        // SOAP Header metadata handler
        List<Handler> handlers = new ArrayList<>();
        handlers.add(new SoapMetadataHandler());
        endpoint.getBinding().setHandlerChain(handlers);

        endpoint.publish(address);
        System.out.println("SOAP servisi çalışıyor: " + address);
        System.out.println("WSDL: " + address + "?wsdl");
    }
}
