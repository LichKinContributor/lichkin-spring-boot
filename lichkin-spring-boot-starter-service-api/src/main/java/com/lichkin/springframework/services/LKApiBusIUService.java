package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 新增编辑接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusIUService<CI, E extends I_ID> extends LKApiBusService<CI, Void, E> implements LKApiVoidService<CI> {

	/**
	 * 保存主表数据前操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 保存前的实体类对象
	 */
	protected void beforeSaveMain(CI cin, ApiKeyValues<CI> params, E entity) {
	}


	/**
	 * 保存主表数据后操作
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 保存后的实体类对象
	 * @param id 主键
	 */
	protected void afterSaveMain(CI cin, ApiKeyValues<CI> params, E entity, String id) {
	}


	/**
	 * 还原时数据需先清除子表数据
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 保存后的实体类对象
	 * @param id 主键
	 */
	protected void clearSubs(CI cin, ApiKeyValues<CI> params, E entity, String id) {
	}


	/**
	 * 新增子表数据
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 保存后的实体类对象
	 * @param id 主键
	 */
	protected void addSubs(CI cin, ApiKeyValues<CI> params, E entity, String id) {
	}

}
