package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_ID;

/**
 * 接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
abstract class LKApiBusChangeService<SI, SO, E extends I_ID> extends LKApiBusService<SI, SO, E> {

	/**
	 * 保存主表数据前操作
	 * @param entity 实体类对象
	 * @param in 入参对象
	 */
	protected void beforeSaveMainTable(E entity, SI in) {
	}


	/**
	 * 清除子表数据
	 * @param entity 实体类对象
	 * @param in 入参对象
	 */
	protected void clearSubTables(E entity, SI in) {
	}


	/**
	 * 新增子表数据
	 * @param entity 实体类对象
	 * @param in 入参对象
	 */
	protected void addSubTables(E entity, SI in) {
	}


	/**
	 * 返回结果
	 * @param entity 实体类对象
	 * @param in 入参对象
	 * @return 结果
	 */
	protected SO handleResult(E entity, SI in) {
		try {
			return classSO.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 从入参复制参数时需要忽略的字段
	 * @return 字段名数组
	 */
	protected String[] excludeFieldNames() {
		return new String[] { "id" };
	}

}
