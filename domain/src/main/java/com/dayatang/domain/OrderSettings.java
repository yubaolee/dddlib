package com.dayatang.domain;

import java.security.InvalidParameterException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OrderSettings {

	private boolean ascending = true;
	private String propName;

	public boolean isAscending() {
		return ascending;
	}

	public String getPropName() {
		return propName;
	}

	private OrderSettings(boolean ascending, String propName) {
		if (StringUtils.isEmpty(propName)) {
			throw new InvalidParameterException("propName should not be empty!");
		}
		this.ascending = ascending;
		this.propName = propName;
	}

	public static OrderSettings asc(String propName) {
		return new OrderSettings(true, propName);
	}

	public static OrderSettings desc(String propName) {
		return new OrderSettings(false, propName);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof OrderSettings))
			return false;
		OrderSettings castOther = (OrderSettings) other;
		return new EqualsBuilder().append(ascending, castOther.ascending)
				.append(propName, castOther.propName).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(ascending).append(propName)
				.toHashCode();
	}

	@Override
	public String toString() {
		return propName + (ascending ? " ascending" : "descending");
	}
}
