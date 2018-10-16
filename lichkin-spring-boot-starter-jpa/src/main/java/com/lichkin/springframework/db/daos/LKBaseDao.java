package com.lichkin.springframework.db.daos;

import static com.lichkin.framework.defines.LKFrameworkStatics.SPLITOR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lichkin.framework.db.beans.DeleteSQL;
import com.lichkin.framework.db.beans.LKDBResource;
import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.db.beans.SQL;
import com.lichkin.framework.db.beans.UpdateSQL;
import com.lichkin.framework.defines.beans.LKPageable;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKRandomUtils;

/**
 * 数据库访问对象
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKBaseDao extends LKDao {

	/** 日志对象 */
	private final LKLog logger = LKLogFactory.getLog(getClass());


	/**
	 * 记录开始日志
	 * @param useSQL true:SQL;false:HQL.
	 * @param sqlId 语句唯一标识
	 * @param sql SQL/HQL语句
	 * @param params 参数
	 */
	private void logBeforeQuery(boolean useSQL, String sqlId, String sql, Object[] params) {
		logger.info("%s[%s] -> %s [params:%s]", useSQL ? "SQL" : "HQL", sqlId, sql, LKJsonUtils.toJson(params));
	}


	/**
	 * 记录结束日志
	 * @param useSQL true:SQL;false:HQL.
	 * @param sqlId 语句唯一标识
	 * @param startTime 开始时间
	 */
	private DateTime logAfterQuery(boolean useSQL, String sqlId, DateTime startTime) {
		DateTime endTime = DateTime.now();
		logger.info("%s[%s] -> %s execution time is %sms, from %s to %s.", useSQL ? "SQL" : "HQL", sqlId, useSQL ? "query" : "find", String.valueOf(endTime.compareTo(startTime)), LKDateTimeUtils.toString(startTime), LKDateTimeUtils.toString(endTime));
		return endTime;
	}


	/**
	 * 记录结束日志
	 * @param sqlId 语句唯一标识
	 * @param type 修改类型
	 * @param startTime 开始时间
	 * @param clazz entity类型
	 * @param id 主键
	 */
	private DateTime logAfterModify(String sqlId, String type, DateTime startTime, Class<?> clazz, String id) {
		DateTime endTime = DateTime.now();
		logger.info("HQL[%s] -> %s execution time is %sms, from %s to %s, %s(id=%s).", sqlId, type, String.valueOf(endTime.compareTo(startTime)), LKDateTimeUtils.toString(startTime), LKDateTimeUtils.toString(endTime), clazz.getName(), id);
		return endTime;
	}


	/**
	 * 记录结束日志
	 * @param sqlId 语句唯一标识
	 * @param startTime 转换开始时间，查询结束时间。
	 */
	private void logAfterConvert(String sqlId, DateTime startTime) {
		DateTime endTime = DateTime.now();
		logger.info("SQL[%s] -> convert execution time is %sms, from %s to %s.", sqlId, String.valueOf(endTime.compareTo(startTime)), LKDateTimeUtils.toString(startTime), LKDateTimeUtils.toString(endTime));
	}


	/**
	 * 创建查询对象
	 * @param sql SQL语句
	 * @param params 参数
	 * @param clazz 查询结果映射对象类型
	 * @return 查询对象
	 */
	@SuppressWarnings("deprecation")
	private <B> Query createSQLQuery(String sql, Object[] params, Class<B> clazz) {
		Query query = getEntityManager().createNativeQuery(sql);

		if (clazz.equals(Map.class)) {
			query.unwrap(NativeQuery.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		}

		if (ArrayUtils.isNotEmpty(params)) {
			for (int i = 0; i < params.length; i++) {
				Object param = params[i];
				if ((param != null) && param.getClass().isEnum()) {
					param = param.toString();
				}
				query.setParameter(i + 1, param);
			}
		}

		return query;
	}


	/**
	 * 创建查询对象
	 * @param <E> 返回值类型为clazz参数定义的类型
	 * @param hql HQL语句
	 * @param params 参数
	 * @param clazz 查询结果映射对象类型
	 * @return 查询对象
	 */
	private <E> TypedQuery<E> createHQLQuery(String hql, Object[] params, Class<E> clazz) {
		TypedQuery<E> query = getEntityManager().createQuery(hql, clazz);

		if (ArrayUtils.isNotEmpty(params)) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}

		return query;
	}


	/** 默认页码 */
	public static int DEFAULT_PAGE_NUMBER = 0;

	/** 默认分页大小 */
	public static int DEFAULT_PAGE_SIZE = 25;


	/**
	 * 校验页码
	 * @param pageNumber 页码
	 * @return 页码
	 */
	private int checkPageNumber(Integer pageNumber) {
		return ((pageNumber == null) || (pageNumber <= 0)) ? DEFAULT_PAGE_NUMBER : pageNumber;
	}


	/**
	 * 每页数据量
	 * @param pageSize 每页数据量
	 * @return 每页数据量
	 */
	private int checkPageSize(Integer pageSize) {
		return ((pageSize == null) || (pageSize <= 0)) ? DEFAULT_PAGE_SIZE : pageSize;
	}


	/**
	 * 初始化分页信息
	 * @param query 查询对象
	 * @param pageNumber 页码。正整数或0。从0开始。
	 * @param pageSize 每页数据量。正整数。传入0时表示取框架约定的默认值。
	 * @return 分页信息
	 */
	private Pageable initPageable(Query query, Integer pageNumber, Integer pageSize) {
		pageNumber = checkPageNumber(pageNumber);
		pageSize = checkPageSize(pageSize);
		query.setFirstResult(pageNumber * pageSize);
		query.setMaxResults(pageSize);
		return PageRequest.of(pageNumber, pageSize);
	}


	/**
	 * 查询列表数据
	 * @param <B> 返回值类型为clazz参数定义的类型
	 * @param sql 查询语句
	 * @param params 参数
	 * @param clazz 查询结果映射对象类型
	 * @return 列表数据。无结果时将返回空对象。
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	protected <B> List<B> queryList(String sql, Object[] params, Class<B> clazz) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logBeforeQuery(true, sqlId, sql, params);

		// 创建查询对象
		Query query = createSQLQuery(sql, params, Map.class);

		// 执行查询
		List<Map<String, Object>> listMap = query.getResultList();

		// 记录结束日志
		DateTime enTime = logAfterQuery(true, sqlId, startTime);

		// 转换结果
		List<B> listBean = LKDaoUtils.listMap2listBean(clazz, listMap);

		// 记录转换结束日志
		logAfterConvert(sqlId, enTime);

		// 返回结果
		return listBean;
	}


	/**
	 * 查询列表数据
	 * @param <E> 返回值类型为clazz参数定义的类型
	 * @param hql 查询语句
	 * @param params 参数
	 * @param clazz 查询结果映射对象类型
	 * @return 列表数据。无结果时将返回空对象。
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected <E> List<E> findList(String hql, Object[] params, Class<E> clazz) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String hqlId = LKRandomUtils.create(32);
		logBeforeQuery(false, hqlId, hql, params);

		// 创建查询对象
		TypedQuery<E> query = createHQLQuery(hql, params, clazz);

		// 执行查询
		List<E> listEntity = query.getResultList();

		// 记录结束日志
		logAfterQuery(false, hqlId, startTime);

		// 返回结果
		return listEntity;
	}


	/**
	 * 查询列表数据
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlObj SQL语句对象
	 * @param clazz 查询结果映射对象类型
	 * @return 列表数据。无结果时将返回空对象。
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected <T> List<T> getList(SQL sqlObj, Class<T> clazz) {
		return sqlObj.isUseSQL() ? queryList(sqlObj.getSQL(), sqlObj.getParams(), clazz) : findList(sqlObj.getSQL(), sqlObj.getParams(), clazz);
	}


	@SuppressWarnings("deprecation")
	@Override
	public <T> List<T> getList(QuerySQL sqlObj, Class<T> clazz) {
		return sqlObj.isUseSQL() ? queryList(sqlObj.getSQL(), sqlObj.getParams(), clazz) : findList(sqlObj.getSQL(), sqlObj.getParams(), clazz);
	}


	/**
	 * 查询分页数据
	 * @param <B> 返回值类型为clazz参数定义的类型
	 * @param sql 查询语句
	 * @param params 参数
	 * @param clazz 查询结果映射对象类型
	 * @param pageNumber 页码。正整数或0。从0开始。
	 * @param pageSize 每页数据量。正整数。传入0时表示取框架约定的默认值。
	 * @return 分页数据。无结果时将返回空对象。
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	protected <B> Page<B> queryPage(String sql, Object[] params, Class<B> clazz, int pageNumber, int pageSize) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logBeforeQuery(true, sqlId, sql, params);

		// 查询数据总数
		long total = queryLong(LKDaoUtils.createCountSQL(sql), params);

		// 创建查询对象
		Query query = createSQLQuery(sql, params, Map.class);

		// 初始化分页信息
		Pageable pageable = initPageable(query, pageNumber, pageSize);

		// 执行查询
		List<Map<String, Object>> listMap = query.getResultList();

		// 记录结束日志
		DateTime enTime = logAfterQuery(true, sqlId, startTime);

		// 转换结果
		List<B> listBean = LKDaoUtils.listMap2listBean(clazz, listMap);

		// 记录转换结束日志
		logAfterConvert(sqlId, enTime);

		// 返回结果
		return new PageImpl<>(listBean, pageable, total);
	}


	/**
	 * 查询分页数据
	 * @param <E> 返回值类型为clazz参数定义的类型
	 * @param hql 查询语句
	 * @param params 参数
	 * @param clazz 查询结果映射对象类型
	 * @param pageNumber 页码。正整数或0。从0开始。
	 * @param pageSize 每页数据量。正整数。传入0时表示取框架约定的默认值。
	 * @return 分页数据。无结果时将返回空对象。
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected <E> Page<E> findPage(String hql, Object[] params, Class<E> clazz, int pageNumber, int pageSize) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String hqlId = LKRandomUtils.create(32);
		logBeforeQuery(false, hqlId, hql, params);

		// 查询数据总数
		long total = findLong(LKDaoUtils.createCountSQL(hql), params);

		// 创建查询对象
		TypedQuery<E> query = createHQLQuery(hql, params, clazz);

		// 初始化分页信息
		Pageable pageable = initPageable(query, pageNumber, pageSize);

		// 执行查询
		List<E> listEntity = query.getResultList();

		// 记录结束日志
		logAfterQuery(false, hqlId, startTime);

		// 返回结果
		return new PageImpl<>(listEntity, pageable, total);
	}


	/**
	 * 查询分页数据
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlObj SQL语句对象
	 * @param clazz 查询结果映射对象类型
	 * @param pageNumber 页码。正整数或0。从0开始。
	 * @param pageSize 每页数据量。正整数。传入0时表示取框架约定的默认值。
	 * @return 分页数据。无结果时将返回空对象。
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected <T> Page<T> getPage(SQL sqlObj, Class<T> clazz, int pageNumber, int pageSize) {
		return sqlObj.isUseSQL() ? queryPage(sqlObj.getSQL(), sqlObj.getParams(), clazz, pageNumber, pageSize) : findPage(sqlObj.getSQL(), sqlObj.getParams(), clazz, pageNumber, pageSize);
	}


	@SuppressWarnings("deprecation")
	@Override
	public <T> Page<T> getPage(QuerySQL sqlObj, Class<T> clazz) {
		LKPageable pageable = sqlObj.getPageable();
		int pageNumber = pageable == null ? DEFAULT_PAGE_NUMBER : checkPageNumber(pageable.getPageNumber());
		int pageSize = pageable == null ? DEFAULT_PAGE_SIZE : checkPageSize(pageable.getPageSize());
		return sqlObj.isUseSQL() ? queryPage(sqlObj.getSQL(), sqlObj.getParams(), clazz, pageNumber, pageSize) : findPage(sqlObj.getSQL(), sqlObj.getParams(), clazz, pageNumber, pageSize);
	}


	/**
	 * 查询单个数据
	 * @param <B> 返回值类型为clazz参数定义的类型
	 * @param sql 查询语句
	 * @param params 参数
	 * @param clazz 查询结果映射对象类型
	 * @return 单个数据
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	protected <B> B queryOne(String sql, Object[] params, Class<B> clazz) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logBeforeQuery(true, sqlId, sql, params);

		// 创建查询对象
		Query query = createSQLQuery(sql, params, Map.class);

		try {
			// 执行查询
			Object map = query.getSingleResult();

			// 记录结束日志
			DateTime endTime = logAfterQuery(true, sqlId, startTime);

			// 转换结果
			B bean = LKDaoUtils.map2bean(clazz, (Map<String, Object>) map);

			// 记录转换结束日志
			logAfterConvert(sqlId, endTime);

			// 返回结果
			return bean;
		} catch (NoResultException e) {
			// 记录结束日志
			logAfterQuery(true, sqlId, startTime);

			// 返回结果
			return null;
		}
	}


	@Override
	public <B> B queryOneById(Class<B> clazz, String id) {
		return queryOne("SELECT * FROM " + LKDBResource.getTableName(clazz) + " WHERE ID = ?", new Object[] { id }, clazz);
	}


	/**
	 * 查询单个数据
	 * @param <E> 返回值类型为clazz参数定义的类型
	 * @param hql 查询语句
	 * @param params 参数
	 * @param clazz 查询结果映射对象类型
	 * @return 单个数据
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected <E> E findOne(String hql, Object[] params, Class<E> clazz) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String hqlId = LKRandomUtils.create(32);
		logBeforeQuery(false, hqlId, hql, params);

		// 创建查询对象
		TypedQuery<E> query = createHQLQuery(hql, params, clazz);

		try {
			// 执行查询
			E entity = query.getSingleResult();

			// 返回结果
			return entity;
		} catch (NoResultException e) {
			// 返回结果
			return null;
		} finally {
			// 记录结束日志
			logAfterQuery(false, hqlId, startTime);
		}

	}


	@Override
	public <E> E findOneById(Class<E> clazz, String id) {
		return findOne("FROM " + clazz.getName() + " WHERE ID = ?", new Object[] { id }, clazz);
	}


	/**
	 * 查询单个数据
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlObj SQL语句对象
	 * @param clazz 查询结果映射对象类型
	 * @return 单个数据
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected <T> T getOne(SQL sqlObj, Class<T> clazz) {
		return sqlObj.isUseSQL() ? queryOne(sqlObj.getSQL(), sqlObj.getParams(), clazz) : findOne(sqlObj.getSQL(), sqlObj.getParams(), clazz);
	}


	@SuppressWarnings("deprecation")
	@Override
	public <T> T getOne(QuerySQL sqlObj, Class<T> clazz) {
		return sqlObj.isUseSQL() ? queryOne(sqlObj.getSQL(), sqlObj.getParams(), clazz) : findOne(sqlObj.getSQL(), sqlObj.getParams(), clazz);
	}


	/**
	 * 查询单个值
	 * @param sql 查询语句
	 * @param params 参数
	 * @param clazz 值类型
	 * @return 单个值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Override
	@Deprecated
	protected Object queryObject(String sql, Object[] params, Class<?> clazz) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logBeforeQuery(true, sqlId, sql, params);

		// 创建查询对象
		Query query = createSQLQuery(sql, params, clazz);

		try {
			// 执行查询返回结果
			return query.getSingleResult();
		} catch (NoResultException e) {
			// 返回结果
			return null;
		} finally {
			// 记录结束日志
			logAfterQuery(true, sqlId, startTime);
		}
	}


	/**
	 * 查询单个值
	 * @param hql 查询语句
	 * @param params 参数
	 * @param clazz 值类型
	 * @return 单个值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Override
	@Deprecated
	protected Object findObject(String hql, Object[] params, Class<?> clazz) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String hqlId = LKRandomUtils.create(32);
		logBeforeQuery(false, hqlId, hql, params);

		// 创建查询对象
		TypedQuery<?> query = createHQLQuery(hql, params, clazz);

		try {
			// 执行查询返回结果
			return query.getSingleResult();
		} catch (NoResultException e) {
			// 返回结果
			return null;
		} finally {
			// 记录结束日志
			logAfterQuery(false, hqlId, startTime);
		}
	}


	/**
	 * 转换成对应类型
	 * @param obj 值对象
	 * @param clazz 值类型
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	private <T> T convert(Object obj, Class<T> clazz) {
		if (obj == null) {
			return null;
		}
		String str = obj.toString();
		String className = clazz.getName();
		switch (className) {
			case "java.lang.String":
				return (T) str;
			case "java.lang.Byte":
				return (T) Byte.valueOf(str);
			case "java.lang.Short":
				return (T) Short.valueOf(str);
			case "java.lang.Integer":
				return (T) Integer.valueOf(str);
			case "java.lang.Long":
				return (T) Long.valueOf(str);
			case "java.lang.Float":
				return (T) Float.valueOf(str);
			case "java.lang.Double":
				return (T) Double.valueOf(str);
			case "java.lang.Boolean":
				return (T) Boolean.valueOf(str);
			default:
			break;
		}
		return null;
	}


	/**
	 * 查询单个字符串
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个字符串
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected String queryString(String sql, Object[] params) {
		return convert(queryObject(sql, params, String.class), String.class);
	}


	/**
	 * 查询单个字符串
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个字符串
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected String findString(String hql, Object[] params) {
		return convert(findObject(hql, params, String.class), String.class);
	}


	/**
	 * 查询单个字符串
	 * @param sqlObj SQL语句对象
	 * @return 单个字符串
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected String getString(SQL sqlObj) {
		return sqlObj.isUseSQL() ? queryString(sqlObj.getSQL(), sqlObj.getParams()) : findString(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public String getString(QuerySQL sqlObj) {
		return sqlObj.isUseSQL() ? queryString(sqlObj.getSQL(), sqlObj.getParams()) : findString(sqlObj.getSQL(), sqlObj.getParams());
	}


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Byte queryByte(String sql, Object[] params) {
		return convert(queryObject(sql, params, Byte.class), Byte.class);
	}


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Byte findByte(String hql, Object[] params) {
		return convert(findObject(hql, params, Byte.class), Byte.class);
	}


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Byte getByte(SQL sqlObj) {
		return sqlObj.isUseSQL() ? queryByte(sqlObj.getSQL(), sqlObj.getParams()) : findByte(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public Byte getByte(QuerySQL sqlObj) {
		return sqlObj.isUseSQL() ? queryByte(sqlObj.getSQL(), sqlObj.getParams()) : findByte(sqlObj.getSQL(), sqlObj.getParams());
	}


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Short queryShort(String sql, Object[] params) {
		return convert(queryObject(sql, params, Short.class), Short.class);
	}


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Short findShort(String hql, Object[] params) {
		return convert(findObject(hql, params, Short.class), Short.class);
	}


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Short getShort(SQL sqlObj) {
		return sqlObj.isUseSQL() ? queryShort(sqlObj.getSQL(), sqlObj.getParams()) : findShort(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public Short getShort(QuerySQL sqlObj) {
		return sqlObj.isUseSQL() ? queryShort(sqlObj.getSQL(), sqlObj.getParams()) : findShort(sqlObj.getSQL(), sqlObj.getParams());
	}


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Integer queryInteger(String sql, Object[] params) {
		return convert(queryObject(sql, params, Integer.class), Integer.class);
	}


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Integer findInteger(String hql, Object[] params) {
		return convert(findObject(hql, params, Integer.class), Integer.class);
	}


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Integer getInteger(SQL sqlObj) {
		return sqlObj.isUseSQL() ? queryInteger(sqlObj.getSQL(), sqlObj.getParams()) : findInteger(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public Integer getInteger(QuerySQL sqlObj) {
		return sqlObj.isUseSQL() ? queryInteger(sqlObj.getSQL(), sqlObj.getParams()) : findInteger(sqlObj.getSQL(), sqlObj.getParams());
	}


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Long queryLong(String sql, Object[] params) {
		return convert(queryObject(sql, params, Long.class), Long.class);
	}


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Long findLong(String hql, Object[] params) {
		return convert(findObject(hql, params, Long.class), Long.class);
	}


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Long getLong(SQL sqlObj) {
		return sqlObj.isUseSQL() ? queryLong(sqlObj.getSQL(), sqlObj.getParams()) : findLong(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public Long getLong(QuerySQL sqlObj) {
		return sqlObj.isUseSQL() ? queryLong(sqlObj.getSQL(), sqlObj.getParams()) : findLong(sqlObj.getSQL(), sqlObj.getParams());
	}


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Float queryFloat(String sql, Object[] params) {
		return convert(queryObject(sql, params, Float.class), Float.class);
	}


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Float findFloat(String hql, Object[] params) {
		return convert(findObject(hql, params, Float.class), Float.class);
	}


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Float getFloat(SQL sqlObj) {
		return sqlObj.isUseSQL() ? queryFloat(sqlObj.getSQL(), sqlObj.getParams()) : findFloat(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public Float getFloat(QuerySQL sqlObj) {
		return sqlObj.isUseSQL() ? queryFloat(sqlObj.getSQL(), sqlObj.getParams()) : findFloat(sqlObj.getSQL(), sqlObj.getParams());
	}


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Double queryDouble(String sql, Object[] params) {
		return convert(queryObject(sql, params, Double.class), Double.class);
	}


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Double findDouble(String hql, Object[] params) {
		return convert(findObject(hql, params, Double.class), Double.class);
	}


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Double getDouble(SQL sqlObj) {
		return sqlObj.isUseSQL() ? queryDouble(sqlObj.getSQL(), sqlObj.getParams()) : findDouble(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public Double getDouble(QuerySQL sqlObj) {
		return sqlObj.isUseSQL() ? queryDouble(sqlObj.getSQL(), sqlObj.getParams()) : findDouble(sqlObj.getSQL(), sqlObj.getParams());
	}


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Boolean queryBoolean(String sql, Object[] params) {
		return convert(queryObject(sql, params, Boolean.class), Boolean.class);
	}


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Boolean findBoolean(String hql, Object[] params) {
		return convert(findObject(hql, params, Boolean.class), Boolean.class);
	}


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected Boolean getBoolean(SQL sqlObj) {
		return sqlObj.isUseSQL() ? queryBoolean(sqlObj.getSQL(), sqlObj.getParams()) : findBoolean(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public Boolean getBoolean(QuerySQL sqlObj) {
		return sqlObj.isUseSQL() ? queryBoolean(sqlObj.getSQL(), sqlObj.getParams()) : findBoolean(sqlObj.getSQL(), sqlObj.getParams());
	}


	/**
	 * INSERT/DELETE/UPDATE
	 *
	 * <pre>
	 * important 谨慎使用
	 * </pre>
	 *
	 * @param sql 更新语句
	 * @param params 参数
	 * @return 更新数据数量
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected int change(String sql, Object[] params) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logger.warn("SQL[%s] -> %s [params:%s]", sqlId, sql, LKJsonUtils.toJson(params));

		// 创建查询对象
		Query query = createSQLQuery(sql, params, Long.class);

		// 执行查询
		int result = query.executeUpdate();

		// 记录结束日志
		DateTime endTime = DateTime.now();
		logger.warn("SQL[%s] -> change execution time is %sms, from %s to %s.", sqlId, String.valueOf(endTime.compareTo(startTime)), LKDateTimeUtils.toString(startTime), LKDateTimeUtils.toString(endTime));

		// 返回结果
		return result;
	}


	/**
	 * INSERT/DELETE/UPDATE
	 *
	 * <pre>
	 * important 谨慎使用
	 * </pre>
	 *
	 * @param sqlObj SQL语句对象
	 * @return 更新数据数量
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	@Override
	protected int change(SQL sqlObj) {
		return change(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public int update(UpdateSQL sqlObj) {
		return change(sqlObj.getSQL(), sqlObj.getParams());
	}


	@SuppressWarnings("deprecation")
	@Override
	public int delete(DeleteSQL sqlObj) {
		return change(sqlObj.getSQL(), sqlObj.getParams());
	}


	@Override
	public <T> int deleteOneOrMoreById(Class<T> clazz, String id) {
		SQL sql = new SQL(true).appendSQL("DELETE FROM " + LKDBResource.getTableName(clazz) + " WHERE");
		if (id.contains(SPLITOR)) {
			return change(sql.in(null, "ID", id));
		} else {
			return change(sql.eq(null, "ID", id));
		}
	}


	@Override
	public <E> E mergeOne(I_ID entity) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		Class<? extends I_ID> clazz = entity.getClass();

		// 初始化对象
		LKDaoUtils.initEntity(entity);

		// 执行修改
		@SuppressWarnings("unchecked")
		E result = getEntityManager().merge((E) entity);

		// 记录结束日志
		logAfterModify(sqlId, "merge", startTime, clazz, ((I_ID) result).getId());

		// 返回结果
		return result;
	}


	@Override
	public Collection<? extends I_ID> mergeList(Collection<? extends I_ID> listEntity) {
		ArrayList<I_ID> list = new ArrayList<>();
		for (Object entity : listEntity) {
			list.add(mergeOne((I_ID) entity));
		}
		return list;
	}


	@Override
	public Object[] mergeArr(I_ID[] objArr) {
		return mergeList(Arrays.asList(objArr)).toArray();
	}


	@Override
	public void persistOne(I_ID entity) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		Class<? extends I_ID> clazz = entity.getClass();

		// 初始化对象
		LKDaoUtils.initEntity(entity);

		// 执行修改
		getEntityManager().persist(entity);

		// 记录结束日志
		logAfterModify(sqlId, "persist", startTime, clazz, entity.getId());
	}


	@Override
	public void persistList(Collection<? extends I_ID> listEntity) {
		for (Object entity : listEntity) {
			persistOne((I_ID) entity);
		}
	}


	@Override
	public void persistArr(I_ID[] objArr) {
		persistList(Arrays.asList(objArr));
	}


	@Override
	public void removeOne(I_ID entity) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		Class<? extends I_ID> clazz = entity.getClass();
		String id = entity.getId();

		// 执行修改
		getEntityManager().remove(entity);

		// 记录结束日志
		logAfterModify(sqlId, "remove", startTime, clazz, id);
	}


	@Override
	public void removeList(Collection<? extends I_ID> listEntity) {
		for (Object entity : listEntity) {
			removeOne((I_ID) entity);
		}
	}


	@Override
	public void removeArr(I_ID[] objArr) {
		removeList(Arrays.asList(objArr));
	}

}
