package com.dayatang.commons.querychannel.service;

import java.io.Serializable;
import java.util.List;

import com.dayatang.commons.domain.Entity;
import com.dayatang.commons.querychannel.support.Page;
import com.dayatang.commons.querychannel.support.QueryProperty;

public interface QueryChannelService extends Serializable {
	/**
	 * 根据查询语句得到结果集的个数
	 * 
	 * @param queryStr  查询语句
	 * @param params 查询参数
	 * @return long 结果集的个数
	 */
	public long queryResultSize(String queryStr, Object[] params);

	/**
	 * 根据查询语句得到结果集(List),带分页功能
	 * 
	 * @param <T>
	 * @param clazz
	 * @param queryStr
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param firstRow
	 *            起始行数
	 * @param pageSize
	 *            查找行数
	 * @return 结果集
	 */
	public <T extends Entity> List<T> queryPagedResult(Class<T> clazz, String queryStr,
			Object[] params, int firstRow, int pageSize);

	/**
	 * 根据查询语句得到结果集(List),不带分页功能
	 * 
	 * @param <T>
	 * @param clazz
	 * @param queryStr
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @return 结果集
	 */
	public <T extends Entity> List<T> queryResult(Class<T> clazz, String queryStr,
			Object[] params);

	/**
	 * 只返回一个对象的查询方法
	 * 
	 * <br/>
	 * 1.查不到结果则返回null <br/>
	 * 2.查询的结果不是只有一个对象会抛运行时异常
	 * 
	 * @param <T>
	 * @param clazz
	 * @param queryStr
	 *            查询语句
	 * @param params
	 *            可变参数.例如查询参数,查询参数数组
	 * @return 查询的对象
	 */
	public <T extends Entity> T querySingleResult(Class<T> clazz, String queryStr,
			Object[] params);

	/**
	 * 根据当前页码的分页查询函数,如果查不到数据则返回一个没有数据的Page
	 * 
	 * @param <T>
	 * @param clazz
	 * @param queryStr
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            查找行数
	 * @return 分页组件
	 */
	public <T extends Entity> Page<T> queryPagedByPageNO(Class<T> clazz, String queryStr,
			Object[] params, int currentPage, int pageSize);

	public <T extends Entity> Page<T> queryPagedByPageNOAndNamedQuery(Class<T> clazz,
			String queryName, Object[] params, int currentPage, int pageSize);

	/**
	 * 根据起始行数的分页查询函数,如果查不到数据则返回一个没有数据的Page
	 * 
	 * @param <T>
	 * @param clazz
	 * @param queryStr
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param firstRow
	 *            起始行数
	 * @param pageSize
	 *            查找行数
	 * @return 分页组件
	 */
	public <T extends Entity> Page<T> queryPagedByFirstRow(Class<T> clazz, String queryStr,
			Object[] params, int firstRow, int pageSize);

	/**
	 * 分页查找(暂时仅适用于单表查询）
	 * 
	 * @param <T>
	 * @param clazz
	 * @param queryProperties
	 *            查询属性
	 * @param orderStr
	 *            字段排序
	 * @param currentPage
	 *            当前页号
	 * @param pageSize
	 *            每页大小
	 * @return
	 */
	public <T extends Entity> Page<T> queryPagedByCriteria(Class<T> clazz,
			List<QueryProperty> queryProperties, String orderStr,
			int currentPage, int pageSize);
}
