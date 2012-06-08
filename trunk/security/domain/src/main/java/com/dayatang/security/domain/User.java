package com.dayatang.security.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.QuerySettings;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY", discriminatorType = DiscriminatorType.STRING)
@Table(name = "users")
@Cacheable
public class User extends AbstractEntity {

	private static final long serialVersionUID = 5429887402331650527L;

	@NotNull
	@Column(nullable = false, unique = true)
	private String username;

	private String password;
	
	private String email;
	
	private String mobile;
	
	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "account_expired")
	private boolean accountExpired = false;

	@Column(name = "account_locked")
	private boolean accountLocked = false;

	@Column(name = "credentials_expired")
	private boolean credentialsExpired = false;

	@Column(name = "account_disabled")
	private boolean disabled = false;

	public User() {
	}

	public User(String username) {
		this.username = username;
	}
	
	public User(String username, String password, String email, String mobile, String phoneNumber) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.mobile = mobile;
		this.phoneNumber = phoneNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String tel) {
		this.phoneNumber = tel;
	}

	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void lock() {
		accountLocked = true;
		save();
	}
	
	public void unlock() {
		accountLocked = false;
		save();
	}

	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void disable() {
		disabled = true;
		save();
	}
	
	public void enable() {
		disabled = false;
		save();
	}

	public Set<Role> getRoles() {
		Set<Role> results = new HashSet<Role>();
		for (RoleAssignment assignment : RoleAssignment.findByUser(this)) {
			results.add(assignment.getRole());
		}
		return results;
	}
	
	public boolean hasRole(Role role) {
		return getRoles().contains(role);
	}
	
	public boolean isPermitted(Permission permission) {
		List<RoleAssignment> assignments = RoleAssignment.findByUser(this);
		for (RoleAssignment assignment : assignments) {
			if (assignment.getRole().isGranted(permission)) {
				return true;
			}
		}
		return false;
	}
		
	@Override
	public void remove() {
		for (RoleAssignment assignment : RoleAssignment.findByUser(this)) {
			assignment.remove();
		}
		super.remove();
	}

	public static User get(Long id) {
		return getRepository().get(User.class, id);
	}

	public static User getByUsername(String username) {
		return getRepository().getSingleResult(QuerySettings.create(User.class).eq("username", username));
	}

	public static List<User> findAll() {
		return getRepository().find(QuerySettings.create(User.class));
	}

	public static List<User> findEnabled() {
		return getRepository().find(QuerySettings.create(User.class).eq("enabled", true));
	}

	public static List<User> findDisabled() {
		return getRepository().find(QuerySettings.create(User.class).eq("enabled", false));
	}

	public static List<User> findActive() {
		return getRepository().find(QuerySettings.create(User.class)
				.eq("enabled", true).eq("accountLocked", false)
				.eq("accountExpired", false).eq("credentialsExpired", false));
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof User)) {
			return false;
		}
		User that = (User) other;
		return new EqualsBuilder().append(this.getUsername(), that.getUsername()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(username).toHashCode();
	}

	@Override
	public String toString() {
		return username;
	}
}
