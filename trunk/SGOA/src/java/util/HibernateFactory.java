package util;

import java.util.Set;
import javax.mail.FetchProfile.Item;
import javax.persistence.Entity;
import model.Cor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
//import org.hibernate.cfg.AnnotationConfiguration;
//import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Configuration;
import org.hibernate.metamodel.Metadata;
import org.hibernate.metamodel.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import java.lang.reflect.Field;
import java.io.IOException;

public class HibernateFactory {

    private static final SessionFactory sessionFactory;
    private static final ThreadLocal sessionThreadLocal = new ThreadLocal();
    private static final ThreadLocal transactionThreadLocal = new ThreadLocal();

    static {
        try {
            final Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Session currentSession() {
        if (sessionThreadLocal.get() == null) {
            Session session = sessionFactory.openSession();
            sessionThreadLocal.set(session);
        }
        return (Session) sessionThreadLocal.get();
    }

    public static void closeSession() {
        Session session = (Session) sessionThreadLocal.get();
        if (session != null) {
            session.close();
        }
        sessionThreadLocal.set(null);
    }

    public static void beginTransaction() {
        if (transactionThreadLocal.get() == null) {
            Transaction transaction = currentSession().beginTransaction();
            transactionThreadLocal.set(transaction);
        }
    }

    public static void commitTransaction() {
        Transaction transaction = (Transaction) transactionThreadLocal.get();
        if (transaction != null && !transaction.wasCommitted()
                && !transaction.wasRolledBack()) {
            transaction.commit();
            transactionThreadLocal.set(null);
        }
    }

    public static void rollbackTransaction() {
        Transaction transaction = (Transaction) transactionThreadLocal.get();
        if (transaction != null && !transaction.wasCommitted()
                && !transaction.wasRolledBack()) {
            transaction.rollback();
            transactionThreadLocal.set(null);
        }
    }
}
