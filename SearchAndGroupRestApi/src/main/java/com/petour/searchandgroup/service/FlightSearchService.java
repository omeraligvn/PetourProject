package com.petour.searchandgroup.service;

import com.petour.searchandgroup.model.SearchResult;

public interface FlightSearchService {

    SearchResult searchFlightsA(String origin, String destination, String date);
    SearchResult searchFlightsB(String origin, String destination, String date);
    SearchResult searchFlightsInAllGetCheaper(String origin, String destination, String date);
    SearchResult searchFlightsInAllGetCheaperInRoute(String origin, String destination, String date);
}
