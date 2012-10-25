package com.dayatang.dsrouter;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.dsrouter.tenantservice.ThreadLocalTenantHolder;
import com.dayatang.spring.factory.SpringIocUtils;

public class SaasDataSourceIntegrationTest {
	
	private SaasDataSource instance;

	@Before
	public void setUp() throws Exception {
		SpringIocUtils.initInstanceProvider("spring/applicationContext.xml");
		instance = InstanceFactory.getInstance(SaasDataSource.class);
		ThreadLocalTenantHolder.removeTenant();
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testGetConnectionStringString() throws SQLException {
		switchTenant("abc");
		assertEquals("China Mobile", getDataFromDb());
		switchTenant("xyz");
		assertEquals("China Unicom", getDataFromDb());
	}

	private void switchTenant(String tenant) {
		ThreadLocalTenantHolder.setTenant(tenant);
	}

	private String getDataFromDb() {
		Connection connection = null;
		try {
			connection = instance.getConnection();
			return useConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return null;
	}

	private String useConnection(Connection connection) {
		String sql = "SELECT name FROM users WHERE id = 1";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sql);
			return usePreparedStatement(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String usePreparedStatement(PreparedStatement stmt) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery();
			rs.next();
			return rs.getString("name");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


}