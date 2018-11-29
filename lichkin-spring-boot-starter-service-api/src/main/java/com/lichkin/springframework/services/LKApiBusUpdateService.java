package com.lichkin.springframework.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.LKCodeEnum;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 编辑接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusUpdateService<CI extends I_ID, E extends I_ID> extends LKApiBusUpdateWithoutCheckerService<CI, E> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusUpdateService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Override
	protected boolean busCheck(CI cin, ApiKeyValues<CI> params, E entity, String id) {
		if (!needCheckExist(cin, params, entity, id)) {// 业务规则字段修改了才需要进行校验
			return true;
		}

		// 查询冲突数据
		final List<E> listExist = findExist(cin, params, entity, id);
		if (CollectionUtils.isNotEmpty(listExist)) {
			// 有冲突数据则抛异常
			throw new LKRuntimeException(existErrorCode(cin, params));
		}

		return true;
	}


	/**
	 * 是否需要校验冲突数据
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 原实体类对象
	 * @param id 主键
	 * @return true:校验;false:不校验;
	 */
	protected boolean needCheckExist(CI cin, ApiKeyValues<CI> params, E entity, String id) {
		return false;
	}


	/**
	 * 查询冲突数据
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param entity 原实体类对象
	 * @param id 主键
	 * @return 冲突数据
	 */
	protected List<E> findExist(CI cin, ApiKeyValues<CI> params, E entity, String id) {
		return null;
	}


	/**
	* 数据已存在错误编码（findExist返回结果时才使用）
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	* @return 错误编码
	*/
	protected LKCodeEnum existErrorCode(CI cin, ApiKeyValues<CI> params) {
		return LKErrorCodesEnum.EXIST;
	}

}
