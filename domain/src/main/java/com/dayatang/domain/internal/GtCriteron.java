package com.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;


public class GtCriteron<T> implements QueryCriterion {
	
	private Comparable<T> value;
	private String propName;

	public GtCriteron(String propName, Comparable<T> value) {
		this.propName = propName;
		this.value = value;
	}

	public Comparable<T> getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof GtCriteron))
			return false;
		GtCriteron<T> castOther = (GtCriteron<T>) other;
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
		return getPropName() + " > " + value;
	}

	public String getPropName() {
		return propName;
	}
	
}
