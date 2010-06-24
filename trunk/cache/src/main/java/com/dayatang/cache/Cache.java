package com.dayatang.cache;

import java.io.Serializable;

/**
 * 缓存接口
 * 
 * @author chencao
 * 
 */
public interface Cache extends Serializable {

	/**
	 * 根据指定key，从缓存中获取对象
	 * 
	 * @param key 对象的key
	 * @return 缓存中的对象
	 */
	public Object get(String key);

	/**
	 * 把对象以key的形式放入缓存（同名key覆盖）
	 * 
	 * @param key 指定对象的key
	 * @param value 放入缓存的对象
	 */
	public void put(String key, Object value);

	/**
	 * 删除key所对应的缓存，key不存在不报错
	 *
	 * @param key 需要删除缓存对象的key
	 * @return true=成功，false=失败
	 */
	public boolean remove(String key);
	
	/**
	 * 判断key是否已经已存在
	 * 
	 * @param key 缓存对象的key
	 * @return true=存在，false=不存在
	 */
	public boolean isKeyInCache(String key);

}
