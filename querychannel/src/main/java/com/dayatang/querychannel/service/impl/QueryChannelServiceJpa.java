package com.dayatang.querychannel.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import com.dayatang.commons.domain.Entity;
import com.dayatang.commons.domain.QuerySettings;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@SuppressWarnings("unchecked")
public class QueryChannelServiceJpa extends JpaDaoSupport implements
		QueryChannelService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2520631490347218114L;

	private Query createQuery(EntityManager em, String hql, Object[] params) {
		Query query = em.createQuery(hql);
		return setParameters(query, params);
	}

	@SuppressWarnings("unused")
	private Query createNamedQuery(EntityManager em, String queryName,
			Object[] params) {
		Query query = em.createNamedQuery(queryName);
		return setParameters(query, params);
	}

	private Query setParameters(Query query, Object[] params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
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
	private static int getFirstRow(int currentPage, int pageSize) {
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
		int index = removedOrdersQueryStr.toLowerCase().indexOf(" from ");

		StringBuilder builder = assemCountString(removedOrdersQueryStr, index);

		if (index != -1) {
			builder.append(removedOrdersQueryStr.substring(index));
		} else {
			builder.append(removedOrdersQueryStr);
		}
		return builder.toString();
	}

	private static StringBuilder assemCountString(String hql, int fromIndex) {

		String strInCount = "*";

		int distinctIndex = -1;

		int tempdistinctIndex = hql.indexOf("distinct(");
		if (tempdistinctIndex != -1 && tempdistinctIndex < fromIndex) {
			distinctIndex = tempdistinctIndex;
		}
		tempdistinctIndex = hql.indexOf("distinct ");
		if (tempdistinctIndex != -1 && tempdistinctIndex < fromIndex) {
			distinctIndex = tempdistinctIndex;
		}

		if (distinctIndex != -1) {
			String distinctToFrom = hql.substring(distinctIndex, fromIndex);

			// 除去“,”之后的语句
			int commaIndex = distinctToFrom.indexOf(",");
			String strMayBeWithAs = "";
			if (commaIndex != -1) {
				strMayBeWithAs = distinctToFrom.substring(0, commaIndex);
			} else {
				strMayBeWithAs = distinctToFrom;
			}

			// 除去as语句
			int asIndex = strMayBeWithAs.indexOf(" as ");
			if (asIndex != -1) {
				strInCount = strMayBeWithAs.substring(0, asIndex);
			} else {
				strInCount = strMayBeWithAs;
			}

			// 除去()，因为hql不支持 select count(distinct (...))，但支持select
			// count(distinct ...)
			strInCount = strInCount.replace("(", " ");
			strInCount = strInCount.replace(")", " ");

		}

		StringBuilder builder = new StringBuilder("select count(" + strInCount
				+ ") ");

		return builder;
	}

	/**
	 * 去除查询语句的orderby 子句
	 * 
	 */
	private static String removeOrders(String queryStr) {
		Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE).matcher(queryStr);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 获取每页数, 如果pageSize为0, 则使用Page对象中默认的值
	 * 
	 * @param pageSize
	 * @return
	 */
	private int getPageSize(int pageSize) {
		return (pageSize == 0) ? Page.DEFAULT_PAGE_SIZE : pageSize;
	}

	private long countSizeInSession(EntityManager session,
			final String queryStr, final Object[] params) {

		long totalCount = 0;

		String countQueryString = "";
		boolean containGroup = false;

		int groupIndex = queryStr.toLowerCase().indexOf(" group by ");
		if (groupIndex != -1) {// 用了group by 则计算group by记录的数量
			containGroup = true;
			countQueryString = removeOrders(queryStr);
		} else {
			countQueryString = buildCountQueryStr(queryStr);
		}

		Query query = createQuery(session, countQueryString, params);

		if (containGroup) {
			totalCount = query.getResultList().size();
		} else {
			List count = query.getResultList();
			if (!count.isEmpty()) {
				totalCount = (Long) count.get(0);
			}
		}
		return totalCount;
	}

	@Override
	public <T> T querySingleResult(final String queryStr, final Object[] params) {
		return (T) getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = createQuery(em, queryStr, params);
				List list = query.getResultList();
				return list.isEmpty() ? null : list.get(0);
			}
		});
	}

	@Override
	public <T> List<T> queryResult(final String queryStr, final Object[] params) {
		return getJpaTemplate().executeFind(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = createQuery(em, queryStr, params);

				return query.getResultList();
			}
		});
	}

	@Override
	public <T> List<T> queryResult(final String queryStr,
			final Object[] params, final long firstRow, final int pageSize) {
		return getJpaTemplate().executeFind(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = createQuery(em, queryStr, params);
				query.setFirstResult(Long.valueOf(firstRow).intValue())
						.setMaxResults(pageSize);
				return query.getResultList();
			}
		});
	}

	@Override
	public long queryResultSize(final String queryStr, final Object[] params) {
		return (Long) getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				return countSizeInSession(em, queryStr, params);
			}
		});
	}

	@Override
	public List<Map<String, Object>> queryMapResult(final String queryStr,
			final Object[] params) {
		
		throw new RuntimeException("not implemented yet!");
		
		
//		return getJpaTemplate().executeFind(new JpaCallback() {
//
//			public Object doInJpa(EntityManager em) throws PersistenceException {
//
//				Session session = (Session) em.getDelegate();

				// 如果不是托管，pojo
				// if (!true) {
//				session = (Session) em.getDelegate();
				// } else {
				// // 如果是托管
				// EntityManagerImpl hibernateImpl = (EntityManagerImpl) em
				// .getDelegate();
				// session = hibernateImpl.getSession();
				// }

//				org.hibernate.Query query = session.createQuery(queryStr);
//				if (params != null) {
//					for (int i = 0; i < params.length; i++) {
//						query.setParameter(i, params[i]);
//					}
//				}
//				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
//
//				return query.list();
//			}
//		});
	}

	@Override
	public <T> Page<T> queryPagedResult(final String queryStr,
			final Object[] params, final long firstRow, final int pageSize) {

		return (Page<T>) getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {

				long totalCount = countSizeInSession(em, queryStr, params);

				List<T> data = createQuery(em, queryStr, params)
						.setFirstResult(Long.valueOf(firstRow).intValue())
						.setMaxResults(getPageSize(pageSize)).getResultList();

				return new Page<T>(firstRow, totalCount, getPageSize(pageSize),
						data);
			}
		});

	}

	@Override
	public <T> Page<T> queryPagedResultByPageNo(String queryStr,
			Object[] params, int currentPage, int pageSize) {

		final int firstRow = getFirstRow(currentPage, pageSize);

		return queryPagedResult(queryStr, params, firstRow, pageSize);
	}

	@Override
	public <T> Page<T> queryPagedResultByNamedQuery(final String queryName,
			final Object[] params, final long firstRow, final int pageSize) {
		
		throw new RuntimeException("not implemented yet!");

		// return (Page<T>) getJpaTemplate().execute(new JpaCallback() {
		//
		// public Object doInJpa(EntityManager em) throws PersistenceException {
		//
		// String queryStr = em.createNamedQuery(queryName).toString();这一句有问题，无法得到queryString
		//
		// long totalCount = countSizeInSession(em, queryStr, params);
		//
		// List<T> data = createNamedQuery(em, queryName, params)
		// .setFirstResult(Long.valueOf(firstRow).intValue())
		// .setMaxResults(getPageSize(pageSize)).getResultList();
		//
		// return new Page<T>(firstRow, totalCount, getPageSize(pageSize),
		// data);
		// }
		// });
	}

	@Override
	public <T> Page<T> queryPagedResultByPageNoAndNamedQuery(String queryName,
			Object[] params, int currentPage, int pageSize) {

		throw new RuntimeException("not implemented yet!");
		
//		final int firstRow = getFirstRow(currentPage, pageSize);
//
//		return queryPagedResultByNamedQuery(queryName, params, firstRow,
//				pageSize);
	}

	@Override
	public Page<Map<String, Object>> queryPagedMapResult(final String queryStr,
			final Object[] params, int currentPage, final int pageSize) {
		
		throw new RuntimeException("not implemented yet!");

//		final int firstRow = getFirstRow(currentPage, pageSize);
//
//		return (Page<Map<String, Object>>) getJpaTemplate().execute(
//				new JpaCallback() {
//
//					public Object doInJpa(EntityManager em)
//							throws PersistenceException {
//
//						long totalCount = countSizeInSession(em, queryStr,
//								params);
//
//						Session session = (Session) em.getDelegate();

						// 如果不是托管，pojo
//						if (!true) {
//							session = (Session) em.getDelegate();
//						} else {
//							// 如果是托管
//							EntityManagerImpl hibernateImpl = (EntityManagerImpl) em
//									.getDelegate();
//							session = hibernateImpl.getSession();
//						}

//						org.hibernate.Query query = session
//								.createQuery(queryStr);
//						if (params != null) {
//							for (int i = 0; i < params.length; i++) {
//								query.setParameter(i, params[i]);
//							}
//						}
//						List<Map<String, Object>> data = query.setFirstResult(
//								Long.valueOf(firstRow).intValue())
//								.setMaxResults(getPageSize(pageSize))
//								.setResultTransformer(
//										Criteria.ALIAS_TO_ENTITY_MAP).list();
//
//						return new Page<Map<String, Object>>(firstRow,
//								totalCount, getPageSize(pageSize), data);
//					}
//				});
	}

	@Override
	public Page<Map<String, Object>> queryPagedMapResultByNamedQuery(
			final String queryName, final Object[] params, int currentPage,
			final int pageSize) {

		throw new RuntimeException("not implemented yet!");
		
		
//		final int firstRow = getFirstRow(currentPage, pageSize);
//
//		return (Page<Map<String, Object>>) getJpaTemplate().execute(
//				new JpaCallback() {
//
//					public Object doInJpa(EntityManager em)
//							throws PersistenceException {
//
//						String queryStr = em.createNamedQuery(queryName)
//								.toString();
//
//						long totalCount = countSizeInSession(em, queryStr,
//								params);
//
//						Session session = (Session) em.getDelegate();

						// 如果不是托管，pojo
//						if (!true) {
//							session = (Session) em.getDelegate();
						// } else {
						// // 如果是托管
						// EntityManagerImpl hibernateImpl = (EntityManagerImpl)
						// em
						// .getDelegate();
						// session = hibernateImpl.getSession();
						// }

//						org.hibernate.Query query = session
//								.getNamedQuery(queryName);
//						if (params != null) {
//							for (int i = 0; i < params.length; i++) {
//								query.setParameter(i, params[i]);
//							}
//						}
//						List<Map<String, Object>> data = query.setFirstResult(
//								Long.valueOf(firstRow).intValue())
//								.setMaxResults(getPageSize(pageSize))
//								.setResultTransformer(
//										Criteria.ALIAS_TO_ENTITY_MAP).list();
//
//						return new Page<Map<String, Object>>(firstRow,
//								totalCount, getPageSize(pageSize), data);
//					}
//				});
	}

	@Override
	public <T extends Entity> Page<T> queryPagedByQuerySettings(
			QuerySettings<T> settings, int currentPage, int pageSize) {
		throw new RuntimeException("not implemented yet!");
	}
}
