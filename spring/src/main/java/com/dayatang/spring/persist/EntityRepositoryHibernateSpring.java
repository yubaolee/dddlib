package com.dayatang.spring.persist;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.dayatang.hibernate.EntityRepositoryHibernate;

/**
 * 通用仓储接口的Hibernate实现。
 * 该类完全没有给基类EntityRepositoryHibernate添加任何新功能。只是为了加上@Repository标注，告知Spring这是一个仓储类，要对它进行异常转译。
 * 
 * @author yyang
 * 
 */
@Repository
public class EntityRepositoryHibernateSpring extends EntityRepositoryHibernate {
	public EntityRepositoryHibernateSpring() {
	}

	public EntityRepositoryHibernateSpring(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
}
