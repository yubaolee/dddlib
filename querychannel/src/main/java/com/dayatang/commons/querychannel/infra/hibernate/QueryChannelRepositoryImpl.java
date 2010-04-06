package com.dayatang.commons.querychannel.infra.hibernate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.dayatang.commons.domain.Entity;
import com.dayatang.commons.querychannel.service.impl.QueryChannelRepository;
import com.dayatang.commons.querychannel.support.Page;
import com.dayatang.commons.querychannel.support.QueryProperty;
import com.dayatang.commons.repository.EntityRepositoryHibernate;

@SuppressWarnings("unchecked")
public class QueryChannelRepositoryImpl extends EntityRepositoryHibernate implements QueryChannelRepository {

	@Override
	public <T extends Entity> Page<T> queryPagedByCriteria(Class<T> clazz, List<QueryProperty> queryProperties,
			String orderStr, int currentPage, int pageSize) {

		// 该实现有待被改造

		String[] order = null;
		Criteria countCriteria = null;
		Criteria dataCriteria = null;
		dataCriteria = getSession().createCriteria(clazz);
		for (QueryProperty q : queryProperties) {
			if (q.getOperator().equals("eq")) {
				countCriteria.add(Restrictions.eq(q.getPropertyName(), q.getFirstValue()));
				dataCriteria.add(Restrictions.eq(q.getPropertyName(), q.getFirstValue()));
			}
			// >
			else if (q.getOperator().equals("gt")) {
				countCriteria.add(Restrictions.gt(q.getPropertyName(), q.getFirstValue()));
				dataCriteria.add(Restrictions.gt(q.getPropertyName(), q.getFirstValue()));
			}
			// >=
			else if (q.getOperator().equals("ge")) {
				countCriteria.add(Restrictions.ge(q.getPropertyName(), q.getFirstValue()));
				dataCriteria.add(Restrictions.ge(q.getPropertyName(), q.getFirstValue()));
			}
			// <
			else if (q.getOperator().equals("lt")) {
				countCriteria.add(Restrictions.lt(q.getPropertyName(), q.getFirstValue()));
				dataCriteria.add(Restrictions.lt(q.getPropertyName(), q.getFirstValue()));
			}
			// <=
			else if (q.getOperator().equals("le")) {
				countCriteria.add(Restrictions.le(q.getPropertyName(), q.getFirstValue()));
				dataCriteria.add(Restrictions.le(q.getPropertyName(), q.getFirstValue()));
			}
			// between
			else if (q.getOperator().equals("between")) {
				countCriteria.add(Restrictions.between(q.getPropertyName(), q.getFirstValue(), q.getSecondValue()));
				dataCriteria.add(Restrictions.between(q.getPropertyName(), q.getFirstValue(), q.getSecondValue()));
			}
			// like
			else if (q.getOperator().equals("like")) {
				countCriteria.add(Restrictions.like(q.getPropertyName(), q.getFirstValue()));
				dataCriteria.add(Restrictions.like(q.getPropertyName(), q.getFirstValue()));
			}
			// and
			else if (q.getOperator().equals("and")) {
				// countCriteria.add(Expression.gt(q.getPropertyName(),q.getValue()));
				// dataCriteria.add(Expression.gt(q.getPropertyName(),q.getValue()));
			}
			// or
			else if (q.getOperator().equals("or")) {
				// countCriteria.add(Expression.gt(q.getPropertyName(),q.getValue()));
				// dataCriteria.add(Expression.gt(q.getPropertyName(),q.getValue()));
			}
			//
			else if (q.getOperator().equals("eqProperty")) {
				// countCriteria.add(Expression.gt(q.getPropertyName(),q.getValue()));
				// dataCriteria.add(Expression.gt(q.getPropertyName(),q.getValue()));
			}
		}
		countCriteria.setProjection(Projections.rowCount());
		long totalCount = ((Integer) countCriteria.uniqueResult()).longValue();
		if (totalCount < 1)
			return new Page<T>(pageSize);
		dataCriteria.setFirstResult(getFirstRow(currentPage, pageSize));
		dataCriteria.setMaxResults(pageSize);
		if (orderStr != null && !"".equals(orderStr)) {
			order = orderStr.split(" ");
			if (order[1].toLowerCase().equals("asc")) {
				dataCriteria.addOrder(Order.asc(order[0]));
			} else if (order[1].toLowerCase().equals("desc")) {
				dataCriteria.addOrder(Order.desc(order[0]));
			}
		}
		List<T> dataList = dataCriteria.list();
		return new Page<T>(getFirstRow(currentPage, pageSize), totalCount, pageSize, dataList);
	}

