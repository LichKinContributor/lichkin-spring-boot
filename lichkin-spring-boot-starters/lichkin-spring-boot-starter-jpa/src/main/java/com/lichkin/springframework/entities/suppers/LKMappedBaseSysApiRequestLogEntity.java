package com.lichkin.springframework.entities.suppers;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.defines.enums.impl.LKClientTypeEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class LKMappedBaseSysApiRequestLogEntity extends LKMappedBaseSysEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 8888886666660004L;

	/**
	 * 国际化
	 * @see Locale
	 */
	@Column(nullable = false, length = 5)
	private String locale;

	/** 客户端唯一标识 */
	@Column(nullable = false, length = 128)
	private String appKey;

	/** 客户端类型 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private LKClientTypeEnum clientType;

	/** 客户端版本号（大版本号） */
	@Column(nullable = false)
	private Byte versionX;

	/** 客户端版本号（中版本号） */
	@Column(nullable = false)
	private Byte versionY;

	/** 客户端版本号（小版本号） */
	@Column(nullable = false)
	private Short versionZ;

	/** 登录后获取得 */
	@Column(nullable = false, length = 64)
	private String token = "";


	@Override
	protected Object[] getCheckCodeFieldValues() {
		return new Object[] { locale, appKey, clientType, versionX, versionY, versionZ, token };
	}

}
