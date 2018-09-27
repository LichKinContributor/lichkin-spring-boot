package com.lichkin.springframework.services;

import org.springframework.data.domain.Page;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.beans.LKPageable;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 获取分页数据接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetPageService<SI extends LKPageable, SO, E extends I_ID> extends LKApiBusService<SI, SO, E> implements LKApiService<SI, Page<SO>> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusGetPageService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = (Class<SO>) types[1];
		classE = (Class<E>) types[2];
	}


	@Override
	public Page<SO> handle(SI sin) throws LKException {
		Page<SO> page = beforeQuery(sin);
		if (page != null) {
			return page;
		}

		QuerySQL sql = new QuerySQL(!classE.equals(classSO), classE, isDistinct());

		initSQL(sin, sql);

		sql.setPage(sin);

		return afterQuery(sin, dao.getPage(sql, classSO));
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
	 * 返回结果
	 * @param sin 入参
	 * @return 出参
	 */
	protected Page<SO> beforeQuery(SI sin) {
		return null;
	}


	/**
	 * 查询语句执行后处理
	 * @param sin 入参
	 * @param page 查询结果
	 * @return 出参
	 */
	protected Page<SO> afterQuery(SI sin, Page<SO> page) {
		return page;
	}

}
