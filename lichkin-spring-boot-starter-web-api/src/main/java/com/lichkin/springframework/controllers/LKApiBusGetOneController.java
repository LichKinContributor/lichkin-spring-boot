package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestIDBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusGetOneService;

public abstract class LKApiBusGetOneController<CI extends LKRequestIDBean, O, E extends I_ID> extends LKApiYYController<CI, O, O> {

	@Override
	protected abstract LKApiBusGetOneService<CI, O, E> getService(CI cin, ApiKeyValues<CI> params);


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
	protected void beforeInvokeService(CI cin, ApiKeyValues<CI> params) throws LKException {
	}


	@Deprecated
	@Override
	protected O afterInvokeService(CI cin, ApiKeyValues<CI> params, O sout) throws LKException {
		return sout;
	}

}
