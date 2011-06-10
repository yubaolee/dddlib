package com.dayatang.dsmonitor.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.time.StopWatch;
import org.springframework.util.Assert;

import com.dayatang.dsmonitor.monitor.ConnectionMonitor;

public class GeminiConnection extends DelegatingConnection implements Connection {

	private long creationTime;

	private StackTraceElement[] stackTraceElements;

	private StopWatch stopWatch;

	private Set<ConnectionMonitor> monitors = new HashSet<ConnectionMonitor>();;

	public GeminiConnection(Connection targetConnection,
			Set<ConnectionMonitor> monitors) throws SQLException {
		Assert.notNull(targetConnection, "'targetConnection' must not be null");
		Exception exception = new Exception();
		StackTraceElement[] stackTraceElements = exception.getStackTrace();
		setTargetConnection(targetConnection);
		setCreationTime(System.currentTimeMillis());
		setStackTraceElements(stackTraceElements);
		setMonitors(monitors);
		beginStopWatch();
		notifyOpenConnection();
	}

	private void notifyOpenConnection() throws SQLException {
		for (ConnectionMonitor monitor : monitors) {
			monitor.openConnection(this);
		}
	}

	private void notifyCloseConnection() throws SQLException {
		for (ConnectionMonitor monitor : monitors) {
			monitor.closeConnection(this);
		}
	}

	@Override
	public void close() throws SQLException {
		super.close();
		if (stopWatch != null) {
			stopWatch.stop();
		}
		notifyCloseConnection();
	}

	public void beginStopWatch() {
		this.stopWatch = new StopWatch();
		stopWatch.start();
	}

	public long getTime() {
		if (stopWatch != null) {
			return stopWatch.getTime();
		}
		return 0;
	}

	public Set<ConnectionMonitor> getMonitors() {
		return monitors;
	}

	public void setMonitors(Set<ConnectionMonitor> monitors) {
		this.monitors = monitors;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public StackTraceElement[] getStackTraceElements() {
		return stackTraceElements;
	}

	public void setStackTraceElements(StackTraceElement[] stackTraceElements) {
		this.stackTraceElements = Arrays.copyOf(stackTraceElements, stackTraceElements.length);
	}

	public StopWatch getStopWatch() {
		return stopWatch;
	}

	public void setStopWatch(StopWatch stopWatch) {
		this.stopWatch = stopWatch;
	}

}
