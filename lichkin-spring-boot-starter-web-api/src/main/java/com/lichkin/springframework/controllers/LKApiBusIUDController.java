package com.lichkin.springframework.controllers;

import java.lang.reflect.ParameterizedType;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.entities.I_ID;

public abstract class LKApiBusIUDController<CI extends LKRequestBean, E extends I_ID> extends LKApiVVController<CI> {

	Class<E> classE;


	@SuppressWarnings("unchecked")
	public LKApiBusIUDController() {
		super();
		classE = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}


	@Deprecated
	@Override
	protected boolean saveLog(CI cin, ApiKeyValues<CI> params) {
		return true;
	}

}
