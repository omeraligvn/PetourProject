# DOALib – BiletBank PostgreSQL DAO

Maven ile derlenen, **PostgreSQL** veritabanına bağlanan Java 17 kütüphanesi.

## Veritabanı

| Ayar    | Değer           |
|--------|------------------|
| Host   | 78.189.176.171   |
| Port   | 5432             |
| DB     | BiletBankDB      |

## Gereksinimler

- JDK 17
- Maven 3.6+

## Yapılandırma

Şifreyi `src/main/resources/database.properties` içinde güncelleyin:

```properties
db.user=postgres
db.password=GERÇEK_ŞİFRE
```

## Derleme

```bash
mvn clean compile
```

JAR oluşturmak için:

```bash
mvn clean package
```

## Kullanım

Bağlantı almak için:

```java
import com.biletbank.dao.DatabaseConfig;
import java.sql.Connection;

Connection conn = DatabaseConfig.getConnection();
// ...
conn.close();
```

## Proje yapısı

```
DOALib/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   ├── module-info.java
│       │   └── com/biletbank/dao/
│       │       └── DatabaseConfig.java
│       └── resources/
│           └── database.properties
└── README.md
```

Eski Eclipse kaynak klasörü (`src/` doğrudan) artık kullanılmıyor; kaynaklar `src/main/java` altındadır.
