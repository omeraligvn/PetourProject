package com.biletbank.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Hibernate SessionFactory yönetimi.
 * database.properties'teki değerler hibernate.cfg.xml'i override eder.
 */
public final class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration config = new Configuration().configure("hibernate.cfg.xml");

            // database.properties varsa bağlantı bilgilerini override et
            try (InputStream in = HibernateUtil.class.getClassLoader()
                    .getResourceAsStream("database.properties")) {
                if (in != null) {
                    Properties db = new Properties();
                    db.load(in);
                    if (db.getProperty("db.url") != null) {
                        config.setProperty("hibernate.connection.url", db.getProperty("db.url"));
                        config.setProperty("hibernate.connection.username", db.getProperty("db.user", "postgres"));
                        config.setProperty("hibernate.connection.password", db.getProperty("db.password", "2715315"));
                    }
                }
            } catch (IOException e) {
                // database.properties yoksa hibernate.cfg.xml kullanılır
            }

            return config.buildSessionFactory();
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null) {
            SESSION_FACTORY.close();
        }
    }
}
