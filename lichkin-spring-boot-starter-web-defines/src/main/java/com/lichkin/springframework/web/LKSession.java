package com.lichkin.springframework.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lichkin.framework.defines.entities.I_Comp;
import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.framework.defines.entities.I_Menu;
import com.lichkin.framework.defines.entities.I_Role;
import com.lichkin.framework.defines.entities.I_User;

/**
 * 会话信息
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKSession {

	/**
	 * 获取字符串
	 * @param session HttpSession
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 字符串
	 */
	public static Object getObject(HttpSession session, String key, Object defaultValue) {
		return null;
	}


	/**
	 * 获取字符串
	 * @param session HttpSession
	 * @param key 键
	 * @param value 值
	 */
	public static void setObject(HttpSession session, String key, Object value) {
	}


	/**
	 * 获取字符串
	 * @param session HttpSession
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 字符串
	 */
	public static String getString(HttpSession session, String key, String defaultValue) {
		return null;
	}


	/**
	 * 获取字符串
	 * @param session HttpSession
	 * @param key 键
	 * @param value 值
	 */
	public static void setString(HttpSession session, String key, String value) {
	}


	/**
	 * 获取令牌
	 * @param session HttpSession
	 * @return 令牌
	 */
	public static String getToken(HttpSession session) {
		return null;
	}


	/**
	 * 设置登录信息
	 * @param session HttpSession
	 * @param login 登录信息
	 */
	public static void setLogin(HttpSession session, I_Login login) {
	}


	/**
	 * 获取登录信息
	 * @param session HttpSession
	 * @return 登录信息
	 */
	public static I_Login getLogin(HttpSession session) {
		return null;
	}


	/**
	 * 设置用户信息
	 * @param session HttpSession
	 * @param user 用户信息
	 */
	public static void setUser(HttpSession session, I_User user) {
	}


	/**
	 * 获取用户信息
	 * @param session HttpSession
	 * @return 用户信息
	 */
	public static I_User getUser(HttpSession session) {
		return null;
	}


	/**
	 * 设置公司信息
	 * @param session HttpSession
	 * @param comp 公司信息
	 */
	public static void setComp(HttpSession session, I_Comp comp) {
	}


	/**
	 * 获取公司信息
	 * @param session HttpSession
	 * @return 公司信息
	 */
	public static I_Comp getComp(HttpSession session) {
		return null;
	}


	/**
	 * 设置角色信息
	 * @param session HttpSession
	 * @param roles 角色信息
	 */
	public static void setRoles(HttpSession session, List<I_Role> roles) {
	}


	/**
	 * 获取角色信息
	 * @param session HttpSession
	 * @return 角色信息
	 */
	public static List<I_Role> getRoles(HttpSession session) {
		return null;
	}


	/**
	 * 设置菜单信息
	 * @param session HttpSession
	 * @param menus 菜单信息
	 */
	public static void setMenus(HttpSession session, List<I_Menu> menus) {
	}


	/**
	 * 获取菜单信息
	 * @param session HttpSession
	 * @return 菜单信息
	 */
	public static List<I_Menu> getMenus(HttpSession session) {
		return null;
	}


	/**
	 * 验证页面访问权限
	 * @param request 请求对象
	 * @param pageName 页面名
	 * @return 可有访问返回true，否则返回false。
	 */
	public static boolean checkMenuAuth(HttpServletRequest request, String pageName) {
		return false;
	}

}
