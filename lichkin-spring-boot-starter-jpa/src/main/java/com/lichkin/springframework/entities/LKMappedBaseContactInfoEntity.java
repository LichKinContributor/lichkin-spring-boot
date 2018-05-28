package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKBaseContactInfoInterface;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础联系方式表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedBaseContactInfoEntity extends LKMappedBaseSysEntity implements LKBaseContactInfoInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 照片（base64） */
	@Column(nullable = false, length = VALUE_BASE64_LENGTH)
	private String photo;

	/** 姓名 */
	@Column(nullable = false, length = NAME_LENGTH)
	private String userName;

	/** 手机号码 */
	@Column(nullable = false, length = CELLPHONE_LENGTH)
	private String cellphone;

	/** 电话号码 */
	@Column(nullable = false, length = TELEPHONE_LENGTH)
	private String telephone1;

	/** 电话号码 */
	@Column(nullable = false, length = TELEPHONE_LENGTH)
	private String telephone2;

	/** 电话号码 */
	@Column(nullable = false, length = TELEPHONE_LENGTH)
	private String telephone3;

	/** 邮箱 */
	@Column(nullable = false, length = EMAIL_LENGTH)
	private String email;

	/** 邮箱 */
	@Column(nullable = false, length = EMAIL_LENGTH)
	private String email1;

	/** 邮箱 */
	@Column(nullable = false, length = EMAIL_LENGTH)
	private String email2;

	/** 邮箱 */
	@Column(nullable = false, length = EMAIL_LENGTH)
	private String email3;

	/** 备用字段1 */
	@Column(nullable = false, length = VALUE_128_LENGTH)
	private String field1;

	/** 备用字段2 */
	@Column(nullable = false, length = VALUE_128_LENGTH)
	private String field2;

	/** 备用字段3 */
	@Column(nullable = false, length = VALUE_128_LENGTH)
	private String field3;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { photo, userName, cellphone, telephone1, telephone2, telephone3, email, email1, email2, email3, field1, field2, field3 };
	}

}
