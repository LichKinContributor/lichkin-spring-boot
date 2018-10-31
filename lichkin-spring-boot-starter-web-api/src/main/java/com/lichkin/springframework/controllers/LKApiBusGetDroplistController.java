package com.lichkin.springframework.controllers;

import java.util.List;

import javax.validation.Valid;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.beans.impl.LKDroplistBean;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusGetDroplistService;

public abstract class LKApiBusGetDroplistController<CI extends LKRequestBean> extends LKApiController<CI, List<LKDroplistBean>> {

	protected abstract LKApiBusGetDroplistService<CI> getService(CI cin);


	@Deprecated
	@Override
	protected boolean saveLog(CI cin) {
		return false;
	}


	@Deprecated
	@Override
	protected LKOperTypeEnum getOperType(@Valid CI cin) {
		return LKOperTypeEnum.SEARCH;
	}


	@Deprecated
	@Override
	protected String getBusType(CI cin) {
		return null;
	}


	@Deprecated
	@Override
	List<LKDroplistBean> handleInvoke(@Valid CI cin, String locale, String compId, String loginId) throws LKException {
		return getService(cin).handle(cin, locale, compId, loginId);
	}

}
