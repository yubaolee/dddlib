package com.dayatang.commons.querychannel.service.impl;

import java.util.List;

import com.dayatang.commons.domain.Entity;
import com.dayatang.commons.querychannel.support.Page;
import com.dayatang.commons.querychannel.support.QueryProperty;

public interface QueryChannelRepository {

	long queryResultSize(String queryStr, Object[] parames);

	<T extends Entity> List<T> queryPagedResult(Class<T> clazz, String queryStr,
			Object[] values, int firstRow, int pageSize);

	<T extends Entity> List<T> queryResult(Class<T> clazz, String queryStr, Object[] values);

	<T extends Entity> T querySingleResult(Class<T> clazz, String queryStr, Object[] values);

	<T extends Entity> Page<T> queryPagedByPageNO(Class<T> clazz, String queryStr,
			Object[] params, int currentPage, int pageSize);

	<T extends Entity> Page<T> queryPagedByFirstRow(Class<T> clazz, String queryStr,
			Object[] params, int firstRow, int pageSize);

	@Deprecated
	<T extends Entity> Page<T> queryPagedByCriteria(Class<T> clazz,
			List<QueryProperty> queryProperties, String orderStr,
			int currentPage, int pageSize);

	<T extends Entity> Page<T> queryPagedByPageNOAndNamedQuery(Class<T> clazz,
			String namedQuery, Object[] params, int currentPage, int pageSize);

}
