package com.lichkin.springframework.services;

import java.util.List;

import com.lichkin.framework.defines.entities.I_ID;

/**
 * 接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
abstract class LKApiBusChangeService<SI, E extends I_ID> extends LKVoidApiBusService<SI, E> {

	/** 服务类入参 */
	protected SI in;

	/** 实体类对象 */
	protected E entity;


	/**
	 * 查询冲突数据
	 * @return 冲突数据
	 */
	protected List<E> findExist() {
		return null;
	}


	/**
	 * 业务验证规则，为了减少查表次数，设置合适的规则判断，如登录名修改时才需要校验等。
	 * @return true:进行业务规则校验;false:不进行业务规则校验.
	 */
	protected boolean needCheckExist() {
		return false;
	}


	/**
	 * 保存主表数据前操作
	 */
	protected void beforeSaveMainTable() {
	}


	/**
	 * 清除子表数据
	 */
	protected void clearSubTables() {
	}


	/**
	 * 新增子表数据
	 */
	protected void addSubTables() {
	}


	/**
	 * 从入参复制参数时需要忽略的字段
	 * @return 字段名数组
	 */
	protected String[] excludeFieldNames() {
		return new String[] { "id" };
	}

}
