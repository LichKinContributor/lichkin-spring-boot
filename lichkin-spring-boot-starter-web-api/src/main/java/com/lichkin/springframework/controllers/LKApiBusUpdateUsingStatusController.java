package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestIDsBean;
import com.lichkin.framework.defines.entities.I_UsingStatus_ID;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusUpdateUsingStatusService;

public abstract class LKApiBusUpdateUsingStatusController<CI extends LKRequestIDsBean, E extends I_UsingStatus_ID> extends LKApiBusIUDController<CI, E> {

	@Override
	protected abstract LKApiBusUpdateUsingStatusService<CI, E> getService(CI cin, ApiKeyValues<CI> params);


	@Deprecated
	@Override
	protected LKOperTypeEnum getOperType(CI cin, ApiKeyValues<CI> params) {
		if (getUsingStatus().equals(LKUsingStatusEnum.DEPRECATED)) {
			return LKOperTypeEnum.REMOVE;
		}
		return LKOperTypeEnum.EDIT;
	}


	/**
	 * 通常使用改状态接口都为逻辑删除
	 * @return 删除状态
	 */
	protected LKUsingStatusEnum getUsingStatus() {
		return LKUsingStatusEnum.DEPRECATED;
	}


	@Override
	protected void beforeInvokeService(CI cin, ApiKeyValues<CI> params) throws LKException {
		params.putObject("_usingStatus", getUsingStatus());
	}

}
