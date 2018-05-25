package com.lichkin.springframework.controllers;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.web.annotations.LKController4Pages;

/**
 * 页面请求控制器类定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Pages
public class LKPagesController extends LKController {

	/** 映射符号 */
	protected static final String MAPPING = LKFrameworkStatics.WEB_MAPPING_PAGES;

}
