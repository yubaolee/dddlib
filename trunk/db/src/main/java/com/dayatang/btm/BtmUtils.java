package com.dayatang.btm;

import java.net.URISyntaxException;
import java.net.URL;

import bitronix.tm.TransactionManagerServices;

/**
 * 通过开源JTA实现库BTM创建数据源并绑定到JNDI.
 * @author yyang
 *
 */
public class BtmUtils {


	private static final String DATASOURCE_CONF_FILE = "/datasources.properties";
	
	/**
	 * 从类路径读入数据源配置文件
	 * @param resourceFile
	 * @return
	 */
	public static BtmUtils readResourceConfigurationFromClasspath(String resourceFile) {
		return new BtmUtils(getFileFromClasspath(resourceFile));
	}
	
	private static String getFileFromClasspath(String resourceFile) {
		URL url = BtmUtils.class.getResource(resourceFile);
		if (url == null) {
			throw new RuntimeException("File " + resourceFile + " does not exist!");
		}
		try {
			return url.toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException("File " + resourceFile + " does not exist!");
		}
	}

	/**
	 * 从文件系统读入数据源配置文件
	 * @param confFile
	 * @return
	 */
	public static BtmUtils readResourceConfigurationFile(String confFile) {
		return new BtmUtils(confFile);
	}

	
	private String confFile;
	
	public BtmUtils() {
		confFile = DATASOURCE_CONF_FILE;
	}

	private BtmUtils(String confFile) {
		this.confFile = confFile;
	}

	/**
	 * 根据数据源配置设置JNDI和事务管理器
	 * @throws Exception
	 */
	public void setupDataSource() throws Exception {
        TransactionManagerServices.getConfiguration().setResourceConfigurationFilename(confFile);
	}

	/**
	 * 关闭BTM服务并释放资源
	 * @throws Exception
	 */
    public void closeDataSource() throws Exception {
    	TransactionManagerServices.getConfiguration().shutdown();
    }

}
