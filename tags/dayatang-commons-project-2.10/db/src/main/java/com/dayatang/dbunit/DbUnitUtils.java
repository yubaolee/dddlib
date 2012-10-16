package com.dayatang.dbunit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
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

import com.dayatang.db.PropertiesUtil;


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
	
	private IDatabaseConnection connection;
	
	public DbUnitUtils() throws Exception {
		super();
		connection = createConnection();
	}
	
	public void importData() throws DatabaseUnitException, SQLException, Exception {
		InputStream in = getClass().getResourceAsStream("/sample-data.xml");
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(in), false);
		IDataSet dataSet = new CachedDataSet(producer);
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
	}

	public void exportData() throws Exception {
		ITableFilter filter = new DatabaseSequenceFilter(connection);
		IDataSet dataSet = new FilteredDataSet(filter, connection.createDataSet());
		File dir = new File("target/dbunit");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream out = new FileOutputStream("target/dbunit/export.xml");
		FlatXmlDataSet.write(dataSet, out);
	}

	public void exportDtd() throws DataSetException, FileNotFoundException, IOException, SQLException {
		ITableFilter filter = new DatabaseSequenceFilter(connection);
		IDataSet dataSet = new FilteredDataSet(filter, connection.createDataSet());
        Writer out = new OutputStreamWriter(new FileOutputStream("target/dataset.dtd"));
        FlatDtdDataSet.write(dataSet, out);
        //FlatDtdWriter datasetWriter = new FlatDtdWriter(out);
        // datasetWriter.setContentModel(FlatDtdWriter.CHOICE);
        // You could also use the sequence model which is the default
        // datasetWriter.setContentModel(FlatDtdWriter.SEQUENCE);
        //datasetWriter.write(dataSet);
	}
	
	public void refreshData() throws DatabaseUnitException, SQLException, FileNotFoundException {
		InputStream in = getClass().getResourceAsStream("/sample-data.xml");
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(in), false);
		IDataSet dataSet = new StreamingDataSet(producer);
		DatabaseOperation.REFRESH.execute(connection, dataSet);
	}

	public IDataSet getDatasetFromFile() throws DataSetException {
		InputStream in = getClass().getResourceAsStream("/sample-data.xml");
		IDataSetProducer producer = new FlatXmlProducer(new InputSource(in), false);
		IDataSet dataSet = new CachedDataSet(producer);
		return dataSet;
	}
	
	private IDatabaseConnection createConnection() throws Exception {
		Class.forName(PropertiesUtil.JDBC_DRIVER);
		Connection jdbcConnection = DriverManager.getConnection(
				PropertiesUtil.JDBC_URL, PropertiesUtil.JDBC_USERNAME,
				PropertiesUtil.JDBC_PASSWD);
		return new DatabaseConnection(jdbcConnection);
	}
}
