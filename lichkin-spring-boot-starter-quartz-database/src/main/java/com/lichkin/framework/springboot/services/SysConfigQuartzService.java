package com.lichkin.framework.springboot.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.db.beans.SysConfigQuartzR;
import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.springframework.entities.impl.SysConfigQuartzEntity;
import com.lichkin.springframework.services.LKDBService;

@Service
public class SysConfigQuartzService extends LKDBService {

	@Transactional
	public SysConfigQuartzEntity getOneByClassName(String className) {
		QuerySQL sql = new QuerySQL(false, SysConfigQuartzEntity.class);
		sql.eq(SysConfigQuartzR.usingStatus, LKUsingStatusEnum.USING);
		sql.eq(SysConfigQuartzR.className, className);
		sql.eq(SysConfigQuartzR.groupName, LKConfigStatics.SYSTEM_DEBUG ? "DEBUG-" + LKConfigStatics.SYSTEM_TAG : LKConfigStatics.SYSTEM_TAG);
		SysConfigQuartzEntity entity = dao.getOne(sql, SysConfigQuartzEntity.class);
		entity.setLastExecuteTime(LKDateTimeUtils.now());
		return entity;
	}


	@Transactional
	public void finished(SysConfigQuartzEntity entity) {
		entity.setLastFinishedTime(LKDateTimeUtils.now());
		dao.mergeOne(entity);
	}

}
