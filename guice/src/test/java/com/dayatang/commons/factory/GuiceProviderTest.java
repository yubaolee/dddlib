package com.dayatang.commons.factory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.dayatang.commons.domain.InstanceFactory;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

public class GuiceProviderTest {

	private GuiceProvider guiceProvider;

	@Test
	public void testConstructorFromModule() {
		guiceProvider = new GuiceProvider(createModule());
		InstanceFactory.setInstanceProvider(guiceProvider);
		assertEquals("I am Sruvice 1", guiceProvider.getInstance(Service.class).sayHello());
	}

	private Module createModule() {
		return new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(Service.class).to(MyService1.class).in(Scopes.SINGLETON);
			}
		};
	}

	@Test
	public void testConstructorFromInjector() {
		guiceProvider = new GuiceProvider(createInjector());
		InstanceFactory.setInstanceProvider(guiceProvider);
		assertEquals("I am Sruvice 1", guiceProvider.getInstance(Service.class).sayHello());
	}

	private Injector createInjector() {
		return Guice.createInjector(createModule());
	}
	
	
	@Test
	public void testGetInstanceClassOfT() {
		guiceProvider = new GuiceProvider(createModule());
		InstanceFactory.setInstanceProvider(guiceProvider);
		assertEquals("I am Sruvice 1", guiceProvider.getInstance(Service.class).sayHello());
	}

	@Test
	public void testGetInstanceStringClassOfT() {
		guiceProvider = new GuiceProvider(createModule2());
		InstanceFactory.setInstanceProvider(guiceProvider);
		assertEquals("I am Sruvice 1", guiceProvider.getInstance(Service.class, "service1").sayHello());
		assertEquals("I am Sruvice 2", guiceProvider.getInstance(Service.class, "service2").sayHello());
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
