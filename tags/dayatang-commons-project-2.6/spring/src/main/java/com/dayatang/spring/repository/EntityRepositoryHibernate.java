package com.dayatang.spring.repository;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.QuerySettings;
import com.dayatang.spring.repository.internal.QueryTranslator;

/**
 * 通用仓储接口的Hibernate实现。
 * 
 * @author yyang
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EntityRepositoryHibernate extends HibernateDaoSupport implements EntityRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityRepositoryHibernate.class);

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#save(com.dayatang.domain.Entity)
	 */
	@Override
	public <T extends Entity> T save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dayatang.domain.EntityRepository#remove(com.dayatang.spring
	 * .domain.Entity)
	 */
	@Override
	public void remove(Entity entity) {
		getHibernateTemplate().delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dayatang.domain.EntityRepository#exists(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> boolean exists(final Class<T> clazz, final Serializable id) {
		return get(clazz, id) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dayatang.domain.EntityRepository#get(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
		return (T) getHibernateTemplate().get(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dayatang.domain.EntityRepository#load(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
		return (T) getHibernateTemplate().load(clazz, id);
	}

	@Override
	public <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		getHibernateTemplate().evict(entity);
		return get(clazz, entity.getId());
	}

	@Override
	public <T extends Entity> List<T> findAll(final Class<T> clazz) {
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createCriteria(clazz).list();
			}
		});
	}

	@Override
	public <T extends Entity> List<T> find(final QuerySettings<T> settings) {
		QueryTranslator translator = new QueryTranslator(settings);
		String queryString = translator.getQueryString(); 
		LOGGER.info("QueryString: '" + queryString + "'");
		List<Object> params = translator.getParams();
		LOGGER.info("params: " + StringUtils.join(params, ", "));
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

	@Override
	public List<Object> find(final String queryString, final Object[] params) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (int i = 0; i < params.length; i++) {
					query = query.setParameter(i, params[i]);
				}
				return query.list();
			}
		});
	}

	@Override
	public List<Object> find(final String queryString, final Map<String, Object> params) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.list();
			}
		});
	}

	@Override
	public List<Object> findByNamedQuery(final String queryName, final Object[] params) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(queryName);
				for (int i = 0; i < params.length; i++) {
					query = query.setParameter(i, params[i]);
				}
				return query.list();
			}
		});
	}

	@Override
	public List<Object> findByNamedQuery(final String queryName, final Map<String, Object> params) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(queryName);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.list();
			}
		});
	}

	@Override
	public <T extends Entity, E extends T> List<T> findByExample(final E example, final ExampleSettings<T> settings) {
		final Example theExample = Example.create(example);
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
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createCriteria(settings.getEntityClass()).add(theExample).list();
			}
		});
	}

	@Override
	public Object getSingleResult(final String queryString, final Object[] params) {
		return getHibernateTemplate().execute(new HibernateCallback<Object>() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (int i = 0; i < params.length; i++) {
					query = query.setParameter(i, params[i]);
				}
				return query.uniqueResult();
			}
		});
	}

	@Override
	public <T extends Entity> T getSingleResult(QuerySettings<T> settings) {
		List<T> results = find(settings);
		return results.isEmpty() ? null : results.get(0);
	}

	@Override
	public Object getSingleResult(final String queryString, final Map<String, Object> params) {
		return getHibernateTemplate().execute(new HibernateCallback<Object>() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
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
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (int i = 0; i < params.length; i++) {
					query = query.setParameter(i, params[i]);
				}
				return query.executeUpdate();
			}
		});
	}

	@Override
	public void executeUpdate(final String queryString, final Map<String, Object> params) {
		getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(queryString);
				for (String key : params.keySet()) {
					query = query.setParameter(key, params.get(key));
				}
				return query.executeUpdate();
			}
		});
	}

}
