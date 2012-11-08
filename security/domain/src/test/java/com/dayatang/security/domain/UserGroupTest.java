package com.dayatang.security.domain;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class UserGroupTest {
	
	private UserGroup instance;

	@Before
	public void setUp() throws Exception {
		instance = new UserGroup("My Group");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void addAndRemoveUser() {
		User user = new User("user1");
		assertFalse(instance.getUsers().contains(user));
		instance.addUser(user);
		assertTrue(instance.getUsers().contains(user));
		instance.removeUser(user);
		assertFalse(instance.getUsers().contains(user));
	}

}