	@Override
	public <T extends Entity> Page<T> queryPagedByFirstRow(Class<T> clazz, String queryStr, Object[] params,
			int firstRow, int pageSize) {

		long totalCount = queryResultSize(queryStr, params);
		if (totalCount < 1)
			return new Page<T>();

		return new Page<T>(firstRow, totalCount, pageSize,
				queryPagedResult(clazz, queryStr, params, firstRow, pageSize));

	}

	@Override
	public <T extends Entity> Page<T> queryPagedByPageNO(Class<T> clazz, String queryStr, Object[] params,
			int currentPage, int pageSize) {

		long totalCount = queryResultSize(queryStr, params);
		if (totalCount < 1)
			return new Page<T>(pageSize);

		return new Page<T>(getFirstRow(currentPage, pageSize), totalCount, pageSize, queryPagedResult(clazz, queryStr,
				params, getFirstRow(currentPage, pageSize), pageSize));

	}

	@Override
	public <T extends Entity> Page<T> queryPagedByPageNOAndNamedQuery(Class<T> clazz, String namedQuery,
			Object[] params, int currentPage, int pageSize) {

		String queryStr = getSession().getNamedQuery(namedQuery).getQueryString();

		long totalCount = queryResultSize(queryStr, params);
		if (totalCount < 1)
			return new Page<T>(pageSize);

		return new Page<T>(getFirstRow(currentPage, pageSize), totalCount, pageSize, queryPagedResult(clazz, queryStr,
				params, getFirstRow(currentPage, pageSize), pageSize));
	}

	@Override
	public <T extends Entity> List<T> queryPagedResult(Class<T> clazz, String queryStr, Object[] values, int firstRow,
			int pageSize) {
		return createQuery(queryStr, values).setFirstResult(firstRow).setMaxResults(pageSize).list();
	}

	@Override
	public <T extends Entity> List<T> queryResult(Class<T> clazz, String queryStr, Object[] values) {
		return createQuery(queryStr, values).list();
	}

	@Override
	public long queryResultSize(String queryStr, Object[] parames) {
		int indexGroup = queryStr.toLowerCase().indexOf("group by");
		if (indexGroup != -1) {// 用了group by 则计算group by记录的数量
			return Integer.valueOf(createQuery(removeOrders(queryStr), parames).list().size()).longValue();
		}
		return (Long) createQuery(buildCountQueryStr(queryStr), parames).uniqueResult();
	}

	@Override
	public <T extends Entity> T querySingleResult(Class<T> clazz, String queryStr, Object[] values) {
		return queryResult(clazz, queryStr, values).get(0);
	}

	public Query createQuery(String queryStr, Object[] values) {
		Query query = getSession().createQuery(queryStr);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	/**
	 * 获取任一页第一条数据在数据集的位置.
	 * 
	 * @param currentPage
	 *            从1开始的当前页号
	 * @param pageSize
	 *            每页记录条数
	 * @return 该页第一条数据号
	 */
	protected static int getFirstRow(int currentPage, int pageSize) {
		return Page.getStartOfPage(currentPage, pageSize);
	}

	/**
	 * 构造一个查询数据条数的语句,不能用于union
	 * 
	 * @param queryStr
	 *            源语句
	 * @return 查询数据条数的语句
	 */
	private static String buildCountQueryStr(String queryStr) {
		String removedOrdersQueryStr = removeOrders(queryStr);
		int index = removedOrdersQueryStr.toLowerCase().indexOf("from");

		StringBuilder builder = new StringBuilder("select count(*) ");
		if (index != -1) {
			builder.append(removedOrdersQueryStr.substring(index));
		} else {
			builder.append(removedOrdersQueryStr);
		}
		return builder.toString();
	}

	/**
	 * 去除查询语句的orderby 子句
	 * 
	 */
	private static String removeOrders(String queryStr) {
		Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE).matcher(queryStr);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
