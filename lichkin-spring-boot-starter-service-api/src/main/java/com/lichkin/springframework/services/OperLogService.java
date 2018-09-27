package com.lichkin.springframework.services;

import org.springframework.transaction.annotation.Transactional;

import com.lichkin.framework.beans.impl.LKRequestBean;
import com.lichkin.framework.defines.enums.LKEnum;
import com.lichkin.framework.defines.enums.impl.LKOperTypeEnum;
import com.lichkin.framework.json.LKJsonUtils;
import com.lichkin.framework.utils.LKDateTimeUtils;
import com.lichkin.springframework.web.beans.LKRequestInfo;

/**
 * 日志服务类
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public abstract class OperLogService extends LKDBService {

	/**
	 * 记录日志
	 * @param compId 公司ID
	 * @param loginId 登录ID
	 * @param requestId 请求ID
	 * @param requestTime 请求时间
	 * @param requestIp 请求IP地址
	 * @param requestUrl 请求地址
	 * @param requestDatas 请求数据
	 * @param operType 操作类型
	 * @param busType 业务类型
	 */
	public abstract void log(String compId, String loginId, String requestId, String requestTime, String requestIp, String requestUrl, String requestDatas, LKOperTypeEnum operType, String busType);


	/**
	 * 记录日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param operType 操作类型
	 * @param busType 业务类型
	 */
	@Transactional
	public void log(LKRequestBean cin, LKRequestInfo requestInfo, LKOperTypeEnum operType, String busType) {
		log(cin.getCompId(), cin.getLoginId(), requestInfo.getRequestId(), LKDateTimeUtils.toString(requestInfo.getRequestTime()), requestInfo.getRequestIp(), requestInfo.getRequestUri(), LKJsonUtils.toJson(requestInfo.getRequestDatas()), operType, busType);
	}


	/**
	 * 记录日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param operType 操作类型
	 * @param busType 业务类型
	 */
	@Transactional
	public void log(LKRequestBean cin, LKRequestInfo requestInfo, LKOperTypeEnum operType, LKEnum busType) {
		log(cin, requestInfo, operType, busType.toString());
	}


	/**
	 * 记录新增日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logAdd(LKRequestBean cin, LKRequestInfo requestInfo, String busType) {
		log(cin, requestInfo, LKOperTypeEnum.ADD, busType);
	}


	/**
	 * 记录新增日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logAdd(LKRequestBean cin, LKRequestInfo requestInfo, LKEnum busType) {
		log(cin, requestInfo, LKOperTypeEnum.ADD, busType);
	}


	/**
	 * 记录删除日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logRemove(LKRequestBean cin, LKRequestInfo requestInfo, String busType) {
		log(cin, requestInfo, LKOperTypeEnum.REMOVE, busType);
	}


	/**
	 * 记录删除日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logRemove(LKRequestBean cin, LKRequestInfo requestInfo, LKEnum busType) {
		log(cin, requestInfo, LKOperTypeEnum.REMOVE, busType);
	}


	/**
	 * 记录编辑日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logEdit(LKRequestBean cin, LKRequestInfo requestInfo, String busType) {
		log(cin, requestInfo, LKOperTypeEnum.EDIT, busType);
	}


	/**
	 * 记录编辑日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logEdit(LKRequestBean cin, LKRequestInfo requestInfo, LKEnum busType) {
		log(cin, requestInfo, LKOperTypeEnum.EDIT, busType);
	}


	/**
	 * 记录查询日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logSearch(LKRequestBean cin, LKRequestInfo requestInfo, String busType) {
		log(cin, requestInfo, LKOperTypeEnum.SEARCH, busType);
	}


	/**
	 * 记录查询日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logSearch(LKRequestBean cin, LKRequestInfo requestInfo, LKEnum busType) {
		log(cin, requestInfo, LKOperTypeEnum.SEARCH, busType);
	}


	/**
	 * 记录其它日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logOther(LKRequestBean cin, LKRequestInfo requestInfo, String busType) {
		log(cin, requestInfo, LKOperTypeEnum.OTHER, busType);
	}


	/**
	 * 记录其它日志
	 * @param cin 入参
	 * @param requestInfo 请求信息
	 * @param busType 业务类型
	 */
	@Transactional
	public void logOther(LKRequestBean cin, LKRequestInfo requestInfo, LKEnum busType) {
		log(cin, requestInfo, LKOperTypeEnum.OTHER, busType);
	}

}
