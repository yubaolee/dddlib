package com.dayatang.commons.domain.internal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dayatang.commons.domain.QueryCriteron;


public class SizeNotEqCriteron extends QueryCriteron {
	
	private int value;

	public SizeNotEqCriteron(String propName, int value) {
		super(propName);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof SizeNotEqCriteron))
			return false;
		SizeNotEqCriteron castOther = (SizeNotEqCriteron) other;
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
		return "size of " + getPropName() + " != " + value;
	}
}
