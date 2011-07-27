package org.beyond.gemini.dsrouter.builder.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0MySQLDataSourceBuilder extends AbstractMySQLDataSourceBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3878478941903135678L;

	private static final Logger logger = LoggerFactory
			.getLogger(C3P0MySQLDataSourceBuilder.class);

	private static final String PROP_FILE = "datasource-default-properties-c3p0.properties";

	private static Properties defaultProp = new Properties();

	private static InputStream defaultPropIns = C3P0MySQLDataSourceBuilder.class
			.getResourceAsStream("/" + PROP_FILE);

	static {
		try {
			defaultProp.load(defaultPropIns);
		} catch (IOException e) {
			System.err.println("initial properties error!!");
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	protected Class<? extends DataSource> getDataSourceClass() {
		return ComboPooledDataSource.class;
	}

	@Override
	protected String getDriverProperty() {
		return "driverClass";
	}

	@Override
	protected String getJdbcUrlProperty() {
		return "jdbcUrl";
	}

	@Override
	protected String getPasswordProperty() {
		return "password";
	}

	@Override
	protected String getUserProperty() {
		return "user";
	}

	@Override
	protected Properties getDefaultProp() {
		return defaultProp;
	}

}
