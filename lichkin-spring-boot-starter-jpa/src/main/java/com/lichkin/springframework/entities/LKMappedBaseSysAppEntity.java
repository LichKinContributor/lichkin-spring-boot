package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端相关表基本实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedBaseSysAppEntity extends _LKMappedBaseSysAppEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 公司ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String compId;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { super.getCheckCodeFieldValues(), compId };
	}

}
