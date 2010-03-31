package com.dayatang.commons.factory;

import org.apache.tapestry5.ioc.ServiceBinder;

public class WithoutAnnotationModule {
	public static void bind(ServiceBinder binder) {
		binder.bind(Service.class, MyService1.class);
	}
}
