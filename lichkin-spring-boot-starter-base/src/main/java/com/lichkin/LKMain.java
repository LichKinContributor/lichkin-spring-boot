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
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ClassUtils;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.log.log4j2.LKLog4j2Initializer;
import com.lichkin.framework.utils.LKRandomUtils;
import com.lichkin.framework.utils.i18n.LKI18NUtils;
import com.lichkin.springframework.utils.LKPropertiesUtils;

/**
 * 项目主类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@SpringBootApplication // 自动配置
public class LKMain {

	static {
		LKLog4j2Initializer.init();
	}

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKMain.class);

	/** Profiles启用配置 */
	private static final String SPRING_PROFILES_ACTIVE = "--spring.profiles.active=";

	/** context环境 */
	public static ConfigurableEnvironment env = null;

	/** 系统ID */
	public static final String SYSTEM_ID = LKRandomUtils.create(32);

	/** 默认加载的Profiles */
	private static String[] PROFILES = {

			"common-base", "base",

			"common-db", "db",

			"common-db2", "db2",

			"common-web", "web",

			"common-admin", "admin",

			"development"

	};

	/** 配置的Profiles */
	private static String[] CONFIGED_PROFILES;


	/**
	 * 主函数
	 * @param args 入口参数
	 * @throws IOException 读取配置文件时可能抛出异常
	 */
	public static void main(String[] args) throws IOException {
		LOGGER.warn("systemId[%s] -> main args before analyze. %s", SYSTEM_ID, ArrayUtils.toString(args));

		// 初始化默认Profiles
		initDefaultProfiles();

		// 默认配置属性
		String logTag = VALUE_LOG_TAG;
		String logLevelSystem = VALUE_LOG_LEVEL_SYSTEM;
		String logLevelOrg = VALUE_LOG_LEVEL_ORG;
		String logLevelNet = VALUE_LOG_LEVEL_NET;

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
				} else if (StringUtils.startsWith(arg, SPRING_PROFILES_ACTIVE)) {
					CONFIGED_PROFILES = arg.substring(SPRING_PROFILES_ACTIVE.length()).split(",");

					if ((CONFIGED_PROFILES.length == 1) && CONFIGED_PROFILES[0].equals("")) {
						CONFIGED_PROFILES = null;
					} else {
						final boolean existDevelopment = ArrayUtils.contains(CONFIGED_PROFILES, "development");
						// 遍历传入的Profiles配置
						for (int j = CONFIGED_PROFILES.length - 1; j >= 0; j--) {
							final String configedProfile = CONFIGED_PROFILES[j];

							if (StringUtils.isBlank(configedProfile) || ArrayUtils.contains(PROFILES, configedProfile)) {
								CONFIGED_PROFILES = ArrayUtils.remove(CONFIGED_PROFILES, j);
								continue;
							}
						}
						if (!existDevelopment && ArrayUtils.isNotEmpty(CONFIGED_PROFILES)) {
							PROFILES = ArrayUtils.remove(PROFILES, ArrayUtils.indexOf(PROFILES, "development"));
						}
					}

					args = ArrayUtils.remove(args, i);
				}
			}
		}

		// 重构MainArgs
		args = ArrayUtils.add(args, MAIN_ARG_LOG_TAG + logTag);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_SYSTEM + logLevelSystem);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_ORG + logLevelOrg);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_NET + logLevelNet);
		args = ArrayUtils.add(args, createProfilesActiveArg());

		LOGGER.warn("systemId[%s] -> main args after analyzed. %s", SYSTEM_ID, ArrayUtils.toString(args));

		// 配置log4j2的参数
		LKLog4j2Initializer.setMainArguments(logTag, logLevelSystem, logLevelOrg, logLevelNet);

		// 创建应用
		final SpringApplication app = new SpringApplication(LKMain.class);

		// 添加初始化对象
		app.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {

			@Override
			public void initialize(ConfigurableApplicationContext applicationContext) {
				// 读取配置属性
				env = applicationContext.getEnvironment();

				// 读取系统配置属性
				LKFrameworkStatics.DEFAULT_LOCALE = LKI18NUtils.getLocale(LKPropertiesUtils.validateConfigValue("lichkin.framework.system.locale", LKFrameworkStatics.DEFAULT_LOCALE.toString()));
				String locales = LKPropertiesUtils.validateConfigValue("lichkin.framework.system.implemented.locales", LKFrameworkStatics.DEFAULT_LOCALE.toString());
				String[] localeArr = locales.split("\\|");
				for (String locale : localeArr) {
					Locale l = LKI18NUtils.getLocale(locale);
					if (!ArrayUtils.contains(LKFrameworkStatics.IMPLEMENTED_LOCALE_ARR, l)) {
						LKFrameworkStatics.IMPLEMENTED_LOCALE_ARR = ArrayUtils.add(LKFrameworkStatics.IMPLEMENTED_LOCALE_ARR, l);
					}
				}
				LKFrameworkStatics.SYSTEM_TAG = LKPropertiesUtils.validateConfigValue("lichkin.framework.system.tag", LKFrameworkStatics.SYSTEM_TAG);
				LKFrameworkStatics.SYSTEM_NAME = LKPropertiesUtils.validateConfigValue("lichkin.framework.system.name", LKFrameworkStatics.SYSTEM_NAME);
				LKFrameworkStatics.SYSTEM_DEBUG = LKPropertiesUtils.validateConfigValue("lichkin.framework.system.debug", LKFrameworkStatics.SYSTEM_DEBUG);

				if (ENV_WEB) {
					LKFrameworkStatics.WEB_DEBUG = LKPropertiesUtils.validateConfigValue("lichkin.framework.web.debug", LKFrameworkStatics.WEB_DEBUG);
				}

				if (ENV_ADMIN) {
					LKFrameworkStatics.WEB_ADMIN_DEBUG = LKPropertiesUtils.validateConfigValue("lichkin.framework.web.admin.debug", LKFrameworkStatics.WEB_ADMIN_DEBUG);
				}
			}

		});

		// 启动应用
		app.run(args);
	}


	/**
	 * 初始化默认Profiles
	 */
	private static void initDefaultProfiles() {
		analyzeProfileDB();// 解析数据库环境
		analyzeProfileDB2();// 解析数据库从库环境
		analyzeProfileWeb();// 解析WEB环境
		analyzeProfileAdmin();// 解析ADMIN环境
	}


	/** 是否包含数据库环境 */
	public static boolean ENV_DB = false;


	/**
	 * 解析数据库环境
	 * @return 依赖数据库环境返回true，否则返回false。
	 */
	private static void analyzeProfileDB() {
		if (!ClassUtils.isPresent("com.lichkin.springframework.db.configs.LKDBPrimaryConfigs", null)) {
			PROFILES = ArrayUtils.removeElement(PROFILES, "db");
			PROFILES = ArrayUtils.removeElement(PROFILES, "common-db");
		} else {
			ENV_DB = true;
		}
	}


	/** 是否包含数据库从库环境 */
	public static boolean ENV_DB2 = false;


	/**
	 * 解析数据库从库环境
	 * @return 依赖数据库环境返回true，否则返回false。
	 */
	private static void analyzeProfileDB2() {
		if (!ClassUtils.isPresent("com.lichkin.springframework.db.configs.LKDBSecondaryConfigs", null)) {
			PROFILES = ArrayUtils.removeElement(PROFILES, "db2");
			PROFILES = ArrayUtils.removeElement(PROFILES, "common-db2");
		} else {
			ENV_DB2 = true;
		}
	}


	/** 是否包含WEB环境 */
	public static boolean ENV_WEB = false;


	/**
	 * 解析WEB环境
	 * @return 依赖Web环境返回true，否则返回false。
	 */
	private static void analyzeProfileWeb() {
		if (!ClassUtils.isPresent("com.lichkin.springframework.web.configs.LKWebMvcConfigurerAdapter", null)) {
			PROFILES = ArrayUtils.removeElement(PROFILES, "web");
			PROFILES = ArrayUtils.removeElement(PROFILES, "common-web");
		} else {
			ENV_WEB = true;
		}
	}


	/** 是否包含ADMIN环境 */
	public static boolean ENV_ADMIN = false;


	/**
	 * 解析ADMIN环境
	 * @return 依赖Web环境返回true，否则返回false。
	 */
	private static void analyzeProfileAdmin() {
		if (!ClassUtils.isPresent("com.lichkin.springframework.web.admin.configs.LKAdminConfigs", null)) {
			PROFILES = ArrayUtils.removeElement(PROFILES, "admin");
			PROFILES = ArrayUtils.removeElement(PROFILES, "common-admin");
		} else {
			ENV_ADMIN = true;
		}
	}


	/**
	 * 构建Profiles参数
	 * @return Profiles参数
	 */
	private static String createProfilesActiveArg() {
		PROFILES = ArrayUtils.addAll(PROFILES, CONFIGED_PROFILES);
		String str = SPRING_PROFILES_ACTIVE;
		for (int j = 0; j < PROFILES.length; j++) {
			if (j == 0) {
				str += PROFILES[j];
			} else {
				str += "," + PROFILES[j];
			}
		}
		return str;
	}

}