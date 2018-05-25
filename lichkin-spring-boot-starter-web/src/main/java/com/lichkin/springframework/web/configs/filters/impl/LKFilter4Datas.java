package com.lichkin.springframework.web.configs.filters.impl;

import javax.servlet.annotation.WebFilter;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.springframework.web.configs.filters.LKFilter;

/**
 * 数据请求过滤器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@WebFilter(filterName = "DatasFilter", urlPatterns = "*" + LKFrameworkStatics.WEB_MAPPING_DATAS)
public class LKFilter4Datas extends LKFilter {

}
