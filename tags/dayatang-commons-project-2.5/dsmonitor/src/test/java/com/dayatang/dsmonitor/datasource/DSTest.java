package com.dayatang.dsmonitor.datasource;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.dsmonitor.dao.Dao;
import com.dayatang.dsmonitor.monitor.GeminiConnectionLogTimeoutMonitor;
import com.dayatang.springtest.PureSpringTestCase;

public class DSTest extends PureSpringTestCase {

	@Test
	public void testApp() throws InterruptedException {
		GeminiConnectionLogTimeoutMonitor monitor = InstanceFactory
				.getInstance(GeminiConnectionLogTimeoutMonitor.class,
						"connectionMonitor");
		Assert.assertEquals(0, monitor.getTimeoutConnections().size());
	}

	@Test
	public void testAppNotCloseConnection() throws InterruptedException {
		Dao dao = InstanceFactory.getInstance(Dao.class, "baseDao");
		List list = dao.listResultWithoutCloseConnection(
				"from CommonsTestChild", new Object[] {});

		Thread.sleep(12000);

		GeminiConnectionLogTimeoutMonitor monitor = InstanceFactory
				.getInstance(GeminiConnectionLogTimeoutMonitor.class,
						"connectionMonitor");
		Assert.assertEquals(1, monitor.getAliveTimeoutConnections().size());
		Assert.assertEquals(0, monitor.getClosedTimeoutConnections().size());

		Thread.sleep(10000);
	}

}