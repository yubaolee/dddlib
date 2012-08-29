package com.dayatang.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.ValidationException;

/**
 * 基于JPA的聚合根实体
 * @author yyang
 *
 */
public abstract class JpaAggregateRootEntity extends AbstractEntity {

	private static final long serialVersionUID = 8301474855157135453L;
	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;

	public static EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManager = entityManagerFactory.createEntityManager();
		}
		return entityManager;
	}

	public static void setEntityManager(EntityManager entityManager) {
		JpaAggregateRootEntity.entityManager = entityManager;
	}
	
	public static void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		JpaAggregateRootEntity.entityManagerFactory = entityManagerFactory;
	}

	public void save() throws ValidationException {
		getEntityManager().merge(this);
	}

	public void remove() {
		getEntityManager().remove(this);
	}

	public static <T extends Entity> boolean exists(Class<T> clazz, Serializable id) {
		T entity = getEntityManager().find(clazz, id);
		return entity == null ? false : true;
	}

	public static <T extends Entity> T get(Class<T> clazz, Serializable id) {
		return getEntityManager().find(clazz, id);
	}

	public static <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		getEntityManager().detach(entity);
		return get(clazz, entity.getId());
	}

	public static <T extends Entity> T load(Class<T> clazz, Serializable id) {
		return getEntityManager().getReference(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Entity> List<T> findAll(Class<T> clazz) {
		return getEntityManager().createQuery("SELECT o FROM " + clazz.getName() + " o").getResultList();
	}
}
