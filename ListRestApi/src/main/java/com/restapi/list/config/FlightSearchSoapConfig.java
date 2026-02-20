package com.restapi.list.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Configuration
public class FlightSearchSoapConfig {

    @Value("${flightsearch.soap.uri.a:http://localhost:8082/flightsearch}")
    private String soapAUri;

    @Value("${flightsearch.soap.uri.b:http://localhost:8083/flightsearch}")
    private String soapBUri;

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        return new SaajSoapMessageFactory();
    }

    @Bean("flightSearchSoapAWebServiceTemplate")
    public WebServiceTemplate flightSearchSoapAWebServiceTemplate() {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageFactory(messageFactory());
        template.setDefaultUri(soapAUri);
        return template;
    }

    @Bean("flightSearchSoapBWebServiceTemplate")
    public WebServiceTemplate flightSearchSoapBWebServiceTemplate() {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMessageFactory(messageFactory());
        template.setDefaultUri(soapBUri);
        return template;
    }
}
