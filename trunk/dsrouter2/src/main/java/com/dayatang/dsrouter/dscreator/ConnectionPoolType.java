package com.dayatang.dsrouter.dscreator;

import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.dayatang.utils.Slf4jLogger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库连接池类型
 * 
 * @author yyang
 * 
 */
public enum ConnectionPoolType {

	C3P0 {
		@Override
		public DataSource createDataSource(String tenant, Properties properties) throws InstantiationException, IllegalAccessException,
				InvocationTargetException, PropertyVetoException {
			ComboPooledDataSource result = ComboPooledDataSource.class.newInstance();
			fillProperties(result, properties);
			result.setDriverClass(properties.getProperty(Constants.JDBC_DRIVER_CLASS_NAME));
			result.setJdbcUrl(getUrl(tenant, properties));
			LOGGER.debug("----------------jdbc url is: {}", result.getJdbcUrl());
			result.setUser(properties.getProperty(Constants.JDBC_USERNAME));
			result.setPassword(properties.getProperty(Constants.JDBC_PASSWORD));
			// printDsProps(result);
			return result;
		}
	},
	PROXOOL {
		@Override
		public DataSource createDataSource(String tenant, Properties properties) throws InstantiationException, IllegalAccessException,
				InvocationTargetException {
			ProxoolDataSource result = ProxoolDataSource.class.newInstance();
			fillProperties(result, properties);
			result.setDriver(properties.getProperty(Constants.JDBC_DRIVER_CLASS_NAME));
			result.setDriverUrl(getUrl(tenant, properties));
			result.setUser(properties.getProperty(Constants.JDBC_USERNAME));
			result.setPassword(properties.getProperty(Constants.JDBC_PASSWORD));
			// printDsProps(result);
			return result;
		}

	},
	COMMONS_DBCP {
		@Override
		public DataSource createDataSource(String tenant, Properties properties) throws InstantiationException, IllegalAccessException,
				InvocationTargetException {
			BasicDataSource result = BasicDataSource.class.newInstance();
			fillProperties(result, properties);
			result.setDriverClassName(properties.getProperty(Constants.JDBC_DRIVER_CLASS_NAME));
			result.setUrl(getUrl(tenant, properties));
			LOGGER.debug("----------------jdbc url is: {}", result.getUrl());
			result.setUsername(properties.getProperty(Constants.JDBC_USERNAME));
			result.setPassword(properties.getProperty(Constants.JDBC_PASSWORD));
			// printDsProps(result);
			return result;
		}
	};

	private static final Slf4jLogger LOGGER = Slf4jLogger.of(ConnectionPoolType.class);

	public abstract DataSource createDataSource(String tenant, Properties properties) throws InstantiationException, IllegalAccessException,
			InvocationTargetException, PropertyVetoException;

	private static void fillProperties(DataSource dataSource, Properties properties) throws IllegalAccessException, InvocationTargetException {
		for (Object key : properties.keySet()) {
			BeanUtils.setProperty(dataSource, key.toString(), properties.get(key));
		}
	}

	private static String getUrl(String tenant, Properties properties) {
		DbType dbType = DbType.valueOf(properties.getProperty(Constants.DB_TYPE));
		return dbType.getJdbcUrl(tenant, properties);
	}

	@SuppressWarnings("unused")
	private static void printDsProps(DataSource result) throws IllegalAccessException, InvocationTargetException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> dsProps = BeanUtils.describe(result);
			for (String key : dsProps.keySet()) {
				LOGGER.debug("----------------{}: {}", key, dsProps.get(key));
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
