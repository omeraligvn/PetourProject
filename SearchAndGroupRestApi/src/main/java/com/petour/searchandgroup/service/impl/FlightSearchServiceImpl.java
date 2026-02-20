package com.petour.searchandgroup.service.impl;


import com.biletbank.dao.HibernateUtil;
import com.biletbank.entity.RequestLog;
import com.biletbank.entity.ResponseLog;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.petour.searchandgroup.client.FlightSearchSoapAClient;
import com.petour.searchandgroup.client.FlightSearchSoapBClient;
import com.petour.searchandgroup.model.AvailabilitySearchResponse;
import com.petour.searchandgroup.model.SearchResult;
import com.petour.searchandgroup.service.FlightSearchService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.catalina.util.StringUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.petour.searchandgroup.model.Flight;
import com.petour.searchandgroup.model.FlightKey;
import com.petour.searchandgroup.model.RouteKey;

/**
 * SOAP uçuş arama sonuçlarını SearchResult modeline parse eden servis.
 */

@Service
public class FlightSearchServiceImpl implements FlightSearchService {

    private static final Logger log = LoggerFactory.getLogger(FlightSearchService.class);

    private final FlightSearchSoapAClient flightSearchSoapAClient;
    private final FlightSearchSoapBClient flightSearchSoapBClient;
    private final XmlMapper xmlMapper;

    public FlightSearchServiceImpl(FlightSearchSoapAClient flightSearchSoapAClient, FlightSearchSoapBClient flightSearchSoapBClient) {
        this.flightSearchSoapAClient = flightSearchSoapAClient;
        this.flightSearchSoapBClient = flightSearchSoapBClient;
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public SearchResult searchFlightsA(String origin, String destination, String date) {
        long startMs = System.currentTimeMillis();
        String requestBody = "origin=" + origin + "&destination=" + destination + "&date=" + date;
        String xmlResponse = flightSearchSoapAClient.searchFlights(origin, destination, date);
        long durationMs = System.currentTimeMillis() - startMs;
        saveSoapCallLog("FlightProviderA", requestBody, xmlResponse, durationMs);
        return parseSoapResponse(xmlResponse);
    }

    @Override
    public SearchResult searchFlightsB(String origin, String destination, String date) {
        long startMs = System.currentTimeMillis();
        String requestBody = "origin=" + origin + "&destination=" + destination + "&date=" + date;
        String xmlResponse = flightSearchSoapBClient.searchFlights(origin, destination, date);
        long durationMs = System.currentTimeMillis() - startMs;
        saveSoapCallLog("FlightProviderB", requestBody, xmlResponse, durationMs);
        return parseSoapResponse(xmlResponse);
    }


    @Override
    public SearchResult searchFlightsInAllGetCheaper(String origin, String destination, String date) {
        String pDate = getTomorrowStr();
        if (date != null && !date.isBlank()) {
            pDate = date;
        }
        SearchResult resultA = searchFlightsA("All", "All", pDate);
        SearchResult resultB = searchFlightsB("All", "All", pDate);
        List<Flight> flights = resultA.getFlightOptions();
        flights.addAll(resultB.getFlightOptions());
        var lst = groupListCheaperAndFiltered(flights, origin, destination, date);
        resultA.setFlightOptions(lst);
        return resultA;
    }

    @Override
    public SearchResult searchFlightsInAllGetCheaperInRoute(String origin, String destination, String date) {
        String pDate = getTomorrowStr();
        if (date != null && !date.isBlank()) {
            pDate = date;
        }
        SearchResult resultA = searchFlightsA("All", "All", pDate);
        SearchResult resultB = searchFlightsB("All", "All", pDate);
        List<Flight> flights = resultA.getFlightOptions();
        flights.addAll(resultB.getFlightOptions());
        var lst = groupCheapestByRoute(flights, origin, destination, date);
        resultA.setFlightOptions(lst);
        return resultA;
    }

    private String getTomorrowStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return LocalDate.now().plusDays(1).format(formatter);

    }

    private List<Flight> groupListCheaper(List<Flight> flightOptions) {

        Map<FlightKey, Flight> cheapestMap = flightOptions.stream().collect(Collectors.toMap(
                f -> new FlightKey(
                        f.getFlightNo(), f.getOrigin(), f.getDestination(), f.getDepartureDateTime(), f.getArrivalDateTime()
                ), f -> f, (f1, f2) -> f1.getPrice() <= f2.getPrice() ? f1 : f2
        ));

        return new ArrayList<>(cheapestMap.values());
    }

    private List<Flight> groupListCheaperAndFiltered(
                                                     List<Flight> flightOptions, String origin, String destination, String date) {

        return new ArrayList<>(
                flightOptions.stream()

                        .filter(f -> origin == null || origin.isBlank() || origin.equals(f.getOrigin()))

                        .filter(f -> destination == null || destination.isBlank() || destination.equals(f.getDestination()))

                        .filter(f -> date == null || date.isBlank() || (f.getDepartureDateTime() != null && f.getDepartureDateTime().startsWith(date)))

                        .collect(Collectors.toMap(
                                f -> new FlightKey(
                                        f.getFlightNo(), f.getOrigin(), f.getDestination(), f.getDepartureDateTime(), f.getArrivalDateTime()
                                ), Function.identity(),

                                BinaryOperator.minBy(
                                        Comparator.comparing(
                                                Flight::getPrice, Comparator.nullsLast(Double::compareTo)
                                        )
                                )
                        )).values()
        );
    }

