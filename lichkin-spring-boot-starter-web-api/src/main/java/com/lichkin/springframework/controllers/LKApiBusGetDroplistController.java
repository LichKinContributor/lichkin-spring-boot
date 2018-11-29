package com.lichkin.springframework.controllers;

import java.util.List;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.beans.impl.LKDroplistBean;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusGetDroplistService;

public abstract class LKApiBusGetDroplistController<CI extends LKRequestBean> extends LKApiController<CI, List<LKDroplistBean>> {

	protected abstract LKApiBusGetDroplistService<CI> getService(CI cin, ApiKeyValues<CI> params);


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
	List<LKDroplistBean> handleInvoke(CI cin, ApiKeyValues<CI> params) throws LKException {
		return getService(cin, params).handle(cin, params);
	}

}
