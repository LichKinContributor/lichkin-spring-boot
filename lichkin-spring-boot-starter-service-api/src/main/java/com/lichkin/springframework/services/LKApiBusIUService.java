package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_ID;

/**
 * 新增编辑接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusIUService<SI, E extends I_ID> extends LKApiBusService<SI, Void, E> implements LKApiVoidService<SI> {

	/**
	 * 保存主表数据前操作
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 保存前的实体类对象
	 */
	protected void beforeSaveMain(SI sin, String locale, String compId, String loginId, E entity) {
	}


	/**
	 * 保存主表数据后操作
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 保存后的实体类对象
	 * @param id 主键
	 */
	protected void afterSaveMain(SI sin, String locale, String compId, String loginId, E entity, String id) {
	}


	/**
	 * 还原时数据需先清除子表数据
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 保存后的实体类对象
	 * @param id 主键
	 */
	protected void clearSubs(SI sin, String locale, String compId, String loginId, E entity, String id) {
	}


	/**
	 * 新增子表数据
	 * @param sin 入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param entity 保存后的实体类对象
	 * @param id 主键
	 */
	protected void addSubs(SI sin, String locale, String compId, String loginId, E entity, String id) {
	}

}
