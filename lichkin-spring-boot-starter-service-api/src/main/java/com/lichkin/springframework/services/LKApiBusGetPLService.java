package com.lichkin.springframework.services;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.defines.entities.I_ID;

/**
 * 获取分页/列表数据接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetPLService<SI, SO, E extends I_ID> extends LKApiBusService<SI, SO, E> {

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

}
