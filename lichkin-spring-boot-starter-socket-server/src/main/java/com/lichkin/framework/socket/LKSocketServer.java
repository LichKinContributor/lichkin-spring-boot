package com.lichkin.framework.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.springframework.configs.LKApplicationContext;

/**
 * Socket服务端
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKSocketServer {

	/** 日志对象 */
	private static final LKLog LOGGER = LKLogFactory.getLog(LKSocketServer.class);

	/** 客户端链接池 */
	public static Map<String, LKSocketClient> clientPool = new HashMap<>();


	/**
	 * 启动服务端
	 */
	public void start() {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("服务端启动，监听端口号【" + LKSocketServerConfigProperties.SOCKET_SERVER_PORT + "】");
		}
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(LKSocketServerConfigProperties.SOCKET_SERVER_PORT);
		} catch (Exception e) {
			LOGGER.error("服务端关闭", e);
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					serverSocket = null;
					return;
				}
			}
		}

		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();// accept方法将在有客户端连接前一直处于阻断状态。
			} catch (IOException e) {
				LOGGER.error(e);// 连接出错，不停服务。
				continue;
			}

			clientConnected(socket);
		}
	}


	/**
	 * 客户端连接
	 * @param socket Socket
	 */
	public static void clientConnected(Socket socket) {
		// 创建客户端读取器
		LKSocketClient client = LKApplicationContext.getBean(LKSocketClient.class);

		// 初始化客户端
		boolean success = client.initSocket(socket);
		if (!success) {
			return;
		}

		// 记录客户端
		clientPool.put(client.getId(), client);

		// 输出日志
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("当前客户端连接数：【" + clientPool.size() + "】");
		}
	}


	/**
	 * 客户端断开连接
	 * @param clientId 客户端ID
	 */
	public static void clientDisconnected(String clientId) {
		// 清除客户端
		clientPool.remove(clientId);

		// 输出日志
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("当前客户端连接数：【" + clientPool.size() + "】");
		}
	}

}
