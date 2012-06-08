package com.dayatang.security.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.CompareToBuilder;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.QuerySettings;

@Entity
@Table(name = "permissions")
@Cacheable
public class Permission extends AbstractEntity implements Comparable<Permission> {

	private static final long serialVersionUID = 8083808914666752782L;

	@NotNull
	@Column(nullable = false, unique = true)
	private String name;

	private String description;

	@Column(name = "sort_order")
	private int sortOrder;

	@ManyToOne
	@JoinTable(name = "permission_relation", joinColumns = @JoinColumn(name = "child_id"), inverseJoinColumns = @JoinColumn(name = "parent_id"))
	private Permission parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<Permission> children = new HashSet<Permission>();

	public Permission() {
	}

	public Permission(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Permission getParent() {
		return parent;
	}

	public void setParent(Permission parent) {
		this.parent = parent;
	}

	public static List<Permission> getRoot() {
		List<Permission> results = new ArrayList<Permission>();

		results.addAll(getRepository().find(QuerySettings.create(Permission.class).isNull("parent")));

		return results;
	}

	public List<Permission> getChildren() {
		List<Permission> results = new ArrayList<Permission>(children);
		Collections.sort(results);
		return results;
	}

	public void addChild(Permission... childrenToAdd) {
		children.addAll(Arrays.asList(childrenToAdd));
	}

	public void removeChild(Permission... childrenToRemove) {
		children.removeAll(Arrays.asList(childrenToRemove));
		for (Permission permission : childrenToRemove) {
			permission.remove();
		}
	}

	public void grantTo(Role... roles) {
		for (Role role : roles) {
			role.grantPermissions(this);
		}
	}

	public void grantTo(Set<Role> roles) {
		for (Role role : roles) {
			role.grantPermissions(this);
		}
	}

	public void withdrawFrom(Role... roles) {
		for (Role role : roles) {
			role.withdrawPermissions(this);
		}
	}

	public void withdrawFrom(Set<Role> roles) {
		for (Role role : roles) {
			role.withdrawPermissions(this);
		}
	}

	public static Permission getByName(String name) {
		return getRepository().getSingleResult(QuerySettings.create(Permission.class).eq("name", name));
	}

	public int compareTo(final Permission other) {
		return new CompareToBuilder().append(sortOrder, other.sortOrder).toComparison();
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Permission))
			return false;
		Permission that = (Permission) other;
		return new EqualsBuilder().append(name, that.name)
				.append(this.getParent(), that.getParent()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(-23053695, -979339559).append(name).append(parent).toHashCode();
	}

	@Override
	public String toString() {
		return "Permission [name=" + name + "]";
	}

}
