package com.dayatang.dsmonitor.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.dayatang.dsmonitor.dao.Dao;

public class HibernateDaoImpl extends HibernateDaoSupport implements Dao {

	public List listResult(final String queryStr, final Object... values) {

		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				return createQuery(queryStr, values).list();
			}
		});

	}

	public List listResultWithoutCloseConnection(String queryStr,
			Object... values) {
		return createQuery(queryStr, values).list();
	}

	private Query createQuery(String queryStr, Object... values) {
		Query query = getSession().createQuery(queryStr);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

}
