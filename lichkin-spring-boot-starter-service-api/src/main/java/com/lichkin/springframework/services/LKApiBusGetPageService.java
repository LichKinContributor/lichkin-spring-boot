package com.lichkin.springframework.services;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.beans.LKPageable;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 获取分页数据接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetPageService<CI extends LKPageable, SO, E extends I_ID> extends LKApiBusGetPLService<CI, SO, E> implements LKApiService<CI, Page<SO>> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusGetPageService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = (Class<SO>) types[1];
		classE = (Class<E>) types[2];
	}


	@Override
	public Page<SO> handle(CI cin, ApiKeyValues<CI> params) throws LKException {
		Page<SO> page = beforeQuery(cin, params);
		if (page != null) {
			return page;
		}

		QuerySQL sql = new QuerySQL(!classE.equals(classSO), classE, isDistinct());

		initSQL(cin, params, sql);

		sql.setPage(params.getPageable());

		return afterQuery(cin, params, dao.getPage(sql, classSO));
	}


	/**
	 * 返回空的分页数据
	 * @param <T> 分页内类型泛型
	 * @return 空的分页数据
	 */
	protected <T> Page<T> emptyPage() {
		return new PageImpl<>(Collections.emptyList());
	}


	/**
	 * 返回结果
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @return 出参
	 */
	protected Page<SO> beforeQuery(CI cin, ApiKeyValues<CI> params) {
		return null;
	}


	/**
	 * 查询语句执行后处理
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param page 查询结果
	 * @return 出参
	 */
	protected Page<SO> afterQuery(CI cin, ApiKeyValues<CI> params, Page<SO> page) {
		return page;
	}

}
