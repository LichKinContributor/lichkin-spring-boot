package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.entities.suppers.LKBaseSysInterface;
import com.lichkin.framework.utils.security.md5.LKMD5Encrypter;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统一般业务实体类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class LKMappedBaseSysEntity extends LKMappedBaseEntity implements LKBaseSysInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = -8888886666660003L;

	/** 系统编码 */
	@Column(name = "SYSTEM_TAG", length = 64)
	protected String systemTag = LKFrameworkStatics.SYSTEM_TAG;

	/** 业务ID */
	@Column(name = "BUS_ID", length = 64)
	protected String busId = "";

	/** 校验码（MD5） */
	@Column(name = "CHECK_CODE", length = 32)
	protected String checkCode = "";


	/**
	 * 初始化校验码
	 * @return 校验码
	 */
	protected String initCheckCode(Object... strs) {
		StringBuffer sb = new StringBuffer(systemTag).append(usingStatus).append(insertSystemTag).append(insertTime).append(insertLoginId).append(updateSystemTag).append(updateTime).append(updateLoginId).append(busId);
		for (Object str : strs) {
			sb.append(str);
		}
		return LKMD5Encrypter.encrypt(sb.toString(), id);
	}

}
