package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.LKRoleInterface;
import com.lichkin.springframework.entities.suppers.LKMappedBaseSysEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 角色表实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public class LKMappedRoleEntity extends LKMappedBaseSysEntity implements LKRoleInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -12L;

	/** 角色名称 */
	@Column(nullable = false, length = NAME_LENGTH)
	private String roleName;

	/** 描述 */
	@Column(nullable = false, length = VALUE_128_LENGTH)
	private String description;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { roleName, description };
	}

}
