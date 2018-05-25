package com.lichkin.springframework.web.configs;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;

/**
 * 应用事件监听器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Component
public class LKApplicationListener implements ApplicationListener<ApplicationEvent> {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKApplicationListener.class);


	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		String eventClassName = event.getClass().getName();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("onApplicationEvent -> %s", eventClassName);
		}
		switch (eventClassName) {
			case "org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent":
			break;
			case "org.springframework.boot.context.event.ApplicationPreparedEvent":
			break;
			case "org.springframework.context.event.ContextRefreshedEvent":
			break;
			case "org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent":
			break;
			case "org.springframework.boot.context.event.ApplicationStartedEvent":
			break;
			case "org.springframework.boot.context.event.ApplicationReadyEvent":
			break;
			case "org.springframework.boot.devtools.classpath.ClassPathChangedEvent":
			break;
			case "org.springframework.context.event.ContextClosedEvent":
			break;
			default:
			break;
		}
	}

}
