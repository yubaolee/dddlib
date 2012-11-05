package com.dayatang.dsmonitor.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.dayatang.dsmonitor.dao.Dao;

public class HibernateDaoImpl implements Dao {
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List listResult(final String queryStr, final Object... values) {
		return createQuery(queryStr, values).list();
	}

	public List listResultWithoutCloseConnection(String queryStr,
			Object... values) {
		return createQuery(queryStr, values).list();
	}

	private Query createQuery(String queryStr, Object... values) {
		Query query = sessionFactory.openSession().createQuery(queryStr);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

}
