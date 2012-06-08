package com.dayatang.security.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang3.builder.EqualsBuilder;


@Entity
@DiscriminatorValue("GROUP")
@NamedQueries({
	@NamedQuery(name = "getParent", query = "select o from UserGroup o where o.leftValue < ? and o.rightValue > ? and level = ?"),
	@NamedQuery(name = "getChildren", query = "select o from UserGroup o where o.leftValue > ? and o.rightValue < ? and level = ?")
})
public class UserGroup extends Assignable {

	private static final long serialVersionUID = -2299149150649121770L;
	
	@ManyToMany
	@JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<User> users = new HashSet<User>();
	
	@Column(name = "left_value")
	private Integer leftValue;
	
	@Column(name = "right_value")
	private Integer rightValue;
	
	private Integer level;

	public UserGroup() {
	}

	public UserGroup(String name) {
		super(name);
	}

	public Integer getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(Integer leftValue) {
		this.leftValue = leftValue;
	}

	public Integer getRightValue() {
		return rightValue;
	}

	public void setRightValue(Integer rightValue) {
		this.rightValue = rightValue;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void addUser(User user) {
		users.add(user);
	}

	public void removeUser(User user) {
		users.remove(user);
	}

	public static UserGroup get(long id) {
		return getRepository().get(UserGroup.class, id);
	}
	
	/**
	 * 获得上级机构。
	 * @return
	 */
	public UserGroup getParent() {
		List<UserGroup> list = getRepository().findByNamedQuery("getParent", 
				new Object[] {leftValue, rightValue, level - 1}, UserGroup.class);
		return list == null || list.isEmpty() ? null : list.get(0);
	}
	
	/**
	 * 获得下属机构。
	 * @return
	 */
	public Set<UserGroup> getChildren() {
		return new HashSet<UserGroup>(getRepository().findByNamedQuery("getChildren", 
				new Object[] {leftValue, rightValue, level + 1}, UserGroup.class));
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
