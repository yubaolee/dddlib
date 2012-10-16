package com.dayatang.security.domain;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.QuerySettings;

/**
 * 用户角色分配实体。表明用户assignable在机构organization中担任role角色。这是用户认证的核心对象。
 * 
 * @author yyang
 * 
 */
@Entity
@Table(name = "role_assignments")
@Cacheable
public class RoleAssignment extends AbstractEntity {

	private static final long serialVersionUID = -7535565535009298913L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "assignable_id", nullable = false)
	private Assignable assignable;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	RoleAssignment() {
		super();
	}

	public RoleAssignment(Assignable assignable, Role role) {
		super();
		this.assignable = assignable;
		this.role = role;
	}

	public Assignable getAssignable() {
		return assignable;
	}

	public void setAssignable(Assignable assignable) {
		this.assignable = assignable;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public static RoleAssignment get(Assignable assignable, Role role) {
		return getRepository().getSingleResult(QuerySettings.create(RoleAssignment.class)
				.eq("assignable", assignable).eq("role", role));
	}
	
	public static List<RoleAssignment> findByAssignable(Assignable assignable) {
		return getRepository().find(QuerySettings.create(RoleAssignment.class)
				.eq("assignable", assignable));
	}
	
	public static List<RoleAssignment> findByRole(Role role) {
		return getRepository().find(QuerySettings.create(RoleAssignment.class)
				.eq("role", role));
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RoleAssignment)) {
			return false;
		}
		RoleAssignment that = (RoleAssignment) other;
		return new EqualsBuilder().append(this.getAssignable(), that.getAssignable())
			.append(this.getRole(), that.getRole())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(assignable).append(role).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(assignable).append(role).toString();
	}

}
