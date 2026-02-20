module DOALib {
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    exports com.biletbank.dao;
    exports com.biletbank.entity;
}
