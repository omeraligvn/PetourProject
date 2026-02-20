package com.restapi.list.service.impl;

import com.biletbank.dao.HibernateUtil;
import com.biletbank.entity.RequestLog;
import com.biletbank.entity.ResponseLog;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.restapi.list.client.FlightSearchSoapAClient;
import com.restapi.list.client.FlightSearchSoapBClient;
import com.restapi.list.model.AvailabilitySearchResponse;
import com.restapi.list.model.SearchResult;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public SearchResult searchFlightsA(String origin, String destination, String date) {
        long startMs = System.currentTimeMillis();
        String requestBody = "origin=" + origin + "&destination=" + destination + "&date=" + date;
        String xmlResponse = flightSearchSoapAClient.searchFlights(origin, destination, date);
        long durationMs = System.currentTimeMillis() - startMs;
        saveSoapCallLog("FlightProviderA", requestBody, xmlResponse, durationMs);
        return parseSoapResponse(xmlResponse);
    }

    public SearchResult searchFlightsB(String origin, String destination, String date) {
        long startMs = System.currentTimeMillis();
        String requestBody = "origin=" + origin + "&destination=" + destination + "&date=" + date;
        String xmlResponse = flightSearchSoapBClient.searchFlights(origin, destination, date);
        long durationMs = System.currentTimeMillis() - startMs;
        saveSoapCallLog("FlightProviderB", requestBody, xmlResponse, durationMs);
        return parseSoapResponse(xmlResponse);
    }

    public SearchResult searchFlightsBAll(String origin, String destination, String date) {
        long startMs = System.currentTimeMillis();
        String requestBody = "origin=" + origin + "&destination=" + destination + "&date=" + date;
        String xmlResponse = flightSearchSoapBClient.searchFlights(origin, destination, date);
        long durationMs = System.currentTimeMillis() - startMs;
        saveSoapCallLog("FlightProviderB", requestBody, xmlResponse, durationMs);
        return parseSoapResponse(xmlResponse);
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
