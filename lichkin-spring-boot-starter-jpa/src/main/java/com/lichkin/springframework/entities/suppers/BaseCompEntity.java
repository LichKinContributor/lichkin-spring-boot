package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 公司基础表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseCompEntity extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 公司ID（SysCompEntity.id） */
	@Column(length = 64, nullable = false)
	private String compId;

}
