package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateFactory {

    private static final SessionFactory sessionFactory;
    private static final ThreadLocal sessionThreadLocal = new ThreadLocal();
    private static final ThreadLocal transactionThreadLocal = new ThreadLocal();

    static {
        try {
            final Configuration configuration = new Configuration();
            configuration.configure("/hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
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
