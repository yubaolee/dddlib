package com.dayatang.utils;

import java.util.Date;

/**
 * 用于读写全局性配置信息的接口
 * @author yyang
 *
 */
public interface WritableConfiguration extends Configuration {

	/**
	 * 设置指定配置项的字符串型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setString(String key, String value);

	/**
	 * 设置指定配置项的整数型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setInt(String key, int value);

	/**
	 * 设置指定配置项的长整型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setLong(String key, long value);

	/**
	 * 设置指定配置项的双精度型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setDouble(String key, double value);

	/**
	 * 设置指定配置项的布尔型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setBoolean(String key, boolean value);

	/**
	 * 设置指定配置项的日期型键值
	 * 
	 * @param key 配置项的键
	 * @param value 要配置的值
	 */
	void setDate(String key, Date value);

	/**
	 * 持久化配置信息。
	 */
	void save();

}