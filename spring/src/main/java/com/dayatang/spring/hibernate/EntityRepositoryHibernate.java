package com.dayatang.spring.hibernate;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.hibernate.AbstractEntityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class EntityRepositoryHibernate extends AbstractEntityRepository {

	private SessionFactory sessionFactory;
	
	public EntityRepositoryHibernate() {
	}

	public EntityRepositoryHibernate(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = InstanceFactory.getInstance(SessionFactory.class);
		}
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	protected Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

}
