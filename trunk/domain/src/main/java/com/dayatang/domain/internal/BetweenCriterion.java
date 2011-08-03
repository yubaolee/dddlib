package com.dayatang.domain.internal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;


public class BetweenCriterion implements QueryCriterion {
	
	private Comparable<?> from;
	
	private Comparable<?> to;

	private String propName;

	public BetweenCriterion(String propName, Comparable<?> from, Comparable<?> to) {
		this.propName = propName;
		this.from = from;
		this.to = to;
	}

	public Comparable<?> getFrom() {
		return from;
	}

	public Comparable<?> getTo() {
		return to;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof BetweenCriterion))
			return false;
		BetweenCriterion castOther = (BetweenCriterion) other;
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

	public String getPropName() {
		return propName;
	}

	
}
