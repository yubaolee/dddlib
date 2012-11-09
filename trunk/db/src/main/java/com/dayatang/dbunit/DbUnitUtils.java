package com.dayatang.dbunit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.stream.IDataSetProducer;
import org.dbunit.dataset.stream.StreamingDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.xml.sax.InputSource;

import com.dayatang.JdbcConstants;


/**
 * 功能描述:使用dbunit作单元测试,用于生成dataSet文件
 * 
 * <p>
 * 有三种生成方式:部分提取、全部提取和关联.
 * 
 * <p>
 * 运行main方法,即可全部提取到dataSet文件.
 * 
 * @author <a href="malto:chencao0524@gmail.com">陈操</a> Created on 2007-9-29
 * 
 * @version $LastChangedRevision$ $LastChangedBy$ $LastChangedDate$
 * 
 */
public class DbUnitUtils {
	
	private static final String JDBC_PROP_FILE = "jdbc.properties";
	private IDatabaseConnection connection;
	private Properties jdbcProperties;
	
	public DbUnitUtils() {
		this(JDBC_PROP_FILE);
	}
	
	public DbUnitUtils(String jdbcFileInClasspath) {
		this(readPropertiesFromClasspath(jdbcFileInClasspath));
	}
	
	public DbUnitUtils(Properties jdbcProperties) {
		this.jdbcProperties = jdbcProperties;
		connection = createConnection();
	}

	private static Properties readPropertiesFromClasspath(String jdbcFileInClasspath) {
		Properties properties = new Properties();
		try {
			properties.load(DbUnitUtils.class.getResourceAsStream(jdbcFileInClasspath));
		} catch (IOException e) {
			throw new RuntimeException("Cannot load properties from file " + jdbcFileInClasspath + ".", e);
		}
		return properties;
	}
	

	public void importDataFromClasspath(String dataFile) {
		InputStream in = getClass().getResourceAsStream(dataFile);
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(in), false);
		IDataSet dataSet;
		try {
			dataSet = new CachedDataSet(producer);
		} catch (DataSetException e) {
			throw new RuntimeException("Cannot load dataset from file " + dataFile + ".", e);
		}
		try {
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		} catch (Exception e) {
			throw new RuntimeException("Cannot clean data from or import data to database.", e);
		}
	}

	public void exportData(String dir, String fileName) {
		try {
			ITableFilter filter = new DatabaseSequenceFilter(connection);
			IDataSet dataSet = new FilteredDataSet(filter, connection.createDataSet());
			File parent = new File(dir);
			if (!parent.exists()) {
				parent.mkdirs();
			}
			OutputStream out = new FileOutputStream(new File(dir, fileName));
			FlatXmlDataSet.write(dataSet, out);
		} catch (Exception e) {
			throw new RuntimeException("Cannot export data.", e);
		}
	}

	public void exportDtd(String dir, String fileName) {
		ITableFilter filter;
		try {
			filter = new DatabaseSequenceFilter(connection);
			IDataSet dataSet = new FilteredDataSet(filter, connection.createDataSet());
			File parent = new File(dir);
			if (!parent.exists()) {
				parent.mkdirs();
			}
	        Writer out = new OutputStreamWriter(new FileOutputStream(new File(dir, fileName)));
	        FlatDtdDataSet.write(dataSet, out);
		} catch (Exception e) {
			throw new RuntimeException("Cannot export DTD.", e);
		}
	}
	
	public void refreshData(String dataFile) {
		InputStream in = getClass().getResourceAsStream(dataFile);
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(in), false);
		IDataSet dataSet = new StreamingDataSet(producer);
		try {
			DatabaseOperation.REFRESH.execute(connection, dataSet);
		} catch (Exception e) {
			throw new RuntimeException("Cannot refresh data.", e);
		}
	}

	public IDataSet getDatasetFromFile(String dataFile) {
		InputStream in = getClass().getResourceAsStream(dataFile);
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(in), false);
		try {
			return new CachedDataSet(producer);
		} catch (DataSetException e) {
			throw new RuntimeException("Cannot get dataset.", e);
		}
	}
	
	private IDatabaseConnection createConnection() {
		try {
			Class.forName(jdbcProperties.getProperty(JdbcConstants.JDBC_DRIVER));
			Connection jdbcConnection = DriverManager.getConnection(
					jdbcProperties.getProperty(JdbcConstants.JDBC_URL), 
					jdbcProperties.getProperty(JdbcConstants.JDBC_USERNAME),
					jdbcProperties.getProperty(JdbcConstants.JDBC_PASSWORD));
			return new DatabaseConnection(jdbcConnection);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create database connection.", e);
		}
	}
}
