package com.lichkin.springframework.db.daos;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.db.beans.SQL;
import com.lichkin.framework.db.beans.UpdateSQL;
import com.lichkin.framework.db.entities.suppers._LKIDInterface;

/**
 * 数据访问接口
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKDao {

	/**
	 * 获取EntityManager
	 * @return EntityManager
	 */
	protected abstract EntityManager getEntityManager();


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
	protected abstract <B> List<B> queryList(String sql, Object[] params, Class<B> clazz);


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
	protected abstract <E> List<E> findList(String hql, Object[] params, Class<E> clazz);


	/**
	 * 查询列表数据
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlObj SQL语句对象
	 * @param clazz 查询结果映射对象类型
	 * @return 列表数据。无结果时将返回空对象。
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract <T> List<T> getList(SQL sqlObj, Class<T> clazz);


	/**
	 * 查询列表数据
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlObj SQL语句对象
	 * @param clazz 查询结果映射对象类型
	 * @return 列表数据。无结果时将返回空对象。
	 */
	public abstract <T> List<T> getList(QuerySQL sqlObj, Class<T> clazz);


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
	protected abstract <B> Page<B> queryPage(String sql, Object[] params, Class<B> clazz, int pageNumber, int pageSize);


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
	protected abstract <E> Page<E> findPage(String hql, Object[] params, Class<E> clazz, int pageNumber, int pageSize);


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
	protected abstract <T> Page<T> getPage(SQL sqlObj, Class<T> clazz, int pageNumber, int pageSize);


	/**
	 * 查询分页数据
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlObj SQL语句对象
	 * @param clazz 查询结果映射对象类型
	 * @return 分页数据。无结果时将返回空对象。
	 */
	public abstract <T> Page<T> getPage(QuerySQL sqlObj, Class<T> clazz);


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
	protected abstract <B> B queryOne(String sql, Object[] params, Class<B> clazz);


	/**
	 * 查询单个数据
	 * @param <B> 返回值类型为clazz参数定义的类型
	 * @param clazz 查询结果映射对象类型
	 * @param id 主键
	 * @return 单个数据
	 */
	public abstract <B> B queryOneById(Class<B> clazz, String id);


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
	protected abstract <E> E findOne(String hql, Object[] params, Class<E> clazz);


	/**
	 * 查询单个数据
	 * @param <E> 返回值类型为clazz参数定义的类型
	 * @param clazz 查询结果映射对象类型
	 * @param id 主键
	 * @return 单个数据
	 */
	public abstract <E> E findOneById(Class<E> clazz, String id);


	/**
	 * 查询单个数据
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlObj SQL语句对象
	 * @param clazz 查询结果映射对象类型
	 * @return 单个数据
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract <T> T getOne(SQL sqlObj, Class<T> clazz);


	/**
	 * 查询单个数据
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlObj SQL语句对象
	 * @param clazz 查询结果映射对象类型
	 * @return 单个数据
	 */
	public abstract <T> T getOne(QuerySQL sqlObj, Class<T> clazz);


	/**
	 * 查询单个值
	 * @param sql 查询语句
	 * @param params 参数
	 * @param clazz 值类型
	 * @return 单个值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Object queryObject(String sql, Object[] params, Class<?> clazz);


	/**
	 * 查询单个值
	 * @param hql 查询语句
	 * @param params 参数
	 * @param clazz 值类型
	 * @return 单个值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Object findObject(String hql, Object[] params, Class<?> clazz);


	/**
	 * 查询单个字符串
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个字符串
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract String queryString(String sql, Object[] params);


	/**
	 * 查询单个字符串
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个字符串
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract String findString(String hql, Object[] params);


	/**
	 * 查询单个字符串
	 * @param sqlObj SQL语句对象
	 * @return 单个字符串
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract String getString(SQL sqlObj);


	/**
	 * 查询单个字符串
	 * @param sqlObj SQL语句对象
	 * @return 单个字符串
	 */
	public abstract String getString(QuerySQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Byte queryByte(String sql, Object[] params);


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Byte findByte(String hql, Object[] params);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Byte getByte(SQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 */
	public abstract Byte getByte(QuerySQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Short queryShort(String sql, Object[] params);


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Short findShort(String hql, Object[] params);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Short getShort(SQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 */
	public abstract Short getShort(QuerySQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Integer queryInteger(String sql, Object[] params);


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Integer findInteger(String hql, Object[] params);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Integer getInteger(SQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 */
	public abstract Integer getInteger(QuerySQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Long queryLong(String sql, Object[] params);


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Long findLong(String hql, Object[] params);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Long getLong(SQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 */
	public abstract Long getLong(QuerySQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Float queryFloat(String sql, Object[] params);


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Float findFloat(String hql, Object[] params);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Float getFloat(SQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 */
	public abstract Float getFloat(QuerySQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Double queryDouble(String sql, Object[] params);


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Double findDouble(String hql, Object[] params);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Double getDouble(SQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 */
	public abstract Double getDouble(QuerySQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Boolean queryBoolean(String sql, Object[] params);


	/**
	 * 查询单个数值
	 * @param hql 查询语句
	 * @param params 参数
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Boolean findBoolean(String hql, Object[] params);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 * @deprecated 框架提供的方法暂不能实现时使用
	 */
	@Deprecated
	protected abstract Boolean getBoolean(SQL sqlObj);


	/**
	 * 查询单个数值
	 * @param sqlObj SQL语句对象
	 * @return 单个数值
	 */
	public abstract Boolean getBoolean(QuerySQL sqlObj);


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
	protected abstract int change(String sql, Object[] params);


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
	protected abstract int change(SQL sqlObj);


	/**
	 * UPDATE
	 *
	 * <pre>
	 * important 谨慎使用
	 * </pre>
	 *
	 * @param sqlObj SQL语句对象
	 * @return 更新数据数量
	 */
	public abstract int update(UpdateSQL sqlObj);


	/**
	 * 删除单条或多条数据
	 * @param <T> clazz参数定义的类型
	 * @param clazz 删除数据对象类型。符合约定的Entity或Bean。
	 * @param id 主键。使用LKFrameworkStatics.SPLITOR分割的形式可删除多条数据。
	 * @return 删除数据数量
	 */
	public abstract <T> int deleteOneOrMoreById(Class<T> clazz, String id);


	/**
	 * 保存对象
	 * @param <E> 返回值类型为entity参数的类型
	 * @param entity 实体类对象
	 * @return 对象
	 */
	public abstract <E> E mergeOne(_LKIDInterface entity);


	/**
	 * 保存对象集合
	 * @param listEntity 实体类对象集合
	 * @return 对象集合
	 */
	public abstract Collection<? extends _LKIDInterface> mergeList(Collection<? extends _LKIDInterface> listEntity);


	/**
	 * 保存对象数组
	 * @param objArr 实体类对象数组
	 * @return 对象数组
	 */
	public abstract Object[] mergeArr(_LKIDInterface[] objArr);


	/**
	 * 保存对象
	 * @param entity 实体类对象
	 */
	public abstract void persistOne(_LKIDInterface entity);


	/**
	 * 保存对象集合
	 * @param listEntity 实体类对象集合
	 */
	public abstract void persistList(Collection<? extends _LKIDInterface> listEntity);


	/**
	 * 保存对象数组
	 * @param objArr 实体类对象数组
	 */
	public abstract void persistArr(_LKIDInterface[] objArr);


	/**
	 * 删除对象
	 *
	 * <pre>
	 * important 谨慎使用
	 * </pre>
	 *
	 * @param entity 实体类对象
	 */
	public abstract void removeOne(_LKIDInterface entity);


	/**
	 * 删除对象集合
	 *
	 * <pre>
	 * important 谨慎使用
	 * </pre>
	 *
	 * @param listEntity 实体类对象集合
	 */
	public abstract void removeList(Collection<? extends _LKIDInterface> listEntity);


	/**
	 * 删除对象数组
	 *
	 * <pre>
	 * important 谨慎使用
	 * </pre>
	 *
	 * @param objArr 实体类对象数组
	 */
	public abstract void removeArr(_LKIDInterface[] objArr);

}
