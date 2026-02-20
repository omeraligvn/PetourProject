package com.petour.searchandgroup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.petour.searchandgroup.model.Flight;
import com.petour.searchandgroup.model.SearchResult;
import com.petour.searchandgroup.service.FlightSearchService;

/**
 * Flight Search SOAP API'yi REST üzerinden expose eden controller.
 */
@Tag(name = "Flight Search", description = "Uçuş arama SOAP API (A: 8082, B: 8083)")
@RestController
@RequestMapping("/api/flightsearch")
@CrossOrigin(origins = "*v")
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @Operation(summary = "Uçuş ara (A servisi)", description = "SOAP A servisine (8082) uçuş araması yapar")
    @GetMapping(value = "/a", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SearchResult> searchFlightsA(
            @Parameter(description = "Kalkış havalimanı kodu (örn: IST)") @RequestParam(required = false, defaultValue = "") String origin,
            @Parameter(description = "Varış havalimanı kodu (örn: SAW)") @RequestParam(required = false, defaultValue = "") String destination,
            @Parameter(description = "Kalkış tarihi (örn: 2026-02-19)") @RequestParam(required = false, defaultValue = "") String date) {
        SearchResult result = flightSearchService.searchFlightsA(origin, destination, date);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Uçuş ara (B servisi)", description = "SOAP B servisine (8083) uçuş araması yapar")
    @GetMapping(value = "/b", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SearchResult> searchFlightsB(
            @Parameter(description = "Kalkış havalimanı kodu (örn: IST)") @RequestParam(required = false, defaultValue = "") String origin,
            @Parameter(description = "Varış havalimanı kodu (örn: SAW)") @RequestParam(required = false, defaultValue = "") String destination,
            @Parameter(description = "Kalkış tarihi (örn: 2026-02-19)") @RequestParam(required = false, defaultValue = "") String date) {
        SearchResult result = flightSearchService.searchFlightsB(origin, destination, date);
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "Uçuş ara (Bütün Servisler)", description = "SOAP A ve B servisinde uçuş numarası, kalkış yeri, varış yeri, kalkış tarih saati, varış tarih saati ne göre en ucuz uçuşları listeler.  ")
    @GetMapping(value = "/search-min-price", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SearchResult> searchFlights(
            @Parameter(description = "Kalkış havalimanı kodu (örn: IST)") @RequestParam(required = false, defaultValue = "") String origin,
            @Parameter(description = "Varış havalimanı kodu (örn: SAW)") @RequestParam(required = false, defaultValue = "") String destination,
            @Parameter(description = "Kalkış tarihi (örn: 2026-02-19)") @RequestParam(required = false, defaultValue = "") String date) {
        
        return ResponseEntity.ok(flightSearchService.searchFlightsInAllGetCheaper(origin, destination, date));
    }
    
    @Operation(summary = "Uçuş ara (Bütün Servisler)", description = "SOAP A ve B servisinde kalkış , varış ve tarihe göre en ucuzları listeler")
    @GetMapping(value = "/search-min-price-route", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SearchResult> searchFlightsByRoute(
            @Parameter(description = "Kalkış havalimanı kodu (örn: IST)") @RequestParam(required = false, defaultValue = "") String origin,
            @Parameter(description = "Varış havalimanı kodu (örn: SAW)") @RequestParam(required = false, defaultValue = "") String destination,
            @Parameter(description = "Kalkış tarihi (örn: 2026-02-19)") @RequestParam(required = false, defaultValue = "") String date) {
        
        return ResponseEntity.ok(flightSearchService.searchFlightsInAllGetCheaperInRoute(origin, destination, date));
    }

}