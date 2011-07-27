package org.beyond.gemini.dsrouter.datasource.examples.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.beyond.gemini.dsrouter.datasource.examples.CustomerContextHolder;
import org.beyond.gemini.dsrouter.datasource.examples.CustomerType;
import org.beyond.gemini.dsrouter.datasource.examples.Item;
import org.beyond.gemini.dsrouter.datasource.examples.TestTable;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class Examples2Test extends
		AbstractDependencyInjectionSpringContextTests {

	private TestTable catalog;

	public void setTestTable(TestTable catalog) {
		this.catalog = catalog;
	}

	public void testDataSourceRouting() throws Exception {
		CustomerContextHolder.setCustomerType(CustomerType.GOLD);
		printConn();
		List<Item> goldItems = catalog.getItems();
		assertEquals(2, goldItems.size());
		print(goldItems);

		CustomerContextHolder.setCustomerType(CustomerType.SILVER);
		printConn();
		List<Item> silverItems = catalog.getItems();
		assertEquals(2, silverItems.size());
		print(silverItems);

		CustomerContextHolder.clearCustomerType();

		printConn();
		List<Item> bronzeItems = catalog.getItems();
		assertEquals(2, bronzeItems.size());
		print(bronzeItems);
	}

	private void printConn() throws SQLException {
		DataSource ds = (DataSource) getApplicationContext().getBean(
				"dataSource");
		Connection connection = ds.getConnection();
		System.out.println(connection.getMetaData().getURL());
		System.out.println(connection.getCatalog());
	}

	private void print(List<Item> goldItems) {
		for (Item item : goldItems) {
			System.out.println(item.getId() + " = " + item.getName());
		}
	}

	protected String[] getConfigLocations() {
		return new String[] { "/spring/examples/dataSourceContext.xml" };
	}

}
