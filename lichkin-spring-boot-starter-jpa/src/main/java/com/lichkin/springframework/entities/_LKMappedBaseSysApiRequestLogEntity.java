package com.lichkin.springframework.entities;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class _LKMappedBaseSysApiRequestLogEntity extends _LKMappedBaseSysAppEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * 国际化
	 * @see Locale
	 */
	@Column(nullable = false, length = 5)
	private String locale;

	/** 登录后获取得 */
	@Column(nullable = false, length = CODE_LENGTH)
	private String token;


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { super.getCheckCodeFieldValues(), locale, token };
	}

}
