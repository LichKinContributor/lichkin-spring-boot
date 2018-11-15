package com.lichkin.framework.db.beans;

/**
 * 数据库资源定义类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface SysConfigQuartzR {

	public static final int id = 0x00001000;

	public static final int usingStatus = 0x00001001;

	public static final int insertTime = 0x00001002;

	public static final int groupName = 0x00001003;

	public static final int jobName = 0x00001004;

	public static final int className = 0x00001005;

	public static final int methodName = 0x00001006;

	public static final int cronExpression = 0x00001007;

	public static final int lastExecuteTime = 0x00001008;

	public static final int lastFinishedTime = 0x00001009;

}