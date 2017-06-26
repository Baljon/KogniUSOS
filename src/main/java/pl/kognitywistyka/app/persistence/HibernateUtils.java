package pl.kognitywistyka.app.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Utility class for Hibernate initialization and SessionFactory creation.
 * It provides SessionFactory for transactions in the service classes.
 */
public class HibernateUtils {

    private static SessionFactory sessionFactory;

    private static void buildSessionFactory() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if(sessionFactory != null) {
            sessionFactory.close();
        }
    }

}
