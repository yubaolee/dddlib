package com.dayatang.hibernate;

import com.dayatang.IocInstanceNotFoundException;
import com.dayatang.domain.InstanceFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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
		} catch (IocInstanceNotFoundException e) {
			SessionFactory sessionFactory = InstanceFactory.getInstance(SessionFactory.class);
			return sessionFactory.getCurrentSession();
		}
	}
}
