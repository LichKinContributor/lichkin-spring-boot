package com.lichkin.springframework.web.configs.filters.impl;

import javax.servlet.annotation.WebFilter;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.springframework.web.configs.filters.LKFilter;

/**
 * API数据请求过滤器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@WebFilter(filterName = "ApiFilter", urlPatterns = LKFrameworkStatics.WEB_MAPPING_API + "*")
public class LKFilter4Api extends LKFilter {

}
