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
	@Column(nullable = false, length = 64)
	private String systemTag = LKFrameworkStatics.SYSTEM_TAG;

	/** 业务ID */
	@Column(nullable = false, length = 64)
	private String busId = "";

	/** 校验码（MD5） */
	@Column(nullable = false, length = 32)
	private String checkCode = "";


	@Override
	public void updateCheckCode() {
		StringBuffer sb = new StringBuffer(getSystemTag()).append(getBusId()).append(getCompId())

				.append(getInsertSystemTag()).append(getInsertTime()).append(getInsertLoginId())

				.append(getUpdateSystemTag()).append(getUpdateTime()).append(getUpdateLoginId())

				.append(getUsingStatus());

		Object[] objs = getCheckCodeFieldValues();
		for (Object obj : objs) {
			sb.append(obj);
		}

		setCheckCode(LKMD5Encrypter.encrypt(sb.toString(), getId()));
	}


	/**
	 * 获取生成校验码所需对字段值
	 * @return 字段值数组
	 */
	protected abstract Object[] getCheckCodeFieldValues();

}
