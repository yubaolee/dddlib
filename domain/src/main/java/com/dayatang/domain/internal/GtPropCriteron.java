package com.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriteron;

public class GtPropCriteron extends QueryCriteron {
	
	private String otherProp;

	public GtPropCriteron(String propName, String otherProp) {
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
		if (!(other instanceof GtPropCriteron))
			return false;
		GtPropCriteron castOther = (GtPropCriteron) other;
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
		return getPropName() + " > " + otherProp;
	}

	
}
