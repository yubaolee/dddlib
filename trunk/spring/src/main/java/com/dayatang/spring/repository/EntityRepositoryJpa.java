package com.dayatang.spring.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;

import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.QuerySettings;
import com.dayatang.spring.repository.internal.JpaCriteriaQueryBuilder;

/**
 * 通用仓储接口的Hibernate实现。
 * 
 * @author yyang
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EntityRepositoryJpa implements EntityRepository {

	private EntityManagerFactory entityManagerFactory;

	public EntityRepositoryJpa() {
		super();
	}

	public EntityRepositoryJpa(EntityManagerFactory entityManagerFactory) {
		setEntityManagerFactory(entityManagerFactory);
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public JpaTemplate getJpaTemplate() {
		return new JpaTemplate(entityManagerFactory);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#save(com.dayatang.domain.Entity)
	 */
	@Override
	public <T extends Entity> T save(T entity) {
		return getJpaTemplate().merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#remove(com.dayatang.domain.Entity)
	 */
	@Override
	public void remove(Entity entity) {
		getJpaTemplate().remove(get(entity.getClass(), entity.getId()));
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#exists(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> boolean exists(final Class<T> clazz, final Serializable id) {
		T entity = getJpaTemplate().find(clazz, id);
		return entity != null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#get(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> T get(final Class<T> clazz, final Serializable id) {
		return getJpaTemplate().find(clazz, id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.dayatang.domain.EntityRepository#load(java.io.Serializable)
	 */
	@Override
	public <T extends Entity> T load(final Class<T> clazz, final Serializable id) {
		return getJpaTemplate().getReference(clazz, id);
	}

	@Override
	public <T extends Entity> T getUnmodified(final Class<T> clazz, final T entity) {
		return get(clazz, entity.getId());
	}

	@Override
	public <T extends Entity> List<T> findAll(final Class<T> clazz) {
		return getJpaTemplate().executeFind(new JpaCallback() {
			
			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				String queryString = "select o from " + clazz.getName() + " as o"; 
				Query query = em.createQuery(queryString);
				return query.getResultList();
			}
		});
	}

	@Override
	public <T extends Entity> List<T> find(final QuerySettings<T> settings) {
		return getJpaTemplate().executeFind(new JpaCallback() {
			
			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				CriteriaQuery<T> criteriaQuery = JpaCriteriaQueryBuilder.createCriteriaQuery(settings, em);
				Query query = em.createQuery(criteriaQuery);
				query.setFirstResult(settings.getFirstResult());
				if (settings.getMaxResults() > 0) {
					query.setMaxResults(settings.getMaxResults());
				}
				return query.getResultList();
			}
		});
	}

	@Override
	public List<Object> find(final String queryString, final Object[] params) {
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
	public List<Object> find(final String queryString, final Map<String, Object> params) {
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
	public List<Object> findByNamedQuery(final String queryName, final Object[] params) {
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
	public List<Object> findByNamedQuery(final String queryName, final Map<String, Object> params) {
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
		return getJpaTemplate().execute(new JpaCallback<Object>() {
			
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
	public Object getSingleResult(final String queryString, final Map<String, Object> params) {
		return getJpaTemplate().execute(new JpaCallback<Object>() {
			
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
	public void executeUpdate(final String queryString, final Map<String, Object> params) {
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
