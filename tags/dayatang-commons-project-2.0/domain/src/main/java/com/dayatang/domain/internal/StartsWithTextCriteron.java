package com.dayatang.domain.internal;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import com.dayatang.domain.QueryCriteron;


public class StartsWithTextCriteron extends QueryCriteron {
	
	private String value;

	public StartsWithTextCriteron(String propName, String value) {
		super(propName);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof StartsWithTextCriteron))
			return false;
		StartsWithTextCriteron castOther = (StartsWithTextCriteron) other;
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
		return getPropName() + " like '" + value + "*'";
	}
}
