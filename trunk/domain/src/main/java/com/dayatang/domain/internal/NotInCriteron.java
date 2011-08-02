package com.dayatang.domain.internal;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;


public class NotInCriteron implements QueryCriterion {
	
	private Collection<? extends Object> value;
	private String propName;

	public NotInCriteron(String propName, Collection<? extends Object> value) {
		this.propName = propName;
		this.value = value;
	}
	
	public NotInCriteron(String propName, Object[] value) {
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
		if (!(other instanceof NotInCriteron))
			return false;
		NotInCriteron castOther = (NotInCriteron) other;
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
		return getPropName() + " not in collection [" + collectionToString(value) + "]";
	}

	private String collectionToString(Collection<? extends Object> value) {
		return StringUtils.join(value, ",");
	}

	public String getPropName() {
		return propName;
	}
	
}
