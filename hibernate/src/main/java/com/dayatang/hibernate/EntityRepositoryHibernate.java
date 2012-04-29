package com.dayatang.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;
import com.dayatang.hibernate.internal.QueryTranslator;

/**
 * 通用仓储接口的Hibernate实现。
 * 
 * @author yyang
 * 
 */
public class EntityRepositoryHibernate implements EntityRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityRepositoryHibernate.class);
	
	@Inject
	private SessionFactory sessionFactory;


	public EntityRepositoryHibernate() {
	}

	public EntityRepositoryHibernate(SessionFactory sessionFactory) {
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

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#save(com.dayatang.domain.Entity)
	 */
	@Override
	public <T extends Entity> T save(T entity) {
		getSessionFactory().getCurrentSession().saveOrUpdate(entity);
		LOGGER.info("save a entity: " + entity.getClass() + "/" + entity.getId() + ".");
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#remove(com.dayatang.domain.Entity)
	 */
	@Override
	public void remove(Entity entity) {
		getSessionFactory().getCurrentSession().delete(entity);
		LOGGER.info("remove a entity: " + entity.getClass() + "/" + entity.getId() + ".");
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#exists(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> boolean exists(final Class<T> clazz, final Serializable id) {
		return get(clazz, id) != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#get(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
		return (T) getSessionFactory().getCurrentSession().get(clazz, id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#load(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
		return (T) getSessionFactory().getCurrentSession().load(clazz, id);
	}

	@Override
	public <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		getSessionFactory().getCurrentSession().evict(entity);
		return get(clazz, entity.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> findAll(Class<T> clazz) {
		return getSessionFactory().getCurrentSession().createCriteria(clazz).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> List<T> find(final QuerySettings<T> settings) {
		QueryTranslator translator = new QueryTranslator(settings);
		String queryString = translator.getQueryString(); 
		LOGGER.info("QueryString: '" + queryString + "'");
		List<Object> params = translator.getParams();
		LOGGER.info("params: " + StringUtils.join(params, ", "));
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
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
	public <T> List<T> find(final String queryString, final Object[] params, final Class<T> resultClass) {
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(final String queryString, final Map<String, Object> params, final Class<T> resultClass) {
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findByNamedQuery(final String queryName, final Object[] params, final Class<T> resultClass) {
		Query query = getSessionFactory().getCurrentSession().getNamedQuery(queryName);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findByNamedQuery(final String queryName, final Map<String, Object> params, final Class<T> resultClass) {
		Query query = getSessionFactory().getCurrentSession().getNamedQuery(queryName);
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
		return getSessionFactory().getCurrentSession().createCriteria(settings.getEntityClass()).add(theExample).list();
	}

	@Override
	public <T extends Entity> T getSingleResult(QuerySettings<T> settings) {
		List<T> list = find(settings);
		return list.isEmpty() ? null : list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T getSingleResult(final String queryString, final Object[] params, Class<T> resultClass) {
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Entity> T getSingleResult(final String queryString, final Map<String, Object> params, Class<T> resultClass) {
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		return (T) query.uniqueResult();
	}

	@Override
	public void executeUpdate(final String queryString, final Object[] params) {
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i, params[i]);
		}
		query.executeUpdate();
	}

	@Override
	public void executeUpdate(final String queryString, final Map<String, Object> params) {
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (String key : params.keySet()) {
			query = query.setParameter(key, params.get(key));
		}
		query.executeUpdate();
	}

	@Override
	public void flush() {
		getSessionFactory().getCurrentSession().flush();
	}

	@Override
	public void refresh(Entity entity) {
		getSessionFactory().getCurrentSession().refresh(entity);
	}
}
