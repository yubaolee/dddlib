/**
 * 
 */
package com.dayatang.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 抽象实体类，可作为所有领域实体的基类，提供ID和版本属性。
 * 
 */
@MappedSuperclass
public abstract class BaseEntity implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2364892694478974374L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	protected Long id;

	@Version
	@Column(name = "VERSION")
	protected int version;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

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
	public String toString() {
		// return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
		// .append(recordId).append(tariffNo).toString();
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
