package com.dayatang.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** 
 * 值
 * 
 * @author chencao
 *
 */
@Embeddable
public class Value implements ValueObject {

	private static final long serialVersionUID = 4254026874177282302L;

	@Enumerated(EnumType.STRING)
	@Column(name = "data_type")
	private DataType dataType;

	@Column(name = "obj_value")
	private String value;

	public Value() {
		super();
	}

	public Value(DataType dataType, String value) {
		super();
		this.dataType = dataType;
		this.value = value;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(dataType).append(value).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Value)) {
			return false;
		}
		Value that = (Value) other;
		return new EqualsBuilder()
			.append(this.getDataType(), that.getDataType())
			.append(this.getValue(), that.getValue())
			.isEquals();
	}

	@Override
	public String toString() {
		return value;
	}

}
