package com.dayatang.utils;

import java.util.Date;

/**
 * 用于读写全局性配置信息的接口
 * @author yyang
 *
 */
public interface WritableConfiguration extends Configuration {

	void setDatePattern(String datePattern);

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	void setString(String key, String value);

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	void setInt(String key, int value);

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	void setLong(String key, long value);

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	void setDouble(String key, double value);

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	void setBoolean(String key, boolean value);

	/**
	 * 设置配置值
	 * 
	 * @param key
	 *            配置项关键字
	 * @param value
	 *            值
	 */
	void setDate(String key, Date value);

	/**
	 * 持久化配置信息。
	 */
	void save();

}