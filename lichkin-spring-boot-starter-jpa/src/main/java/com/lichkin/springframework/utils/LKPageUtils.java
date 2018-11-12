package com.lichkin.springframework.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.lichkin.framework.defines.func.BeanConverter;
import com.lichkin.framework.utils.LKListUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 分页工具类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LKPageUtils {

	/**
	 * 将源分页数据转换为目标分页数据
	 * @param <Source> 源对象泛型
	 * @param <Target> 目标对象泛型
	 * @param pageSource 源分页数据
	 * @param converter 转换器
	 * @return 目标分页数据
	 */
	public static <Source, Target> Page<Target> convert(Page<Source> pageSource, BeanConverter<Source, Target> converter) {
		if (pageSource == null) {
			return null;
		}

		return new PageImpl<>(LKListUtils.convert(pageSource.getContent(), converter), pageSource.getPageable(), pageSource.getTotalElements());
	}

}
