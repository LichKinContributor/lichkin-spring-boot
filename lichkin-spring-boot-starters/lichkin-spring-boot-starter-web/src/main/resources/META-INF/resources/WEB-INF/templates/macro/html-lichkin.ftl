<#-- 内嵌页面的HTML文档基本结构扩展，只有body以内的内容会被使用,本质上只是HTML文档片段。 -->
<#-- 引入宏文件 -->
<#import "/spring.ftl" as spring/>
<#import "/lichkin.ftl" as lichkin/>

<#-- 参数定义 -->
<#assign
  ctx=request.getContextPath()
  requestUri=springMacroRequestContext.requestUri
  mappingUri=requestUri?substring(0,requestUri?index_of('.html'))
  webjarsLichKin="${ctx}/webjars"
>

<#-- 引入宏文件，此处的宏需要依赖于环境，不能使用import。 -->
<#include "/lichkin-static-resource-provider.ftl"/>

<#macro html type="">

<#if type!="">
	<#include "html.ftl"/>

	<@html ;section>
		<#if section="DOCTYPE">
			<!DOCTYPE html>
		</#if>
		<#if section="meta">
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<meta name="viewport" content="width=device-width, initial-scale=1">
			<#if type=="web">
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
			</#if>
			<#nested "meta"/>
		</#if>
		<#if section="link">
			<link href="${ctx}/res/img/favicon.ico" type="image/x-icon" rel="shortcut icon">
			<@lichkin@cssTag url="${webjarsLichKin}/lichkin-${type}${compressSuffix}.css" />
			<#nested "link"/>
		</#if>
		<#if section="style">
			<style>#lichkin-html{padding:0px;margin:0px;border:none;}.lichkin-body{padding:0px;margin:0px;border:none;}</style>
			<style>
				<#nested "style"/>
			</style>
		</#if>
		<#if section="body-attributes"><#nested "body-attributes"/></#if>
		<#if section="body-content">
			<#nested "body-content"/>
			<script type="text/javascript">let _CTX='${ctx}';</script>
		</#if>
		<#if section="javascript-links">
			<@lichkin@jsTag url="${ctx}/webjars/jquery-3.3.1/jquery-3.3.1.min.js"/>
			<@lichkin@jsTag url="${webjarsLichKin}/lichkin${compressSuffix}.js" />
			<@lichkin@jsTag url="${webjarsLichKin}/lichkin-${type}${compressSuffix}.js" />
			<#nested "javascript-links"/>
		</#if>
		<#if section="javascript-contents">
			<#nested "javascript-contents"/>
		</#if>
	</@html>
<#else>
	<div id="${mappingUri}" class="lichkin-body" <#nested "body-attributes"/>>
		<#nested "link"/>
		<style>
			<#nested "style"/>
		</style>
		<#nested "body-content"/>
		<#nested "javascript-links"/>
		<script type="text/javascript">
			<#nested "javascript-contents"/>
		</script>
	</div>
</#if>

</#macro>