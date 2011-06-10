package com.dayatang.domain.internal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriteron;


public class ContainsElementCriteron extends QueryCriteron {
	
	private Object value;

	public ContainsElementCriteron(String propName, Object value) {
		super(propName);
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof ContainsElementCriteron))
			return false;
		ContainsElementCriteron castOther = (ContainsElementCriteron) other;
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
	
}
