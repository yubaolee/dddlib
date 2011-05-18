package com.dayatang.provider;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dayatang.domain.InstanceProvider;
import com.dayatang.service.MyService2;
import com.dayatang.service.Service;

public class Jdk6InstanceProviderTest {

	@Test
	public void testGetInstance() {
		InstanceProvider provider = new Jdk6InstanceProvider();
		Service service = provider.getInstance(Service.class);
		//断言service的类型是MyService2或它的子类。
		assertTrue(MyService2.class.isAssignableFrom(service.getClass()));
	}

}
