package com.dayatang.domain;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.Entity;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.InstanceProvider;

public class InstanceFactoryTest {

	private InstanceProvider instanceProvider;

	private Mockery context = new Mockery();

	@Before
	public void setUp() throws Exception {
		instanceProvider = context.mock(InstanceProvider.class);
		InstanceFactory.setInstanceProvider(instanceProvider);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		
		final Entity entity = new AbstractEntity() {

			private static final long serialVersionUID = 4007757659440622574L;

			@Override
			public boolean equals(Object other) {
				return this == other;
			}

			@Override
			public int hashCode() {
				return 0;
			}

			@Override
			public String toString() {
				return null;
			}
			
		};
		context.checking(new Expectations() {
			{
				oneOf(instanceProvider).getInstance(Entity.class);
				will(returnValue(entity));
			}
		});
		assertEquals(entity, InstanceFactory.getInstance(Entity.class));
		context.assertIsSatisfied();
	}

}
