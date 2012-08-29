package com.dayatang.commons.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.stream.IDataSetProducer;
import org.dbunit.dataset.stream.StreamingDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.xml.sax.InputSource;


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
	
	private static final String DATA_FILE = "/sample-data.xml";
	private IDatabaseConnection connection;
	
	private Properties properties;

	public void exportData(String directory, String fileName) throws Exception {
		connection = createConnection();
		ITableFilter filter = new DatabaseSequenceFilter(connection);
		IDataSet dataSet = new FilteredDataSet(filter, connection.createDataSet());
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream out = new FileOutputStream(new File(dir, fileName));
		FlatXmlDataSet.write(dataSet, out);
		connection.close();
	}

	
	public void importDate(InputStream source) throws DatabaseUnitException, SQLException, Exception {
		connection = createConnection();
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(source), false);
		IDataSet dataSet = new CachedDataSet(producer);
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
	}
	
	public void refreshData(InputStream source) throws Exception {
		connection = createConnection();
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(source), false);
		IDataSet dataSet = new StreamingDataSet(producer);
		DatabaseOperation.REFRESH.execute(connection, dataSet);
		connection.close();
	}
	
	public IDatabaseTester getDatabaseTester() throws Exception {
		connection = createConnection();
		IDatabaseTester result = new DataSourceDatabaseTester(createDataSource());
		result.setDataSet(getDatasetFromFile(getClass().getResourceAsStream(DATA_FILE)));
		result.setSetUpOperation(DatabaseOperation.REFRESH);
		connection.close();
		return result;
	}

	public void exportDtd(String directory, String fileName) throws Exception {
		connection = createConnection();
		ITableFilter filter = new DatabaseSequenceFilter(connection);
		IDataSet dataSet = new FilteredDataSet(filter, connection.createDataSet());
        Writer out = new OutputStreamWriter(new FileOutputStream(new File(directory, fileName)));
        FlatDtdDataSet.write(dataSet, out);
		connection.close();
	}
	
	public IDataSet getDatasetFromFile(InputStream source) throws Exception {
		connection = createConnection();
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(source), false);
		IDataSet dataSet = new CachedDataSet(producer);
		connection.close();
		return dataSet;
	}
	
	private IDatabaseConnection createConnection() throws SQLException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		DatabaseDataSourceConnection connection = new DatabaseDataSourceConnection(createDataSource());
		DatabaseConfig config = connection.getConfig();
		IDataTypeFactory datatypeFactory = (IDataTypeFactory) Class.forName(getProperties().getProperty("dbunit.dataTypeFactoryName")).newInstance();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, datatypeFactory);
		return connection;
	}

	private Properties getProperties() throws IOException {
		if (properties == null) {
			properties = new Properties();
			properties.load(getClass().getResourceAsStream("/jdbc.properties"));
		}
		return properties;
	}

	private DataSource createDataSource() throws IOException {
		Properties properties = getProperties();
		properties.load(getClass().getResourceAsStream("/jdbc.properties"));
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(properties.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(properties.getProperty("jdbc.url"));
		dataSource.setUsername(properties.getProperty("jdbc.username"));
		dataSource.setPassword(properties.getProperty("jdbc.password"));
		return dataSource;
	}
}
