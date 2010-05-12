package com.dayatang.db.mysql;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dayatang.db.AbstractDBManager;
import com.dayatang.db.DBConnection;
import com.dayatang.db.DBManagerUtils;
import com.dayatang.db.DataObject;
import com.dayatang.db.JDBCConnection;

public class MySQLDBManager extends AbstractDBManager {
	
	private Log logger = LogFactory.getLog(MySQLDBManager.class);
	
	private DBConnection jConn;
	
	
	public MySQLDBManager() throws Exception{
		super();
		jConn = JDBCConnection.getJDBCConnection();
	}
	
	@Override
	public void cleanDatabase() {
		
		ResultSet rs = null;
		try {
			dropConstraint();
			
			// 获取所有表
			List<DataObject> allTableNames = DBManagerUtils.getAllTableNames(jConn);
			
			// 删除表
			DBManagerUtils.dropTables(jConn,allTableNames);

			logger.info("清理数据库，已删除所有表和视图  !");
		
		} catch (Exception e) {
			logger.error("cleanDatabase() ",e);
		
		} finally {
			DBManagerUtils.colseRs(rs);
		}
	}
	
	
	/**
	 * 删除关联
	 * 
	 * @throws Exception
	 */
	private void dropConstraint() throws Exception {
		jConn.execute("SET FOREIGN_KEY_CHECKS=0;");
	}
	
	
	@Override
	public void executeSQL(String sqlFile){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("cmd /c mysql");
		sb.append(" -h " + hostName);
		
		sb.append(" -u" + username);
		sb.append(" -p" + password);
		
		sb.append(" " + databaseName + " < ");
		sb.append(sqlFile);
		
		
		
		try {
			Process process = Runtime.getRuntime().exec(sb.toString());
			process.waitFor();
			
			logger.info("已执行完SQL脚本："+sqlFile );
		
		} catch (IOException e) {
			logger.error("executeSQL() ",e);
		} catch (InterruptedException e) {
			logger.error("executeSQL() ",e);
		}

	}

	@Override
	public void exportAll(String filePath) {
		
		StringBuffer commond = getBaseMySQLDumpCommond();

		String file = exportFileName(filePath);
		commond.append(" > "+file);
		
		try {
			Runtime.getRuntime().exec(commond.toString());
	
			logger.info("已导出数据库中所有表(包括视图)到："+file);
	
		} catch (IOException e) {
			logger.error("exportAll() ",e);
		}

	}

	@Override
	public void exportExcludes(String filePath,List<String> excludedTablePrefixs) {
		
		try {
			
			List<String> loadExcludedTableName = DBManagerUtils.loadExcludedTableName(jConn,excludedTablePrefixs);
			
			if(loadExcludedTableName == null || loadExcludedTableName.size() <= 0){
				logger.info("没有任何表需要导出.");
				return;
			}
			
			StringBuffer commond = getBaseMySQLDumpCommond();
			commond.append(" --tables ");
			
			for(String name:loadExcludedTableName){
				commond.append(name+" ");
			}
			
			String file = exportFileName(filePath);
			commond.append(" > " + file);
		
			Runtime.getRuntime().exec(commond.toString());
			
			logger.info("已导出数据库中不是以[ "+DBManagerUtils.getString(excludedTablePrefixs)+" ]前缀开头的表(包括视图)到："+file);
		
		} catch (IOException e) {
			logger.error("exportIncludes() ",e);
		} catch(Exception e1){
			logger.error(e1);
		}
	}
	
	
	@Override
	public void exportIncludes(String filePath,List<String> includedTablePrefixs) {
		try {
			List<String> includedTableName = DBManagerUtils.loadIncludedTableName(jConn,includedTablePrefixs);
			

			if(includedTableName == null || includedTableName.size() <= 0){
				logger.info("没有任何表需要导出.");
				return;
			}
			
			StringBuffer commond = getBaseMySQLDumpCommond();
			commond.append(" --tables ");
	
			for(String name:includedTableName){
				commond.append(name+" ");
			}

			String file = exportFileName(filePath);
			commond.append(" > " + file);
		
			Runtime.getRuntime().exec(commond.toString());
			logger.info("已导出数据库中以[ "+DBManagerUtils.getString(includedTablePrefixs)+" ]前缀开头的表(包括视图)到："+file);
			
		} catch (IOException e) {
			logger.error("exportIncludes() ",e);
		} catch(Exception e1){
			logger.error("exportIncludes() ",e1);
		}
		
	}
	
	/**
	 * 生成的sql文件名
	 * @param filePath
	 * @return
	 */
	private String exportFileName(String filePath){
		
		if(filePath.endsWith(".sql")){
			return filePath;
		}
		
		if(! filePath.endsWith("/")){
			filePath = filePath + "/";
		}
		
		String fileName = DBManagerUtils.generateUniqueName();
		
		return filePath + fileName + ".sql";
	}

	
	/**
	 * MySQl基础导出命令
	 * @return
	 */
	private StringBuffer getBaseMySQLDumpCommond(){
		
		
		StringBuffer commond = new StringBuffer();
		
		commond.append("cmd /c mysqldump");
		commond.append(" -h " + hostName);
		
		commond.append(" -u" + username);
		commond.append(" -p" + password);
		
		commond.append(" "+ databaseName);
		
		return commond;
	}
	
}
