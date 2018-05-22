package com.lichkin.springframework.db.daos;

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

import com.lichkin.framework.defines.entities.suppers.LKIDInterface;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKRandomUtils;

/**
 * 数据库访问对象
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKBaseDao implements LKDao {

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
	 * 记录开始日志
	 * @param sqlId 语句唯一标识
	 * @param type 修改类型
	 * @param entity 实体类对象
	 */
	private void logBeforeModify(String sqlId, String type, LKIDInterface entity) {
		logger.warn("HQL[%s] -> %s [entity:%s]", sqlId, type, LKJsonUtils.toJson(entity));
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
	 */
	private DateTime logAfterModify(String sqlId, String type, DateTime startTime) {
		DateTime endTime = DateTime.now();
		logger.info("HQL[%s] -> %s execution time is %sms, from %s to %s.", sqlId, type, String.valueOf(endTime.compareTo(startTime)), LKDateTimeUtils.toString(startTime), LKDateTimeUtils.toString(endTime));
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


	/** 默认分页大小 */
	public static int DEFAULT_PAGE_SIZE = 25;


	/**
	 * 校验页码
	 * @param pageNumber 页码
	 * @return 页码
	 */
	private int checkPageNumber(int pageNumber) {
		if (pageNumber < 0) {
			pageNumber = 0;
		}
		return pageNumber;
	}


	/**
	 * 每页数据量
	 * @param pageSize 每页数据量
	 * @return 每页数据量
	 */
	private int checkPageSize(int pageSize) {
		if (pageSize <= 0) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		return pageSize;
	}


	/**
	 * 初始化分页信息
	 * @param query 查询对象
	 * @param pageNumber 页码。正整数或0。从0开始。
	 * @param pageSize 每页数据量。正整数。传入0时表示取框架约定的默认值。
	 * @return 分页信息
	 */
	private Pageable initPageable(Query query, int pageNumber, int pageSize) {
		pageNumber = checkPageNumber(pageNumber);
		pageSize = checkPageSize(pageSize);
		query.setFirstResult(pageNumber * pageSize);
		query.setMaxResults(pageSize);
		return PageRequest.of(pageNumber, pageSize);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <B> List<B> queryList(String sql, Object[] params, Class<B> clazz) {
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


	@Override
	public <E> List<E> findList(String hql, Object[] params, Class<E> clazz) {
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


	@SuppressWarnings("unchecked")
	@Override
	public <B> Page<B> queryPage(String sql, Object[] params, Class<B> clazz, int pageNumber, int pageSize) {
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


	@Override
	public <E> Page<E> findPage(String hql, Object[] params, Class<E> clazz, int pageNumber, int pageSize) {
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


	@SuppressWarnings("unchecked")
	@Override
	public <B> B queryOne(String sql, Object[] params, Class<B> clazz) {
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
	public <E> E findOne(String hql, Object[] params, Class<E> clazz) {
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
	public String queryString(String sql, Object[] params) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logBeforeQuery(true, sqlId, sql, params);

		// 创建查询对象
		Query query = createSQLQuery(sql, params, String.class);

		try {
			// 执行查询
			Object obj = query.getSingleResult();

			// 返回结果
			if (obj == null) {
				return null;
			}
			return obj.toString();
		} catch (NoResultException e) {
			// 返回结果
			return null;
		} finally {
			// 记录结束日志
			logAfterQuery(true, sqlId, startTime);
		}
	}


	@Override
	public String findString(String hql, Object[] params) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String hqlId = LKRandomUtils.create(32);
		logBeforeQuery(false, hqlId, hql, params);

		// 创建查询对象
		TypedQuery<String> query = createHQLQuery(hql, params, String.class);

		try {
			// 执行查询
			Object obj = query.getSingleResult();

			// 返回结果
			if (obj == null) {
				return null;
			}
			return obj.toString();
		} catch (NoResultException e) {
			// 返回结果
			return null;
		} finally {
			// 记录结束日志
			logAfterQuery(false, hqlId, startTime);
		}
	}


	@Override
	public Long queryLong(String sql, Object[] params) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logBeforeQuery(true, sqlId, sql, params);

		// 创建查询对象
		Query query = createSQLQuery(sql, params, Long.class);

		try {
			// 执行查询
			Object obj = query.getSingleResult();

			// 返回结果
			if (obj == null) {
				return null;
			}
			return Long.valueOf(obj.toString());
		} catch (NoResultException e) {
			// 返回结果
			return null;
		} finally {
			// 记录结束日志
			logAfterQuery(true, sqlId, startTime);
		}
	}


	@Override
	public Long findLong(String hql, Object[] params) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String hqlId = LKRandomUtils.create(32);
		logBeforeQuery(false, hqlId, hql, params);

		// 创建查询对象
		TypedQuery<Long> query = createHQLQuery(hql, params, Long.class);

		try {
			// 执行查询
			Object obj = query.getSingleResult();

			// 返回结果
			if (obj == null) {
				return null;
			}
			return Long.valueOf(obj.toString());
		} catch (NoResultException e) {
			// 返回结果
			return null;
		} finally {
			// 记录结束日志
			logAfterQuery(false, hqlId, startTime);
		}
	}


	@Override
	public int change(String sql, Object[] params) {
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


	@Override
	public <E> E mergeOne(LKIDInterface entity) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logBeforeModify(sqlId, "merge", entity);

		// 初始化对象
		LKDaoUtils.initEntity(entity);

		// 执行修改
		@SuppressWarnings("unchecked")
		E result = getEntityManager().merge((E) entity);

		// 记录结束日志
		logAfterModify(sqlId, "merge", startTime);

		// 返回结果
		return result;
	}


	@Override
	public Collection<? extends LKIDInterface> mergeList(Collection<? extends LKIDInterface> listEntity) {
		ArrayList<LKIDInterface> list = new ArrayList<>();
		for (Object entity : listEntity) {
			list.add(mergeOne((LKIDInterface) entity));
		}
		return list;
	}


	@Override
	public Object[] mergeArr(LKIDInterface[] objArr) {
		return mergeList(Arrays.asList(objArr)).toArray();
	}


	@Override
	public void persistOne(LKIDInterface entity) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logBeforeModify(sqlId, "persist", entity);

		// 初始化对象
		LKDaoUtils.initEntity(entity);

		// 执行修改
		getEntityManager().persist(entity);

		// 记录结束日志
		logAfterModify(sqlId, "persist", startTime);
	}


	@Override
	public void persistList(Collection<? extends LKIDInterface> listEntity) {
		for (Object entity : listEntity) {
			persistOne((LKIDInterface) entity);
		}
	}


	@Override
	public void persistArr(LKIDInterface[] objArr) {
		persistList(Arrays.asList(objArr));
	}


	@Override
	public void removeOne(LKIDInterface entity) {
		// 记录开始日志
		DateTime startTime = DateTime.now();
		String sqlId = LKRandomUtils.create(32);
		logBeforeModify(sqlId, "remove", entity);

		// 执行修改
		getEntityManager().remove(entity);

		// 记录结束日志
		logAfterModify(sqlId, "remove", startTime);
	}


	@Override
	public void removeList(Collection<? extends LKIDInterface> listEntity) {
		for (Object entity : listEntity) {
			removeOne((LKIDInterface) entity);
		}
	}


	@Override
	public void removeArr(LKIDInterface[] objArr) {
		removeList(Arrays.asList(objArr));
	}

}
