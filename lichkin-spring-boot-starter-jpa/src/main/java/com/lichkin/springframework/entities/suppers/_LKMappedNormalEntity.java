package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.suppers._LKNormalInterface;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * 一般业务实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class _LKMappedNormalEntity extends _LKMappedIDEntity implements _LKNormalInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 在用状态（枚举） */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private LKUsingStatusEnum usingStatus;

}
