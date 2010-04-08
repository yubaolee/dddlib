package com.dayatang.hibernate;

import org.hibernate.Session;

import com.dayatang.domain.InstanceFactory;

public abstract class AbstractRepositoryImpl {

	private Session session; 


	/**
	 * @return the session
	 */
	public Session getSession() {
		if (session == null) {
			session = InstanceFactory.getInstance(Session.class);
		}
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

}
