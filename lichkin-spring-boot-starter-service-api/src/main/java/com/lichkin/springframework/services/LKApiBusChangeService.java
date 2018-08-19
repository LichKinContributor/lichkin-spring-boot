package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_ID;

/**
 * 接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
abstract class LKApiBusChangeService<SI, E extends I_ID> extends LKVoidApiBusService<SI, E> {

	/**
	 * 保存主表数据前操作
	 * @param sin 入参
	 * @param entity 新建实体类对象
	 * @param exist 原实体类对象
	 */
	protected void beforeSaveMainTable(SI sin, E entity, E exist) {
	}


	/**
	 * 清除子表数据
	 * @param sin 入参
	 * @param entity 新建实体类对象
	 * @param exist 原实体类对象
	 */
	protected void clearSubTables(SI sin, E entity, E exist) {
	}


	/**
	 * 新增子表数据
	 * @param sin 入参
	 * @param entity 新建实体类对象
	 * @param exist 原实体类对象
	 */
	protected void addSubTables(SI sin, E entity, E exist) {
	}


	/**
	 * 从入参复制参数时需要忽略的字段
	 * @param sin 入参
	 * @param exist 原实体类对象
	 * @return 字段名数组
	 */
	protected String[] excludeFieldNames(SI sin, E exist) {
		return new String[] { "id" };
	}

}
