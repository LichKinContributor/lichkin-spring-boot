package com.lichkin.springframework.services;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 获取分页/列表数据接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetPLService<CI, SO, E extends I_ID> extends LKApiBusService<CI, SO, E> {

	/**
	 * 是否去重
	 * @return true:去重;false:不去重;
	 */
	protected boolean isDistinct() {
		return false;
	}


	/**
	 * 初始化SQL语句
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param sql SQL语句对象
	 */
	protected void initSQL(CI cin, ApiKeyValues<CI> params, QuerySQL sql) {
	}

}
