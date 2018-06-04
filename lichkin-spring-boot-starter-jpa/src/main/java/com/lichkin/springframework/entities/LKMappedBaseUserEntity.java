package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKBaseUserInterface;
import com.lichkin.framework.defines.enums.impl.LKGenderEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedBaseUserEntity extends LKMappedBaseContactInfoEntity implements LKBaseUserInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 性别 */
	@Column(nullable = false, length = 7)
	@Enumerated(EnumType.STRING)
	private LKGenderEnum gender;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { super.getCheckCodeFieldValues(), gender };
	}

}
