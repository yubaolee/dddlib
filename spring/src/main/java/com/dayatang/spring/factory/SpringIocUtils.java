package com.dayatang.spring.factory;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.InstanceProvider;

public class SpringIocUtils {

	private static final ThreadLocal<SpringProvider> providerHolder = new ThreadLocal<SpringProvider>();

	public static void initInstanceProvider(Class<?>... annotatedClasses) {
		InstanceFactory.setInstanceProvider(getInstanceProvider(annotatedClasses));
	}

	private static InstanceProvider getInstanceProvider(Class<?>[] annotatedClasses) {
		SpringProvider result = providerHolder.get();
		if (result == null) {
			result = new SpringProvider(annotatedClasses);
			providerHolder.set(result);
		}
		return result;
	}

	public static void initInstanceProvider(String... acFiles) {
		InstanceFactory.setInstanceProvider(getInstanceProvider(acFiles));
	}

	private static InstanceProvider getInstanceProvider(String... acFiles) {
		SpringProvider result = providerHolder.get();
		if (result == null) {
			result = new SpringProvider(acFiles);
			providerHolder.set(result);
		}
		return result;
	}

}
