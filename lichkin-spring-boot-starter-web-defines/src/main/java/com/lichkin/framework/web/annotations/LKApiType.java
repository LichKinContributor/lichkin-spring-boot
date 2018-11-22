package com.lichkin.framework.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lichkin.framework.web.enums.ApiType;

/**
 * <pre>
 * 在控制器类上配置该注解，以标明该接口类型。
 * </pre>
 *
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LKApiType {

	ApiType apiType();

}
