package com.lichkin.springframework.db.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;

/**
 * 数据访问接口
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface LKDao {

	/**
	 * 获取EntityManager
	 * @return EntityManager
	 */
	public EntityManager getEntityManager();


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
	public <B> List<B> queryList(String sql, Object[] params, Class<B> clazz);


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
	public <E> List<E> findList(String hql, Object[] params, Class<E> clazz);


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
	public <B> Page<B> queryPage(String sql, Object[] params, Class<B> clazz, int pageNumber, int pageSize);


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
	public <E> Page<E> findPage(String hql, Object[] params, Class<E> clazz, int pageNumber, int pageSize);


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
	public <B> B queryOne(String sql, Object[] params, Class<B> clazz);


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
	public <E> E findOne(String hql, Object[] params, Class<E> clazz);


	/**
	 * 查询单个字符串
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个字符串
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	public String queryString(String sql, Object[] params);


	/**
	 * 查询单个字符串
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个字符串
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	public String findString(String hql, Object[] params);


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	public Long queryLong(String sql, Object[] params);


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	public Long findLong(String hql, Object[] params);

}
