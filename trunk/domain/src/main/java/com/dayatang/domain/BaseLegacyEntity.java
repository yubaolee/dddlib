/**
 * 
 */
package com.dayatang.domain;

import javax.persistence.MappedSuperclass;

import com.dayatang.domain.Entity;

/**
 * 抽象实体类，可作为所有遗留系统领域实体的基类。
 * 
 */
@MappedSuperclass
public abstract class BaseLegacyEntity implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 871428741460277125L;

	/**
	 * 
	 * @return
	 */
	public boolean isNew() {
		return getId() == null;
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object arg0);

	@Override
	public abstract String toString();
}
