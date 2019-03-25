package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.defines.entities.I_LoginId;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class OperLogEntity extends IDEntity implements I_LoginId {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 操作类型（枚举） */
	@Enumerated(EnumType.STRING)
	@Column(length = 6, nullable = false)
	private LKOperTypeEnum operType;

	/** 操作人ID */
	@Column(length = 64)
	private String loginId;

	/** 请求ID */
	@Column(length = 64, nullable = false)
	private String requestId;

	/** 请求时间（yyyyMMddHHmmssSSS） */
	@Column(length = 17, nullable = false)
	private String requestTime;

	/** 请求IP */
	@Column(length = 64, nullable = false)
	private String requestIp;

	/** 请求URL */
	@Column(length = 128, nullable = false)
	private String requestUrl;

	/** 请求数据 */
	@Lob
	@Column(nullable = false)
	private String requestDatas;

}
