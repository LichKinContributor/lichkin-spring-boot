package com.lichkin.springframework.entities.suppers;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.lichkin.framework.db.entities.suppers._LKBaseSysInterface;
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
public abstract class _LKMappedBaseSysEntity extends _LKMappedBaseEntity implements _LKBaseSysInterface {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 系统编码 */
	@Column(nullable = false, length = SYSTEM_TAG_LENGTH)
	private String systemTag;

	/** 业务ID */
	@Column(nullable = false, length = ID_LENGTH)
	private String busId;

	/** 校验码（MD5） */
	@Column(nullable = false, length = CODE_LENGTH)
	private String checkCode;


	/**
	 * 更新校验码
	 * @deprecated 框架内部使用
	 */
	@Deprecated
	@Override
	public void updateCheckCode() {
		StringBuffer sb = new StringBuffer()

				.append(getSystemTag()).append(getBusId())

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
