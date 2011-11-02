package com.dayatang.spring.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;

import com.dayatang.domain.BaseEntityRepository;
import com.dayatang.domain.Entity;
import com.dayatang.domain.InstanceFactory;

/**
 * 通用仓储接口的JPA实现。
 * 
 * @author chencao
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class BaseEntityRepositoryJpaSpring<T extends Entity, ID extends Serializable>
		implements BaseEntityRepository<T, ID> {

	private Class<T> entityClass;

	private static EntityManagerFactory entityManagerFactory;

	/**
	 * Constructor that takes in a class to see which type of entity to persist
	 * 
	 * @param persistentClass
	 *            the class type you'd like to persist
	 */
	public BaseEntityRepositoryJpaSpring(final Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the sessionFactory
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {
			entityManagerFactory = InstanceFactory
					.getInstance(EntityManagerFactory.class);
		}
		return entityManagerFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		BaseEntityRepositoryJpaSpring.entityManagerFactory = entityManagerFactory;
	}

	/**
	 * @return the hibernateTemplate
	 */
	public JpaTemplate getJpaTemplate() {
		return new JpaTemplate(getEntityManagerFactory());
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
		entity = getJpaTemplate().merge(entity);
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
		getJpaTemplate().remove(entity);
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
		return (T) getJpaTemplate().find(entityClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.beyond.commons.domain.EntityRepository#load(java.io.Serializable)
	 */
	@Override
	public T load(ID id) {
		return (T) getJpaTemplate().getReference(entityClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.beyond.commons.domain.EntityRepository#getUnmodified(org.beyond.commons
	 * .domain.Entity)
	 */
	@Override
	public T getUnmodified(T entity) {
		return (T) getJpaTemplate().find(entityClass, entity.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#findAll()
	 */
	@Override
	public List<T> findAll() {
		return getJpaTemplate().find(
				"select o from " + entityClass.getName() + " o");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#findAll(int, int)
	 */
	@Override
	public List<T> findAll(final int firstResult, final int maxResults) {
		return getJpaTemplate().executeFind(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				return em
						.createQuery(
								"select o from " + entityClass.getName() + " o")
						.setFirstResult(firstResult).setMaxResults(maxResults)
						.getResultList();
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#get(java.lang.Class,
	 * java.io.Serializable)
	 */
	@Override
	public <E extends Entity> E get(Class<E> entityClass, ID id) {
		return getJpaTemplate().find(entityClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#findAll()
	 */
	@Override
	public List<T> findAll(Class<T> entityClass) {
		return getJpaTemplate().find(
				"select o from " + entityClass.getName() + " o");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.beyond.commons.domain.EntityRepository#findAll(int, int)
	 */
	@Override
	public List<T> findAll(final Class<T> entityClass, final int firstResult,
			final int maxResults) {
		return getJpaTemplate().executeFind(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				return em
						.createQuery(
								"select o from " + entityClass.getName() + " o")
						.setFirstResult(firstResult).setMaxResults(maxResults)
						.getResultList();
			}

		});
	}

	@Override
	public List find(final String queryString, final Object[] params) {
		return getJpaTemplate().executeFind(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery(queryString);
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
				return query.getResultList();
			}
		});
	}

	@Override
	public List find(final String queryString,
			final Map<String, Object> params) {
		return getJpaTemplate().executeFind(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery(queryString);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.getResultList();
			}
		});
	}

	@Override
	public List findByNamedQuery(final String queryName,
			final Object[] params) {
		return getJpaTemplate().executeFind(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createNamedQuery(queryName);
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
				return query.getResultList();
			}
		});
	}

	@Override
	public List findByNamedQuery(final String queryName,
			final Map<String, Object> params) {
		return getJpaTemplate().executeFind(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createNamedQuery(queryName);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.getResultList();
			}
		});
	}

	@Override
	public Object getSingleResult(final String queryString,
			final Object[] params) {
		return getJpaTemplate().execute(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery(queryString);
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
				return query.getSingleResult();
			}
		});
	}

	@Override
	public Object getSingleResult(final String queryString,
			final Map<String, Object> params) {
		return getJpaTemplate().execute(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery(queryString);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.getSingleResult();
			}
		});
	}

	@Override
	public void executeUpdate(final String queryString, final Object[] params) {
		getJpaTemplate().execute(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery(queryString);
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void executeUpdate(final String queryString,
			final Map<String, Object> params) {
		getJpaTemplate().execute(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery(queryString);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.executeUpdate();
			}
		});
	}
}
