package com.lichkin.springframework.entities.impl;

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
public class SysAppVersionEntity extends LKMappedBaseSysEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -8888886666666002L;

	/** 客户端唯一标识 */
	@Column(nullable = false, length = 128)
	private String appKey;

	/** 客户端类型 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private LKClientTypeEnum clientType;

	/** 强制更新 */
	@Column(nullable = false)
	private Boolean forceUpdate = false;

	/** 客户端版本号（大版本号） */
	@Column(nullable = false)
	private Byte versionX;

	/** 客户端版本号（中版本号） */
	@Column(nullable = false)
	private Byte versionY;

	/** 客户端版本号（小版本号） */
	@Column(nullable = false)
	private Short versionZ;

	/** 版本信息 */
	@Column(nullable = false, length = 64)
	private String tip = "";

	/** 版本信息页面地址 */
	@Column(nullable = false, length = 200)
	private String url = "";


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { appKey, clientType, forceUpdate, versionX, versionY, versionZ, tip, url };
	}

}
