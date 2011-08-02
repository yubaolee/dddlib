package com.dayatang.domain.internal;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;


public class InCriteron implements QueryCriterion {
	
	private Collection<? extends Object> value;
	private String propName;

	public InCriteron(String propName, Collection<? extends Object> value) {
		this.propName = propName;
		this.value = value;
	}
	
	public InCriteron(String propName, Object[] value) {
		this.propName = propName;
		this.value = Arrays.asList(value);
	}

	public Collection<? extends Object> getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof InCriteron))
			return false;
		InCriteron castOther = (InCriteron) other;
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
		return getPropName() + " in collection [" + collectionToString(value) + "]";
	}

	private String collectionToString(Collection<? extends Object> value) {
		return StringUtils.join(value, ",");
	}

	public String getPropName() {
		return propName;
	}
	
}
