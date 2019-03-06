package com.lichkin.framework.springboot.configurations;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.lichkin.framework.db.beans.QuerySQL;
import com.lichkin.framework.db.beans.SysConfigQuartzR;
import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.springboot.applications.LKTaskDBRunner;
import com.lichkin.springframework.entities.impl.SysConfigQuartzEntity;

@Configuration
@Order(value = 1000)
public class LKTaskRunner4SysConfigQuartz extends LKTaskDBRunner {

	@Autowired
	private Scheduler scheduler;


	@Override
	protected void doTask() {
		logger.info("►►►►►►►►►►►►►►►►►►LKTaskRunner4SysConfigQuartz◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄");
		LKQuartzManager.getInstance().initScheduler(scheduler);

		// 加载配置信息
		logger.info("load configs from T_SYS_CONFIG_QUARTZ.");

		QuerySQL sql = new QuerySQL(SysConfigQuartzEntity.class);
		sql.eq(SysConfigQuartzR.usingStatus, LKUsingStatusEnum.USING);
		if (LKConfigStatics.SYSTEM_DEBUG) {
			sql.eq(SysConfigQuartzR.groupName, "DEBUG-" + LKConfigStatics.SYSTEM_TAG);
		} else {
			sql.eq(SysConfigQuartzR.groupName, LKConfigStatics.SYSTEM_TAG);
		}

		final List<SysConfigQuartzEntity> list = dao.getList(sql, SysConfigQuartzEntity.class);

		if (CollectionUtils.isEmpty(list)) {
			logger.warn("no configs configed in T_SYS_CONFIG_QUARTZ.");
		} else {
			logger.info("loaded configs from T_SYS_CONFIG_QUARTZ.");
			for (final SysConfigQuartzEntity quartz : list) {
				boolean simple = false;
				if (LKConfigStatics.SYSTEM_DEBUG || quartz.getCronExpression().endsWith("once")) {
					simple = true;
				}
				LKQuartzManager.getInstance().scheduleJob(simple, quartz.getGroupName(), quartz.getJobName(), quartz.getClassName(), quartz.getMethodName(), quartz.getCronExpression(), null);
			}
		}

		LKQuartzManager.getInstance().start();
		logger.info("load configs from T_SYS_CONFIG_QUARTZ finished.");
	}

}
