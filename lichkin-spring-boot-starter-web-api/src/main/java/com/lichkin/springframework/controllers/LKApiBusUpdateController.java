package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestIDBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.springframework.services.LKApiBusUpdateWithoutCheckerService;

public abstract class LKApiBusUpdateController<CI extends LKRequestIDBean, E extends I_ID> extends LKApiBusIUDController<CI, E> {

	@Override
	protected abstract LKApiBusUpdateWithoutCheckerService<CI, E> getService(CI cin, ApiKeyValues<CI> params);


	@Deprecated
	@Override
	protected LKOperTypeEnum getOperType(CI cin, ApiKeyValues<CI> params) {
		return LKOperTypeEnum.EDIT;
	}

}
