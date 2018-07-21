package com.lichkin;

import static com.lichkin.framework.log.log4j2.LKLog4j2Log.MAIN_ARG_LOG_LEVEL_NET;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.MAIN_ARG_LOG_LEVEL_ORG;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.MAIN_ARG_LOG_LEVEL_SYSTEM;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.MAIN_ARG_LOG_TAG;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.VALUE_LOG_LEVEL_NET;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.VALUE_LOG_LEVEL_ORG;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.VALUE_LOG_LEVEL_SYSTEM;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.VALUE_LOG_TAG;

import java.io.IOException;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.log.log4j2.LKLog4j2Initializer;
import com.lichkin.framework.utils.LKArrayUtils;
import com.lichkin.framework.utils.LKRandomUtils;

/**
 * 项目主类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@SpringBootApplication // 自动配置
public class LKMain {

	private static final String DEFAULT_LOCALE = "--lichkin.locale.default=";

	private static final String IMPLEMENTED_LOCALE_ARR = "--lichkin.locale.implemented=";

	private static final String SYSTEM_TAG = "--lichkin.system.tag=";

	private static final String SYSTEM_NAME = "--lichkin.system.name=";

	private static final String SYSTEM_DEBUG = "--lichkin.system.debug=";

	private static final String WEB_DEBUG = "--lichkin.web.debug=";

	private static final String WEB_ADMIN_DEBUG = "--lichkin.web.admin.debug=";

	static {
		LKLog4j2Initializer.init();
	}

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKMain.class);

	/** 系统ID */
	public static final String SYSTEM_ID = LKRandomUtils.create(32);


	/**
	 * 主函数
	 * @param args 入口参数
	 * @throws IOException 读取配置文件时可能抛出异常
	 */
	public static void main(String[] args) throws IOException {
		LOGGER.warn("systemId[%s] -> main args before analyze. %s", SYSTEM_ID, ArrayUtils.toString(args));

		// 默认配置属性
		String logTag = VALUE_LOG_TAG;
		String logLevelSystem = VALUE_LOG_LEVEL_SYSTEM;
		String logLevelOrg = VALUE_LOG_LEVEL_ORG;
		String logLevelNet = VALUE_LOG_LEVEL_NET;
		String defaultLocale = LKConfigStatics.DEFAULT_LOCALE.toString();
		String implementedLocaleArr = LKArrayUtils.toString(LKConfigStatics.IMPLEMENTED_LOCALE_ARR);
		String systemTag = LKConfigStatics.SYSTEM_TAG;
		String systemName = LKConfigStatics.SYSTEM_NAME;
		String systemDebug = String.valueOf(LKConfigStatics.SYSTEM_DEBUG);
		String webDebug = String.valueOf(LKConfigStatics.WEB_DEBUG);
		String webAdminDebug = String.valueOf(LKConfigStatics.WEB_ADMIN_DEBUG);

		if (ArrayUtils.isNotEmpty(args)) {
			// 遍历主参数
			for (int i = args.length - 1; i >= 0; i--) {
				final String arg = args[i];
				if (StringUtils.startsWith(arg, MAIN_ARG_LOG_TAG)) {
					logTag = arg.substring(MAIN_ARG_LOG_TAG.length());

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_LEVEL_SYSTEM)) {
					logLevelSystem = arg.substring(MAIN_ARG_LOG_LEVEL_SYSTEM.length());

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_LEVEL_ORG)) {
					logLevelOrg = arg.substring(MAIN_ARG_LOG_LEVEL_ORG.length());

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_LEVEL_NET)) {
					logLevelNet = arg.substring(MAIN_ARG_LOG_LEVEL_NET.length());

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, DEFAULT_LOCALE)) {
					String[] strs = arg.substring(DEFAULT_LOCALE.length()).split("_");
					if (strs.length == 1) {
						LKConfigStatics.DEFAULT_LOCALE = new Locale(strs[0]);
					} else {
						LKConfigStatics.DEFAULT_LOCALE = new Locale(strs[0], strs[1]);
					}

					defaultLocale = LKConfigStatics.DEFAULT_LOCALE.toString();

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, IMPLEMENTED_LOCALE_ARR)) {
					String[] impls = arg.substring(IMPLEMENTED_LOCALE_ARR.length()).split(",");
					Locale[] locales = new Locale[impls.length];
					for (int j = 0; j < impls.length; j++) {
						String[] strs = impls[j].split("_");
						if (strs.length == 1) {
							locales[j] = new Locale(strs[0]);
						} else {
							locales[j] = new Locale(strs[0], strs[1]);
						}
					}
					LKConfigStatics.IMPLEMENTED_LOCALE_ARR = locales;

					implementedLocaleArr = LKArrayUtils.toString(LKConfigStatics.IMPLEMENTED_LOCALE_ARR);

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, SYSTEM_TAG)) {
					LKConfigStatics.SYSTEM_TAG = arg.substring(SYSTEM_TAG.length());

					systemTag = LKConfigStatics.SYSTEM_TAG;

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, SYSTEM_NAME)) {
					LKConfigStatics.SYSTEM_NAME = arg.substring(SYSTEM_NAME.length());

					systemName = LKConfigStatics.SYSTEM_NAME;

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, SYSTEM_DEBUG)) {
					LKConfigStatics.SYSTEM_DEBUG = Boolean.parseBoolean(arg.substring(SYSTEM_DEBUG.length()));

					systemDebug = String.valueOf(LKConfigStatics.SYSTEM_DEBUG);

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, WEB_DEBUG)) {
					LKConfigStatics.WEB_DEBUG = Boolean.parseBoolean(arg.substring(WEB_DEBUG.length()));

					webDebug = String.valueOf(LKConfigStatics.WEB_DEBUG);

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, WEB_ADMIN_DEBUG)) {
					LKConfigStatics.WEB_ADMIN_DEBUG = Boolean.parseBoolean(arg.substring(WEB_ADMIN_DEBUG.length()));

					webAdminDebug = String.valueOf(LKConfigStatics.WEB_ADMIN_DEBUG);

					args = ArrayUtils.remove(args, i);
				}
			}
		}

		// 重构MainArgs
		args = ArrayUtils.add(args, MAIN_ARG_LOG_TAG + logTag);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_SYSTEM + logLevelSystem);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_ORG + logLevelOrg);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_NET + logLevelNet);
		args = ArrayUtils.add(args, DEFAULT_LOCALE + defaultLocale);
		args = ArrayUtils.add(args, IMPLEMENTED_LOCALE_ARR + implementedLocaleArr);
		args = ArrayUtils.add(args, SYSTEM_TAG + systemTag);
		args = ArrayUtils.add(args, SYSTEM_NAME + systemName);
		args = ArrayUtils.add(args, SYSTEM_DEBUG + systemDebug);
		args = ArrayUtils.add(args, WEB_DEBUG + webDebug);
		args = ArrayUtils.add(args, WEB_ADMIN_DEBUG + webAdminDebug);

		LOGGER.warn("systemId[%s] -> main args after analyzed. %s", SYSTEM_ID, ArrayUtils.toString(args));

		// 配置log4j2的参数
		LKLog4j2Initializer.setMainArguments(logTag, logLevelSystem, logLevelOrg, logLevelNet);

		// 创建应用
		final SpringApplication app = new SpringApplication(LKMain.class);

		// 启动应用
		app.run(args);
	}

}
