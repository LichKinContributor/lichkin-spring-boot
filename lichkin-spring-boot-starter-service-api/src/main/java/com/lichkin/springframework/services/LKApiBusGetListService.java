package com.lichkin.springframework.services;

import java.util.Collections;
import java.util.List;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 获取列表数据接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetListService<CI, SO, E extends I_ID> extends LKApiBusGetPLService<CI, SO, E> implements LKApiService<CI, List<SO>> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusGetListService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = (Class<SO>) types[1];
		classE = (Class<E>) types[2];
	}


	@Override
	public List<SO> handle(CI cin, ApiKeyValues<CI> params) throws LKException {
		List<SO> list = beforeQuery(cin, params);
		if (list != null) {
			return list;
		}

		QuerySQL sql = new QuerySQL(!classE.equals(classSO), classE, isDistinct());

		initSQL(cin, params, sql);

		return afterQuery(cin, params, dao.getList(sql, classSO));
	}


	/**
	 * 返回空的列表数据
	 * @param <T> 列表内类型泛型
	 * @return 空的列表数据
	 */
	protected <T> List<T> emptyList() {
		return Collections.emptyList();
	}


	/**
	 * 查询语句执行前处理，如返回值不为null，则直接返回该结果。
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return 出参
	 */
	protected List<SO> beforeQuery(CI cin, ApiKeyValues<CI> params) {
		return null;
	}


	/**
	 * 查询语句执行后处理
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param list 查询结果
	 * @return 出参
	 */
	protected List<SO> afterQuery(CI cin, ApiKeyValues<CI> params, List<SO> list) {
		return list;
	}

}
