package com.dayatang.utils;

import java.util.Date;

/**
 * 用于读取全局性配置信息的接口。
 * @author yyang
 *
 */
public interface Configuration {

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	String getString(String key, String defaultValue);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值
	 */
	String getString(String key);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	int getInt(String key, int defaultValue);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到返回0
	 */
	int getInt(String key);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	long getLong(String key, long defaultValue);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到返回0
	 */
	long getLong(String key);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	double getDouble(String key, double defaultValue);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到返回0
	 */
	double getDouble(String key);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	boolean getBoolean(String key, boolean defaultValue);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到配置返回false
	 */
	boolean getBoolean(String key);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @param defaultValue
	 *            配置文件中找不到则返回默认值
	 * @return 返回的配置值
	 */
	Date getDate(String key, Date defaultValue);

	/**
	 * 获取某关键字对应的配置
	 * 
	 * @param key
	 *            配置关键字，如果激活前缀，则在配置文件中查找先查找key，如果找不到则查找prefix+key的配置项
	 * @return 返回的配置值 找不到返回0
	 */
	Date getDate(String key);

}