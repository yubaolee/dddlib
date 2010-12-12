package com.dayatang.guice;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.dayatang.domain.InstanceFactory;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

public class GuiceProviderTest {

	private GuiceProvider instance;

	@Test
	public void testConstructorFromModule() {
		instance = new GuiceProvider(createModule());
		InstanceFactory.setInstanceProvider(instance);
		assertEquals("I am Service 1", instance.getInstance(Service.class).sayHello());
	}

	@Test
	public void testConstructorFromInjector() {
		instance = new GuiceProvider(createInjector());
		InstanceFactory.setInstanceProvider(instance);
		assertEquals("I am Service 1", instance.getInstance(Service.class).sayHello());
	}

	private Injector createInjector() {
		return Guice.createInjector(createModule());
	}
	
	
	@Test
	public void testGetInstanceClassOfT() {
		instance = new GuiceProvider(createModule());
		InstanceFactory.setInstanceProvider(instance);
		assertEquals("I am Service 1", instance.getInstance(Service.class).sayHello());
	}

	@Test
	public void testGetInstanceStringClassOfT() {
		instance = new GuiceProvider(createModule2());
		InstanceFactory.setInstanceProvider(instance);
		assertEquals("I am Service 1", instance.getInstance(Service.class, "service1").sayHello());
		assertEquals("I am Service 2", instance.getInstance(Service.class, "service2").sayHello());
	}

	private Module createModule() {
		return new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(Service.class).to(MyService1.class).in(Scopes.SINGLETON);
			}
		};
	}

	private Module createModule2() {
		return new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(Service.class).annotatedWith(Names.named("service1")).to(MyService1.class).in(Scopes.SINGLETON);
				binder.bind(Service.class).annotatedWith(Names.named("service2")).to(MyService2.class).in(Scopes.SINGLETON);
			}
		};
	}
}
