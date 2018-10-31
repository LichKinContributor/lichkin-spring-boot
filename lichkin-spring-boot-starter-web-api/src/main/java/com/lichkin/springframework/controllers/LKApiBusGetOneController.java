package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.impl.LKRequestIDBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.springframework.services.LKApiBusGetOneService;

public abstract class LKApiBusGetOneController<CI extends LKRequestIDBean, O, E extends I_ID> extends LKApiYYController<CI, O, CI, O> {

	@Override
	protected abstract LKApiBusGetOneService<CI, O, E> getService(CI cin);


	@Deprecated
	@Override
	protected boolean saveLog(CI cin) {
		return false;
	}


	@Deprecated
	@Override
	protected CI beforeInvokeService(CI cin) throws LKException {
		return cin;
	}


	@Deprecated
	@Override
	protected O afterInvokeService(CI cin, CI sin, O sout) throws LKException {
		return sout;
	}

}
