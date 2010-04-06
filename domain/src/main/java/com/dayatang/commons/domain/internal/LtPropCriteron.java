package com.dayatang.commons.domain.internal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dayatang.commons.domain.QueryCriteron;

public class LtPropCriteron extends QueryCriteron {
	
	private String otherProp;

	public LtPropCriteron(String propName, String otherProp) {
		super(propName);
		this.otherProp = otherProp;
	}

	public String getOtherProp() {
		return otherProp;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof LtPropCriteron))
			return false;
		LtPropCriteron castOther = (LtPropCriteron) other;
		return new EqualsBuilder()
			.append(this.getPropName(), castOther.getPropName())
			.append(otherProp, castOther.otherProp).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPropName()).append(otherProp).toHashCode();
	}

	@Override
	public String toString() {
		return getPropName() + " < " + otherProp;
	}

	
}
