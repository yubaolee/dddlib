package com.dayatang.security.domain;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.QuerySettings;

/**
 * 用户角色分配实体。表明用户user在机构organization中担任role角色。这是用户认证的核心对象。
 * 
 * @author yyang
 * 
 */
@Entity
@Table(name = "role_assignments", 
	uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id", "org_id"}))
@Cacheable
public class RoleAssignment extends AbstractEntity {

	private static final long serialVersionUID = -7535565535009298913L;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	RoleAssignment() {
		super();
	}

	public RoleAssignment(User user, Role role) {
		super();
		this.user = user;
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public static RoleAssignment get(User user, Role role) {
		return getRepository().getSingleResult(QuerySettings.create(RoleAssignment.class)
				.eq("user", user).eq("role", role));
	}
	
	public static List<RoleAssignment> findByUser(User user) {
		return getRepository().find(QuerySettings.create(RoleAssignment.class)
				.eq("user", user));
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
		return new EqualsBuilder().append(this.getUser(), that.getUser())
			.append(this.getRole(), that.getRole())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(user).append(role).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(user).append(role).toString();
	}

}
