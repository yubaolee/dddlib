package com.dayatang.dsrouter.mappingstrategy;

import java.util.Properties;

import com.dayatang.dsrouter.Constants;
import com.dayatang.dsrouter.urltranslator.DbMappingStrategy;

/**
 * 租户数据库映射策略。
 * @author yyang
 *
 */
public abstract class AbstractDbMappingStrategy implements DbMappingStrategy {

	private DbMapper dbMapper;

	public AbstractDbMappingStrategy(DbMapper dbMapper) {
		this.dbMapper = dbMapper;
	}

	public DbMapper getDbMapper() {
		return dbMapper;
	}

	public String getPort(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_PORT);
	}

	public String getDbName(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_DB_NAME);
	}

	public String getHost(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_HOST);
	}

	public String getSchema(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_SCHEMA);
	}

	public String getInstanceName(String tenant, Properties properties) {
		return properties.getProperty(Constants.JDBC_INSTANCE);
	}
}
