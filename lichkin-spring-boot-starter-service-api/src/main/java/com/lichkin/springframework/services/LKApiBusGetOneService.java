package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKBeanUtils;

/**
 * 获取单个数据接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <SO> 服务类出参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusGetOneService<SI extends I_ID, SO, E extends I_ID> extends LKApiBusService<SI, SO, E> {

	@Override
	public SO handle(SI in) throws LKException {
		// 先取业务规则值
		String id = in.getId();

		// 通过ID找到该条数据
		E entity = dao.findOneById(classE, id);
		if (entity == null) {
			// 无数据则抛异常
			throw new LKRuntimeException(inexistentErrorCode);
		}

		// 初始化返回值
		SO out = LKBeanUtils.newInstance(entity, classSO);

		// 其它字段赋值
		setOtherValues(entity, in, out);

		// 返回结果
		return out;
	}


	/**
	 * 其它字段赋值
	 * @param entity 实体类对象
	 * @param in 入参对象
	 * @param out 出参对象
	 */
	protected abstract void setOtherValues(E entity, SI in, SO out);

}
