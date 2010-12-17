package com.dayatang.domain.internal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriteron;


public class BetweenCriteron extends QueryCriteron {
	
	private Object from;
	
	private Object to;

	public BetweenCriteron(String propName, Object from, Object to) {
		super(propName);
		this.from = from;
		this.to = to;
	}

	public Object getFrom() {
		return from;
	}

	public Object getTo() {
		return to;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof BetweenCriteron))
			return false;
		BetweenCriteron castOther = (BetweenCriteron) other;
		return new EqualsBuilder()
			.append(this.getPropName(), castOther.getPropName())
			.append(from, castOther.from)
			.append(this.to, castOther.to)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPropName())
		.append(from).append(to).toHashCode();
	}

	@Override
	public String toString() {
		return getPropName() + " between " + from + " and " + to;
	}

	
}
