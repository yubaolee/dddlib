package com.dayatang.observer;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.dayatang.observer.domain.FatherObserver;
import com.dayatang.observer.domain.MotherObserver;

public class HibernateUtils {
	private static Configuration cfg;
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
        	cfg = new Configuration()
        		.addAnnotatedClass(FatherObserver.class)
        		.addAnnotatedClass(MotherObserver.class)
        		.configure();
        	new SchemaExport(cfg).create(false, true);
            return cfg.buildSessionFactory();
        }
        catch (Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
       
    }

    public static synchronized SessionFactory getSessionFactory() {
    	if (sessionFactory == null || sessionFactory.isClosed()) {
    		sessionFactory = buildSessionFactory();
    	}
        return sessionFactory;
    }

    public static void close() {
    	sessionFactory.close();
    }


}
