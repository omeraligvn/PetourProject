# SearchAndGroupRestApi

Spring Boot REST API - Arama ve Gruplama işlemleri için REST endpoint'leri sağlar.

## Gereksinimler

- Java 17+
- Maven 3.6+ (veya Maven Wrapper kullanın)

## Projeyi Çalıştırma

```bash
# Maven Wrapper ile (önerilen)
./mvnw spring-boot:run

# Windows'ta
mvnw.cmd spring-boot:run

# Maven yüklüyse
mvn spring-boot:run
```

Uygulama varsayılan olarak `http://localhost:8080` adresinde çalışır.

## API Endpoint'leri

### Search (Arama)

**POST** `/api/search`
```json
{
  "query": "arama terimi",
  "page": 0,
  "size": 20
}
```

**GET** `/api/search?query=arama&page=0&size=20`

### Group (Gruplama)

**POST** `/api/group`
```json
{
  "items": ["item1", "item2", "item3"],
  "groupBy": "category"
}
```

## Proje Yapısı

```
src/main/java/com/petour/searchandgroup/
├── SearchAndGroupRestApiApplication.java   # Ana uygulama
├── controller/
│   ├── SearchController.java
│   └── GroupController.java
├── service/
│   ├── SearchService.java
│   └── GroupService.java
├── model/dto/
│   ├── SearchRequest.java
│   ├── SearchResponse.java
│   ├── GroupRequest.java
│   └── GroupResponse.java
└── exception/
    └── GlobalExceptionHandler.java
```

## Geliştirme

- `SearchService` ve `GroupService` sınıfları örnek implementasyon içerir; gerçek veri kaynağına (veritabanı, Elasticsearch vb.) bağlanacak şekilde genişletilebilir.
- Validation için `jakarta.validation` kullanılmaktadır.
