package com.lichkin.springframework.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedBaseSysApiRequestLogEntity extends _LKMappedBaseSysApiRequestLogEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 公司ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String compId;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { super.getCheckCodeFieldValues(), compId };
	}

}
