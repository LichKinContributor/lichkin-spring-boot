<#include "/_define.ftl"/>

<#macro html type="">

<#assign _$=_$!"">
<#assign calculateType=type>
<#if type=="" && _$=="">
	<#assign calculateType="web">
</#if>

<#if calculateType!="">
	<#include "html-lichkin-simple.ftl"/>

	<@html ;section>
		<#if section="meta">
			<#nested "meta"/>
		</#if>
		<#if section="link">
			<#nested "link"/>
			<@lichkin@cssTag/>
		</#if>
		<#if section="style">
			<#nested "style"/>
		</#if>
		<#if section="body-attributes"><#nested "body-attributes"/></#if>
		<#if section="body-content">
			<#nested "body-content"/>
		</#if>
		<#if section="javascript-contents-before-links">
			<#nested "javascript-contents-before-links"/>
		</#if>
		<#if section="javascript-links">
			<#nested "javascript-links"/>
			<@lichkin@jsTag url="/res/js/index/i18n/${locale}.js" />
			<@lichkin@jsTag url="/res/js/index/i18n/addition/${locale}.js" />
			<@lichkin@jsTag url="/webjars/spark-md5/spark-md5${compressSuffix}.js" />
			<@lichkin@jsTag/>
		</#if>
		<#if section="javascript-contents-after-links">
			<#nested "javascript-contents-after-links"/>
		</#if>
	</@html>
</#if>

</#macro>