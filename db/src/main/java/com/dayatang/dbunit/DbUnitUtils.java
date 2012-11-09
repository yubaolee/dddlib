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
 * XML数据文件只支持FlatXml格式，则数据库中的每行数据表示为一个XML元素，
 * 每个列成为该元素的一个属性。
 * @author yyang
 *
 */
public class DbUnitUtils {
	
	private Properties jdbcProperties;
	
	/**
	 * 从类路径属性文件中读入JDBC连接信息
	 * @param resourceFile
	 * @return
	 */
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
	
	/**
	 * 从磁盘文件中读入JDBC连接信息
	 * @param configFile
	 * @return
	 */
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

	/**
	 * 从XML数据文件中读入数据集，写入到数据库。数据库表中的原有数据将被清除。
	 * @param flatXmlDataFile XML数据文件
	 */
	public void importDataFromClasspath(String flatXmlDataFile) {
		IDatabaseConnection connection = null;
		try {
			connection = createConnection();
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDatasetFromFile(flatXmlDataFile));
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

	/**
	 * 将数据库中的数据导出到XML文件中。
	 * @param dir 文件存放目录
	 * @param fileName 生成的XML数据文件名。
	 */
	public void exportData(String dir, String fileName) {
		IDatabaseConnection connection = null;
		try {
			connection = createConnection();
			IDataSet dataSet = new FilteredDataSet(new DatabaseSequenceFilter(connection), 
					connection.createDataSet());
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

	/**
	 * 读取数据库表结构，生成XML数据文件的DTD文件
	 * @param dir 文件存放目录
	 * @param fileName 生成的XML数据文件名。
	 */
	public void exportDtd(String dir, String fileName) {
		IDatabaseConnection connection = null;
		try {
			connection = createConnection();
			IDataSet dataSet = new FilteredDataSet(new DatabaseSequenceFilter(connection), 
					connection.createDataSet());
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
	
	/**
	 * 从XML数据文件中读入数据集，写入到数据库。数据库表中的原有数据将被清除。
	 * @param flatXmlDataFile XML数据文件
	 */
	public void refreshData(String flatXmlDataFile) {
		InputStream in = getClass().getResourceAsStream(flatXmlDataFile);
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
	
	private IDataSet getDatasetFromFile(String flatXmlDataFile) {
		return getDatasetFromInputStream(getClass().getResourceAsStream(flatXmlDataFile));
	}

	private IDataSet getDatasetFromInputStream(InputStream in) {
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
