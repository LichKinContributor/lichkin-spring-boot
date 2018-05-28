package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKLoginRoleInterface;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录&amp;角色关联表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedLoginRoleEntity extends LKMappedBaseSysEntity implements LKLoginRoleInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 登录ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String loginId;

	/** 角色ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String roleId;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { loginId, roleId };
	}

}
