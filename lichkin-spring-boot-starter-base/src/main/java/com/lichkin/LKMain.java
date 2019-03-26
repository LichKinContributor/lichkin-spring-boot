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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

	private static final String MAIN_ARG_PROFILES_ACTIVE = "--spring.profiles.active=";

	private static final String MAIN_ARG_DEFAULT_LOCALE = "--lichkin.locale.default=";

	private static final String MAIN_ARG_IMPLEMENTED_LOCALE_ARR = "--lichkin.locale.implemented=";

	private static final String MAIN_ARG_SYSTEM_DEBUG = "--lichkin.system.debug=";

	private static final String MAIN_ARG_SYSTEM_TAG = "--lichkin.system.tag=";

	private static final String MAIN_ARG_SYSTEM_NAME = "--lichkin.system.name=";

	private static final String MAIN_ARG_WEB_DEBUG = "--lichkin.web.debug=";

	private static final String MAIN_ARG_WEB_COMPRESS = "--lichkin.web.compress=";

	private static final String MAIN_ARG_WEB_CONTEXT_PATH = "--server.servlet.context-path=";

	private static final String MAIN_ARG_WEB_SERVER_PORT = "--server.port=";

	private static final String MAIN_ARG_WEB_ADMIN_DEBUG = "--lichkin.web.admin.debug=";

	private static final String MAIN_ARG_SOCKET_SERVER_CONFIG_IDX = "--socket.server.config.idx=";

	static {
		LKLog4j2Initializer.init();
	}

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKMain.class);

	/** 系统ID */
	public static final String SYSTEM_ID = LKRandomUtils.create(32);

	/** 配置项 */
	private static final Map<String, String> configs = new HashMap<>();

	static {
		// 激活配置
		configs.put(MAIN_ARG_PROFILES_ACTIVE, LKConfigStatics.PROFILES_ACTIVE);
		// 日志配置
		configs.put(MAIN_ARG_LOG_TAG, VALUE_LOG_TAG);
		configs.put(MAIN_ARG_LOG_LEVEL_SYSTEM, VALUE_LOG_LEVEL_SYSTEM);
		configs.put(MAIN_ARG_LOG_LEVEL_ORG, VALUE_LOG_LEVEL_ORG);
		configs.put(MAIN_ARG_LOG_LEVEL_NET, VALUE_LOG_LEVEL_NET);
		configs.put(MAIN_ARG_LOG_LEVEL_IO, VALUE_LOG_LEVEL_IO);
		// 国际化配置
		configs.put(MAIN_ARG_DEFAULT_LOCALE, LKConfigStatics.DEFAULT_LOCALE.toString());
		configs.put(MAIN_ARG_IMPLEMENTED_LOCALE_ARR, LKArrayUtils.toString(LKConfigStatics.IMPLEMENTED_LOCALE_ARR));
		// 系统配置
		configs.put(MAIN_ARG_SYSTEM_TAG, LKConfigStatics.SYSTEM_TAG);
		configs.put(MAIN_ARG_SYSTEM_NAME, LKConfigStatics.SYSTEM_NAME);
		configs.put(MAIN_ARG_SYSTEM_DEBUG, String.valueOf(LKConfigStatics.SYSTEM_DEBUG));
		// WEB项目配置
		configs.put(MAIN_ARG_WEB_DEBUG, String.valueOf(LKConfigStatics.WEB_DEBUG));
		configs.put(MAIN_ARG_WEB_COMPRESS, String.valueOf(LKConfigStatics.WEB_COMPRESS));
		configs.put(MAIN_ARG_WEB_CONTEXT_PATH, LKConfigStatics.WEB_CONTEXT_PATH);
		configs.put(MAIN_ARG_WEB_SERVER_PORT, LKConfigStatics.WEB_SERVER_PORT);
		// ADMIN项目配置
		configs.put(MAIN_ARG_WEB_ADMIN_DEBUG, String.valueOf(LKConfigStatics.WEB_ADMIN_DEBUG));
		// Socket项目配置
		configs.put(MAIN_ARG_SOCKET_SERVER_CONFIG_IDX, String.valueOf(LKConfigStatics.SOCKET_SERVER_CONFIG_IDX));
	}


	/**
	 * 激活配置
	 * @param profile 配置
	 */
	private static void activeProfile(String profile) {
		switch (profile) {
			case "production":// 生产环境
				configs.put(MAIN_ARG_LOG_LEVEL_SYSTEM, "info");
				configs.put(MAIN_ARG_LOG_LEVEL_ORG, "warn");
				configs.put(MAIN_ARG_LOG_LEVEL_NET, "warn");
				configs.put(MAIN_ARG_LOG_LEVEL_IO, "warn");
				configSystemDebug("false");
				configWebDebug("false");
				configWebCompress("true");
				configWebAdminDebug("false");
			break;
			case "integration":// 集成环境
				configSystemDebug("false");
				configWebDebug("false");
				configWebCompress("true");
			break;
			case "development":// 开发环境
			break;
			default:// 其它环境
			break;
		}
	}


	/**
	 * 主函数
	 * @param args 入口参数
	 * @throws IOException 读取配置文件时可能抛出异常
	 */
	public static void main(String[] args) throws IOException {
		LOGGER.warn("systemId[%s] -> main args before analyze. %s", SYSTEM_ID, ArrayUtils.toString(args));

		// 激活配置
		for (String arg : args) {
			if (StringUtils.startsWith(arg, MAIN_ARG_PROFILES_ACTIVE)) {
				String[] profiles = arg.substring(MAIN_ARG_PROFILES_ACTIVE.length()).split(",");
				for (String profile : profiles) {
					activeProfile(profile);
				}
				break;
			}
		}

		// 默认配置属性
		if (ArrayUtils.isNotEmpty(args)) {
			// 遍历主参数
			for (int i = args.length - 1; i >= 0; i--) {
				final String arg = args[i];
				if (StringUtils.startsWith(arg, MAIN_ARG_PROFILES_ACTIVE)) {
					configs.put(MAIN_ARG_PROFILES_ACTIVE, arg.substring(MAIN_ARG_PROFILES_ACTIVE.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_TAG)) {
					configs.put(MAIN_ARG_LOG_TAG, arg.substring(MAIN_ARG_LOG_TAG.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_LEVEL_SYSTEM)) {
					configs.put(MAIN_ARG_LOG_LEVEL_SYSTEM, arg.substring(MAIN_ARG_LOG_LEVEL_SYSTEM.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_LEVEL_ORG)) {
					configs.put(MAIN_ARG_LOG_LEVEL_ORG, arg.substring(MAIN_ARG_LOG_LEVEL_ORG.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_LEVEL_NET)) {
					configs.put(MAIN_ARG_LOG_LEVEL_NET, arg.substring(MAIN_ARG_LOG_LEVEL_NET.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_LOG_LEVEL_IO)) {
					configs.put(MAIN_ARG_LOG_LEVEL_IO, arg.substring(MAIN_ARG_LOG_LEVEL_IO.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_DEFAULT_LOCALE)) {
					configDefaultLocale(arg.substring(MAIN_ARG_DEFAULT_LOCALE.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_IMPLEMENTED_LOCALE_ARR)) {
					configImplementedLocaleArr(arg.substring(MAIN_ARG_IMPLEMENTED_LOCALE_ARR.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_SYSTEM_TAG)) {
					configSystemTag(arg.substring(MAIN_ARG_SYSTEM_TAG.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_SYSTEM_NAME)) {
					configSystemName(arg.substring(MAIN_ARG_SYSTEM_NAME.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_SYSTEM_DEBUG)) {
					configSystemDebug(arg.substring(MAIN_ARG_SYSTEM_DEBUG.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_WEB_DEBUG)) {
					configWebDebug(arg.substring(MAIN_ARG_WEB_DEBUG.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_WEB_COMPRESS)) {
					configWebCompress(arg.substring(MAIN_ARG_WEB_COMPRESS.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_WEB_CONTEXT_PATH)) {
					configWebContextPath(arg.substring(MAIN_ARG_WEB_CONTEXT_PATH.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_WEB_SERVER_PORT)) {
					configWebServerPort(arg.substring(MAIN_ARG_WEB_SERVER_PORT.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_WEB_ADMIN_DEBUG)) {
					configWebAdminDebug(arg.substring(MAIN_ARG_WEB_ADMIN_DEBUG.length()));
				} else if (StringUtils.startsWith(arg, MAIN_ARG_SOCKET_SERVER_CONFIG_IDX)) {
					configSocketServerConfigIdx(arg.substring(MAIN_ARG_SOCKET_SERVER_CONFIG_IDX.length()));
				} else {
					continue;
				}
				args = ArrayUtils.remove(args, i);
			}
		}

		// 重构MainArgs
		args = ArrayUtils.add(args, MAIN_ARG_PROFILES_ACTIVE + configs.get(MAIN_ARG_PROFILES_ACTIVE));
		args = ArrayUtils.add(args, MAIN_ARG_LOG_TAG + configs.get(MAIN_ARG_LOG_TAG));
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_SYSTEM + configs.get(MAIN_ARG_LOG_LEVEL_SYSTEM));
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_ORG + configs.get(MAIN_ARG_LOG_LEVEL_ORG));
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_NET + configs.get(MAIN_ARG_LOG_LEVEL_NET));
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_IO + configs.get(MAIN_ARG_LOG_LEVEL_IO));
		args = ArrayUtils.add(args, MAIN_ARG_DEFAULT_LOCALE + configs.get(MAIN_ARG_DEFAULT_LOCALE));
		args = ArrayUtils.add(args, MAIN_ARG_IMPLEMENTED_LOCALE_ARR + configs.get(MAIN_ARG_IMPLEMENTED_LOCALE_ARR));
		args = ArrayUtils.add(args, MAIN_ARG_SYSTEM_TAG + configs.get(MAIN_ARG_SYSTEM_TAG));
		args = ArrayUtils.add(args, MAIN_ARG_SYSTEM_NAME + configs.get(MAIN_ARG_SYSTEM_NAME));
		args = ArrayUtils.add(args, MAIN_ARG_SYSTEM_DEBUG + configs.get(MAIN_ARG_SYSTEM_DEBUG));

		String webContextPath = configs.get(MAIN_ARG_WEB_CONTEXT_PATH);
		String webServerPort = configs.get(MAIN_ARG_WEB_SERVER_PORT);

		// ADMIN项目特有参数
		if (ClassUtils.isPresent("com.lichkin.application.services.extend.impl.XAdminLoginService", null)) {
			args = ArrayUtils.add(args, MAIN_ARG_WEB_ADMIN_DEBUG + configs.get(MAIN_ARG_WEB_ADMIN_DEBUG));

			if (StringUtils.isBlank(webContextPath) || webContextPath.equals(LKConfigStatics.DEFAULT_VALUE_WEB_CONTEXT_PATH)) {
				configs.put(MAIN_ARG_WEB_CONTEXT_PATH, "/ADMIN");
			}
			if (StringUtils.isBlank(webServerPort) || webServerPort.equals(LKConfigStatics.DEFAULT_VALUE_WEB_SERVER_PORT)) {
				configs.put(MAIN_ARG_WEB_SERVER_PORT, "18888");
			}
		}

		// EMPLOYEE项目特有参数
		if (ClassUtils.isPresent("com.lichkin.application.services.extend.impl.XEmployeeLoginService", null)) {
			if (StringUtils.isBlank(webContextPath) || webContextPath.equals(LKConfigStatics.DEFAULT_VALUE_WEB_CONTEXT_PATH)) {
				configs.put(MAIN_ARG_WEB_CONTEXT_PATH, "/EMPLOYEE");
			}
			if (StringUtils.isBlank(webServerPort) || webServerPort.equals(LKConfigStatics.DEFAULT_VALUE_WEB_SERVER_PORT)) {
				configs.put(MAIN_ARG_WEB_SERVER_PORT, "28888");
			}
		}

		// USER项目特有参数
		if (ClassUtils.isPresent("com.lichkin.application.services.extend.impl.XUserLoginService", null)) {
			if (StringUtils.isBlank(webContextPath) || webContextPath.equals(LKConfigStatics.DEFAULT_VALUE_WEB_CONTEXT_PATH)) {
				configs.put(MAIN_ARG_WEB_CONTEXT_PATH, "/USER");
			}
			if (StringUtils.isBlank(webServerPort) || webServerPort.equals(LKConfigStatics.DEFAULT_VALUE_WEB_SERVER_PORT)) {
				configs.put(MAIN_ARG_WEB_SERVER_PORT, "38888");
			}
		}

		// WEB项目特有参数
		if (ClassUtils.isPresent("org.springframework.web.context.ConfigurableWebApplicationContext", null)) {
			args = ArrayUtils.add(args, MAIN_ARG_WEB_DEBUG + configs.get(MAIN_ARG_WEB_DEBUG));
			args = ArrayUtils.add(args, MAIN_ARG_WEB_COMPRESS + configs.get(MAIN_ARG_WEB_COMPRESS));
			args = ArrayUtils.add(args, MAIN_ARG_WEB_CONTEXT_PATH + configs.get(MAIN_ARG_WEB_CONTEXT_PATH));
			args = ArrayUtils.add(args, MAIN_ARG_WEB_SERVER_PORT + configs.get(MAIN_ARG_WEB_SERVER_PORT));
		}

		// Socket服务端项目特有参数
		if (ClassUtils.isPresent("com.lichkin.framework.socket.LKSocketServer", null)) {
			args = ArrayUtils.add(args, MAIN_ARG_SOCKET_SERVER_CONFIG_IDX + configs.get(MAIN_ARG_SOCKET_SERVER_CONFIG_IDX));
		}

		LOGGER.warn("systemId[%s] -> main args after analyzed. %s", SYSTEM_ID, ArrayUtils.toString(args));

		// 配置log4j2的参数
		LKLog4j2Initializer.setMainArguments(

				configs.get(MAIN_ARG_LOG_TAG),

				configs.get(MAIN_ARG_LOG_LEVEL_SYSTEM),

				configs.get(MAIN_ARG_LOG_LEVEL_ORG),

				configs.get(MAIN_ARG_LOG_LEVEL_NET),

				configs.get(MAIN_ARG_LOG_LEVEL_IO)

		);

		// 创建应用
		final SpringApplication app = new SpringApplication(LKMain.class);

		// 启动应用
		app.run(args);
	}


	private static Locale toLocale(String localeStr) {
		String[] strs = localeStr.split("_");
		switch (strs.length) {
			case 1:
				return new Locale(strs[0]);
			case 2:
				return new Locale(strs[0], strs[1]);
			case 3:
				return new Locale(strs[0], strs[1], strs[2]);
			default:
				return null;
		}
	}


	private static void configDefaultLocale(String defaultLocale) {
		if (StringUtils.isBlank(defaultLocale)) {
			return;
		}
		Locale locale = toLocale(defaultLocale);
		if (locale == null) {
			return;
		}
		LKConfigStatics.DEFAULT_LOCALE = locale;
		configs.put(MAIN_ARG_DEFAULT_LOCALE, locale.toString());
	}


	private static void configImplementedLocaleArr(String implementedLocaleArr) {
		if (StringUtils.isBlank(implementedLocaleArr)) {
			return;
		}
		String[] impls = implementedLocaleArr.split(",");
		Locale[] locales = new Locale[impls.length];
		for (int j = 0; j < impls.length; j++) {
			Locale locale = toLocale(impls[j]);
			locales[j] = locale == null ? LKConfigStatics.DEFAULT_LOCALE : locale;
		}
		LKConfigStatics.IMPLEMENTED_LOCALE_ARR = locales;
		configs.put(MAIN_ARG_IMPLEMENTED_LOCALE_ARR, LKArrayUtils.toString(locales));
	}


	private static void configSystemDebug(String systemDebug) {
		if (StringUtils.isBlank(systemDebug)) {
			return;
		}
		LKConfigStatics.SYSTEM_DEBUG = Boolean.parseBoolean(systemDebug);
		configs.put(MAIN_ARG_SYSTEM_DEBUG, systemDebug);
	}


	private static void configSystemTag(String systemTag) {
		if (StringUtils.isBlank(systemTag)) {
			return;
		}
		LKConfigStatics.SYSTEM_TAG = systemTag;
		configs.put(MAIN_ARG_SYSTEM_TAG, systemTag);
	}


	private static void configSystemName(String systemName) {
		if (StringUtils.isBlank(systemName)) {
			return;
		}
		LKConfigStatics.SYSTEM_NAME = systemName;
		configs.put(MAIN_ARG_SYSTEM_NAME, systemName);
	}


	private static void configWebDebug(String webDebug) {
		if (StringUtils.isBlank(webDebug)) {
			return;
		}
		LKConfigStatics.WEB_DEBUG = Boolean.parseBoolean(webDebug);
		configs.put(MAIN_ARG_WEB_DEBUG, webDebug);
	}


	private static void configWebCompress(String webCompress) {
		if (StringUtils.isBlank(webCompress)) {
			return;
		}
		LKConfigStatics.WEB_COMPRESS = Boolean.parseBoolean(webCompress);
		configs.put(MAIN_ARG_WEB_COMPRESS, webCompress);
	}


	private static void configWebContextPath(String webContextPath) {
		if (StringUtils.isBlank(webContextPath)) {
			return;
		}
		webContextPath = LKStringUtils.toStandardPath(webContextPath);
		LKConfigStatics.WEB_CONTEXT_PATH = webContextPath;
		configs.put(MAIN_ARG_WEB_CONTEXT_PATH, webContextPath);
	}


	private static void configWebServerPort(String webServerPort) {
		if (StringUtils.isBlank(webServerPort)) {
			return;
		}
		LKConfigStatics.WEB_SERVER_PORT = webServerPort;
		configs.put(MAIN_ARG_WEB_SERVER_PORT, webServerPort);
	}


	private static void configWebAdminDebug(String webAdminDebug) {
		if (StringUtils.isBlank(webAdminDebug)) {
			return;
		}
		LKConfigStatics.WEB_ADMIN_DEBUG = Boolean.parseBoolean(webAdminDebug);
		configs.put(MAIN_ARG_WEB_ADMIN_DEBUG, webAdminDebug);
	}


	private static void configSocketServerConfigIdx(String socketServerConfigIdx) {
		if (StringUtils.isBlank(socketServerConfigIdx)) {
			return;
		}
		LKConfigStatics.SOCKET_SERVER_CONFIG_IDX = Integer.parseInt(socketServerConfigIdx);
		configs.put(MAIN_ARG_SOCKET_SERVER_CONFIG_IDX, socketServerConfigIdx);
	}

}
