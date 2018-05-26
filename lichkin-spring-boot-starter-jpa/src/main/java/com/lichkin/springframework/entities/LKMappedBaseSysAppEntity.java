package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.defines.enums.impl.LKClientTypeEnum;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端相关表基本实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public class LKMappedBaseSysAppEntity extends LKMappedBaseSysEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -600L;

	/** 客户端唯一标识 */
	@Column(nullable = false, length = VALUE_128_LENGTH)
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


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { appKey, clientType, versionX, versionY, versionZ };
	}

}
