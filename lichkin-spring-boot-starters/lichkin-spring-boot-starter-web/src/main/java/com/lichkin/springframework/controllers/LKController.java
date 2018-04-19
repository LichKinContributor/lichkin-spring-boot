package com.lichkin.springframework.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.lichkin.framework.defines.LKFramework;

/**
 * 控制器类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
class LKController extends LKFramework {

	@Autowired
	protected HttpServletRequest request;

}
