package com.lichkin.springframework.entities.impl;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang3.ArrayUtils;

import com.lichkin.springframework.entities.suppers.LKMappedBaseSysApiRequestLogEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端版本信息实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@Entity
public class SysApiRequestLogEntity extends LKMappedBaseSysApiRequestLogEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 8888886666666001L;

	/** 客户端系统版本 */
	@Column(nullable = false, length = 16)
	private String osVersion;

	/** 生产厂商 */
	@Column(nullable = false, length = 64)
	private String brand;

	/** 机型信息 */
	@Column(nullable = false, length = 128)
	private String model;

	/** 设备唯一标识 */
	@Column(nullable = false, length = 64)
	private String uuid;

	/** 屏幕宽 */
	private Short screenWidth;

	/** 屏幕高 */
	private Short screenHeight;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return ArrayUtils.addAll(super.getCheckCodeFieldValues(), osVersion, brand, model, uuid, screenWidth, screenHeight);
	}

}
