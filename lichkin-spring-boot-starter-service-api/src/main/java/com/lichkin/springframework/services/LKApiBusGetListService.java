package com.lichkin.springframework.services;

import java.util.List;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 获取列表数据接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetListService<SI, SO, E extends I_ID> extends LKApiBusService<SI, SO, E> implements LKApiService<SI, List<SO>> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusGetListService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = (Class<SO>) types[1];
		classE = (Class<E>) types[2];
	}


	@Override
	public List<SO> handle(SI sin, String locale, String compId, String loginId) throws LKException {
		List<SO> list = beforeQuery(sin, locale, compId, loginId);
		if (list != null) {
			return list;
		}

		QuerySQL sql = new QuerySQL(!classE.equals(classSO), classE, isDistinct());

		initSQL(sin, locale, compId, loginId, sql);

		return afterQuery(sin, locale, compId, loginId, dao.getList(sql, classSO));
	}


	/**
	 * 是否去重
	 * @return true:去重;false:不去重;
	 */
	protected boolean isDistinct() {
		return false;
	}


	/**
	 * 初始化SQL语句
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param sql SQL语句对象
	 */
	protected void initSQL(SI sin, String locale, String compId, String loginId, QuerySQL sql) {
	}


	/**
	 * 查询语句执行前处理，如返回值不为null，则直接返回该结果。
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @return 出参
	 */
	protected List<SO> beforeQuery(SI sin, String locale, String compId, String loginId) {
		return null;
	}


	/**
	 * 查询语句执行后处理
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param list 查询结果
	 * @return 出参
	 */
	protected List<SO> afterQuery(SI sin, String locale, String compId, String loginId, List<SO> list) {
		return list;
	}

}
