package com.lichkin.springframework.controllers;

import java.lang.reflect.ParameterizedType;

import org.apache.commons.lang3.StringUtils;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.entities.I_ID;
import com.lichkin.framework.defines.exceptions.LKException;

public abstract class LKApiBusIUDController<CI extends LKRequestBean, E extends I_ID> extends LKApiVVController<CI, CI> {

	Class<E> classE;


	@SuppressWarnings("unchecked")
	public LKApiBusIUDController() {
		super();
		classE = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}


	@Deprecated
	@Override
	protected String getBusType(CI cin) {
		String subOperBusType = StringUtils.trimToEmpty(getSubOperBusType(cin));
		return getOperType(cin).toString() + "_" + classE.getSimpleName().replace("Entity", "") + (subOperBusType.isEmpty() ? "" : "_" + subOperBusType);
	}


	protected String getSubOperBusType(CI cin) {
		return null;
	}


	@Deprecated
	@Override
	protected CI beforeInvokeService(CI cin) throws LKException {
		return cin;
	}

}
