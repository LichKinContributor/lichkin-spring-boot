package com.lichkin.springframework.entities.impl;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.lichkin.springframework.entities.suppers.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 定时任务配置表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@Entity
public final class SysConfigQuartzEntity extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 00001L;

	/** 组名称 */
	@Column(nullable = false, length = 64)
	private String groupName;

	/** 任务名称 */
	@Column(nullable = false, length = 128)
	private String jobName;

	/** 类名称 */
	@Column(nullable = false, length = 256)
	private String className;

	/** 方法名称 */
	@Column(nullable = false, length = 32)
	private String methodName;

	/** 表达式 */
	@Column(nullable = false, length = 64)
	private String cronExpression;

	/** 最后一次任务执行时间（yyyyMMddHHmmssSSS） */
	@Column(length = 17)
	private String lastExecuteTime;

	/** 最后一次任务完成时间（yyyyMMddHHmmssSSS） */
	@Column(length = 17)
	private String lastFinishedTime;

}
