package com.lichkin.framework.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.lang3.ArrayUtils;

import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.log.LKLog;
import com.lichkin.framework.log.LKLogFactory;
import com.lichkin.framework.utils.LKRandomUtils;
import com.lichkin.springframework.services.LKService;

import lombok.Getter;

/**
 * Socket客户端
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@Getter
public abstract class LKSocketClient extends LKService implements Runnable {

	/** 日志对象 */
	protected final LKLog logger = LKLogFactory.getLog(getClass());

	/** 主键 */
	protected String id = LKRandomUtils.create(32);

	/** Socket */
	protected Socket socket = null;

	/** 客户端线程 */
	private Thread thread;

	/** 关闭标志位 */
	private boolean off = false;


	@Override
	public void run() {
		if (logger.isInfoEnabled()) {
			logger.info("客户端【" + id + "】【" + socket.getRemoteSocketAddress() + "】。 " + "连接");
		}

		if ((socket == null) || socket.isClosed()) {
			logger.error("socket需进行初始化");
			return;
		}

		try {
			DataInputStream dis = null;
			try {
				dis = new DataInputStream(socket.getInputStream());
				while (true) {
					if ((socket == null) || socket.isClosed()) {
						break;
					}

					byte[] buffer = new byte[LKSocketServerConfigProperties.SOCKET_SERVER_READER_BUFFER_SIZE];

					// read方法将在客户端发送数据前一直处于阻断状态。
					// 接收到数据后将会进行实际的读取操作，并进入下一个循环，继续处于阻断状态。
					// 如果缓冲区不足以读取当前请求的所有数据，则只读取缓冲区大小的数据，之后进入下一个循环，继续读取剩余部分，直到数据全部读取结束后，进入阻断状态。
					int length = dis.read(buffer);
					if (length == -1) {// 客户端关闭连接时，读取的长度将为-1。服务器需关闭该连接，并不再进行数据的读取。
						break;
					} else {
						byte[] bytes = ArrayUtils.subarray(buffer, 0, length);
						if (logger.isInfoEnabled()) {
							logger.info("客户端【" + id + "】【" + socket.getRemoteSocketAddress() + "】。 " + "接收到数据 => " + ArrayUtils.toString(bytes));
						}
						try {
							afterSocketClientReceiveData(bytes);
						} catch (Exception e) {
							logger.error(e);
						}
					}
				}
			} catch (IOException e) {
				throw new LKRuntimeException(LKErrorCodesEnum.INTERNAL_SERVER_ERROR, e);
			} finally {
				if (dis != null) {
					try {
						dis.close();
					} catch (IOException e) {
					}
					dis = null;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("客户端【" + id + "】【" + socket.getRemoteSocketAddress() + "】。 " + "关闭");
			}

			// 停止客户端线程
			if (thread != null) {
				try {
					thread.isInterrupted();
				} catch (Exception e2) {
					logger.error(e2);
				}
			}

			// 销毁Socket
			if (socket != null) {
				if (!off) {
					off = true;
				}
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					socket = null;
				}
			}

			// 回调服务器方法
			LKSocketServer.clientDisconnected(id);
		}
	}


	/**
	 * 接收到客户端数据后执行方法
	 * @param bytes 接收到的客户端数据
	 */
	protected abstract void afterSocketClientReceiveData(byte[] bytes);


	/**
	 * 初始化Socket
	 * @param socket Socket
	 * @return 成功返回true；失败返回false。
	 */
	public boolean initSocket(Socket socket) {
		if (this.socket != null) {
			logger.error("client只能初始化一次");
			return false;
		}

		this.socket = socket;

		try {
			beforeSocketClientStart();
			thread = new Thread(this);
			thread.start();
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	/**
	 * 客户端启动前执行方法
	 */
	protected void beforeSocketClientStart() {
	}

}
