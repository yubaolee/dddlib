package com.dayatang.service;

import javax.inject.Named;

@Named("service3")
public class MyService3 implements Service {
	@Override
	public String sayHello() {
		return "I am Service 3";
	}
}
