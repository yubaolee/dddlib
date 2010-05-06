package com.dayatang.domain.internal;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dayatang.domain.QueryCriteron;


public class NotInCriteron extends QueryCriteron {
	
	private Collection<? extends Object> value;

	public NotInCriteron(String propName, Collection<? extends Object> value) {
		super(propName);
		this.value = value;
	}
	
	public NotInCriteron(String propName, Object[] value) {
		super(propName);
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
		return getPropName() + " in collection [" + collectionToString(value) + "]";
	}

	private String collectionToString(Collection<? extends Object> value) {
		return StringUtils.join(value, ",");
	}
	
}
