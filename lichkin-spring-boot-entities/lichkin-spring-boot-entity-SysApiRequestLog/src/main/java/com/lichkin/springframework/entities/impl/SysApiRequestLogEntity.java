package com.lichkin.springframework.entities.impl;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.lichkin.framework.defines.enums.impl.LKClientTypeEnum;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端版本信息实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@Entity
public class SysApiRequestLogEntity extends LKMappedBaseSysEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1560109149982803644L;

	/** 客户端唯一标识 */
	@Column(nullable = false, length = 128)
	private String appKey;

	/** 客户端类型 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private LKClientTypeEnum clientType;

	/** 客户端版本号（大版本号） */
	@Column(nullable = false)
	private Byte versionX;

	/** 客户端版本号（中版本号） */
	@Column(nullable = false)
	private Byte versionY;

	/** 客户端版本号（小版本号） */
	@Column(nullable = false)
	private Short versionZ;

	/**
	 * 国际化
	 * @see Locale
	 */
	@Column(nullable = false, length = 5)
	private String locale;

	/** 登录后获取得 */
	@Column(nullable = false, length = 64)
	private String token = "";

	/** 客户端系统版本 */
	@Column(nullable = false, length = 32)
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
		return new Object[] { appKey, clientType, versionX, versionY, versionZ, locale, token, osVersion, brand, model, uuid, screenWidth, screenHeight };
	}

}
