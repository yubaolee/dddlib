package com.dayatang.commons.repository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import com.dayatang.commons.domain.Entity;
import com.dayatang.commons.domain.EntityRepository;
import com.dayatang.commons.domain.ExampleSettings;
import com.dayatang.commons.domain.InstanceFactory;
import com.dayatang.commons.domain.QuerySettings;
import com.dayatang.commons.repository.internal.QueryTranslator;

/**
 * 通用仓储接口的Hibernate实现。
 * 
 * @author yyang
 * 
 */
public class EntityRepositoryHibernate implements EntityRepository {

	private Session session; 


	/**
	 * @return the session
	 */
	public Session getSession() {
		if (session == null) {
			session = InstanceFactory.getInstance(Session.class);
		}
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.commons.domain.EntityRepository#save(com.dayatang.commons.domain.Entity)
	 */
	@Override
	public void save(Entity entity) {
		getSession().saveOrUpdate(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.commons.domain.EntityRepository#remove(com.dayatang.commons.domain.Entity)
	 */
	@Override
	public void remove(Entity entity) {
		getSession().delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.commons.domain.EntityRepository#exists(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> boolean exists(final Class<T> clazz, final Serializable id) {
		return get(clazz, id) != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dayatang.commons.domain.EntityRepository#get(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
		return (T) getSession().get(clazz, id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dayatang.commons.domain.EntityRepository#load(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
		return (T) getSession().load(clazz, id);
	}

	@Override
	public <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		session.evict(entity);
		return get(clazz, entity.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> findAll(Class<T> clazz) {
		return getSession().createCriteria(clazz).list();
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
		Query query = getSession().createQuery(queryString);
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
		query.setFirstResult(settings.getFirstResult());
		if (settings.getMaxResults() > 0) {
			query.setMaxResults(settings.getMaxResults());
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> find(final String queryString, final Object[] params) {
		Query query = getSession().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> find(final String queryString, final Map<String, Object> params) {
		Query query = getSession().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> findByNamedQuery(String queryName, Object[] params) {
		Query query = getSession().getNamedQuery(queryName);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> findByNamedQuery(String queryName, Map<String, Object> params) {
		Query query = getSession().getNamedQuery(queryName);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity, E extends T> List<T> findByExample(final E example, final ExampleSettings<T> settings) {
		Example theExample = Example.create(example);
		if (settings.isLikeEnabled()) {
			theExample.enableLike(MatchMode.ANYWHERE);
		}
		if (settings.isIgnoreCaseEnabled()) {
			theExample.ignoreCase();
		}
		if (settings.isExcludeNone()) {
			theExample.excludeNone();
		}
		if (settings.isExcludeZeroes()) {
			theExample.excludeZeroes();
		}
		for (String propName : settings.getExcludedProperties()) {
			theExample.excludeProperty(propName);
		}
		return getSession().createCriteria(settings.getEntityClass()).add(theExample).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T getSingleResult(QuerySettings<T> settings) {
		if (settings.containsInCriteronWithEmptyValue()) {
			return null;
		}
		QueryTranslator translator = new QueryTranslator(settings);
		String queryString = translator.getQueryString(); 
		List<Object> params = translator.getParams();
		Query query = getSession().createQuery(queryString);
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
		query.setFirstResult(settings.getFirstResult());
		if (settings.getMaxResults() > 0) {
			query.setMaxResults(settings.getMaxResults());
		}
		return (T) query.uniqueResult();
	}

	@Override
	public Object getSingleResult(final String queryString, final Object[] params) {
		Query query = getSession().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		return query.uniqueResult();
	}

	@Override
	public Object getSingleResult(final String queryString, final Map<String, Object> params) {
		Query query = getSession().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.uniqueResult();
	}

	@Override
	public void executeUpdate(final String queryString, final Object[] params) {
		Query query = getSession().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		query.executeUpdate();
	}

	@Override
	public void executeUpdate(final String queryString, final Map<String, Object> params) {
		Query query = getSession().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		query.executeUpdate();
	}
}
