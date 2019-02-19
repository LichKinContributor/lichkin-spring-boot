package com.lichkin.springframework.scoket.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lichkin.framework.socket.LKSocketClient;
import com.lichkin.framework.socket.LKSocketServer;

/**
 * socket-server相关配置
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Configuration
public class LKScoketServerConfigs implements CommandLineRunner {

	@Bean
	public LKSocketServer initSocketServer(LKSocketClient socketReader) {
		return new LKSocketServer();
	}


	@Autowired
	private LKSocketServer socketServer;


	@Override
	public void run(String... args) throws Exception {
		socketServer.start();
	}

}
