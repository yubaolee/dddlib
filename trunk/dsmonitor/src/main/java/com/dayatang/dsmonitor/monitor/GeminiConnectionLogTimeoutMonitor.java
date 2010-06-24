package com.dayatang.dsmonitor.monitor;

import java.sql.SQLException;
import java.util.Set;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.dsmonitor.datasource.GeminiConnection;

public class GeminiConnectionLogTimeoutMonitor extends
		AbstractGeminiConnectionTimeoutMonitor {

	protected static final Logger logger = LoggerFactory
			.getLogger(GeminiConnectionLogTimeoutMonitor.class);

	private static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	@Override
	public void monitor() {
		Set<GeminiConnection> closedTimeoutConnections = getClosedTimeoutConnections();
		logConnections(closedTimeoutConnections, "已关闭的超时数据库连接");

		Set<GeminiConnection> aliveTimeoutConnections = getAliveTimeoutConnections();
		logConnections(aliveTimeoutConnections, "活动的超时数据库连接");
	}

	private void logConnections(Set<GeminiConnection> closedTimeoutConnections,
			String title) {
		if (logger.isInfoEnabled()) {
			if (!closedTimeoutConnections.isEmpty()) {

				logger
						.info("=======================================================================================");
				logger
						.info("=======================================================================================");
				logger
						.info("=============                                                             =============");
				logger
						.info(
								"=============            以下是{}，超时时间为：【{}】ms         =============",
								title, getTimeout());
				logger
						.info("=============                                                             =============");
				logger
						.info("=======================================================================================");
				logger
						.info("=======================================================================================");

				for (GeminiConnection conn : closedTimeoutConnections) {
					logConnection(conn);
				}
			} else {
				logger.info("没有【{}】", title);
			}
		}
	}

	private void logConnection(GeminiConnection conn) {
		try {
			if (conn.isClosed()) {
				logger.info("数据库连接HashCode【{}】，创建时间【{}】，耗时【{}】", new Object[] {
						conn.hashCode(),
						DateFormatUtils.format(conn.getCreationTime(),
								DATE_PATTERN), conn.getStopWatch() });
			} else {
				logger.info("数据库连接HashCode【{}】，URL=【{}】，创建时间【{}】，耗时【{}】",
						new Object[] {
								conn.hashCode(),
								conn.getMetaData().getURL(),
								DateFormatUtils.format(conn.getCreationTime(),
										DATE_PATTERN), conn.getStopWatch() });
			}

			StackTraceElement[] stackTraceElements = conn
					.getStackTraceElements();
			logger.info("调用堆栈为：");
			for (StackTraceElement ste : stackTraceElements) {
				logger.info("     " + ste);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
