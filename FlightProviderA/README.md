# FlightProviderA – SOAP API

Uçuş müsaitlik araması sunan SOAP web servisi.

## Gereksinimler

- Java 17
- Maven 3.6+

## Derleme ve çalıştırma

```bash
mvn compile
mvn exec:java
```

Servis adresi: **http://localhost:8082/flightsearch**  
WSDL: **http://localhost:8082/flightsearch?wsdl**

## SOAP yapısı

### Header (metadata)

**Request (opsiyonel):**
- `RequestMetadata` – requestId, clientId, timestamp

**Response (otomatik):**
- `ResponseMetadata` – responseId, correlationId, timestamp

### Body

- **availabilitySearch(searchRequest) → searchResult**
  - Giriş: `origin`, `destination`, `departureDate` (ISO-8601)
  - Çıkış: `hasError`, `flightOptions`, `errorMessage`
  - Her uçuş: `flightNo`, `origin`, `destination`, `departureDateTime`, `arrivalDateTime`, `price`

### Örnek SOAP isteği

```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:svc="http://service.flightprovider.com/"
               xmlns:meta="http://metadata.service.flightprovider.com/">
  <soap:Header>
    <meta:RequestMetadata>
      <meta:RequestId>req-123</meta:RequestId>
      <meta:ClientId>client-001</meta:ClientId>
      <meta:Timestamp>2025-02-19T12:00:00</meta:Timestamp>
    </meta:RequestMetadata>
  </soap:Header>
  <soap:Body>
    <svc:availabilitySearch>
      <searchRequest>
        <origin>IST</origin>
        <destination>COV</destination>
        <departureDate>2025-03-01T00:00:00</departureDate>
      </searchRequest>
    </svc:availabilitySearch>
  </soap:Body>
</soap:Envelope>
```

## Eclipse

Projeyi **Import → Maven → Existing Maven Projects** ile açın. Maven bağımlılıkları otomatik çözülür.
