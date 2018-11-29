package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.springframework.services.LKApiBusInsertWithoutCheckerService;

public abstract class LKApiBusInsertController<CI extends LKRequestBean, E extends I_ID> extends LKApiBusIUDController<CI, E> {

	@Override
	protected abstract LKApiBusInsertWithoutCheckerService<CI, E> getService(CI cin, ApiKeyValues<CI> params);


	@Deprecated
	@Override
	protected LKOperTypeEnum getOperType(CI cin, ApiKeyValues<CI> params) {
		return LKOperTypeEnum.ADD;
	}

}
