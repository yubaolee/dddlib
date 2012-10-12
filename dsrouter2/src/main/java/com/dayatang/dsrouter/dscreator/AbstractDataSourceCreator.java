package com.dayatang.dsrouter.dscreator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.dayatang.dsrouter.DataSourceCreationException;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.utils.Configuration;
import com.dayatang.utils.ConfigurationFileImpl;
import com.dayatang.utils.Slf4jLogger;

public abstract class AbstractDataSourceCreator implements DataSourceCreator {
	
	private static final Slf4jLogger LOGGER = Slf4jLogger.of(AbstractDataSourceCreator.class);

	private JdbcUrlTranslator urlTranslator;
	private Configuration configuration;

	public AbstractDataSourceCreator(JdbcUrlTranslator urlTranslator) {
		this.urlTranslator = urlTranslator;
		this.configuration = ConfigurationFileImpl.fromClasspath(Constants.DB_CONF_FILE);
	}

	public AbstractDataSourceCreator(JdbcUrlTranslator urlTranslator, Configuration configuration) {
		this.urlTranslator = urlTranslator;
		this.configuration = configuration;
	}

	public DataSource createDataSourceForTenant(String tenant) {
		DataSource result = createDataSource();
		try {
			fillProperties(result);
			fillStandardProperties(result, tenant);
			return result;
		} catch (Exception e) {
			String message = "Create data source failure.";
			LOGGER.error(message, e);
			throw new DataSourceCreationException(message, e);
		}
	}

	protected abstract DataSource createDataSource();

	private void fillProperties(DataSource dataSource) throws IllegalAccessException, InvocationTargetException {
		Properties properties = configuration.getProperties();
		for (Object key : properties.keySet()) {
			BeanUtils.setProperty(dataSource, key.toString(), properties.get(key));
		}
	}
	
	private void fillStandardProperties(DataSource dataSource, String tenant) throws IllegalAccessException, InvocationTargetException {
		Map<String, String> standardProperties = getStandardPropMappings();
		for (Object key : standardProperties.keySet()) {
			BeanUtils.setProperty(dataSource, standardProperties.get(key), configuration.getString(key.toString()));
		}
		BeanUtils.setProperty(dataSource, standardProperties.get(Constants.JDBC_URL), urlTranslator.translateUrl(tenant, configuration.getProperties()));
	}
	
	protected abstract Map<String, String> getStandardPropMappings();

	@SuppressWarnings("unused")
	private static void printDsProps(DataSource result) throws IllegalAccessException, InvocationTargetException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> dsProps = BeanUtils.describe(result);
			for (String key : dsProps.keySet()) {
				//LOGGER.debug("----------------{}: {}", key, dsProps.get(key));
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

}
