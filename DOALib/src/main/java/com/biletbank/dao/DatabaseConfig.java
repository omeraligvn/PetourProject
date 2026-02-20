package com.biletbank.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * BiletBankDB PostgreSQL bağlantı yapılandırması.
 * 78.189.176.171:5432 / BiletBankDB
 */
public final class DatabaseConfig {

    private static final String PROPERTIES_FILE = "database.properties";
    private static final Properties props = new Properties();

    static {
        try (InputStream in = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("database.properties yüklenemedi", e);
        }
    }

    private DatabaseConfig() {}

    public static String getUrl() {
        return props.getProperty("db.url",
                "jdbc:postgresql://78.189.176.171:5432/BiletBankDB");
    }

    public static String getUser() {
        return props.getProperty("db.user", "postgres");
    }

    public static String getPassword() {
        return props.getProperty("db.password", "2715315");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUser(), getPassword());
    }
}
