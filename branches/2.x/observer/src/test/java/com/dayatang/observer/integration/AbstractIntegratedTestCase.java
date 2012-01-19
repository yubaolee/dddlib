package com.dayatang.observer.integration;

import org.apache.tapestry5.hibernate.HibernateCoreModule;
import org.apache.tapestry5.ioc.services.TapestryIOCModule;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.tapestry.factory.TapestryIocUtils;

public abstract class AbstractIntegratedTestCase {

	protected Transaction tx;

	protected Session session;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TapestryIocUtils.initInstanceProvider(TapestryIOCModule.class, HibernateCoreModule.class, AppModule.class);
	}

	@Before
	public void setUp() throws Exception {

		session = getSesion();
		clearSession();
		tx = getTransaction();

	}

	protected boolean isCommit() {
		return false;
	}

	@After
	public void tearDown() throws Exception {

		if (isCommit()) {
			tx.commit();
		} else {
			tx.rollback();
		}

	}

	protected Transaction getTransaction() {
		return getSesion().beginTransaction();
	}

	protected Session getSesion() {
		return session = InstanceFactory.getInstance(Session.class);
	}

	protected void clearSession() {
		getSesion().clear();
	}

}
