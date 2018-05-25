package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKUserInterface;
import com.lichkin.framework.defines.enums.impl.LKAuthenticationEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public class LKMappedUserEntity extends LKMappedBaseUserEntity implements LKUserInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -8888886666661006L;

	/** 身份证号 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String userCard;

	/** 生日（yyyy-MM-dd） */
	@Column(nullable = false, length = 10)
	private String birthday;

	/** 出生地（字典） */
	@Column(nullable = false, length = CODE_LENGTH)
	private String birthplace;

	/** 学历（字典） */
	@Column(nullable = false, length = CODE_LENGTH)
	private String degree;

	/** 学位（字典） */
	@Column(nullable = false, length = CODE_LENGTH)
	private String education;

	/** 婚姻状态（字典） */
	@Column(nullable = false, length = CODE_LENGTH)
	private String maritalStatus;

	/** 民族（字典） */
	@Column(nullable = false, length = CODE_LENGTH)
	private String nation;

	/** 实名认证等级（枚举） */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = ENUM_LENGTH)
	private LKAuthenticationEnum authentication;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { super.getCheckCodeFieldValues(), userCard, birthday, birthplace, degree, education, maritalStatus, nation, authentication };
	}

}
