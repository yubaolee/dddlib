package com.dayatang.security.domain;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.AbstractEntity;

@Entity
@Table(name = "permission_granting")
@Cacheable
public class PermissionGranting extends AbstractEntity {

	private static final long serialVersionUID = 6173530235500608015L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "grantable_id", nullable = false)
	private Grantable grantable;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "permission_id", nullable = false)
	private Permission permission;
	
	protected PermissionGranting() {
	}

	public PermissionGranting(Grantable grantable, Permission permission) {
		super();
		this.grantable = grantable;
		this.permission = permission;
	}

	public Grantable getGrantable() {
		return grantable;
	}

	public void setGrantable(Grantable grantable) {
		this.grantable = grantable;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(grantable).append(permission).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (! (obj instanceof PermissionGranting)) {
			return false;
		}
		PermissionGranting that = (PermissionGranting) obj;
		return new EqualsBuilder().append(this.getGrantable(), that.getGrantable()).append(this.getPermission(), that.getPermission()).isEquals();
	}

	@Override
	public String toString() {
		return "PermissionGranting [grantable=" + grantable + ", permission=" + permission + "]";
	}

}
