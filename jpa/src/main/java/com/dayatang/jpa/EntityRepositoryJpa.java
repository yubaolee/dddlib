package com.dayatang.jpa;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;
import com.dayatang.jpa.internal.QueryTranslator;

/**
 * 通用仓储接口的Hibernate实现。
 * 
 * @author yyang
 * 
 */
public class EntityRepositoryJpa implements EntityRepository {

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
		return getEntityManager().merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#remove(com.dayatang.domain.Entity)
	 */
	@Override
	public void remove(Entity entity) {
		getEntityManager().remove(get(entity.getClass(), entity.getId()));
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
		if (settings.containsInCriteronWithEmptyValue()) {
			return Collections.EMPTY_LIST;
		}
		QueryTranslator translator = new QueryTranslator(settings);
		String queryString = translator.getQueryString(); 
		List<Object> params = translator.getParams();
		int firstResult = settings.getFirstResult();
		int maxResults = settings.getMaxResults();
		Query query = getEntityManager().createQuery(queryString);
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i + 1, params.get(i));
		}
		if (firstResult < 0) {
			firstResult =0;
		}
		query.setFirstResult(settings.getFirstResult());
		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> find(final String queryString, final Object[] params) {
		Query query = getEntityManager().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> find(final String queryString, final Map<String, Object> params) {
		Query query = getEntityManager().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> findByNamedQuery(final String queryName, final Object[] params) {
		Query query = getEntityManager().createNamedQuery(queryName);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> findByNamedQuery(final String queryName, final Map<String, Object> params) {
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

	@Override
	public Object getSingleResult(final String queryString, final Object[] params) {
		Query query = getEntityManager().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getSingleResult();
	}

	@Override
	public Object getSingleResult(final String queryString, final Map<String, Object> params) {
		Query query = getEntityManager().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.getSingleResult();
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

}
