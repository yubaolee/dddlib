package com.dayatang.tapestry.factory;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.InstanceProvider;
import com.dayatang.tapestry.factory.TapestryProvider;

public class TapestryIocUtils {

	private static final ThreadLocal<TapestryProvider> tapestryProviderHolder = new ThreadLocal<TapestryProvider>();

	@SuppressWarnings("unchecked")
	public static void initInstanceProvider(Class... iocModules) {
		InstanceFactory.setInstanceProvider(getInstanceProvider(iocModules));
	}

	@SuppressWarnings("unchecked")
	private static InstanceProvider getInstanceProvider(Class... iocModules) {
		TapestryProvider result = tapestryProviderHolder.get();
		if (result == null) {
			result = new TapestryProvider(iocModules);
			tapestryProviderHolder.set(result);
		}
		return result;
	}
}
