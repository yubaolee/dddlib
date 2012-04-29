package com.dayatang.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;
import com.dayatang.jpa.internal.JpaCriteriaQueryBuilder;

/**
 * 通用仓储接口的JPA实现。
 * 
 * @author yyang
 * 
 */
public class EntityRepositoryJpa implements EntityRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityRepositoryJpa.class);

	@Inject
	@PersistenceContext
	private EntityManager entityManager;
	
	public EntityRepositoryJpa() {
	}

	public EntityRepositoryJpa(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#save(com.dayatang.domain.Entity)
	 */
	@Override
	public <T extends Entity> T save(T entity) {
		T result = getEntityManager().merge(entity);
		LOGGER.info("save a entity: " + entity.getClass() + "/" + entity.getId() + ".");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#remove(com.dayatang.domain.Entity)
	 */
	@Override
	public void remove(Entity entity) {
		getEntityManager().remove(get(entity.getClass(), entity.getId()));
		LOGGER.info("remove a entity: " + entity.getClass() + "/" + entity.getId() + ".");
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#exists(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> boolean exists(final Class<T> clazz, final Serializable id) {
		T entity = getEntityManager().find(clazz, id);
		return entity != null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#get(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
		return getEntityManager().find(clazz, id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#load(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
		return getEntityManager().getReference(clazz, id);
	}

	@Override
	public <T extends Entity> T getUnmodified(final Class<T> clazz, final T entity) {
		getEntityManager().detach(entity);
		return get(clazz, entity.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> findAll(final Class<T> clazz) {
		String queryString = "select o from " + clazz.getName() + " as o"; 
		return getEntityManager().createQuery(queryString).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> find(final QuerySettings<T> settings) {
		CriteriaQuery<T> criteriaQuery = JpaCriteriaQueryBuilder.getInstance().createCriteriaQuery(settings, getEntityManager());
		Query query = getEntityManager().createQuery(criteriaQuery);
		query.setFirstResult(settings.getFirstResult());
		if (settings.getMaxResults() > 0) {
			query.setMaxResults(settings.getMaxResults());
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(final String queryString, final Object[] params, final Class<T> resultClass) {
		Query query = getEntityManager().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(final String queryString, final Map<String, Object> params, final Class<T> resultClass) {
		Query query = getEntityManager().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findByNamedQuery(final String queryName, final Object[] params, final Class<T> resultClass) {
		Query query = getEntityManager().createNamedQuery(queryName);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findByNamedQuery(final String queryName, final Map<String, Object> params, final Class<T> resultClass) {
		Query query = getEntityManager().createNamedQuery(queryName);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.getResultList();
	}

	@Override
	public <T extends Entity, E extends T> List<T> findByExample(final E example, final ExampleSettings<T> settings) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public <T extends Entity> T getSingleResult(QuerySettings<T> settings) {
		List<T> results = find(settings);
		return results.isEmpty() ? null : results.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T  getSingleResult(final String queryString, final Object[] params, Class<T> resultClass) {
		Query query = getEntityManager().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return (T) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T  getSingleResult(final String queryString, final Map<String, Object> params, Class<T> resultClass) {
		Query query = getEntityManager().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return (T) query.getSingleResult();
	}

	@Override
	public void executeUpdate(final String queryString, final Object[] params) {
		Query query = getEntityManager().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		query.executeUpdate();
	}

	@Override
	public void executeUpdate(final String queryString, final Map<String, Object> params) {
		Query query = getEntityManager().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		query.executeUpdate();
	}
	
	private EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManager = InstanceFactory.getInstance(EntityManager.class); 
		}
		return entityManager;
	}

	@Override
	public void flush() {
		getEntityManager().flush();		
	}

	@Override
	public void refresh(Entity entity) {
		getEntityManager().refresh(entity);
	}

}
