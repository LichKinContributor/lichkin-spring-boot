package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.defines.annotations.FieldGenerator;
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
public abstract class OperLogEntity extends CompIDEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 操作类型（枚举） */
	@Enumerated(EnumType.STRING)
	@FieldGenerator(queryCondition = true, resultColumn = true)
	@Column(length = 6, nullable = false)
	private LKOperTypeEnum operType;

	/** 业务操作类型（字典） */
	@FieldGenerator(queryCondition = true, resultColumn = true, dictionary = true)
	@Column(length = 64, nullable = false)
	private String busType;

	/** 操作人ID */
	@Column(length = 64)
	private String loginId;

	/** 请求ID */
	@FieldGenerator(resultColumn = true)
	@Column(length = 64, nullable = false)
	private String requestId;

	/** 请求时间（yyyyMMddHHmmssSSS） */
	@FieldGenerator(resultColumn = true)
	@Column(length = 17, nullable = false)
	private String requestTime;

	/** 请求IP */
	@FieldGenerator(resultColumn = true)
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
