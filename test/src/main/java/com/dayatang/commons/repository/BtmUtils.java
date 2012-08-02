package com.dayatang.commons.repository;

import java.sql.SQLException;

import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import bitronix.tm.TransactionManagerServices;

public class BtmUtils {
	
	private BtmUtils() {
	}

	private static final String DB_NAME = "test-db";

    private static boolean setupDataSource = true;
	private static H2Server server = new H2Server();

	@BeforeClass
	public static void setupDataSource() throws Exception {
        if (setupDataSource) {
            server.start();
            String resourceConfFile = BtmUtils.class.getResource("/datasources.properties").toURI().getPath();
            TransactionManagerServices.getConfiguration().setResourceConfigurationFilename(resourceConfFile);
            TransactionManagerServices.getTransactionManager();
        }
	}

	@AfterClass
    public static void closeDataSource() throws Exception {
    	if (setupDataSource) {
    		server.stop();
    		DeleteDbFiles.execute("~", DB_NAME, true);
    		TransactionManagerServices.getTransactionManager().shutdown();
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
