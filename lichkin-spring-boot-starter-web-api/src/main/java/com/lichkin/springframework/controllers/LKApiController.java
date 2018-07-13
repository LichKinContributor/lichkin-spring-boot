package com.lichkin.springframework.controllers;

import com.lichkin.framework.beans.LKRequestInterface;
import com.lichkin.framework.web.annotations.LKController4Api;

/**
 * API数据请求控制器类定义（开放接口）
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKApiController<I extends LKRequestInterface, O> extends LKStandardApiController<I, O, I, O> {

	@Override
	protected I in2sin(I in) {
		return in;
	}


	@Override
	protected O sout2out(O out) {
		return out;
	}

}
