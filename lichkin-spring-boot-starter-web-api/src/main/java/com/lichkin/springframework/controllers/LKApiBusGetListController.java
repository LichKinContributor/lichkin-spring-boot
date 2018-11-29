package com.lichkin.springframework.controllers;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusGetListService;

public abstract class LKApiBusGetListController<CI extends LKRequestBean, O, E extends I_ID> extends LKApiController<CI, List<O>> {

	protected abstract LKApiBusGetListService<CI, O, E> getService(CI cin, ApiKeyValues<CI> params);


	/** 实体类类型 */
	Class<E> classE;


	@SuppressWarnings("unchecked")
	public LKApiBusGetListController() {
		super();
		classE = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[2];
	}


	@Override
	protected boolean saveLog(CI cin, ApiKeyValues<CI> params) {// 通常不记录日志
		return false;
	}


	@Deprecated
	@Override
	protected LKOperTypeEnum getOperType(CI cin, ApiKeyValues<CI> params) {
		return LKOperTypeEnum.SEARCH;
	}


	@Deprecated
	@Override
	List<O> handleInvoke(CI cin, ApiKeyValues<CI> params) throws LKException {
		return getService(cin, params).handle(cin, params);
	}

}
