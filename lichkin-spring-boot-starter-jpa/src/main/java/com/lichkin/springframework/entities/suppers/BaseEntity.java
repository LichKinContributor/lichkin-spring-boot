package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.defines.entities.I_Base;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class BaseEntity extends IDEntity implements I_Base {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 在用状态（枚举） */
	@Enumerated(EnumType.STRING)
	@Column(length = 10, nullable = false)
	private LKUsingStatusEnum usingStatus;

	/** 新增操作时间（yyyyMMddHHmmssSSS） */
	@Column(length = 17, nullable = false, updatable = false)
	private String insertTime;


	/**
	 * 设置新增操作时间
	 * @param insertTime 新增操作时间
	 * @deprecated 系统将自动注入值，手动注入将失效。
	 */
	@Deprecated
	@Override
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

}
