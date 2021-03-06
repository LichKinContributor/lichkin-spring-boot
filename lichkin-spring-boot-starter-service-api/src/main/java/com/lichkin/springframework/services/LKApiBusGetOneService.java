package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKBeanUtils;
import com.lichkin.springframework.controllers.ApiKeyValues;

/**
 * 获取单个数据接口服务类定义
 * @param <CI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetOneService<CI extends I_ID, SO, E extends I_ID> extends LKApiBusService<CI, SO, E> implements LKApiService<CI, SO> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusGetOneService() {
		super();
		classCI = (Class<CI>) types[0];
		classSO = (Class<SO>) types[1];
		classE = (Class<E>) types[2];
	}


	@Override
	public SO handle(CI cin, ApiKeyValues<CI> params) throws LKException {
		// 先取业务规则值
		String id = params.getId();

		// 通过ID找到该条数据
		E entity = dao.findOneById(classE, id);
		if (entity == null) {
			// 无数据则抛异常
			throw new LKRuntimeException(LKErrorCodesEnum.INEXIST);
		}

		// 初始化返回值
		SO out = LKBeanUtils.newInstance(entity, classSO);

		// 其它字段赋值
		setOtherValues(entity, id, cin, params, out);

		// 返回结果
		return out;
	}


	/**
	 * 其它字段赋值
	 * @param entity 实体类对象
	 * @param id 主键
	 * @param cin 控制器类入参
	 * @param params 解析值参数
	 * @param out 出参对象
	 */
	protected void setOtherValues(E entity, String id, CI cin, ApiKeyValues<CI> params, SO out) {
	}

}