    private List<Flight> groupCheapestByRoute(
                                              List<Flight> flightOptions, String origin, String destination, String date) {

        return new ArrayList<>(
                flightOptions.stream()
                        // FILTER
                        .filter(f -> origin == null || origin.isBlank() || origin.equals(f.getOrigin()))

                        .filter(f -> destination == null || destination.isBlank() || destination.equals(f.getDestination()))

                        .filter(f -> date == null || date.isBlank() || (f.getDepartureDateTime() != null && f.getDepartureDateTime().startsWith(date)))


                        .collect(Collectors.toMap(

                                // ROUTE KEY
                                f -> new RouteKey(
                                        f.getOrigin(), f.getDestination(), extractDate(f.getDepartureDateTime())
                                ),

                                Function.identity(),

                                BinaryOperator.minBy(
                                        Comparator.comparing(
                                                Flight::getPrice, Comparator.nullsLast(Double::compareTo)
                                        )
                                )
                        ))

                        .values()
        );
    }

    private String extractDate(String dateTime) {
        if (dateTime == null || dateTime.length() < 10)
            return dateTime;

        return dateTime.substring(0, 10);
    }

    /**
     * DOALib ile istek/yanıt loglarını veritabanına yazar.
     */
    private void saveSoapCallLog(String provider, String requestBody, String responseBody, long durationMs) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            RequestLog requestLog = new RequestLog();
            requestLog.setMethod("SOAP");
            requestLog.setUri(provider);
            requestLog.setBody(truncateForLog(requestBody, 65535));
            session.persist(requestLog);
            session.flush();

            ResponseLog responseLog = new ResponseLog();
            responseLog.setRequestLog(requestLog);
            responseLog.setStatusCode(200);
            responseLog.setBody(truncateForLog(responseBody, 65535));
            responseLog.setDurationMs(durationMs);
            session.persist(responseLog);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.warn("DOALib log kaydı atlanıyor: {}", e.getMessage());
        } finally {
            if (session != null) session.close();
        }
    }

    private static String truncateForLog(String value, int maxLength) {
        if (value == null) return null;
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    /**
     * SOAP envelope XML'den body içeriğini çıkarıp SearchResult'a parse eder.
     */
    private SearchResult parseSoapResponse(String soapXml) {
        try {
            String bodyContent = extractSoapBodyContent(soapXml);
            if (bodyContent == null || bodyContent.isBlank()) {
                return createErrorResult("SOAP yanıtında body içeriği bulunamadı");
            }
            try {
                return parseBodyContent(bodyContent);
            } catch (Exception e) {
                // Namespace prefix'leri kaldırıp tekrar dene
                String normalized = bodyContent.replaceAll("\\s+xmlns:[^=]*=\"[^\"]*\"", "").replaceAll("<([a-zA-Z0-9]+):([a-zA-Z0-9]+)([\\s>])", "<$2$3").replaceAll("</([a-zA-Z0-9]+):([a-zA-Z0-9]+)>", "</$2>");
                return parseBodyContent(normalized);
            }
        } catch (Exception e) {
            log.warn("SOAP XML parse hatası: {}", e.getMessage());
            return createErrorResult("Parse hatası: " + e.getMessage());
        }
    }

    private String extractSoapBodyContent(String soapXml) {
        if (soapXml == null || soapXml.isBlank()) return "";
        String trimmed = soapXml.trim();
        int bodyStart = findBodyStart(trimmed);
        if (bodyStart < 0) return trimmed;
        int bodyEnd = findBodyEnd(trimmed, bodyStart);
        if (bodyEnd <= bodyStart) return "";
        return trimmed.substring(bodyStart, bodyEnd).trim();
    }

    private int findBodyStart(String xml) {
        String lower = xml.toLowerCase();
        int idx = lower.indexOf("<soap:body");
        if (idx >= 0) return skipTag(xml, idx);
        idx = lower.indexOf("<body");
        if (idx >= 0) return skipTag(xml, idx);
        return -1;
    }

    private int skipTag(String xml, int tagStart) {
        int close = xml.indexOf('>', tagStart);
        return close > 0 ? close + 1 : -1;
    }

    private int findBodyEnd(String xml, int afterBodyStart) {
        String lower = xml.toLowerCase();
        int idx = lower.indexOf("</soap:body>", afterBodyStart);
        if (idx >= 0) return idx;
        idx = lower.indexOf("</body>", afterBodyStart);
        return idx >= 0 ? idx : -1;
    }

    private SearchResult parseBodyContent(String bodyContent) throws Exception {
        AvailabilitySearchResponse response = xmlMapper.readValue(bodyContent, AvailabilitySearchResponse.class);
        return response.getSearchResult() != null ? response.getSearchResult() : new SearchResult();
    }

    private SearchResult createErrorResult(String errorMessage) {
        SearchResult result = new SearchResult();
        result.setHasError(true);
        result.setErrorMessage(errorMessage);
        return result;
    }
}

