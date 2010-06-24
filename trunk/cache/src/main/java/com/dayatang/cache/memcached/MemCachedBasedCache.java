package com.dayatang.cache.memcached;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.danga.MemCached.ErrorHandler;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.dayatang.cache.Cache;

/**
 * 基于Memcached的缓存实现
 * 
 * @author chencao
 * 
 */
public class MemCachedBasedCache implements Cache, InitializingBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6808451597098234796L;

	private static final Logger logger = LoggerFactory
			.getLogger(MemCachedBasedCache.class);

	private MemCachedClient mcc;

	private String[] servers;

	private int initConn = 100;

	private int minConn = 100;

	private int maxConn = 250;

	private int connectTimeout = 3000;

	private ErrorHandler errorHandler;

	@Override
	public void afterPropertiesSet() {
		Assert.notEmpty(servers);

		if (logger.isInfoEnabled()) {
			for (String server : servers) {
				logger.info("准备为Memcached服务器{}创建客户端...", server);
				logger.info("最小连接数为：{}", minConn);
				logger.info("最大接数为：{}", maxConn);
				logger.info("初始化连接数为：{}", initConn);
				logger.info("连接超时时间为：{}毫秒", connectTimeout);
			}
		}
		prepareClient();
		// if (logger.isDebugEnabled()) {
		// logger.debug("客户端创建完成。...");
		// }
	}

	@Override
	public Object get(String key) {
		Object obj = mcc.get(key);

		if (logger.isDebugEnabled()) {
			logger.debug("命中缓存：{}，key：{}", new Object[] { (obj != null), key });
		}

		return obj;
	}

	@Override
	public boolean isKeyInCache(String key) {
		return mcc.keyExists(key);
	}

	@Override
	public void put(String key, Object value) {
		mcc.set(key, value);
		if (logger.isDebugEnabled()) {
			logger.debug("缓存数据，key：{}", key);
		}
	}

	@Override
	public boolean remove(String key) {
		boolean result = mcc.delete(key);
		if (logger.isDebugEnabled()) {
			logger.debug("删除缓存，key：{}", key);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected void prepareClient() {

		mcc = new MemCachedClient();

		if (errorHandler != null) {
			mcc.setErrorHandler(errorHandler);
		}

		// Integer[] weights = { 5, 1 };

		// grab an instance of our connection pool
		SockIOPool pool = SockIOPool.getInstance();

		// set the servers and the weights
		pool.setServers(servers);
		// pool.setWeights(weights);

		// set some basic pool settings
		// 5 initial, 5 min, and 250 max conns
		// and set the max idle time for a conn
		// to 6 hours
		pool.setInitConn(getInitConn());
		pool.setMinConn(getMinConn());
		pool.setMaxConn(getMaxConn());
		pool.setMaxIdle(1000 * 60 * 60 * 6);

		// set the sleep for the maint thread
		// it will wake up every x seconds and
		// maintain the pool size
		pool.setMaintSleep(30);

		// set some TCP settings
		// disable nagle
		// set the read timeout to 3 secs
		// and don't set a connect timeout
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(getConnectTimeout());

		// initialize the connection pool
		pool.initialize();

		// lets set some compression on for the client
		// compress anything larger than 64k
		mcc.setCompressEnable(true);
		mcc.setCompressThreshold(64 * 1024);

		if (logger.isInfoEnabled()) {
			Map map = mcc.stats(servers);
			Set keys = map.keySet();
			if (keys.size() == 0) {
				logger.error("客户端创建遇到错误，无法创建。");
			}
			for (Object key : keys) {
				logger.info("客户端创建完成。key = 【{}】， status = 【{}】", key, map
						.get(key));
			}
		}

	}

	public String[] getServers() {
		return servers;
	}

	public void setServers(String[] servers) {
		this.servers = servers;
	}

	/**
	 * 获取初始化连接数
	 * 
	 * @return 初始化连接数
	 */
	public int getInitConn() {
		return initConn;
	}

	/**
	 * 设置初始化连接数
	 * 
	 * @param initConn
	 *            初始化连接数
	 */
	public void setInitConn(int initConn) {
		this.initConn = initConn;
	}

	/**
	 * 获取最小连接数
	 * 
	 * @return 最小连接数
	 */
	public int getMinConn() {
		return minConn;
	}

	/**
	 * 设置最小连接数
	 * 
	 * @param minConn
	 *            最小连接数
	 */
	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}

	/**
	 * 获取最大连接数
	 * 
	 * @return 最大连接数
	 */
	public int getMaxConn() {
		return maxConn;
	}

	/**
	 * 设置最大连接数
	 * 
	 * @param maxConn
	 *            最大连接数
	 */
	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	/**
	 * 获取连接超时时间（毫秒）
	 * 
	 * @return 连接超时时间（毫秒）
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * 设置连接超时时间（毫秒）
	 * 
	 * @param connectTimeout
	 *            连接超时时间（毫秒）
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 获取错误处理器
	 * 
	 * @return 错误处理器
	 */
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * 设置错误处理器
	 * 
	 * @param errorHandler
	 *            错误处理器
	 */
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

}
