package com.lichkin.springframework.services;

import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;

/**
 * 删除接口服务类定义
 * @param <SI> 服务类入参类型
 * @param <E> 实体类类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKApiBusDeleteService<SI extends I_ID, E extends I_ID> extends LKApiBusService<SI, Void, E> implements LKApiVoidService<SI> {

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public LKApiBusDeleteService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}


	@Transactional
	@Override
	public void handle(SI in) throws LKException {
		dao.deleteOneOrMoreById(classE, in.getId());
	}

}
