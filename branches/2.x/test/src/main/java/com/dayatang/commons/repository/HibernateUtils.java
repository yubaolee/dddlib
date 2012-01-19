package com.dayatang.commons.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.dayatang.commons.domain.Dictionary;
import com.dayatang.commons.domain.DictionaryCategory;

public class HibernateUtils {
	private static Configuration cfg;
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
        	cfg = new Configuration()
        		.addAnnotatedClass(DictionaryCategory.class)
        		.addAnnotatedClass(Dictionary.class)
        		.configure();
        	//new SchemaExport(cfg).create(false, true);
            return cfg.buildSessionFactory();
        }
        catch (Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
       
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void close() {
    	sessionFactory.close();
    }


}
