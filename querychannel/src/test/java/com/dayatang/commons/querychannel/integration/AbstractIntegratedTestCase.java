package com.dayatang.commons.querychannel.integration;

import org.apache.tapestry5.hibernate.HibernateCoreModule;
import org.apache.tapestry5.ioc.services.TapestryIOCModule;
import org.junit.BeforeClass;

import com.dayatang.commons.factory.TapestryIocUtils;
import com.dayatang.commons.querychannel.infra.tapestry.QueryChannelModule;

public abstract class AbstractIntegratedTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TapestryIocUtils.initInstanceProvider(
				TapestryIOCModule.class, 
				HibernateCoreModule.class,
				AppModule.class,
				QueryChannelModule.class);
	}
}
