package com.lichkin.framework.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.lichkin.springframework.db.daos.LKDao;

/**
 * 通用基本任务服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKBaseTaskDBService extends LKBaseTaskService {

	/** 数据库访问对象 */
	@Autowired
	protected LKDao dao;

}
