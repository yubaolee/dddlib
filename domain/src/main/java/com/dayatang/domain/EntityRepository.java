package com.dayatang.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 仓储访问接口。用于存取和查询数据库中的各种类型的实体。
 * 
 * @author yyang
 * 
 */
public interface EntityRepository {

	/**
	 * 将实体（无论是新的还是修改了的）保存到仓储中。
	 * @param entity 要存储的实体实例。
	 */
	<T extends Entity> T save(T entity);

	/**
	 * 将实体从仓储中删除。如果仓储中不存在此实例将抛出EntityNotExistedException异常。
	 * @param entity 要删除的实体实例。
	 */
	void remove(Entity entity);

	/**
	 * 判断仓储中是否存在指定ID的实体实例。
	 * @param <T> 实体类型
	 * @param clazz 实体的类
	 * @param id 实体标识
	 * @return 如果实体实例存在，返回true，否则返回false
	 */
	<T extends Entity> boolean exists(Class<T> clazz, Serializable id);

	/**
	 * 从仓储中获取指定类型、指定ID的实体
	 * @param <T> 实体类型
	 * @param clazz 实体的类
	 * @param id 实体标识
	 * @return 一个实体实例。
	 */
	<T extends Entity> T get(Class<T> clazz, Serializable id);

	/**
	 * 从仓储中装载指定类型、指定ID的实体
	 * @param <T> 实体类型
	 * @param clazz 实体的类
	 * @param id 实体标识
	 * @return 一个实体实例。
	 */
	<T extends Entity> T load(Class<T> clazz, Serializable id);

	/**
	 * 从仓储中获取entity参数所代表的未修改的实体
	 * @param <T> 实体类型
	 * @param clazz 实体的类
	 * @param entity 要查询的实体
	 * @return 参数entity在仓储中的未修改版本
	 */
	<T extends Entity> T getUnmodified(Class<T> clazz, T entity);

	/**
	 * 查找指定类型的所有实体
	 * @param <T> 实体类型
	 * @param clazz 实体的类
	 * @return 符合条件的实体集合
	 */
	<T extends Entity> List<T> findAll(Class<T> clazz);
	
	/**
	 * 根据指定的条件查找实体
	 * @param <T> 实体类型
	 * @param settings 查询的条件及排序信息
	 * @return 符合条件的实体集合
	 */
	<T extends Entity> List<T> find(QuerySettings<T> settings);
	
	/**
	 * 根据查询语句和指定的参数从仓储中查询符合条件的结果
	 * @param <T> 返回值集合中包含的元素的类型
	 * @param queryString 访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params 查询参数，以定位参数的形式代入queryString中的问号占位符
	 * @param resultClass 查询目标对象的类
	 * @return 符合查询条件的实体的集合.
	 */
	<T> List<T> find(String queryString, Object[] params, Class<T> resultClass);
	
	/**
	 * 根据查询语句和指定的参数从仓储中查询符合条件的的结果
	 * @param <T> 返回值集合中包含的元素的类型
	 * @param queryString 访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params 查询参数，以命名参数的形式代入queryString中的占位符
	 * @param resultClass 查询目标对象的类
	 * @return 符合查询条件的实体的集合.
	 */
	<T> List<T> find(String queryString, Map<String, Object> params, Class<T> resultClass);
	
	/**
	 * 根据命名的查询和指定的参数从仓储中查询符合条件的的结果
	 * @param <T> 返回值集合中包含的元素的类型
	 * @param queryName 命名查询的名字。
	 * @param params 查询参数，以定位参数的形式代入queryString中的问号占位符
	 * @param resultClass 查询目标对象的类
	 * @return 符合查询条件的实体的集合.
	 */
	<T> List<T> findByNamedQuery(String queryName, Object[] params, Class<T> resultClass);
	
	/**
	 * 根据命名的查询和指定的参数从仓储中查询符合条件的的结果
	 * @param <T> 返回值集合中包含的元素的类型
	 * @param queryName 命名查询的名字。
	 * @param params 查询参数，以命名参数的形式代入queryString中的占位符
	 * @param resultClass 查询目标对象的类
	 * @return 符合查询条件的实体的集合.
	 */
	<T> List<T> findByNamedQuery(String queryName, Map<String, Object> params, Class<T> resultClass);

	/**
	 * 按例查询。
	 * @param <T> 查询的目标实体类型
	 * @param <E> 查询样例的类型
	 * @param example 查询样例
	 * @param settings 查询设置
	 * @return 与example相似的T类型的范例
	 */
	<T extends Entity, E extends T> List<T> findByExample(E example, ExampleSettings<T> settings);
	
	/**
	 * 根据查询设置返回单一结果
	 * @param <T>
	 * @param settings
	 * @return
	 */
	<T extends Entity> T getSingleResult(QuerySettings<T> settings);
	
	/**
	 * 根据查询语句和指定的参数访问仓储，返回单一结果。
	 * @param queryString 访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params 查询参数，以定位参数的形式代入queryString中的问号占位符
	 * @return 查询的单一结果
	 */
	Object getSingleResult(String queryString, Object[] params);
	
	/**
	 * 根据查询语句和指定的参数访问仓储，返回单一结果。
	 * @param queryString 访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params 查询参数，以命名参数的形式代入queryString中的占位符
	 * @return 查询的单一结果
	 */
	Object getSingleResult(String queryString, Map<String, Object> params);
	
	/**
	 * 执行更新仓储的操作。
	 * @param queryString 访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params 查询参数，以定位参数的形式代入queryString中的问号占位符
	 */
	void executeUpdate(String queryString, Object[] params);
	
	/**
	 * 执行更新仓储的操作。
	 * @param queryString 访问仓储的DSL语句，采用JPA QL的语义，但不一定用JPA实现。
	 * @param params 查询参数，以命名参数的形式代入queryString中的占位符
	 */
	void executeUpdate(String queryString, Map<String, Object> params);
}
