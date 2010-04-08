package com.dayatang.db;

import java.util.List;

public interface DBManager {

	public void executeSQL(String sqlFile);

	public void cleanDatabase();

	public void genAll(String filePath);

	public void genExcludes(String filePath, List<String> excludedTablePrefixs);

	public void genIncludes(String filePath, List<String> includedTablePrefixs);

}
