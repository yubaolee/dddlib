package com.dayatang.tapestry.factory;

import org.apache.tapestry5.ioc.ServiceBinder;

public class WithAnnotationModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(Service.class, MyService1.class).withId("service1");
		binder.bind(Service.class, MyService2.class).withId("service2");
	}

}
