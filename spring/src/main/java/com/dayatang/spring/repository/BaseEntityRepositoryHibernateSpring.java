package com.dayatang.spring.repository;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.dayatang.domain.BaseEntityRepository;
import com.dayatang.domain.Entity;

/**
 * 通用仓储接口的Hibernate实现。
 * 
 * @author chencao
 * 
 */
@SuppressWarnings("unchecked")
public abstract class BaseEntityRepositoryHibernateSpring<T extends Entity, ID extends Serializable>
		extends HibernateDaoSupport implements BaseEntityRepository<T, ID> {

	private Class<T> entityClass;

	/**
	 * Constructor that takes in a class to see which type of entity to persist
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 */
	public BaseEntityRepositoryHibernateSpring(final Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.beyond.commons.domain.EntityRepository#save(org.beyond.commons.domain
	 * .Entity)
	 */
	@Override
	public T save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.beyond.commons.domain.EntityRepository#remove(org.beyond.commons.
	 * domain.Entity)
	 */
	@Override
	public void remove(T entity) {
		getHibernateTemplate().delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.beyond.commons.domain.EntityRepository#exists(java.io.Serializable)
	 */
	@Override
	public boolean exists(ID id) {
		return get(id) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#get(java.io.Serializable)
	 */
	@Override
	public T get(ID id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.beyond.commons.domain.EntityRepository#load(java.io.Serializable)
	 */
	@Override
	public T load(ID id) {
		return (T) getHibernateTemplate().load(entityClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#findAll()
	 */
	@Override
	public List<T> findAll() {
		HibernateCallback callback = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createCriteria(entityClass)
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}
		};
		return getHibernateTemplate().executeFind(callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#findAll(int, int)
	 */
	@Override
	public List<T> findAll(final int firstResult, final int maxResults) {
		HibernateCallback callback = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createCriteria(entityClass)
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.setFirstResult(firstResult).setMaxResults(maxResults)
						.list();
			}
		};
		return getHibernateTemplate().executeFind(callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#get(java.lang.Class,
	 * java.io.Serializable)
	 */
	@Override
	public <E extends Entity> E get(Class<E> entityClass, ID id) {
		return (E) getHibernateTemplate().get(entityClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#findAll()
	 */
	@Override
	public List<T> findAll(final Class<T> entityClass) {
		HibernateCallback callback = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createCriteria(entityClass)
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}
		};
		return getHibernateTemplate().executeFind(callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#findAll(int, int)
	 */
	@Override
	public List<T> findAll(final Class<T> entityClass, final int firstResult,
			final int maxResults) {
		HibernateCallback callback = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createCriteria(entityClass)
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.setFirstResult(firstResult).setMaxResults(maxResults)
						.list();
			}
		};
		return getHibernateTemplate().executeFind(callback);
	}

	@Override
	public <E extends Entity> List<E> find(final String queryString,
			final Object[] params) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (int i = 0; i < params.length; i++) {
					query = query.setParameter(i, params[i]);
				}
				return query.list();
			}
		});
	}

	@Override
	public <E extends Entity> List<E> find(final String queryString,
			final Map<String, Object> params) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.list();
			}
		});
	}

	@Override
	public <E extends Entity> List<E> findByNamedQuery(final String queryName,
			final Object[] params) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(queryName);
				for (int i = 0; i < params.length; i++) {
					query = query.setParameter(i, params[i]);
				}
				return query.list();
			}
		});
	}

	@Override
	public <E extends Entity> List<E> findByNamedQuery(final String queryName,
			final Map<String, Object> params) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(queryName);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.list();
			}
		});
	}

	@Override
	public Object getSingleResult(final String queryString,
			final Object[] params) {
		return getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (int i = 0; i < params.length; i++) {
					query = query.setParameter(i, params[i]);
				}
				return query.uniqueResult();
			}
		});
	}

	@Override
	public Object getSingleResult(final String queryString,
			final Map<String, Object> params) {
		return getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.uniqueResult();
			}
		});
	}

	@Override
	public void executeUpdate(final String queryString, final Object[] params) {
		getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (int i = 0; i < params.length; i++) {
					query = query.setParameter(i, params[i]);
				}
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void executeUpdate(final String queryString,
			final Map<String, Object> params) {
		getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.executeUpdate();
			}
		});
	}
}
