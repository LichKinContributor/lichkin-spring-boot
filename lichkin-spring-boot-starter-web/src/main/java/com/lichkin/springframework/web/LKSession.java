package com.lichkin.springframework.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.entities.I_Comp;
import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.framework.defines.entities.I_Menu;
import com.lichkin.framework.defines.entities.I_Role;
import com.lichkin.framework.defines.entities.I_User;
import com.lichkin.springframework.web.utils.LKRequestUtils;

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
	public static String getString(HttpSession session, String key, String defaultValue) {
		Object obj = session.getAttribute(key);
		return obj == null ? defaultValue : obj.toString();
	}


	/**
	 * 获取字符串
	 * @param session HttpSession
	 * @param key 键
	 * @param value 值
	 */
	public static void setString(HttpSession session, String key, String value) {
		session.setAttribute(key, value);
	}


	/** 键：公司信息 */
	private static final String KEY_COMP = "KEY_COMP";


	/**
	 * 设置公司信息
	 * @param session HttpSession
	 * @param comp 公司信息
	 */
	public static void setComp(HttpSession session, I_Comp comp) {
		session.setAttribute(KEY_COMP, comp);
	}


	/**
	 * 获取公司信息
	 * @param session HttpSession
	 * @return 公司信息
	 */
	public static I_Comp getComp(HttpSession session) {
		Object comp = session.getAttribute(KEY_COMP);
		return comp == null ? null : (I_Comp) comp;
	}


	/**
	 * 获取公司ID
	 * @param session HttpSession
	 * @return 公司ID
	 */
	public static String getCompId(HttpSession session) {
		I_Comp comp = getComp(session);
		if (comp == null) {
			return null;
		}
		return comp.getId();
	}


	/** 键：登录信息 */
	private static final String KEY_LOGIN = "KEY_LOGIN";


	/**
	 * 设置登录信息
	 * @param session HttpSession
	 * @param login 登录信息
	 */
	public static void setLogin(HttpSession session, I_Login login) {
		session.setAttribute(KEY_LOGIN, login);
	}


	/**
	 * 获取登录信息
	 * @param session HttpSession
	 * @return 登录信息
	 */
	public static I_Login getLogin(HttpSession session) {
		Object login = session.getAttribute(KEY_LOGIN);
		return login == null ? null : (I_Login) login;
	}


	/**
	 * 获取登录ID
	 * @param session HttpSession
	 * @return 登录ID
	 */
	public static String getLoginId(HttpSession session) {
		I_Login login = getLogin(session);
		if (login == null) {
			return null;
		}
		return login.getId();
	}


	/**
	 * 获取令牌
	 * @param session HttpSession
	 * @return 令牌
	 */
	public static String getToken(HttpSession session) {
		I_Login login = getLogin(session);
		if (login == null) {
			return null;
		}
		return login.getToken();
	}


	/** 键：用户信息 */
	private static final String KEY_USER = "KEY_USER";


	/**
	 * 设置用户信息
	 * @param session HttpSession
	 * @param user 用户信息
	 */
	public static void setUser(HttpSession session, I_User user) {
		session.setAttribute(KEY_USER, user);
	}


	/**
	 * 获取用户信息
	 * @param session HttpSession
	 * @return 用户信息
	 */
	public static I_User getUser(HttpSession session) {
		Object user = session.getAttribute(KEY_USER);
		return user == null ? null : (I_User) user;
	}


	/** 键：角色信息 */
	private static final String KEY_ROLES = "KEY_ROLES";


	/**
	 * 设置角色信息
	 * @param session HttpSession
	 * @param roles 角色信息
	 */
	public static void setRoles(HttpSession session, List<I_Role> roles) {
		session.setAttribute(KEY_ROLES, roles);
	}


	/**
	 * 获取角色信息
	 * @param session HttpSession
	 * @return 角色信息
	 */
	@SuppressWarnings("unchecked")
	public static List<I_Role> getRoles(HttpSession session) {
		Object roles = session.getAttribute(KEY_ROLES);
		return roles == null ? null : new ArrayList<>((List<I_Role>) roles);
	}


	/** 键：菜单信息 */
	private static final String KEY_MENUS = "KEY_MENUS";


	/**
	 * 设置菜单信息
	 * @param session HttpSession
	 * @param menus 菜单信息
	 */
	public static void setMenus(HttpSession session, List<I_Menu> menus) {
		session.setAttribute(KEY_MENUS, menus);
	}


	/**
	 * 获取菜单信息
	 * @param session HttpSession
	 * @return 菜单信息
	 */
	@SuppressWarnings("unchecked")
	public static List<I_Menu> getMenus(HttpSession session) {
		Object menus = session.getAttribute(KEY_MENUS);
		return menus == null ? null : new ArrayList<>((List<I_Menu>) menus);
	}


	/**
	 * 验证页面访问权限
	 * @param request 请求对象
	 * @param pageName 页面名
	 * @return 可有访问返回true，否则返回false。
	 */
	public static boolean checkMenuAuth(HttpServletRequest request, String pageName) {
		String url = LKRequestUtils.getRequestURI(request).replace(pageName + LKFrameworkStatics.WEB_MAPPING_PAGES, "");
		List<I_Menu> menus = getMenus(request.getSession());
		for (I_Menu menu : menus) {
			if (menu.getUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}

}
