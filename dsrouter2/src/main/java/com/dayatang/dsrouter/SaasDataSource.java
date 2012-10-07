package com.dayatang.dsrouter;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class SaasDataSource implements DataSource {

	private TenantHolder tenantHolder;
	private DataSourceRegistry dataSourceRegistry;
	
	public void setTenantHolder(TenantHolder tenantHolder) {
		this.tenantHolder = tenantHolder;
	}

	public void setDataSourceRegistry(DataSourceRegistry dataSourceRegistry) {
		this.dataSourceRegistry = dataSourceRegistry;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return getActualDataSource().getLogWriter();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return getActualDataSource().getLoginTimeout();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		getActualDataSource().setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		getActualDataSource().setLoginTimeout(seconds);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getActualDataSource().isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getActualDataSource().unwrap(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getActualDataSource().getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return getActualDataSource().getConnection(username, password);
	}

	private DataSource getActualDataSource() {
		return dataSourceRegistry.getOrCreateDataSourceByTenantId(tenantHolder.getTenantId());
	}
	
}
