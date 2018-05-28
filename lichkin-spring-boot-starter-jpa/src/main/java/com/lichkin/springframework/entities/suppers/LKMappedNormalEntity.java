package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.suppers.LKNormalInterface;

import lombok.Getter;
import lombok.Setter;

/**
 * 一般业务实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedNormalEntity extends _LKMappedNormalEntity implements LKNormalInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 公司ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String compId;

}
