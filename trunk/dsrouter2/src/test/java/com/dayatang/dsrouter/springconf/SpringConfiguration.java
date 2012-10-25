package com.dayatang.dsrouter.springconf;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dayatang.dsrouter.DataSourceRegistry;
import com.dayatang.dsrouter.SaasDataSource;
import com.dayatang.dsrouter.TenantService;
import com.dayatang.dsrouter.dscreator.C3P0DataSourceCreator;
import com.dayatang.dsrouter.dscreator.JdbcUrlTranslator;
import com.dayatang.dsrouter.dsregistry.DataSourceCreator;
import com.dayatang.dsrouter.dsregistry.MappedDataSourceRegistry;
import com.dayatang.dsrouter.mappingstrategy.DbNameBasedMappingStrategy;
import com.dayatang.dsrouter.tenantservice.ThreadLocalTenantService;
import com.dayatang.dsrouter.urltranslator.DbMappingStrategy;
import com.dayatang.dsrouter.urltranslator.MySqlUrlTranslator;
import com.dayatang.utils.ConfigurationFileImpl;
import com.dayatang.utils.ConfigurationUrlImpl;

@Configuration
public class SpringConfiguration {
	
	@Bean
	public DataSource dataSource() {
		SaasDataSource result = new SaasDataSource();
		result.setTenantService(tenantService());
		result.setDataSourceRegistry(dataSourceRegistry());
		return result;
	}

	@Bean
	public TenantService tenantService() {
		return new ThreadLocalTenantService();
	}

	@Bean
	public DataSourceRegistry dataSourceRegistry() {
		MappedDataSourceRegistry result = new MappedDataSourceRegistry();
		result.setDataSourceCreator(dataSourceCreator());
		return result;
	}

	@Bean
	public DataSourceCreator dataSourceCreator() {
		C3P0DataSourceCreator result = new C3P0DataSourceCreator(urlTranslator(), dsConfiguration());
		return result;
	}

	@Bean
	public JdbcUrlTranslator urlTranslator() {
		return new MySqlUrlTranslator(mappingStrategy());
	}

	@Bean
	public DbMappingStrategy mappingStrategy() {
		return new DbNameBasedMappingStrategy(mappingConfiguration());
	}

	@Bean
	public com.dayatang.utils.Configuration mappingConfiguration() {
		return ConfigurationUrlImpl.fromUrl("http://www.dayatang.com/tenant-db-mapping.properties");
	}

	@Bean
	public com.dayatang.utils.Configuration dsConfiguration() {
		return ConfigurationFileImpl.fromClasspath("/ds-config.properties");
	}

}
