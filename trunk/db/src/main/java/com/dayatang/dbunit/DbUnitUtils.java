package com.dayatang.dbunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
 * DBUnit实用工具类。用于的在数据库和XML数据文件之间的相互输入输出。
 * @author yyang
 *
 */
public class DbUnitUtils {
	
	private Properties jdbcProperties;
	
	public static DbUnitUtils configFromClasspath(String resourceFile) {
		return new DbUnitUtils(readPropertiesFromClasspath(resourceFile));
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
	
	public static DbUnitUtils configFromFile(String configFile) {
		return new DbUnitUtils(readPropertiesFromFile(configFile));
	}

	private static Properties readPropertiesFromFile(String configFile) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(configFile)));
		} catch (IOException e) {
			throw new RuntimeException("Cannot load properties from file " + configFile + ".", e);
		}
		return properties;
	}
		
	private DbUnitUtils(Properties jdbcProperties) {
		this.jdbcProperties = jdbcProperties;
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
		IDatabaseConnection connection = null;
		try {
			connection = createConnection();
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		} catch (Exception e) {
			throw new RuntimeException("Cannot clean data from or import data to database.", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void exportData(String dir, String fileName) {
		IDatabaseConnection connection = null;
		try {
			connection = createConnection();
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
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void exportDtd(String dir, String fileName) {
		IDatabaseConnection connection = null;
		try {
			connection = createConnection();
			ITableFilter filter = new DatabaseSequenceFilter(connection);
			IDataSet dataSet = new FilteredDataSet(filter, connection.createDataSet());
			File parent = new File(dir);
			if (!parent.exists()) {
				parent.mkdirs();
			}
	        Writer out = new OutputStreamWriter(new FileOutputStream(new File(dir, fileName)));
	        FlatDtdDataSet.write(dataSet, out);
		} catch (Exception e) {
			throw new RuntimeException("Cannot export DTD.", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void refreshData(String dataFile) {
		InputStream in = getClass().getResourceAsStream(dataFile);
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(in), false);
		IDataSet dataSet = new StreamingDataSet(producer);
		IDatabaseConnection connection = null;
		try {
			connection = createConnection();
			DatabaseOperation.REFRESH.execute(connection, dataSet);
		} catch (Exception e) {
			throw new RuntimeException("Cannot refresh data.", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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

	public IDataSet getDatasetFromInputStream(InputStream in) {
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
