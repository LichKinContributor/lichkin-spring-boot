package com.lichkin.springframework.web;

import javax.servlet.http.HttpSession;

import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * 会话信息
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKSession {

	/**
	 * 获取会话
	 * @param session 会话
	 * @return 会话
	 */
	private static HttpSession getSession(HttpSession session) {
		if (session == null) {
			session = LKRequestUtils.getRequest().getSession();
		}
		return session;
	}


	/**
	 * 获取字符串
	 * @param session HttpSession
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 字符串
	 */
	public static String getString(HttpSession session, String key, String defaultValue) {
		Object obj = getSession(session).getAttribute(key);
		return obj == null ? defaultValue : obj.toString();
	}


	/**
	 * 获取字符串
	 * @param session HttpSession
	 * @param key 键
	 * @param value 值
	 * @return 字符串
	 */
	public static void setString(HttpSession session, String key, String value) {
		getSession(session).setAttribute(key, value);
	}


	/** 键：令牌 */
	private static final String KEY_TOKEN = "LK_TOKEN";


	/**
	 * 获取令牌
	 * @return 令牌
	 */
	public static String getToken() {
		return getString(null, KEY_TOKEN, null);
	}


	/**
	 * 获取令牌
	 * @param session HttpSession
	 * @return 令牌
	 */
	public static String getToken(HttpSession session) {
		return getString(session, KEY_TOKEN, null);
	}


	/**
	 * 设置令牌
	 * @param token 令牌
	 */
	public static void setToken(String token) {
		setString(null, KEY_TOKEN, token);
	}


	/**
	 * 设置令牌
	 * @param session HttpSession
	 * @param token 令牌
	 */
	public static void setToken(HttpSession session, String token) {
		setString(session, KEY_TOKEN, token);
	}


	/** 键：公司ID */
	private static final String KEY_COMP_ID = "LK_COMP_ID";


	/**
	 * 获取公司ID
	 * @return 公司ID
	 */
	public static String getCompId() {
		return getString(null, KEY_COMP_ID, null);
	}


	/**
	 * 获取公司ID
	 * @param session HttpSession
	 * @return 公司ID
	 */
	public static String getCompId(HttpSession session) {
		return getString(session, KEY_COMP_ID, null);
	}


	/**
	 * 设置公司ID
	 * @param compId 公司ID
	 */
	public static void setCompId(String compId) {
		setString(null, KEY_COMP_ID, compId);
	}


	/**
	 * 设置公司ID
	 * @param session HttpSession
	 * @param compId 公司ID
	 */
	public static void setCompId(HttpSession session, String compId) {
		setString(session, KEY_COMP_ID, compId);
	}


	/** 键：登录ID */
	private static final String KEY_LOGIN_ID = "KEY_LOGIN_ID";

	/** 默认值：登录ID */
	private static final String DEFAULT_VALUE_LOGIN_ID = "";


	/**
	 * 获取登录ID
	 * @return 登录ID
	 */
	public static String getLoginId() {
		return getString(null, KEY_LOGIN_ID, DEFAULT_VALUE_LOGIN_ID);
	}


	/**
	 * 获取登录ID
	 * @param session HttpSession
	 * @return 登录ID
	 */
	public static String getLoginId(HttpSession session) {
		return getString(session, KEY_LOGIN_ID, DEFAULT_VALUE_LOGIN_ID);
	}


	/**
	 * 设置登录ID
	 * @param compId 登录ID
	 */
	public static void setLoginId(String compId) {
		setString(null, KEY_LOGIN_ID, compId);
	}


	/**
	 * 设置登录ID
	 * @param session HttpSession
	 * @param compId 登录ID
	 */
	public static void setLoginId(HttpSession session, String compId) {
		setString(session, KEY_LOGIN_ID, compId);
	}


	/** 键：登录信息 */
	private static final String KEY_LOGIN = "KEY_LOGIN";


	/**
	 * 获取登录信息
	 * @return 登录信息
	 */
	public static I_Login getLogin() {
		return getLogin(null);
	}


	/**
	 * 获取登录信息
	 * @param session HttpSession
	 * @return 登录信息
	 */
	public static I_Login getLogin(HttpSession session) {
		Object login = getSession(session).getAttribute(KEY_LOGIN);
		return login == null ? null : (I_Login) login;
	}


	/**
	 * 设置登录信息
	 * @param login 登录信息
	 */
	public static void setLogin(I_Login login) {
		setLogin(null, login);
	}


	/**
	 * 设置登录信息
	 * @param session HttpSession
	 * @param login 登录信息
	 */
	public static void setLogin(HttpSession session, I_Login login) {
		getSession(session).setAttribute(KEY_LOGIN, login);
	}

}
