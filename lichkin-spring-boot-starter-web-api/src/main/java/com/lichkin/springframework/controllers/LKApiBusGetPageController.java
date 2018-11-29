package com.lichkin.springframework.controllers;

import java.lang.reflect.ParameterizedType;

import org.springframework.data.domain.Page;

import com.lichkin.framework.beans.impl.LKRequestPageBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusGetPageService;

public abstract class LKApiBusGetPageController<CI extends LKRequestPageBean, O, E extends I_ID> extends LKApiController<CI, Page<O>> {

	protected abstract LKApiBusGetPageService<CI, O, E> getService(CI cin, ApiKeyValues<CI> params);


	Class<E> classE;


	@SuppressWarnings("unchecked")
	public LKApiBusGetPageController() {
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
	Page<O> handleInvoke(CI cin, ApiKeyValues<CI> params) throws LKException {
		return getService(cin, params).handle(cin, params);
	}

}
