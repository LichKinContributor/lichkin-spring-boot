package com.lichkin.springframework.controllers;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lichkin.framework.beans.impl.Datas;
import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.beans.impl.LKResponseBean;
import com.lichkin.framework.defines.LKFrameworkStatics;
import com.lichkin.framework.defines.entities.I_Comp;
import com.lichkin.framework.defines.entities.I_Login;
import com.lichkin.framework.defines.enums.impl.LKErrorCodesEnum;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.defines.exceptions.LKException;
import com.lichkin.framework.defines.exceptions.LKFrameworkException;
import com.lichkin.framework.defines.exceptions.LKRuntimeException;
import com.lichkin.framework.utils.LKClassUtils;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.framework.utils.LKStringUtils;
import com.lichkin.framework.web.annotations.LKApiType;
import com.lichkin.framework.web.annotations.LKController4Api;
import com.lichkin.springframework.services.OperLogService;
import com.lichkin.springframework.web.LKSession;
import com.lichkin.springframework.web.beans.LKRequestInfo;
import com.lichkin.springframework.web.utils.LKRequestUtils;

/**
 * API数据请求控制器类定义
 * @param <CI> 控制器类入参类型
 * @param <CO> 控制器类出参类型
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
@LKController4Api
public abstract class LKApiController<CI extends LKRequestBean, CO> extends LKController implements ApiController {

	/**
	 * 请求处理方法
	 * @deprecated API必有方法，不可重写。
	 * @param cin 控制器类入参
	 * @return 控制器类出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	@Deprecated
	@PostMapping
	public LKResponseBean<CO> invoke(@Valid @RequestBody CI cin) throws LKException {
		Datas datas = cin.getDatas();
		String requestURI = LKRequestUtils.getRequestURI(request);
		String userType = requestURI.substring(LKStringUtils.indexOf(requestURI, "/", 2) + 1, LKStringUtils.indexOf(requestURI, "/", 3));
		initCI(datas, requestURI, userType);
		LKResponseBean<CO> responseBean = new LKResponseBean<>(handleInvoke(cin, datas.getLocale(), datas.getCompId(), datas.getLoginId()));
		if (saveLog(cin)) {
			saveLog(cin, datas, requestURI, userType);
		}
		return responseBean;
	}


	/**
	 * 初始化控制器类入参
	 * @param datas 统一请求参数
	 * @param requestURI 请求地址
	 * @param userType 用户类型
	 */
	private void initCI(Datas datas, String requestURI, String userType) {
		datas.setLocale(LKRequestUtils.getLocale(request).toString());
		if (requestURI.startsWith(LKFrameworkStatics.WEB_MAPPING_API_APP)) {
			initAsApp(datas, userType);
		} else {
			initAsWeb(datas, userType);
		}
	}


	/**
	 * 客户端访问接口
	 * @param datas 统一请求参数
	 * @param userType 用户类型
	 */
	private void initAsApp(Datas datas, String userType) {
	}


	/**
	 * 页面访问接口
	 * @param datas 统一请求参数
	 * @param userType 用户类型
	 */
	private void initAsWeb(Datas datas, String userType) {
		switch (((LKApiType) LKClassUtils.deepGetAnnotation(getClass(), LKApiType.class.getName())).apiType()) {
			case OPEN: {
			}
			break;
			case ROOT_QUERY: {
				datas.setCompId(LKFrameworkStatics.LichKin);
			}
			break;
			case BEFORE_LOGIN: {
				datas.setToken(LKSession.getToken(session));
				datas.setLogin(LKSession.getLogin(session));
				datas.setLoginId(LKSession.getLoginId(session, "Employee".equals(userType)));
				datas.setUser(LKSession.getUser(session));
				datas.setUserId(LKSession.getUserId(session));
			}
			break;
			case PERSONAL_BUSINESS: {
				String token = LKSession.getToken(session);
				I_Login login = LKSession.getLogin(session);
				String loginId = LKSession.getLoginId(session, "Employee".equals(userType));
				if (StringUtils.isBlank(token) || StringUtils.isBlank(loginId) || (login == null)) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("You must login before when invoke a personal business API."));
				}
				datas.setToken(token);
				datas.setLogin(login);
				datas.setLoginId(loginId);
				datas.setUser(LKSession.getUser(session));
				datas.setUserId(LKSession.getUserId(session));
			}
			break;
			case COMPANY_BUSINESS: {
				String token = LKSession.getToken(session);
				I_Login login = LKSession.getLogin(session);
				String loginId = LKSession.getLoginId(session, "Employee".equals(userType));
				if (StringUtils.isBlank(token) || StringUtils.isBlank(loginId) || (login == null)) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("You must login before when invoke a company business API."));
				}
				I_Comp comp = LKSession.getComp(session);
				String compId = LKSession.getCompId(session);
				if (StringUtils.isBlank(compId) || (comp == null)) {
					throw new LKRuntimeException(LKErrorCodesEnum.PARAM_ERROR, new LKFrameworkException("You must belongs to a company when invoke a company business API."));
				}
				datas.setToken(token);
				datas.setLogin(login);
				datas.setLoginId(loginId);
				datas.setUser(LKSession.getUser(session));
				datas.setUserId(LKSession.getUserId(session));
				datas.setComp(comp);
				datas.setCompId(compId);
			}
			break;
		}
	}


	/**
	 * 请求处理方法
	 * @param cin 控制器类入参
	 * @param locale 国际化
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @return 控制器类出参
	 * @throws LKException 业务处理失败但不希望已处理数据回滚时抛出异常
	 */
	abstract CO handleInvoke(@Valid CI cin, String locale, String compId, String loginId) throws LKException;


	@Autowired
	private OperLogService operLogService;


	/**
	 * 是否记录日志
	 * @param cin 控制器类入参
	 * @return true:记录日志;false:不记录日志;
	 */
	protected boolean saveLog(CI cin) {
		return true;
	}


	/**
	 * 记录日志
	 * @param cin 控制器类入参
	 * @param datas 统一请求参数
	 * @param requestURI 请求地址
	 * @param userType 用户类型
	 */
	private void saveLog(CI cin, Datas datas, String requestURI, String userType) {
		if (LKFrameworkStatics.LichKin.equals(datas.getCompId())) {
			return;
		}
		try {
			LKRequestInfo requestInfo = (LKRequestInfo) request.getAttribute("requestInfo");
			operLogService.save(

					userType,

					StringUtils.trimToEmpty(datas.getCompId()),

					datas.getLoginId(),

					requestInfo.getRequestId(),

					LKDateTimeUtils.toString(requestInfo.getRequestTime()),

					requestInfo.getRequestIp(),

					requestInfo.getRequestUri(),

					requestInfo.getRequestDatas(),

					getOperType(cin),

					getBusType(cin)

			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获取操作类型
	 * @param cin 控制器类入参
	 * @return 操作类型
	 */
	protected LKOperTypeEnum getOperType(@Valid CI cin) {
		return LKOperTypeEnum.OTHER;
	}


	/**
	 * 获取业务操作类型
	 * @param cin 控制器类入参
	 * @return 业务操作类型
	 */
	protected String getBusType(CI cin) {
		return null;
	}

}
