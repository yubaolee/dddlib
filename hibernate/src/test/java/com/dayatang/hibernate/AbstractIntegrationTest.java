package com.dayatang.hibernate;

import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.dayatang.JdbcConstants;
import com.dayatang.btm.BtmUtils;
import com.dayatang.commons.repository.HibernateUtils;
import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.h2.H2Server;

public class AbstractIntegrationTest {
	
	private static H2Server h2Server;
	
	private static BtmUtils btmUtils;
	
	protected static SessionFactory sessionFactory;

	@BeforeClass
	public static void setUpClass() throws Exception {
		Configuration configuration = new ConfigurationFactory().fromClasspath("/jdbc.properties");
		h2Server = new H2Server(configuration.getString("h2.db.dir"), configuration.getString("h2.db.file"));
		h2Server.start();
		btmUtils = BtmUtils.readConfigurationFromClasspath("/jdbc.properties");
		btmUtils.setupDataSource();
		sessionFactory = HibernateUtils.getSessionFactory();
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		sessionFactory.close();
		btmUtils.closeDataSource();
		h2Server.shutdown();
	}

}
