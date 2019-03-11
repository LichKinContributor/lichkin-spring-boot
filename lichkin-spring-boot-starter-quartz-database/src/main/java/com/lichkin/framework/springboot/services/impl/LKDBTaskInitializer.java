package com.lichkin.framework.springboot.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.db.beans.SysConfigQuartzR;
import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.springboot.beans.LKJob;
import com.lichkin.framework.springboot.services.LKTaskInitializer;
import com.lichkin.springframework.entities.impl.SysConfigQuartzEntity;
import com.lichkin.springframework.services.LKDBService;

/**
 * 定时任务初始化器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Service
public class LKDBTaskInitializer extends LKDBService implements LKTaskInitializer {

	@Override
	public void init(List<LKJob> listJob) {
		// 加载配置信息
		if (logger.isInfoEnabled()) {
			logger.info("load configs from T_SYS_CONFIG_QUARTZ.");
		}

		QuerySQL sql = new QuerySQL(SysConfigQuartzEntity.class);
		sql.eq(SysConfigQuartzR.usingStatus, LKUsingStatusEnum.USING);
		if (LKConfigStatics.SYSTEM_DEBUG) {
			sql.eq(SysConfigQuartzR.groupName, "DEBUG-" + LKConfigStatics.SYSTEM_TAG);
		} else {
			sql.eq(SysConfigQuartzR.groupName, LKConfigStatics.SYSTEM_TAG);
		}

		final List<SysConfigQuartzEntity> list = dao.getList(sql, SysConfigQuartzEntity.class);

		if (CollectionUtils.isNotEmpty(list)) {
			for (final SysConfigQuartzEntity quartz : list) {
				boolean simple = false;
				if (LKConfigStatics.SYSTEM_DEBUG || quartz.getCronExpression().endsWith("once")) {
					simple = true;
				}
				listJob.add(new LKJob(simple, quartz.getGroupName(), quartz.getJobName(), quartz.getClassName(), quartz.getMethodName(), quartz.getCronExpression(), null));
			}
		}

		// 加载配置信息完成
		if (logger.isInfoEnabled()) {
			logger.info("load configs from T_SYS_CONFIG_QUARTZ finished.");
		}
	}

}
