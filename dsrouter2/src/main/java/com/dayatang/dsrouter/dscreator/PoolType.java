package com.dayatang.dsrouter.dscreator;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public enum PoolType {
	
	C3P0 {
		@Override
		public DataSource createDataSource(String tenantId) throws InstantiationException,
				IllegalAccessException, InvocationTargetException, PropertyVetoException {
			ComboPooledDataSource result =  ComboPooledDataSource.class.newInstance();
			fillProperties(result, properties);
			result.setDriverClass(properties.getProperty(Constants.JDBC_DRIVER_CLASS_NAME));
			DbType dbType = DbType.valueOf(properties.getProperty(Constants.DB_TYPE));
			result.setJdbcUrl(dbType.getJdbcUrl(tenantId, properties));
			result.setUser(properties.getProperty(Constants.JDBC_USERNAME));
			result.setPassword(properties.getProperty(Constants.JDBC_PASSWORD));
			//printDsProps(result);
			return result;
		}
	},
	PROXOOL {
		@Override
		public DataSource createDataSource(String tenantId) throws InstantiationException,
				IllegalAccessException, InvocationTargetException {
			ProxoolDataSource result = ProxoolDataSource.class.newInstance();
			fillProperties(result, properties);
			result.setDriver(properties.getProperty(Constants.JDBC_DRIVER_CLASS_NAME));
			DbType dbType = DbType.valueOf(properties.getProperty(Constants.DB_TYPE));
			result.setDriverUrl(dbType.getJdbcUrl(tenantId, properties));
			debug("----------------jdbc url is: {}", result.getDriverUrl());
			result.setUser(properties.getProperty(Constants.JDBC_USERNAME));
			result.setPassword(properties.getProperty(Constants.JDBC_PASSWORD));
			//printDsProps(result);
			return result;
		}
	};

	private static final Logger LOGGER = LoggerFactory.getLogger(PoolType.class);
	private static Properties properties = getPoolProperties();
	
	public abstract DataSource createDataSource(String tenantId)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, PropertyVetoException;

	private static Properties getPoolProperties() {
		Properties results = new Properties();
		try {
			results.load(PoolType.class.getResourceAsStream(Constants.DB_CONF_FILE));
		} catch (IOException e) {
			throw new DataSourceCreationException("Cannot read DB configuration file! please check it's existence and format.", e);
		}
		return results;
	}

	private static void fillProperties(DataSource dataSource, Properties properties) throws IllegalAccessException, InvocationTargetException {
		for (Object key : properties.keySet()) {
			BeanUtils.setProperty(dataSource, key.toString(), properties.get(key));
		}
	}

	@SuppressWarnings("unused")
	private static void printDsProps(DataSource result)
			throws IllegalAccessException, InvocationTargetException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> dsProps = BeanUtils.describe(result);
			for (String key : dsProps.keySet()) {
				debug("----------------{}: {}", key, dsProps.get(key));
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private static void debug(String message, Object... params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(message, params);
		}
	}
}
