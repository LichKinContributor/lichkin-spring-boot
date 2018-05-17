package com.lichkin.springframework.db.utils;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.entities.suppers.LKBaseInterface;
import com.lichkin.framework.defines.entities.suppers.LKBaseSysInterface;
import com.lichkin.framework.defines.entities.suppers.LKIDInterface;
import com.lichkin.framework.defines.entities.suppers.LKNormalInterface;
import com.lichkin.framework.defines.enums.impl.LKRangeTypeEnum;
import com.lichkin.framework.defines.enums.impl.LKUsingStatusEnum;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKRandomUtils;

/**
 * 实体类初始化工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKEntityInitializer {

	/**
	 * 初始化参数
	 * @param obj 实体类对象
	 * @return 实体类对象
	 */
	@SuppressWarnings("deprecation")
	public static Object initEntity(final Object obj) {
		final String currentTime = LKDateTimeUtils.now();
		final String systemTag = LKFrameworkStatics.SYSTEM_TAG;
		final String loginId = getLoginId();

		// ID check
		if (obj instanceof LKIDInterface) {// 属于框架定义的才需要自动初始化
			if (StringUtils.isBlank(((LKIDInterface) obj).getId())) {// 新增数据
				// TODO ID init

				// Normal check
				if ((obj instanceof LKNormalInterface)) {
					// Normal init
					if (((LKNormalInterface) obj).getUsingStatus() == null) {
						((LKNormalInterface) obj).setUsingStatus(LKUsingStatusEnum.USING);
					}

					// Base check
					if ((obj instanceof LKBaseInterface)) {
						// Base init
						((LKBaseInterface) obj).setInsertTime(currentTime);
						((LKBaseInterface) obj).setUpdateTime(currentTime);
						((LKBaseInterface) obj).setInsertSystemTag(systemTag);
						((LKBaseInterface) obj).setUpdateSystemTag(systemTag);
						((LKBaseInterface) obj).setInsertLoginId(loginId);
						((LKBaseInterface) obj).setUpdateLoginId(loginId);

						// Sys check
						if ((obj instanceof LKBaseSysInterface)) {
							// Sys init
							if (((LKBaseSysInterface) obj).getSystemTag() == null) {
								((LKBaseSysInterface) obj).setSystemTag(systemTag);
							}
							if (((LKBaseSysInterface) obj).getBusId() == null) {
								((LKBaseSysInterface) obj).setBusId(LKRandomUtils.create(64, LKRangeTypeEnum.NUMBER_AND_LETTER_FULL));
							}
							((LKBaseSysInterface) obj).updateCheckCode();
						}
					}
				}
			} else {// 更新数据
				// Base check
				if ((obj instanceof LKBaseInterface)) {
					// Base init
					((LKBaseInterface) obj).setUpdateTime(currentTime);
					((LKBaseInterface) obj).setUpdateSystemTag(systemTag);
					((LKBaseInterface) obj).setUpdateLoginId(loginId);

					// Sys check
					if ((obj instanceof LKBaseSysInterface)) {
						// Sys init
						((LKBaseSysInterface) obj).updateCheckCode();
					}
				}
			}
		}

		return obj;
	}


	/**
	 * 获取登录人登录ID
	 * @return 登录人登录ID
	 */
	public static String getLoginId() {
		String loginId = null;
		try {// web项目从session中取当前登录人ID
			final Class<?> clazz = Class.forName("com.lichkin.framework.springboot.web.utils.LKSessionUserUtilsOnSpring");
			final Method method = clazz.getMethod("getLoginId");
			loginId = (String) method.invoke(clazz);
		} catch (final Exception exception) {// TODO 非web项目取当前任务ID
			loginId = "GUEST";
		}
		return loginId;
	}

}
