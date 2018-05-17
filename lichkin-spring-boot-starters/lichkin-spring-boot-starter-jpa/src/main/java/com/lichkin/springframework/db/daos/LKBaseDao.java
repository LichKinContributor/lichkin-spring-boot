package com.lichkin.springframework.db.daos;

import static com.lichkin.framework.db.statics.LKSQLStatics.DEFAULT_PAGE_NUMBER;
import static com.lichkin.framework.db.statics.LKSQLStatics.DEFAULT_PAGE_SIZE;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.HibernateException;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.lichkin.framework.db.statics.LKSQLStatics;
import com.lichkin.framework.db.vos.LKOneVo;
import com.lichkin.framework.db.vos.LKSqlVo;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKStringUtils;
import com.lichkin.springframework.db.utils.LKEntityInitializer;

/**
 * 基本数据库访问类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKBaseDao implements LKDao {

	/** 日志对象 */
	protected final LKLog logger = LKLogFactory.getLog(getClass());


	/**
	 * 创建日志信息
	 * @param isSQL true为SQL语句；fales为HQL语句。
	 * @param sql SQL语句
	 * @param params 参数列表
	 * @return 日志信息
	 */
	private StringBuilder createLogInfo(final boolean isSQL, final String sql, final Object[] params) {
		final StringBuilder fullSql = new StringBuilder(isSQL ? "SQL: " : "HQL: ");
		fullSql.append(sql);
		if (ArrayUtils.isNotEmpty(params)) {
			fullSql.append(" [params: ");
			for (int i = 0; i < params.length; i++) {
				fullSql.append(String.valueOf(params[i]));
				if (i != (params.length - 1)) {
					fullSql.append(", ");
				}
			}
			fullSql.append("]");
		}
		return fullSql;
	}


	/**
	 * 记录日志
	 * @param isSQL true为SQL语句；fales为HQL语句。
	 * @param sql SQL语句
	 * @param params 参数列表
	 */
	private void logSql(final boolean isSQL, final String sql, final Object[] params) {
		logger.info(createLogInfo(isSQL, sql, params).toString());
	}


	/**
	 * 记录日志
	 * @param isSQL true为SQL语句；fales为HQL语句。
	 * @param sql SQL语句
	 * @param params 参数列表
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 */
	private void logSql(final boolean isSQL, final String sql, final Object[] params, final long startTime, final long endTime) {
		logger.info("query execution time is %sms, from %s to %s.【%s】", String.valueOf(endTime - startTime), formatTime(startTime), formatTime(endTime), createLogInfo(isSQL, sql, params).toString());
	}


	/**
	 * 转换成时间格式
	 * @param millisencond 毫秒数
	 * @return 时间字符串
	 */
	private String formatTime(final long millisencond) {
		return LKDateTimeUtils.toString(new DateTime(millisencond));
	}


	/**
	 * 设置分页信息
	 * @param query 查询对象
	 * @param pageable 分页信息
	 */
	private void setPageable(final Query query, final Pageable pageable) {
		if (pageable != null) {
			final int pageNumber = pageable.getPageNumber();
			final int pageSize = pageable.getPageSize();
			query.setFirstResult(pageNumber * pageSize);
			query.setMaxResults(pageSize);
		}
	}


	/**
	 * 创建SQL查询对象
	 * @param clazz 查询结果映射对象
	 * @param allMappingAliases 是否全映射
	 * @param sql SQL语句
	 * @param pageable 分页信息
	 * @return SQL查询对象
	 */
	private <T> Query createSQLQuery(final Class<T> clazz, final boolean allMappingAliases, final String sql, final Pageable pageable) {
		final Query query = getEntityManager().createNativeQuery(sql, clazz);
		setPageable(query, pageable);
		return query;
	}


	/**
	 * 创建HQL查询对象
	 * @param clazz 查询结果映射对象
	 * @param sql SQL语句
	 * @param pageable 分页信息
	 * @return HQL查询对象
	 */
	private <T> Query createHQLQuery(final Class<T> clazz, final String sql, final Pageable pageable) {
		final Query query = getEntityManager().createQuery(sql, clazz);
		setPageable(query, pageable);
		return query;
	}


	/**
	 * 设置参数
	 * @param isSQL 是否为SQL语句
	 * @param query 查询对象
	 * @param params 参数数组
	 * @return 参数
	 */
	private void setParams(final boolean isSQL, final Query query, final Object[] params) {
		if (params != null) {
			if (isSQL) {
				for (int i = 0; i < params.length; i++) {
					Object param = params[i];
					if ((param != null) && param.getClass().isEnum()) {
						param = param.toString();
					}
					query.setParameter(i + 1, param);
				}
			} else {
				for (int i = 0; i < params.length; i++) {
					query.setParameter(i + 1, params[i]);
				}
			}
		}
	}


	/**
	 * 查询条数
	 * @param sql SQL语句
	 * @param params 参数数组
	 * @return 条数
	 */
	private long queryCountBySql(final String sql, final Object[] params) {
		final boolean isSQL = true;

		// 条数查询语句
		final String cntSql = "select count(1) as one from (" + sql + ") AS LK";

		// 记录日志
		logSql(isSQL, cntSql, params);

		// 创建查询
		final Query cntQuery = getEntityManager().createNativeQuery(cntSql);

		// 设置参数
		setParams(isSQL, cntQuery, params);

		// 执行查询
		final Object result = cntQuery.getSingleResult();
		if (result != null) {
			final Object one = ((LKOneVo) result).getOne();
			if (one != null) {
				return Long.parseLong(one.toString());
			}
		}

		return 0L;
	}


	/**
	 * 查询条数
	 * @param clazz 查询结果映射对象
	 * @param sql SQL语句
	 * @param params 参数数组
	 * @return 条数
	 */
	private <T> long queryCountByHql(final Class<T> clazz, final String sql, final Object[] params) {
		final boolean isSQL = false;

		// 条数查询语句
		final String entityName = clazz.getName();
		final String upperCase = sql.toUpperCase();
		String cntSql = "select count(LK) from " + entityName + " AS LK";
		if (upperCase.contains(LKSQLStatics.WHERE)) {
			cntSql = cntSql + LKSQLStatics.BLANKWHEREBLANK + sql.substring(upperCase.indexOf(LKSQLStatics.WHERE) + LKSQLStatics.WHERE.length());
		}

		// 记录日志
		logSql(isSQL, cntSql, params);

		// 创建查询
		final Query cntQuery = getEntityManager().createQuery(cntSql);

		// 设置参数
		setParams(false, cntQuery, params);

		// 执行查询
		final Object result = cntQuery.getSingleResult();
		if (result != null) {
			return Long.parseLong(result.toString());
		}
		return 0L;
	}


	/**
	 * 拼接排序
	 * @param isSQL true为SQL语句；fales为HQL语句。
	 * @param pageable 分页信息
	 * @param sql SQL语句
	 * @return 分页查询语句
	 */
	private String appendSorts(final boolean isSQL, final Pageable pageable, final String sql) {
		final Sort sort = pageable.getSort();
		if (sort == null) {
			return sql;
		}

		final StringBuilder pageSql = new StringBuilder(sql);
		pageSql.append(" ORDER BY");
		for (final Order order : sort) {
			if (order != null) {
				final String property = order.getProperty();
				final String direction = order.getDirection().toString();
				pageSql.append(" ").append(isSQL ? LKStringUtils.humpToUnderline(property) : property).append(" ").append(direction).append(",");
			}
		}
		return pageSql.deleteCharAt(pageSql.length() - 1).toString();
	}


	/**
	 * 返回对象
	 * @param list 列表
	 * @return 对象
	 */
	private <T> T returnOne(final List<T> list) {
		if ((list != null) && !list.isEmpty()) {
			if (list.size() != 1) {
				throw new HibernateException(String.format("you have %s results", String.valueOf(list.size())));
			}
			return list.get(0);
		}
		return null;
	}


	/**
	 * 执行查询
	 * @param isSQL true为SQL语句；fales为HQL语句。
	 * @param query 查询对象
	 * @param sql SQL语句
	 * @param params 参数
	 * @return 列表
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> getResultList(final boolean isSQL, final Query query, final String sql, final Object[] params) {
		final long startTime = System.currentTimeMillis();
		final List<T> resultList = query.getResultList();
		final long endTime = System.currentTimeMillis();
		logSql(isSQL, sql, params, startTime, endTime);
		return resultList;
	}


	/**
	 * 执行更新
	 * @param isSQL true为SQL语句；fales为HQL语句。
	 * @param query 查询对象
	 * @param sql SQL语句
	 * @param params 参数
	 * @return 更新的条数
	 */
	private <T> int executeUpdate(final boolean isSQL, final Query query, final String sql, final Object[] params) {
		final long startTime = System.currentTimeMillis();
		final int resultList = query.executeUpdate();
		final long endTime = System.currentTimeMillis();
		logSql(isSQL, sql, params, startTime, endTime);
		return resultList;
	}


	/**
	 * 获取列表
	 * @param isSQL true为SQL语句；fales为HQL语句。
	 * @param sqlVo 查询对象
	 * @param clazz 查询结果映射对象
	 * @param allMappingAliases 是否全映射
	 * @return 列表
	 */
	private <T> List<T> getList(final boolean isSQL, final LKSqlVo sqlVo, final Class<T> clazz, final boolean allMappingAliases) {
		final String sql = sqlVo.getSql();
		final Object[] params = sqlVo.getParams();

		// 记录日志
		logSql(isSQL, sql, params);

		// 创建查询
		final Query query = isSQL ? createSQLQuery(clazz, allMappingAliases, sql, null) : createHQLQuery(clazz, sql, null);

		// 设置参数
		setParams(isSQL, query, params);

		// 执行查询
		return getResultList(isSQL, query, sql, params);
	}


	/**
	 * 获取分页
	 * @param isSQL true为SQL语句；fales为HQL语句。
	 * @param sqlVo 查询对象
	 * @param clazz 查询结果映射对象
	 * @param pageable 分页信息
	 * @param allMappingAliases 是否全映射
	 * @return 分页
	 */
	private <T> Page<T> getPage(final boolean isSQL, final LKSqlVo sqlVo, final Class<T> clazz, Pageable pageable, final boolean allMappingAliases) {
		final String sql = sqlVo.getSql();
		final Object[] params = sqlVo.getParams();

		// 记录日志
		logSql(isSQL, sql, params);

		// 查询条数
		final long cnt = isSQL ? queryCountBySql(sql, params) : queryCountByHql(clazz, sql, params);

		// 参数判断
		if (pageable == null) {
			pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
		}
		if (cnt == 0L) {
			return new PageImpl<>(new ArrayList<T>(), pageable, cnt);
		}

		// 分页查询语句
		final String pageSql = appendSorts(true, pageable, sql);

		// 记录日志
		logSql(isSQL, pageSql, params);

		// 创建查询
		final Query pageQuery = isSQL ? createSQLQuery(clazz, allMappingAliases, sql, pageable) : createHQLQuery(clazz, sql, pageable);
		setPageable(pageQuery, pageable);

		// 设置参数
		setParams(isSQL, pageQuery, params);

		// 执行查询
		return new PageImpl<>(getResultList(isSQL, pageQuery, pageSql, params), pageable, cnt);
	}


	@Override
	public <T> List<T> queryListBySql(final LKSqlVo sqlVo, final Class<T> clazz, final boolean allMappingAliases) {
		return getList(true, sqlVo, clazz, allMappingAliases);
	}


	@Override
	public <T> List<T> queryListBySql(final LKSqlVo sqlVo, final Class<T> clazz) {
		return queryListBySql(sqlVo, clazz, false);
	}


	@Override
	public <T> Page<T> queryPageBySql(final LKSqlVo sqlVo, final Class<T> clazz, final Pageable pageable, final boolean allMappingAliases) {
		return getPage(true, sqlVo, clazz, pageable, allMappingAliases);
	}


	@Override
	public <T> Page<T> queryPageBySql(final LKSqlVo sqlVo, final Class<T> clazz, final Pageable pageable) {
		return queryPageBySql(sqlVo, clazz, pageable, false);
	}


	@Override
	public <T> T queryOneBySql(final LKSqlVo sqlVo, final Class<T> clazz, final boolean allMappingAliases) {
		return returnOne(queryListBySql(sqlVo, clazz, allMappingAliases));
	}


	@Override
	public <T> T queryOneBySql(final LKSqlVo sqlVo, final Class<T> clazz) {
		return queryOneBySql(sqlVo, clazz, false);
	}


	@Override
	public String queryStringBySql(final LKSqlVo sqlVo) {
		final LKOneVo oneVo = queryOneBySql(sqlVo, LKOneVo.class, true);
		if (oneVo == null) {
			return null;
		}
		final Object one = oneVo.getOne();
		if (one == null) {
			return null;
		}
		return one.toString();
	}


	@Override
	public Long queryLongBySql(final LKSqlVo sqlVo) {
		final String one = queryStringBySql(sqlVo);
		if (one != null) {
			return Long.parseLong(one);
		}
		return null;
	}


	@Override
	public int executeUpdateBySql(final LKSqlVo sqlVo) {
		final boolean isSQL = true;
		final String sql = sqlVo.getSql();
		final Object[] params = sqlVo.getParams();

		// 记录日志
		logSql(isSQL, sql, params);

		// 创建更新
		final Query query = getEntityManager().createNativeQuery(sql);

		// 设置参数
		setParams(isSQL, query, params);

		// 执行更新
		return executeUpdate(isSQL, query, sql, params);
	}


	@Override
	public <T> List<T> findListByHql(final LKSqlVo sqlVo, final Class<T> clazz) {
		return getList(false, sqlVo, clazz, false);
	}


	@Override
	public <T> Page<T> findPageByHql(final LKSqlVo sqlVo, final Class<T> clazz, final Pageable pageable) {
		return getPage(false, sqlVo, clazz, pageable, false);
	}


	@Override
	public <T> T findOneByHql(final LKSqlVo sqlVo, final Class<T> clazz) {
		return returnOne(findListByHql(sqlVo, clazz));
	}


	@Override
	public <T> T findOneById(final Class<T> clazz, final String id) {
		final LKSqlVo sqlVo = new LKSqlVo();
		sqlVo.appendSql("from");
		sqlVo.appendSql(clazz.getName());
		sqlVo.appendSql("where id = ?");
		sqlVo.addParam(id);
		return findOneByHql(sqlVo, clazz);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T save(final Object t, final boolean merge) {
		final long startTime = System.currentTimeMillis();
		String json = null;
		final StringBuilder logInfo = new StringBuilder();
		try {
			if (merge) {
				logInfo.append("merge");
				if (t instanceof List<?>) {
					json = LKJsonUtils.toJson(t);
					logInfo.append(" list【");
					logInfo.append(t.getClass().getName());
					logInfo.append("<");
					logInfo.append(((List<?>) t).get(0).getClass().getName());
					logInfo.append(">");
					final List<?> list = (List<?>) t;
					final List<T> ls = new ArrayList<>(list.size());
					for (final Object e : list) {
						final T s = (T) getEntityManager().merge(LKEntityInitializer.initEntity(e));
						ls.add(s);
					}
					return (T) ls;
				} else if (t instanceof Object[]) {
					json = LKJsonUtils.toJson(t);
					logInfo.append(" array【");
					logInfo.append(((Object[]) t)[0].getClass().getName());
					logInfo.append("[]");
					final Object[] list = (Object[]) t;
					final T[] ls = null;
					for (final Object e : list) {
						final T s = (T) getEntityManager().merge(LKEntityInitializer.initEntity(e));
						ArrayUtils.add(ls, s);
					}
					return (T) ls;
				} else {
					json = LKJsonUtils.toJson(t);
					logInfo.append(" one【");
					return (T) getEntityManager().merge(LKEntityInitializer.initEntity(t));
				}
			} else {
				logInfo.append("persist");
				if (t instanceof List<?>) {
					json = LKJsonUtils.toJson(t);
					logInfo.append(" list【");
					logInfo.append(t.getClass().getName());
					logInfo.append("<");
					logInfo.append(((List<?>) t).get(0).getClass().getName());
					logInfo.append(">");
					final List<?> list = (List<?>) t;
					for (final Object e : list) {
						getEntityManager().persist(LKEntityInitializer.initEntity(e));
					}
				} else if (t instanceof Object[]) {
					json = LKJsonUtils.toJson(t);
					logInfo.append(" array【");
					logInfo.append(((Object[]) t)[0].getClass().getName());
					logInfo.append("[]");
					final Object[] list = (Object[]) t;
					for (final Object e : list) {
						getEntityManager().persist(LKEntityInitializer.initEntity(e));
					}
				} else {
					json = LKJsonUtils.toJson(t);
					logInfo.append(" one【");
					getEntityManager().persist(LKEntityInitializer.initEntity(t));
				}
				return null;
			}
		} finally {
			final long endTime = System.currentTimeMillis();

			logInfo.append(t.getClass().getName());
			logInfo.append("】【");
			logInfo.append(json);
			logInfo.append("】, execution time is ");
			logInfo.append(String.valueOf(endTime - startTime));
			logInfo.append("ms, from ");
			logInfo.append(formatTime(startTime));
			logInfo.append(" to ");
			logInfo.append(formatTime(endTime));
			logger.info(logInfo.toString());
		}
	}


	@Override
	public <T> T save(final Object t) {
		return save(t, true);
	}


	@Override
	public <T> T merge(final Object t) {
		return save(t, true);
	}


	@Override
	public <T> T persist(final Object t) {
		return save(t, false);
	}


	@Override
	public void remove(final Object t) {
		final long startTime = System.currentTimeMillis();
		String json = null;
		final StringBuilder logInfo = new StringBuilder("remove");
		if (t instanceof List<?>) {
			json = LKJsonUtils.toJson(t);
			logInfo.append(" list【");
			logInfo.append(t.getClass().getName());
			logInfo.append("<");
			logInfo.append(((List<?>) t).get(0).getClass().getName());
			logInfo.append(">");
			final List<?> list = (List<?>) t;
			for (final Object e : list) {
				getEntityManager().remove(e);
			}
		} else if (t instanceof Object[]) {
			json = LKJsonUtils.toJson(t);
			logInfo.append(" array【");
			logInfo.append(((Object[]) t)[0].getClass().getName());
			logInfo.append("[]");
			final Object[] list = (Object[]) t;
			for (final Object e : list) {
				getEntityManager().remove(e);
			}
		} else {
			json = LKJsonUtils.toJson(t);
			logInfo.append(" one【");
			logInfo.append(t.getClass().getName());
			getEntityManager().remove(t);
		}
		final long endTime = System.currentTimeMillis();
		logInfo.append("】【");
		logInfo.append(json);
		logInfo.append("】, execution time is ");
		logInfo.append(String.valueOf(endTime - startTime));
		logInfo.append("ms, from ");
		logInfo.append(formatTime(startTime));
		logInfo.append(" to ");
		logInfo.append(formatTime(endTime));
		logger.info(logInfo.toString());
	}

}
