package com.lichkin.springframework.web.configs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lichkin.LKMain;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.utils.LKStringUtils;
import com.lichkin.framework.web.annotations.LKController4Datas;
import com.lichkin.framework.web.annotations.LKController4Pages;
import com.lichkin.springframework.web.beans.LKPage;

/**
 * 应用事件监听器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Component
public class LKApplicationListener4ServletWebServerInitializedEvent implements ApplicationListener<ServletWebServerInitializedEvent> {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKApplicationListener4ServletWebServerInitializedEvent.class);

	/** 首页是否已经实现 */
	private boolean indexPageImplemented = false;


	@Override
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		LOGGER.info("onApplicationEvent -> ServletWebServerInitializedEvent");

		// 验证框架约定
		ApplicationContext context = event.getApplicationContext();

		// 验证服务类
		// 0、在规定的包中。
		Map<String, Object> services = context.getBeansWithAnnotation(Service.class);
		for (Entry<String, Object> entry : services.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			Class<?> beanClass = value.getClass();
			String beanName = beanClass.getName();
			LOGGER.info(String.format("check service[%s] managed by key[%s]", beanName, key));

			// 验证包
			checkPackage(LKFrameworkStatics.SERVICE_PACKAGES, beanName);
		}

		// 验证控制器类
		// 0、在规定的包中。
		// 1、所有约定的映射值的方法都必须在标注有约定的注解的类中。
		// 2、所有标注有约定注解的类中的所有方法必须为约定的映射。
		if (LKMain.ENV_WEB) {
			Map<String, Object> controllers = context.getBeansWithAnnotation(Controller.class);
			for (Entry<String, Object> entry : controllers.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				Class<?> beanClass = value.getClass();
				String beanName = beanClass.getName();
				if (!beanName.startsWith("org.springframework") && !beanClass.getSuperclass().equals(LKErrorController.class)) {
					LOGGER.info(String.format("check controller[%s] managed by key[%s]", beanName, key));

					// 验证包
					checkPackage(LKFrameworkStatics.CONTROLLER_PACKAGES, beanName);

					// 验证类注解
					RequestMapping requestMapping = beanClass.getAnnotation(RequestMapping.class);
					if (requestMapping == null) {
						throw new LKFrameworkException(String.format("controller[%s]. must be annotated with [%s].", beanName, RequestMapping.class.getName()));
					}
					String[] controllerMappings = requestMapping.value();
					Boolean api = null;
					if (controllerMappings.length != 1) {
						for (String controllerMapping : controllerMappings) {
							if (controllerMapping.startsWith(LKFrameworkStatics.WEB_MAPPING_API)) {
								api = true;
							} else {
								api = false;
								break;
							}
						}
						if (!api) {
							throw new LKFrameworkException(String.format("controller[%s] annotation[%s][values] must be only one.", beanName, RequestMapping.class.getName()));
						}
					}

					// 取得类上的映射值
					String controllerMapping = LKStringUtils.toStandardPath(controllerMappings[0]);

					// 验证API数据请求
					if (controllerMapping.startsWith(LKFrameworkStatics.WEB_MAPPING_API)) {
						checkApi(beanClass, beanName, controllerMapping);
						continue;
					}

					// 页面请求控制器
					Annotation pagesAnnotation = LKClassUtils.deepGetAnnotation(beanClass, LKController4Pages.class.getName());
					// 数据请求控制器
					Annotation datasAnnotation = LKClassUtils.deepGetAnnotation(beanClass, LKController4Datas.class.getName());

					// 验证方法注解
					Method[] methods = beanClass.getDeclaredMethods();
					for (Method method : methods) {
						String methodName = method.getName();
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(String.format("check method[%s]", methodName));
						}

						// 页面请求返回值验证
						if ((pagesAnnotation != null) && !method.getReturnType().equals(LKPage.class)) {
							throw new LKFrameworkException(String.format("check controller[%s], method[%s]. the class annotated with [%s] can only contains method with return value as type [%s].", beanName, methodName, LKController4Pages.class.getName(), LKPage.class.getName()));
						}

						// 支持的两种注解
						GetMapping getMapping = method.getAnnotation(GetMapping.class);
						PostMapping postMapping = method.getAnnotation(PostMapping.class);

						if (getMapping == null) {
							if (postMapping == null) {
								throw new LKFrameworkException(String.format("controller[%s], method[%s]. must be annotated with [%s] or [%s].", beanName, methodName, GetMapping.class.getName(), PostMapping.class.getName()));
							} else {
								String[] mappings = postMapping.value();
								for (String mapping : mappings) {
									checkMapping(true, beanClass, method, controllerMapping, beanName, methodName, mapping, pagesAnnotation, datasAnnotation);
								}
							}
						} else {
							if (postMapping == null) {
								String[] mappings = getMapping.value();
								for (String mapping : mappings) {
									checkMapping(false, beanClass, method, controllerMapping, beanName, methodName, mapping, pagesAnnotation, datasAnnotation);
								}
							} else {
								throw new LKFrameworkException(String.format("controller[%s], method[%s]. must be annotated with [%s] or [%s].", beanName, methodName, GetMapping.class.getName(), PostMapping.class.getName()));
							}
						}
					}
				}
			}
			if (!indexPageImplemented) {
				throw new LKFrameworkException("mapping [/index" + LKFrameworkStatics.WEB_MAPPING_PAGES + "] no found.");
			}
		}
	}


	/**
	 * 校验API请求
	 */
	private void checkApi(Class<?> beanClass, String beanName, String controllerMapping) {
		boolean allow = false;
		List<Class<?>> supperClasses = LKClassUtils.getAllExtendsClasses(beanClass);
		for (Class<?> cls : supperClasses) {
			if (cls.getName().equals("com.lichkin.springframework.controllers.LKStandardApiController")) {
				allow = true;
				break;
			}
		}
		if (!allow) {
			throw new LKFrameworkException(String.format("controller[%s] annotation[%s] mapping[%s] starts with [%s] must extends [%s].", beanName, RequestMapping.class.getName(), controllerMapping, LKFrameworkStatics.WEB_MAPPING_API, "com.lichkin.springframework.controllers.LKStandardApiController"));
		}
	}


	/**
	 * 约定校验
	 */
	private void checkMapping(Boolean isPost, Class<?> beanClass, Method method, String controllerMapping, String beanName, String methodName, String mapping, Annotation pagesAnnotation, Annotation datasAnnotation) {
		String fullMapping = LKStringUtils.joinPath(controllerMapping, mapping);
		if (fullMapping.equals("/index" + LKFrameworkStatics.WEB_MAPPING_PAGES) && !isPost) {
			indexPageImplemented = true;
		}
		if (fullMapping.startsWith(LKFrameworkStatics.WEB_MAPPING_API)) {
			checkApi(beanClass, beanName, controllerMapping);
			return;
		}

		// 请求参数验证
		checkParameter: if ((pagesAnnotation != null) || (datasAnnotation != null)) {
			Parameter[] parameters = method.getParameters();
			if (parameters.length == 0) {
				break checkParameter;
			}
			if (parameters.length != 1) {
				throw new LKFrameworkException(String.format("controller[%s], method[%s]. must be one parameter.", beanName, methodName));
			}
			boolean hasRequestBodyAnnotation = false;
			Annotation[] parameterAnnotations = method.getParameterAnnotations()[0];
			for (Annotation parameterAnnotation : parameterAnnotations) {
				if (parameterAnnotation.annotationType().equals(RequestBody.class)) {
					hasRequestBodyAnnotation = true;
				}
			}
			if (isPost) {
				if (!hasRequestBodyAnnotation) {
					throw new LKFrameworkException(String.format("controller[%s], method[%s]. must be one parameter annotated with [%s].", beanName, methodName, RequestBody.class.getName()));
				}
			} else {
				if ((pagesAnnotation != null) && hasRequestBodyAnnotation) {
					throw new LKFrameworkException(String.format("controller[%s], method[%s]. must be one parameter and not annotated with [%s].", beanName, methodName, RequestBody.class.getName()));
				}
			}
		}

		// 类上标注了约定的注解，但是方法映射的不是约定值。
		if ((pagesAnnotation != null) && !mapping.endsWith(LKFrameworkStatics.WEB_MAPPING_PAGES)) {
			throw new LKFrameworkException(String.format("check controller[%s], method[%s], mapping[%s]. the class annotated with [%s] can only contains method with mapping that ends with [%s].", beanName, methodName, mapping, LKController4Pages.class.getName(), LKFrameworkStatics.WEB_MAPPING_PAGES));
		}
		if ((datasAnnotation != null) && !mapping.endsWith(LKFrameworkStatics.WEB_MAPPING_DATAS)) {
			throw new LKFrameworkException(String.format("check controller[%s], method[%s], mapping[%s]. the class annotated with [%s] can only contains method with mapping that ends with [%s].", beanName, methodName, mapping, LKController4Datas.class.getName(), LKFrameworkStatics.WEB_MAPPING_DATAS));
		}
		// 符合映射约定值，但是没有在类上标注约定的注解。
		if (mapping.endsWith(LKFrameworkStatics.WEB_MAPPING_PAGES) && (pagesAnnotation == null)) {
			throw new LKFrameworkException(String.format("check controller[%s], method[%s], mapping[%s]. method must in the class annotated with [%s].", beanName, methodName, mapping, LKController4Pages.class.getName()));
		}
		if (mapping.endsWith(LKFrameworkStatics.WEB_MAPPING_DATAS) && (datasAnnotation == null) && isPost) {
			throw new LKFrameworkException(String.format("check controller[%s], method[%s], mapping[%s]. method must in the class annotated with [%s].", beanName, methodName, mapping, LKController4Datas.class.getName()));
		}
	}


	/**
	 * 验证包名
	 * @param definedPackageName 定义的包名
	 * @param className 待验证的类名字
	 */
	private static void checkPackage(String definedPackageName, String className) {
		String[] controllerPackages = definedPackageName.split("\\*\\*");
		if (controllerPackages.length == 2) {
			if (className.startsWith(controllerPackages[0]) && className.substring(0, className.lastIndexOf(".")).endsWith(controllerPackages[1])) {
				return;
			}
		}
		throw new LKFrameworkException(String.format("package check failed for bean[%s] with definedPackageName[%s]", className, definedPackageName));
	}

}
