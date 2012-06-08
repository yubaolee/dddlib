package com.dayatang.security.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;

@Entity
@DiscriminatorValue("GROUP")
public class UserGroup extends Assignable {

	private static final long serialVersionUID = -2299149150649121770L;

	public UserGroup() {
	}

	public UserGroup(String name) {
		super(name);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof UserGroup)) {
			return false;
		}
		UserGroup that = (UserGroup) other;
		return new EqualsBuilder().append(this.getName(), that.getName()).isEquals();
	}

}
