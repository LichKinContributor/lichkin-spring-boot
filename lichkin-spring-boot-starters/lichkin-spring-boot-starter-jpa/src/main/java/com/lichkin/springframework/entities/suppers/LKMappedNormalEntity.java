package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.defines.entities.suppers.LKNormalInterface;
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
public class LKMappedNormalEntity extends LKMappedIDEntity implements LKNormalInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -8888886666660001L;

	/** 在用状态（枚举） */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	protected LKUsingStatusEnum usingStatus = LKUsingStatusEnum.USING;

}
