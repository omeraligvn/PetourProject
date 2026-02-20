package com.petour.searchandgroup.model;


public record FlightKey(
        String flightNo,
        String departure,
        String arrival,
        String departureDateTime,
        String arrivalDateTime
) {}
