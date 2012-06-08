package com.dayatang.security.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.dayatang.domain.QuerySettings;

@Entity
@DiscriminatorValue("ROLE")
public class Role extends Grantable {

	private static final long serialVersionUID = 5429887402331650527L;

	public static final long SYSTEM_ROLE_ID = 1L;
	
	private String description;

	@ManyToMany
	@JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Permission> permissions = new HashSet<Permission>();

	public Role() {
	}

	public Role(String name) {
		super(name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void grantPermissions(Permission... permissions) {
		this.permissions.addAll(Arrays.asList(permissions));
		save();
	}

	public void withdrawPermissions(Permission... permissions) {
		this.permissions.removeAll(Arrays.asList(permissions));
		save();
	}

	public void removeAllPermissions() {
		this.permissions.clear();
	}

	public void grantPermissions(Set<Permission> permissions) {
		this.permissions.addAll(permissions);
		save();
	}

	public void withdrawPermissions(Set<Permission> permissions) {
		this.permissions.removeAll(permissions);
		save();
	}

	public Set<Permission> getPermissions() {
		return Collections.unmodifiableSet(permissions);
	}

	public boolean isGranted(Permission permission) {
		return permissions.contains(permission);
	}

	@Override
	public void remove() {
		for (RoleAssignment assignment : RoleAssignment.findByRole(this)) {
			assignment.remove();
		}
		super.remove();
	}

	public static Role get(Long id) {
		return getRepository().get(Role.class, id);
	}

	public static List<Role> findAll() {
		return getRepository().find(QuerySettings.create(Role.class));
	}

	public static Role findBy(String name) {
		return getRepository().getSingleResult(QuerySettings.create(Role.class).eq("name", name));
	}
	
	public static Role findBy(String name,String description) {
		return getRepository().getSingleResult(QuerySettings.create(Role.class).eq("name", name).eq("description", description));
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Role)) {
			return false;
		}
		Role that = (Role) other;
		return new EqualsBuilder().append(this.getName(), that.getName()).isEquals();
	}
}
