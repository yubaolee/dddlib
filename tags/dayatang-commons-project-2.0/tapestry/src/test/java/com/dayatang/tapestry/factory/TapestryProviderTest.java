package com.dayatang.tapestry.factory;

import static org.junit.Assert.*;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.tapestry.factory.TapestryProvider;

public class TapestryProviderTest {

	private TapestryProvider tapestryProvider;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		InstanceFactory.setInstanceProvider(null);
	}

	@Test
	public void testConstructorFromModule() {
		tapestryProvider = new TapestryProvider(WithoutAnnotationModule.class);
		InstanceFactory.setInstanceProvider(tapestryProvider);
		assertEquals("I am Sruvice 1", tapestryProvider.getInstance(Service.class).sayHello());
	}

	@Test
	public void testConstructorFromRegistry() {
		tapestryProvider = new TapestryProvider(createRegistry(WithoutAnnotationModule.class));
		InstanceFactory.setInstanceProvider(tapestryProvider);
		assertEquals("I am Sruvice 1", tapestryProvider.getInstance(Service.class).sayHello());
	}

	@SuppressWarnings("unchecked")
	private Registry createRegistry(Class... moduleClass) {
		RegistryBuilder builder = new RegistryBuilder();
		for (Class clazz : moduleClass) {
			builder.add(clazz);
		}
		return builder.build();
	}
	
	@Test
	public void testGetInstanceClassOfT() {
		tapestryProvider = new TapestryProvider(WithoutAnnotationModule.class);
		InstanceFactory.setInstanceProvider(tapestryProvider);
		assertEquals("I am Sruvice 1", tapestryProvider.getInstance(Service.class).sayHello());
	}

	@Test
	public void testGetInstanceStringClassOfT() {
		tapestryProvider = new TapestryProvider(WithAnnotationModule.class);
		InstanceFactory.setInstanceProvider(tapestryProvider);
		assertEquals("I am Sruvice 1", tapestryProvider.getInstance(Service.class, "service1").sayHello());
		assertEquals("I am Sruvice 2", tapestryProvider.getInstance(Service.class, "service2").sayHello());
	}

}

