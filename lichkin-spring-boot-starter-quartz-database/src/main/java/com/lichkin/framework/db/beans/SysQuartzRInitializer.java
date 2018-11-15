package com.lichkin.framework.db.beans;

/**
 * 数据库资源初始化类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
class SysQuartzRInitializer implements LKRInitializer {

	/**
	 * 初始化数据库资源
	 */
	public static void init() {
		LKDBResource.addTable("com.lichkin.springframework.entities.impl.SysConfigQuartzEntity", "T_SYS_CONFIG_QUARTZ", "SysConfigQuartzEntity");
		LKDBResource.addColumn("00001000", "SysConfigQuartzEntity", "id");
		LKDBResource.addColumn("00001001", "SysConfigQuartzEntity", "usingStatus");
		LKDBResource.addColumn("00001002", "SysConfigQuartzEntity", "insertTime");
		LKDBResource.addColumn("00001003", "SysConfigQuartzEntity", "groupName");
		LKDBResource.addColumn("00001004", "SysConfigQuartzEntity", "jobName");
		LKDBResource.addColumn("00001005", "SysConfigQuartzEntity", "className");
		LKDBResource.addColumn("00001006", "SysConfigQuartzEntity", "methodName");
		LKDBResource.addColumn("00001007", "SysConfigQuartzEntity", "cronExpression");
		LKDBResource.addColumn("00001008", "SysConfigQuartzEntity", "lastExecuteTime");
		LKDBResource.addColumn("00001009", "SysConfigQuartzEntity", "lastFinishedTime");
	}

}