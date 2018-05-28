package com.lichkin.springframework.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.lichkin.springframework.db.daos.LKDao;

/**
 * 服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class LKDBService extends LKService {

	@Autowired
	protected LKDao dao;

}
