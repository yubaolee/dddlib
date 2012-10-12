package com.dayatang.dsrouter.dscreator;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.utils.Slf4jLogger;

public class ProxoolDataSourceCreator extends AbstractDataSourceCreator {

	private static final Slf4jLogger LOGGER = Slf4jLogger.of(ProxoolDataSourceCreator.class);

	public ProxoolDataSourceCreator(Properties properties) {
		super(properties);
	}

	@Override
	protected DataSource createDataSource() {
		try {
			return BasicDataSource.class.newInstance();
		} catch (Exception e) {
			String message = "Create Proxool data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}

	@Override
	protected Map<String, String> getStandardPropMappings() {
		Map<String, String> results = new HashMap<String, String>();
		results.put(Constants.JDBC_DRIVER_CLASS_NAME, "driver");
		results.put(Constants.JDBC_URL, "driverUrl");
		results.put(Constants.JDBC_USERNAME, "user");
		results.put(Constants.JDBC_PASSWORD, "password");
		return results;
	}

}
