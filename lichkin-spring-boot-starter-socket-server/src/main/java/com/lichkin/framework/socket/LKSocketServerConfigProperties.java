package com.lichkin.framework.socket;

import java.util.Properties;

import com.lichkin.framework.utils.security.properties.LKPropertiesReader;

/**
 * 使用socket-server包相关配置值
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKSocketServerConfigProperties {

	public static int SOCKET_SERVER_PORT = 31111;

	public static int SOCKET_SERVER_READER_BUFFER_SIZE = 10240;

	static {
		try {
			Properties properties = LKPropertiesReader.read("/opt/socket/server.properties");
			SOCKET_SERVER_PORT = Integer.parseInt(properties.getProperty("socket.server.port"));
			SOCKET_SERVER_READER_BUFFER_SIZE = Integer.parseInt(properties.getProperty("socket.server.reader.bufferSize"));
		} catch (Exception e) {
		}
	}

}
