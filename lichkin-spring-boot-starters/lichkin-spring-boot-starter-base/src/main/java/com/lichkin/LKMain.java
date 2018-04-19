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
	private static String[] PROFILES = { "db", "db2", "web", "admin", "common", "development" };

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

		boolean existTriggerFile = false;
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
				} else if (arg.startsWith("--spring.devtools.restart.trigger-file=")) {
					existTriggerFile = true;
				}
			}
		}

		// 重构MainArgs
		args = ArrayUtils.add(args, MAIN_ARG_LOG_TAG + logTag);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_SYSTEM + logLevelSystem);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_ORG + logLevelOrg);
		args = ArrayUtils.add(args, MAIN_ARG_LOG_LEVEL_NET + logLevelNet);
		args = ArrayUtils.add(args, createProfilesActiveArg());
		if (!existTriggerFile) {
			args = ArrayUtils.add(args, "--spring.devtools.restart.trigger-file=restart.trigger-file");
		}

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
				LKFrameworkStatics.SYSTEM_TAG = LKPropertiesUtils.validateConfigValue("lichkin.framework.system.tag", LKFrameworkStatics.SYSTEM_TAG);
				LKFrameworkStatics.SYSTEM_NAME = LKPropertiesUtils.validateConfigValue("lichkin.framework.system.name", LKFrameworkStatics.SYSTEM_NAME);
				LKFrameworkStatics.SYSTEM_DEBUG = LKPropertiesUtils.validateConfigValue("lichkin.framework.system.debug", LKFrameworkStatics.SYSTEM_DEBUG);

				if (ArrayUtils.contains(PROFILES, "db")) {
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.primary.jpa.show-sql", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.primary.jpa.hibernate.ddl-auto", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.primary.jpa.hibernate.naming.physical-strategy", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.primary.jdbc-url", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.primary.username", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.primary.password", null);
				}

				if (ArrayUtils.contains(PROFILES, "db2")) {
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.secondary.jpa.show-sql", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.secondary.jpa.hibernate.ddl-auto", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.secondary.jpa.hibernate.naming.physical-strategy", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.secondary.jdbc-url", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.secondary.username", null);
					LKPropertiesUtils.validateConfigValue("lichkin.framework.db.secondary.password", null);
				}

				if (ArrayUtils.contains(PROFILES, "web")) {
					LKFrameworkStatics.WEB_DEBUG = LKPropertiesUtils.validateConfigValue("lichkin.framework.web.debug", LKFrameworkStatics.WEB_DEBUG);
					LKFrameworkStatics.WEB_REQUEST_SUFFIX_PATTERN = LKPropertiesUtils.validateConfigValue("lichkin.framework.web.requestSuffixPattern", LKFrameworkStatics.WEB_REQUEST_SUFFIX_PATTERN);
				}

				if (ArrayUtils.contains(PROFILES, "admin")) {
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


	/**
	 * 解析数据库环境
	 * @return 依赖数据库环境返回true，否则返回false。
	 */
	private static void analyzeProfileDB() {
		if (!ClassUtils.isPresent("com.lichkin.springframework.db.configs.LKDBPrimaryConfigs", null)) {
			PROFILES = ArrayUtils.removeElement(PROFILES, "db");
		}
	}


	/**
	 * 解析数据库从库环境
	 * @return 依赖数据库环境返回true，否则返回false。
	 */
	private static void analyzeProfileDB2() {
		if (!ClassUtils.isPresent("com.lichkin.springframework.db.configs.LKDBSecondaryConfigs", null)) {
			PROFILES = ArrayUtils.removeElement(PROFILES, "db2");
		}
	}


	/**
	 * 解析WEB环境
	 * @return 依赖Web环境返回true，否则返回false。
	 */
	private static void analyzeProfileWeb() {
		if (!ClassUtils.isPresent("com.lichkin.springframework.web.configs.LKWebMvcConfigurerAdapter", null)) {
			PROFILES = ArrayUtils.removeElement(PROFILES, "web");
		}
	}


	/**
	 * 解析ADMIN环境
	 * @return 依赖Web环境返回true，否则返回false。
	 */
	private static void analyzeProfileAdmin() {
		if (!ClassUtils.isPresent("com.lichkin.springframework.web.admin.configs.LKAdminConfigs", null)) {
			PROFILES = ArrayUtils.removeElement(PROFILES, "admin");
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
