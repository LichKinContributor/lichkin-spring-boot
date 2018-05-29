package com.lichkin.springframework.db.configs;

import java.lang.reflect.InvocationTargetException;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.lichkin.framework.db.beans.LKDBResource;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;

/**
 * 数据库应用事件监听器
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Component
public class LKDBApplicationListenerOnContextRefreshedEvent implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			LKDBResource.load();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new LKRuntimeException(LKErrorCodesEnum.CONFIG_ERROR, e);
		}
	}

}
