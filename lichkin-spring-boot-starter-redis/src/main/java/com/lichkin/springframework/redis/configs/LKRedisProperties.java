package com.lichkin.springframework.redis.configs;

import lombok.Getter;
import lombok.Setter;

/**
 * Redis配置属性
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
@Setter
public class LKRedisProperties {

	/** 主机地址 */
	private String hostName;

	/** 端口号 */
	private int port;

	/** 数据库索引 */
	private int database;

	/** 密码 */
	private String password;

}
