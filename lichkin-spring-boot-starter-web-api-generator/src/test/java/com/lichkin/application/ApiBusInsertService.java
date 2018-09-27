package com.lichkin.application;

public class ApiBusInsertService<SI, E> extends ApiBusService<SI, Void, E> {

	@SuppressWarnings("unchecked")
	public ApiBusInsertService() {
		super();
		classSI = (Class<SI>) types[0];
		classSO = Void.class;
		classE = (Class<E>) types[1];
	}

}
