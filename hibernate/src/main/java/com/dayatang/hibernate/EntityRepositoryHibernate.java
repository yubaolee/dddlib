package com.dayatang.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dayatang.IocException;
import com.dayatang.domain.InstanceFactory;

/**
 * 通用仓储接口的Hibernate实现。
 * 
 * @author yyang
 * 
 */
public class EntityRepositoryHibernate extends AbstractEntityRepository {

	public EntityRepositoryHibernate() {
	}

	@Override
	protected Session getSession() {
		try {
			return InstanceFactory.getInstance(Session.class);
		} catch (IocException e) {
			SessionFactory sessionFactory = InstanceFactory.getInstance(SessionFactory.class);
			return sessionFactory.getCurrentSession();
		}
	}
}
