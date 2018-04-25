package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.entities.suppers.LKBaseInterface;
import com.lichkin.framework.utils.LKDateTimeUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 一般业务实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public class LKMappedBaseEntity extends LKMappedNormalEntity implements LKBaseInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -8888886666660002L;

	/** 新增操作系统编码 */
	@Column(nullable = false, length = 64)
	private String insertSystemTag = LKFrameworkStatics.SYSTEM_TAG;

	/** 新增操作时间（yyyyMMddHHmmssSSS） */
	@Column(nullable = false, length = 17)
	private String insertTime = LKDateTimeUtils.now();

	/** 新增操作人登录ID */
	@Column(nullable = false, length = 64)
	private String insertLoginId = "";

	/** 更新操作系统编码 */
	@Column(nullable = false, length = 64)
	private String updateSystemTag = LKFrameworkStatics.SYSTEM_TAG;

	/** 更新操作时间（yyyyMMddHHmmssSSS） */
	@Column(nullable = false, length = 17)
	private String updateTime = LKDateTimeUtils.now();

	/** 更新操作人登录ID */
	@Column(nullable = false, length = 64)
	private String updateLoginId = "";

}
