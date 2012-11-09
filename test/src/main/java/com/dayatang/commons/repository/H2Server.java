package com.dayatang.commons.repository;

import java.sql.SQLException;

import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;

public class H2Server {
	private Server server;

	private String dir;

	private String dbFile;

	public H2Server(String dir, String dbFile) {
		this.dir = dir;
		this.dbFile = dbFile;
	}

	public H2Server(String dbFile) {
		this(".", dbFile);
	}

	public synchronized void start() {
		if (server != null && server.isRunning(true)) {
			return;
		}
		try {
			DeleteDbFiles.execute(dir, dbFile, true);
			server = Server.createTcpServer(new String[0]);
			server.start();
		} catch (SQLException e) {
			throw new RuntimeException("Cannot start h2 server database", e);
		}
	}

	public synchronized void shutdown() {
		if (server == null) {
			return;
		}
		if (server.isRunning(true)) {
			server.stop();
			server.shutdown();
			DeleteDbFiles.execute(dir, dbFile, true);
		}
		server = null;
	}
}
