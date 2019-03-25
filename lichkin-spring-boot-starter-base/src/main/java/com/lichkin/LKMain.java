package com.lichkin;

import static com.lichkin.framework.log.log4j2.LKLog4j2Log.MAIN_ARG_LOG_LEVEL_IO;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.MAIN_ARG_LOG_LEVEL_NET;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.MAIN_ARG_LOG_LEVEL_ORG;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.MAIN_ARG_LOG_LEVEL_SYSTEM;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.MAIN_ARG_LOG_TAG;
import static com.lichkin.framework.log.log4j2.LKLog4j2Log.VALUE_LOG_LEVEL_IO;
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
import org.springframework.util.ClassUtils;

import com.lichkin.framework.defines.LKConfigStatics;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.log.log4j2.LKLog4j2Initializer;
import com.lichkin.framework.utils.LKArrayUtils;
import com.lichkin.framework.utils.LKRandomUtils;
import com.lichkin.framework.utils.LKStringUtils;

/**
 * 项目主类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@SpringBootApplication // 自动配置
public class LKMain {

	private static final String PROFILES_ACTIVE = "--spring.profiles.active=";

	private static final String DEFAULT_LOCALE = "--lichkin.locale.default=";

	private static final String IMPLEMENTED_LOCALE_ARR = "--lichkin.locale.implemented=";

	private static final String SYSTEM_TAG = "--lichkin.system.tag=";

	private static final String SYSTEM_NAME = "--lichkin.system.name=";

	private static final String SYSTEM_DEBUG = "--lichkin.system.debug=";

	private static final String WEB_DEBUG = "--lichkin.web.debug=";

	private static final String WEB_COMPRESS = "--lichkin.web.compress=";

	private static final String WEB_ADMIN_DEBUG = "--lichkin.web.admin.debug=";

	private static final String WEB_SERVER_PORT = "--server.port=";

	private static final String WEB_CONTEXT_PATH = "--server.servlet.context-path=";

	private static final String SOCKET_SERVER_CONFIG_IDX = "--socket.server.config.idx=";

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
		String profilesActive = LKConfigStatics.PROFILES_ACTIVE;
		String logTag = VALUE_LOG_TAG;
		String logLevelSystem = VALUE_LOG_LEVEL_SYSTEM;
		String logLevelOrg = VALUE_LOG_LEVEL_ORG;
		String logLevelNet = VALUE_LOG_LEVEL_NET;
		String logLevelIo = VALUE_LOG_LEVEL_IO;
		String defaultLocale = LKConfigStatics.DEFAULT_LOCALE.toString();
		String implementedLocaleArr = LKArrayUtils.toString(LKConfigStatics.IMPLEMENTED_LOCALE_ARR);
		String systemTag = LKConfigStatics.SYSTEM_TAG;
		String systemName = LKConfigStatics.SYSTEM_NAME;
		String systemDebug = String.valueOf(LKConfigStatics.SYSTEM_DEBUG);
		String webDebug = String.valueOf(LKConfigStatics.WEB_DEBUG);
		String webCompress = String.valueOf(LKConfigStatics.WEB_COMPRESS);
		String webContextPath = String.valueOf(LKConfigStatics.WEB_CONTEXT_PATH);
		String webServerPort = String.valueOf(LKConfigStatics.WEB_SERVER_PORT);
		String webAdminDebug = String.valueOf(LKConfigStatics.WEB_ADMIN_DEBUG);
		String socketServerConfigIdx = String.valueOf(LKConfigStatics.SOCKET_SERVER_CONFIG_IDX);

		if (ArrayUtils.isNotEmpty(args)) {
			// 遍历主参数
			for (int i = args.length - 1; i >= 0; i--) {
				final String arg = args[i];
				if (StringUtils.startsWith(arg, PROFILES_ACTIVE)) {
					profilesActive = arg.substring(PROFILES_ACTIVE.length());

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_TAG)) {
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
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_LEVEL_IO)) {
					logLevelIo = arg.substring(MAIN_ARG_LOG_LEVEL_IO.length());

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
				} else if (StringUtils.startsWith(arg, WEB_COMPRESS)) {
					LKConfigStatics.WEB_COMPRESS = Boolean.parseBoolean(arg.substring(WEB_COMPRESS.length()));

					webCompress = String.valueOf(LKConfigStatics.WEB_COMPRESS);

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, WEB_CONTEXT_PATH)) {
					LKConfigStatics.WEB_CONTEXT_PATH = arg.substring(WEB_CONTEXT_PATH.length());

					webContextPath = LKStringUtils.toStandardPath(LKConfigStatics.WEB_CONTEXT_PATH);

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, WEB_SERVER_PORT)) {
					LKConfigStatics.WEB_SERVER_PORT = arg.substring(WEB_SERVER_PORT.length());

					webServerPort = LKConfigStatics.WEB_SERVER_PORT;

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, WEB_ADMIN_DEBUG)) {
					LKConfigStatics.WEB_ADMIN_DEBUG = Boolean.parseBoolean(arg.substring(WEB_ADMIN_DEBUG.length()));

					webAdminDebug = String.valueOf(LKConfigStatics.WEB_ADMIN_DEBUG);

					args = ArrayUtils.remove(args, i);
				} else if (StringUtils.startsWith(arg, SOCKET_SERVER_CONFIG_IDX)) {
					LKConfigStatics.SOCKET_SERVER_CONFIG_IDX = Integer.parseInt(arg.substring(SOCKET_SERVER_CONFIG_IDX.length()));

					socketServerConfigIdx = String.valueOf(LKConfigStatics.SOCKET_SERVER_CONFIG_IDX);

					args = ArrayUtils.remove(args, i);
				}
			}
		}

		// 重构MainArgs
		args = ArrayUtils.add(args, PROFILES_ACTIVE + profilesActive);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_TAG + logTag);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_SYSTEM + logLevelSystem);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_ORG + logLevelOrg);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_NET + logLevelNet);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_IO + logLevelNet);
		args = ArrayUtils.add(args, DEFAULT_LOCALE + defaultLocale);
		args = ArrayUtils.add(args, IMPLEMENTED_LOCALE_ARR + implementedLocaleArr);
		args = ArrayUtils.add(args, SYSTEM_TAG + systemTag);
		args = ArrayUtils.add(args, SYSTEM_NAME + systemName);
		args = ArrayUtils.add(args, SYSTEM_DEBUG + systemDebug);

		// ADMIN项目特有参数
		if (ClassUtils.isPresent("com.lichkin.application.services.extend.impl.XAdminLoginService", null)) {
			args = ArrayUtils.add(args, WEB_ADMIN_DEBUG + webAdminDebug);

			if (webContextPath.equals("")) {
				webContextPath = "/ADMIN";
			}
			if (webServerPort.equals("33333")) {
				webServerPort = "18888";
			}
		}

		// EMPLOYEE项目特有参数
		if (ClassUtils.isPresent("com.lichkin.application.services.extend.impl.XEmployeeLoginService", null)) {
			if (webContextPath.equals("")) {
				webContextPath = "/EMPLOYEE";
			}
			if (webServerPort.equals("33333")) {
				webServerPort = "28888";
			}
		}

		// USER项目特有参数
		if (ClassUtils.isPresent("com.lichkin.application.services.extend.impl.XUserLoginService", null)) {
			if (webContextPath.equals("")) {
				webContextPath = "/USER";
			}
			if (webServerPort.equals("33333")) {
				webServerPort = "38888";
			}
		}

		// WEB项目特有参数
		if (ClassUtils.isPresent("org.springframework.web.context.ConfigurableWebApplicationContext", null)) {
			args = ArrayUtils.add(args, WEB_DEBUG + webDebug);
			args = ArrayUtils.add(args, WEB_COMPRESS + webCompress);
			args = ArrayUtils.add(args, WEB_CONTEXT_PATH + webContextPath);
			args = ArrayUtils.add(args, WEB_SERVER_PORT + webServerPort);
		}

		// Socket服务端项目特有参数
		if (ClassUtils.isPresent("com.lichkin.framework.socket.LKSocketServer", null)) {
			args = ArrayUtils.add(args, SOCKET_SERVER_CONFIG_IDX + socketServerConfigIdx);
		}

		LOGGER.warn("systemId[%s] -> main args after analyzed. %s", SYSTEM_ID, ArrayUtils.toString(args));

		// 配置log4j2的参数
		LKLog4j2Initializer.setMainArguments(logTag, logLevelSystem, logLevelOrg, logLevelNet, logLevelIo);

		// 创建应用
		final SpringApplication app = new SpringApplication(LKMain.class);

		// 启动应用
		app.run(args);
	}

}
