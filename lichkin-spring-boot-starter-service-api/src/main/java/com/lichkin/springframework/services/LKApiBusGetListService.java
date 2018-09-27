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
	public List<SO> handle(SI sin) throws LKException {
		List<SO> list = beforeQuery(sin);
		if (list != null) {
			return list;
		}

		QuerySQL sql = new QuerySQL(!classE.equals(classSO), classE, isDistinct());

		initSQL(sin, sql);

		return afterQuery(sin, dao.getList(sql, classSO));
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
	 * @param sql SQL语句对象
	 */
	protected void initSQL(SI sin, QuerySQL sql) {
	}


	/**
	 * 查询语句执行前处理，如返回值不为null，则直接返回该结果。
	 * @param sin 入参
	 * @return 出参
	 */
	protected List<SO> beforeQuery(SI sin) {
		return null;
	}


	/**
	 * 查询语句执行后处理
	 * @param sin 入参
	 * @param list 查询结果
	 * @return 出参
	 */
	protected List<SO> afterQuery(SI sin, List<SO> list) {
		return list;
	}

}
