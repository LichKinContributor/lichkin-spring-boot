<#include "/_define.ftl"/>

<#macro html type="">

<#if type!="">
	<#include "html.ftl"/>

	<@html ;section>
		<#if section="DOCTYPE">
			<!DOCTYPE html>
		</#if>
		<#if section="meta">
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
			<#if type=="web">
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
			</#if>
			<#nested "meta"/>
		</#if>
		<#if section="link">
			<link href="${ctx}/res/img/favicon.ico" type="image/x-icon" rel="shortcut icon">
			<@lichkin@cssTag url="/webjars/LichKin-UI/lichkin-${type}${compressSuffix}.css" />
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
			<script type="text/javascript">let _CTX='${ctx}',_LANG='${language!"zh-CN"}',_MAPPING_PAGES='${mappingPages}',_MAPPING_DATAS='${mappingDatas}',_MAPPING_API='${mappingApi}';</script>
		</#if>
		<#if section="javascript-links">
			<@lichkin@jsTag url="/webjars/jquery/jquery${compressSuffix}.js"/>
			<@lichkin@jsTag url="/webjars/LichKin-UI/lichkin${compressSuffix}.js" />
			<@lichkin@jsTag url="/webjars/LichKin-UI/lichkin-${type}${compressSuffix}.js" />
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