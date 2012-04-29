package com.dayatang.spring.persist;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.dayatang.jpa.EntityRepositoryJpa;

/**
 * 通用仓储接口的JPA实现。
 * 该类完全没有给基类EntityRepositoryJpa添加任何新功能。只是为了加上@Repository标注，告知Spring这是一个仓储类，要对它进行异常转译。
 * 
 * @author yyang
 * 
 */
@Repository
public class EntityRepositoryJpaSpring extends EntityRepositoryJpa {
	public EntityRepositoryJpaSpring() {
	}

	public EntityRepositoryJpaSpring(EntityManager entityManager) {
		super(entityManager);
	}
}
