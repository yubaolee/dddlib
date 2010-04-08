package com.dayatang.querychannel.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.dayatang.commons.domain.Entity;
import com.dayatang.commons.domain.QuerySettings;
import com.dayatang.querychannel.support.Page;

public interface QueryChannelService extends Serializable {

	public long queryResultSize(String queryStr, Object[] params);

	public <T> List<T> queryResult(String queryStr, Object[] params,
			long firstRow, int pageSize);

	/**
	 * 若结果有多个，返回第一个；没有结果返回NULL
	 * 
	 * @param <T>
	 * @param queryStr
	 * @param params
	 * @return
	 */
	public <T> T querySingleResult(String queryStr, Object[] params);

	public <T> List<T> queryResult(String queryStr, Object[] params);

	public List<Map<String, Object>> queryMapResult(String queryStr,
			Object[] params);

	public <T> Page<T> queryPagedResult(String queryStr, Object[] params,
			long firstRow, int pageSize);

	public <T> Page<T> queryPagedResultByPageNo(String queryStr,
			Object[] params, int currentPage, int pageSize);

	public <T> Page<T> queryPagedResultByNamedQuery(String queryName,
			Object[] params, long firstRow, int pageSize);

	public <T> Page<T> queryPagedResultByPageNoAndNamedQuery(String queryName,
			Object[] params, int currentPage, int pageSize);

	public Page<Map<String, Object>> queryPagedMapResult(String queryStr,
			Object[] params, int currentPage, int pageSize);

	public Page<Map<String, Object>> queryPagedMapResultByNamedQuery(
			String queryName, Object[] params, int currentPage, int pageSize);

	public <T extends Entity> Page<T> queryPagedByQuerySettings(
			QuerySettings<T> settings, int currentPage, int pageSize);
}
