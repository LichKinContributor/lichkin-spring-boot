package com.lichkin.springframework.web.beans;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lichkin.framework.defines.beans.LKResponseBean;
import com.lichkin.framework.json.deserializer.LKJsonDeserializer4DateTime;
import com.lichkin.framework.json.serializer.LKJsonSerializer4DateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({ "requestId", "requestTime", "requestUri", "requestIp", "handlerClassName", "handlerMethod", "exceptionClassName", "exceptionMessage", "responseTime", "elapsedTime", "responseBean" })
public class LKResponseInfo extends LKRequestInfo {

	/**
	 * 构造方法
	 * @param requestInfo LKRequestInfo
	 */
	public LKResponseInfo(LKRequestInfo requestInfo, LKResponseBean<Object> responseBean) {
		requestId = requestInfo.requestId;
		requestTime = requestInfo.requestTime;
		requestUri = requestInfo.requestUri;
		requestIp = requestInfo.requestIp;
		handlerClassName = requestInfo.handlerClassName;
		handlerMethod = requestInfo.handlerMethod;
		exceptionClassName = requestInfo.exceptionClassName;
		exceptionMessage = requestInfo.exceptionMessage;
		responseTime = DateTime.now();
		elapsedTime = responseTime.getMillis() - requestTime.getMillis();
		this.responseBean = responseBean;
	}


	/** 响应时间 */
	@JsonSerialize(using = LKJsonSerializer4DateTime.class)
	@JsonDeserialize(using = LKJsonDeserializer4DateTime.class)
	protected DateTime responseTime;

	/** 耗时 */
	protected long elapsedTime;

	/** 响应信息 */
	protected LKResponseBean<Object> responseBean;

}
