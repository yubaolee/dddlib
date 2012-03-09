package com.dayatang.domain.internal;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QueryException;


public class NotInCriterion implements QueryCriterion {
	
	private String propName;

	private Collection<? extends Object> value;

	public NotInCriterion(String propName, Collection<? extends Object> value) {
		if (StringUtils.isEmpty(propName)) {
			throw new QueryException("Property name is null!");
		}
		if (value == null || value.isEmpty()) {
			throw new QueryException("Value collection is null or empty!");
		}
		this.propName = propName;
		this.value = value;
	}
	
	public NotInCriterion(String propName, Object[] value) {
		if (StringUtils.isEmpty(propName)) {
			throw new QueryException("Property name is null!");
		}
		if (value == null || value.length == 0) {
			throw new QueryException("Value array is null or empty!");
		}
		this.propName = propName;
		this.value = Arrays.asList(value);
	}

	public String getPropName() {
		return propName;
	}

	public Collection<? extends Object> getValue() {
		return value;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof NotInCriterion))
			return false;
		NotInCriterion castOther = (NotInCriterion) other;
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
	
}