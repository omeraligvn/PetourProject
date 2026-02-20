package com.flightprovidera.service;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JAXB adapter: LocalDateTime &harr; ISO-8601 string (SOAP/XML için).
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime unmarshal(String v) {
        return v == null || v.isBlank() ? null : LocalDateTime.parse(v, ISO);
    }

    @Override
    public String marshal(LocalDateTime v) {
        return v == null ? null : v.format(ISO);
    }
}
