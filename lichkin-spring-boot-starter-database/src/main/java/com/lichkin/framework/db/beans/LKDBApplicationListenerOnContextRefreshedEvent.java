package com.lichkin.framework.db.beans;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 数据库应用事件监听器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Component
public class LKDBApplicationListenerOnContextRefreshedEvent implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		LKDBResource.load();
	}

}
