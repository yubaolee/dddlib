package com.dayatang.db.mysql;

import java.sql.Driver;
import java.util.List;

import com.dayatang.db.AbstractDBManager;

public class MySQLDBManager extends AbstractDBManager {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8901910732615981112L;


	public MySQLDBManager(String jdbcUrl, String username, String password,
			Class<Driver> driverClass) {
		super(jdbcUrl, username, password, driverClass);
	}
	
	@Override
	public void cleanDatabase() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeSQL(String sqlFile) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportAll(String filePath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportExcludes(String filePath,
			List<String> excludedTablePrefixs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportIncludes(String filePath,
			List<String> includedTablePrefixs) {
		// TODO Auto-generated method stub

	}

}
