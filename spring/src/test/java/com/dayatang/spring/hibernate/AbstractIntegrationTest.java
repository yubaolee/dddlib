package com.dayatang.spring.hibernate;

import javax.transaction.UserTransaction;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.btm.BtmUtils;
import com.dayatang.configuration.Configuration;
import com.dayatang.configuration.ConfigurationFactory;
import com.dayatang.h2.H2Server;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-hibernate.xml"})
@TransactionConfiguration(transactionManager = "txManager")
@Transactional
public class AbstractIntegrationTest {
	
	@Autowired
	protected ApplicationContext applicationContext;
	
	private static H2Server h2Server;
	
	private static BtmUtils btmUtils;

	@BeforeClass
	public static void setUpClass() throws Exception {
		Configuration configuration = new ConfigurationFactory().fromClasspath("/jdbc.properties");
		h2Server = new H2Server(configuration.getString("h2.db.dir"), configuration.getString("h2.db.file"));
		h2Server.start();
		btmUtils = BtmUtils.readConfigurationFromClasspath("/datasources.properties");
		btmUtils.setupDataSource();
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		btmUtils.closeDataSource();
		btmUtils = null;
		h2Server.shutdown();
		System.out.println("================================================");
		System.out.println("关闭BTM和H2");
	}

	public UserTransaction getTransaction() {
		return btmUtils.getTransaction();
	}
	
}
