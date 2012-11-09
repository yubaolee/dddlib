package com.dayatang.commons.repository;

import bitronix.tm.TransactionManagerServices;

/**
 * 通过开源JTA实现库BTM创建数据源并绑定到JNDI.
 * @author yyang
 *
 */
public class BtmUtils {

	private static H2Server server = H2Server.getSingleton();

	private static final String DATASOURCE_CONF_FILE = "/datasources.properties";
	
	private BtmUtils() {
	}

	public static void setupDataSource() throws Exception {
        server.start();
        String resourceConfFile = BtmUtils.class.getResource(DATASOURCE_CONF_FILE).toURI().getPath();
        TransactionManagerServices.getConfiguration().setResourceConfigurationFilename(resourceConfFile);
	}

    public static void closeDataSource() throws Exception {
		server.shutdown();
    }
}
