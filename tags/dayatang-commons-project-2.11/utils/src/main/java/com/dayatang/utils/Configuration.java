package com.dayatang.utils;

import java.util.Date;

/**
 * 用于读取全局性配置信息的接口。
 * @author yyang
 *
 */
public interface Configuration {

	/**
	 * 获取指定的键对应的字符串型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	String getString(String key, String defaultValue);

	/**
	 * 获取指定的键对应的字符串型键值。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。如果键值不存在则返回空字符串。
	 */
	String getString(String key);

	/**
	 * 获取指定的键对应的整数型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	int getInt(String key, int defaultValue);

	/**
	 * 获取指定的键对应的整数型键值。如果键值不存在则返回0。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。
	 */
	int getInt(String key);

	/**
	 * 获取指定的键对应的长整型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	long getLong(String key, long defaultValue);

	/**
	 * 获取指定的键对应的长整型键值。如果键值不存在则返回0。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。
	 */
	long getLong(String key);

	/**
	 * 获取指定的键对应的双精度型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	double getDouble(String key, double defaultValue);

	/**
	 * 获取指定的键对应的双精度型键值。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。如果键值不存在则返回0。
	 */
	double getDouble(String key);

	/**
	 * 获取指定的键对应的布尔型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	boolean getBoolean(String key, boolean defaultValue);

	/**
	 * 获取指定的键对应的布尔型键值。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。如果键值不存在则返回false。
	 */
	boolean getBoolean(String key);

	/**
	 * 获取指定的键对应的日期型键值。
	 * 
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 参数key对应的键值。如果键值不存在则返回参数defaultValue代表的默认值。
	 */
	Date getDate(String key, Date defaultValue);

	/**
	 * 获取指定的键对应的日期型键值。
	 * 
	 * @param key 键
	 * @return 参数key对应的键值。如果键值不存在则返回null。
	 */
	Date getDate(String key);
	
	/**
	 * 从持久化存储中读入配置信息，更新内存中的配置数据。
	 */
	void refresh();

}