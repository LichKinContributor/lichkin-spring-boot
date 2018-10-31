package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import com.lichkin.framework.beans.impl.LKRequestIDsUsingStatusBean;
import com.lichkin.framework.defines.entities.I_UsingStatus;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.springframework.services.LKApiBusUpdateUsingStatusService;

public abstract class LKApiBusUpdateUsingStatusController<CI extends LKRequestIDsUsingStatusBean, E extends I_UsingStatus> extends LKApiBusIUDController<CI, E> {

	@Override
	protected abstract LKApiBusUpdateUsingStatusService<CI, E> getService(CI cin);


	@Deprecated
	@Override
	protected LKOperTypeEnum getOperType(@Valid CI cin) {
		if (cin.getUsingStatus().equals(LKUsingStatusEnum.DEPRECATED)) {
			return LKOperTypeEnum.REMOVE;
		}
		return LKOperTypeEnum.EDIT;
	}


	@Deprecated
	@Override
	protected String getSubOperBusType(CI cin) {
		return cin.getUsingStatus().toString();
	}

}
