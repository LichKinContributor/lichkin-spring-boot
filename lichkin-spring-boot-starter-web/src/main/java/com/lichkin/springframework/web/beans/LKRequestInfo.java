package com.lichkin.springframework.web.beans;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lichkin.framework.json.deserializer.LKJsonDeserializer4DateTime;
import com.lichkin.framework.json.serializer.LKJsonSerializer4DateTime;
import com.lichkin.framework.utils.LKRandomUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({ "requestId", "requestTime", "requestUri", "requestIp", "handlerClassName", "handlerMethod", "exceptionClassName", "exceptionMessage" })
public class LKRequestInfo {

	/** 请求ID */
	protected String requestId = LKRandomUtils.create(32);

	/** 请求时间 */
	@JsonSerialize(using = LKJsonSerializer4DateTime.class)
	@JsonDeserialize(using = LKJsonDeserializer4DateTime.class)
	protected DateTime requestTime = DateTime.now();

	/** 请求URI */
	protected String requestUri;

	/** 请求IP */
	protected String requestIp;

	/** 处理类 */
	protected String handlerClassName;

	/** 处理方法 */
	protected String handlerMethod;

	/** 处理异常类名 */
	protected String exceptionClassName;

	/** 处理异常信息 */
	protected String exceptionMessage;

}
