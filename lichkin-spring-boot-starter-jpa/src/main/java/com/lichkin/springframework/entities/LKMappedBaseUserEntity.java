package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKBaseUserInterface;
import com.lichkin.framework.defines.enums.impl.LKGenderEnum;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public class LKMappedBaseUserEntity extends LKMappedBaseSysEntity implements LKBaseUserInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -8888886666661005L;

	/** 姓名 */
	@Column(nullable = false, length = NAME_LENGTH)
	private String userName;

	/** 性别 */
	@Column(nullable = false, length = 6)
	@Enumerated(EnumType.STRING)
	private LKGenderEnum gender;

	/** 照片（base64） */
	@Column(nullable = false, length = 20480)
	private String photo;

	/** 手机号码 */
	@Column(nullable = false, length = 11)
	private String cellphone;

	/** 电话号码 */
	@Column(nullable = false, length = 20)
	private String telephone1;

	/** 电话号码 */
	@Column(nullable = false, length = 20)
	private String telephone2;

	/** 电话号码 */
	@Column(nullable = false, length = 20)
	private String telephone3;

	/** 邮箱 */
	@Column(nullable = false, length = VALUE_SHORT_LENGTH)
	private String email;

	/** 邮箱 */
	@Column(nullable = false, length = VALUE_SHORT_LENGTH)
	private String email1;

	/** 邮箱 */
	@Column(nullable = false, length = VALUE_SHORT_LENGTH)
	private String email2;

	/** 邮箱 */
	@Column(nullable = false, length = VALUE_SHORT_LENGTH)
	private String email3;

	/** 备用字段1 */
	@Column(nullable = false, length = VALUE_SHORT_LENGTH)
	private String field1;

	/** 备用字段2 */
	@Column(nullable = false, length = VALUE_SHORT_LENGTH)
	private String field2;

	/** 备用字段3 */
	@Column(nullable = false, length = VALUE_SHORT_LENGTH)
	private String field3;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { userName, gender, photo, cellphone, telephone1, telephone2, telephone3, email, email1, email2, email3, field1, field2, field3 };
	}

}
