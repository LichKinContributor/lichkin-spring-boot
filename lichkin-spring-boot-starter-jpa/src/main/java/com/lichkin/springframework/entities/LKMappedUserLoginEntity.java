package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKUserLoginInterface;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户登录表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedUserLoginEntity extends LKMappedBaseSysEntity implements LKUserLoginInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 照片（base64） */
	@Column(nullable = false, length = VALUE_BASE64_LENGTH)
	private String photo;

	/** 用户ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String userId;

	/** 登录名 */
	@Column(nullable = false, length = NAME_LENGTH)
	private String loginName;

	/** 身份证号 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String userCard;

	/** 手机号码 */
	@Column(nullable = false, length = CELLPHONE_LENGTH)
	private String cellphone;

	/** 邮箱 */
	@Column(nullable = false, length = EMAIL_LENGTH)
	private String email;

	/** 密码 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String pwd;

	/** 验证码 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String securityCode;

	/** 密码错误次数 */
	@Column(nullable = false)
	private Byte errorTimes;

	/** 登录token */
	@Column(nullable = false, length = CODE_LENGTH)
	private String token;

	/** 微信ID */
	@Column(nullable = false, length = CODE_LENGTH)
	private String wechatId;

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
		return new Object[] { userId, loginName, userCard, cellphone, email, pwd, securityCode, errorTimes, token, wechatId, field1, field2, field3 };
	}

}
