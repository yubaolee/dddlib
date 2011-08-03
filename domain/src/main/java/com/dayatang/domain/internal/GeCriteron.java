package com.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;


public class GeCriteron implements QueryCriterion {
	
	private Comparable<?> value;
	private String propName;

	public GeCriteron(String propName, Comparable<?> value) {
		this.propName = propName;
		this.value = value;
	}

	public Comparable<?> getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof GeCriteron))
			return false;
		GeCriteron castOther = (GeCriteron) other;
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
		return getPropName() + " >= " + value;
	}

	public String getPropName() {
		return propName;
	}
	
}
