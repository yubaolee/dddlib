/**
 * 
 */
package com.dayatang.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.ValidationException;

/**
 * 聚合的根实体。聚合中的其他实体和值对象，只能通过根实体访问和持久化
 * 
 * @author yang
 * 
 */
@MappedSuperclass
public abstract class AggregateRootEntity extends AbstractEntity {

	private static final long serialVersionUID = 2364892694478974374L;

	private static EntityRepository repository;

	@Transient
	private StringBuilder validationMessageBuilder = new StringBuilder();

	public static EntityRepository getRepository() {
		if (repository == null) {
			repository = InstanceFactory.getInstance(EntityRepository.class);
		}
		return repository;
	}

	public static void setRepository(EntityRepository repository) {
		AggregateRootEntity.repository = repository;
	}

	public void save() throws ValidationException {
		validate();
		getRepository().save(this);
	}

	public void remove() {
		getRepository().remove(this);
	}

	public static <T extends Entity> boolean exists(Class<T> clazz, Serializable id) {
		return getRepository().exists(clazz, id);
	}

	public static <T extends Entity> T get(Class<T> clazz, Serializable id) {
		return getRepository().get(clazz, id);
	}

	public static <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		return getRepository().getUnmodified(clazz, entity);
	}

	public static <T extends Entity> T load(Class<T> clazz, Serializable id) {
		return getRepository().load(clazz, id);
	}

	public static <T extends Entity> List<T> findAll(Class<T> clazz) {
		return getRepository().find(QuerySettings.create(clazz));
	}
}
