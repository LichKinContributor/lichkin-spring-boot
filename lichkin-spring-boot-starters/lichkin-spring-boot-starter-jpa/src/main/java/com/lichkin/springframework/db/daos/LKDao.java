package com.lichkin.springframework.db.daos;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lichkin.framework.db.vos.LKSqlUpdateVo;
import com.lichkin.framework.db.vos.LKSqlVo;

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
	 * 查询列表
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlVo 查询语句对象
	 * @param clazz 查询结果映射对象
	 * @param allMappingAliases 是否全映射
	 * @return 列表
	 */
	public <T> List<T> queryListBySql(LKSqlVo sqlVo, Class<T> clazz, boolean allMappingAliases);


	/**
	 * 查询列表
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlVo 查询语句对象
	 * @param clazz 查询结果映射对象
	 * @return 列表
	 */
	public <T> List<T> queryListBySql(LKSqlVo sqlVo, Class<T> clazz);


	/**
	 * 查询分页
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlVo 查询语句对象
	 * @param clazz 查询结果映射对象
	 * @param pageable 分页信息
	 * @param allMappingAliases 是否全映射
	 * @return 分页
	 */
	public <T> Page<T> queryPageBySql(LKSqlVo sqlVo, Class<T> clazz, Pageable pageable, boolean allMappingAliases);


	/**
	 * 查询分页
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlVo 查询语句对象
	 * @param clazz 查询结果映射对象
	 * @param pageable 分页信息
	 * @return 分页
	 */
	public <T> Page<T> queryPageBySql(LKSqlVo sqlVo, Class<T> clazz, Pageable pageable);


	/**
	 * 获取对象
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlVo 查询语句对象
	 * @param clazz 查询结果映射对象
	 * @param allMappingAliases 是否全映射
	 * @return 对象
	 */
	public <T> T queryOneBySql(LKSqlVo sqlVo, Class<T> clazz, boolean allMappingAliases);


	/**
	 * 获取对象
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlVo 查询语句对象
	 * @param clazz 查询结果映射对象
	 * @return 对象
	 */
	public <T> T queryOneBySql(LKSqlVo sqlVo, Class<T> clazz);


	/**
	 * 获取字符串（必须使用select count(1) as one from t的形式）
	 * @param sqlVo 查询语句对象
	 * @return 字符串
	 */
	public String queryStringBySql(LKSqlVo sqlVo);


	/**
	 * 获取数字（必须使用select count(1) as one from t的形式）
	 * @param sqlVo 查询语句对象
	 * @return 字符串
	 */
	public Long queryLongBySql(LKSqlVo sqlVo);


	/**
	 * 执行更新
	 * @param sqlVo 更新条件对象
	 * @return 影响的条数
	 */
	public int executeUpdate(LKSqlUpdateVo sqlVo);


	/**
	 * 查询列表
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlVo 查询语句对象
	 * @param clazz 查询结果映射对象
	 * @return 列表
	 */
	public <T> List<T> findListByHql(LKSqlVo sqlVo, Class<T> clazz);


	/**
	 * 查询分页
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlVo 查询语句对象
	 * @param clazz 查询结果映射对象
	 * @param pageable 分页信息
	 * @return 分页
	 */
	public <T> Page<T> findPageByHql(LKSqlVo sqlVo, Class<T> clazz, Pageable pageable);


	/**
	 * 获取对象
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param sqlVo 查询语句对象
	 * @param clazz 查询结果映射对象
	 * @return 对象
	 */
	public <T> T findOneByHql(LKSqlVo sqlVo, Class<T> clazz);


	/**
	 * 获取对象
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param clazz 查询结果映射对象
	 * @param id 主键
	 * @return 对象
	 */
	public <T> T findOneById(Class<T> clazz, String id);


	/**
	 * 保存对象
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param obj 对象
	 * @param merge true：调用merge方法；false：调用persist方法。
	 * @return 对象
	 */
	public <T> T save(Object obj, boolean merge);


	/**
	 * 保存对象（merge方法）
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param obj 对象
	 * @return 对象
	 */
	public <T> T save(Object obj);


	/**
	 * 保存对象
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param obj 对象
	 * @return 对象
	 */
	public <T> T merge(Object obj);


	/**
	 * 保存对象
	 * @param <T> 返回值类型为clazz参数定义的类型
	 * @param obj 对象
	 * @return 对象
	 */
	public <T> T persist(Object obj);


	/**
	 * 删除对象
	 * @param obj 对象
	 */
	public void remove(Object obj);

}
