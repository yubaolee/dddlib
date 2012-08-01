package com.dayatang.commons.repository;

import java.sql.SQLException;

import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

public class BtmUtils {
	
	private BtmUtils() {
	}

	private static final String DB_NAME = "test-db";

    private static boolean setupDataSource = true;
	private static PoolingDataSource dataSource;
	private static H2Server server = new H2Server();

	@BeforeClass
	public static void setupDataSource() throws Exception {
        if (setupDataSource) {
            server.start();
        	dataSource = setupPoolingDataSource();
        }
	}

	@AfterClass
    public static void closeDataSource() throws Exception {
    	if (setupDataSource) {
    		if (dataSource != null) {
    			dataSource.close();
    			dataSource = null;
    		}
    		server.stop();
    		DeleteDbFiles.execute("~", DB_NAME, true);
    		TransactionManagerServices.getTransactionManager().shutdown();
    	}
    }

	private static PoolingDataSource setupPoolingDataSource() {
        PoolingDataSource pds = new PoolingDataSource();
        pds.setUniqueName("jdbc/testDS");
        pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
        pds.setMaxPoolSize(5);
        pds.setAllowLocalTransactions(true);
        pds.getDriverProperties().put("user", "sa");
        pds.getDriverProperties().put("password", "");
        pds.getDriverProperties().put("url", "jdbc:h2:tcp://localhost/~/" + DB_NAME);
        pds.getDriverProperties().put("driverClassName", "org.h2.Driver");
        pds.init();
        return pds;
    }

	public static Transaction getTransaction() {
		try {
			return TransactionManagerServices.getTransactionManager().getTransaction();
		} catch (SystemException e) {
			throw new RuntimeException("Cannot get transaction", e);
		}
	}
	
	private static class H2Server {
		private Server server;

		public synchronized void start() {
			if (server == null || !server.isRunning(false)) {
				try {
					DeleteDbFiles.execute("~", DB_NAME, true);
					server = Server.createTcpServer(new String[0]);
					server.start();
				} catch (SQLException e) {
					throw new RuntimeException("Cannot start h2 server database", e);
				}
			}
		}

		public synchronized void finalize() throws Throwable {
			stop();
			super.finalize();
		}

		public void stop() {
			if (server != null) {
				server.stop();
				server.shutdown();
				DeleteDbFiles.execute("~", DB_NAME, true);
				server = null;
			}
		}
	}

}
