package com.restapi.list.service.impl;

import com.restapi.list.model.SearchResult;

public interface FlightSearchService {

    SearchResult searchFlightsA(String origin, String destination, String date);
    SearchResult searchFlightsB(String origin, String destination, String date);
    SearchResult searchFlightsBAll(String origin, String destination, String date);
}
