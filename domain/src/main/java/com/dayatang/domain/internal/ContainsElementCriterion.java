package com.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;


public class ContainsElementCriterion implements QueryCriterion {
	
	private Object value;
	private String propName;

	public ContainsElementCriterion(String propName, Object value) {
		this.propName = propName;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof ContainsElementCriterion))
			return false;
		ContainsElementCriterion castOther = (ContainsElementCriterion) other;
		return new EqualsBuilder()
			.append(this.getPropName(), castOther.getPropName())
			.append(value, castOther.value).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPropName()).append(value).toHashCode();
	}

	@Override
	public String toString() {
		return getPropName() + " contains " + value;
	}

	public String getPropName() {
		return propName;
	}
	
}
