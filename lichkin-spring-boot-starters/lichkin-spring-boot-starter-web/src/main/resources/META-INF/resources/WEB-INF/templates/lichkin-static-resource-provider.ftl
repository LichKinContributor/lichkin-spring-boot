<#--
	将URL解析成增加了MD5及版本信息的URL
	@param url 待解析地址。
	@param type 类型。js/css。
-->
<#function provideUrl url="",type="">
	<#assign result = "${url}">

	<#if type!="">
		<#if result=="">
			<#-- 没有输入地址，处理为当前页面对应的脚本文件。 -->
			<#assign result = "${provider.getForLookupPath('${ctx}/res/${type}${mappingUri}.${type}')}"/>
		<#else>
			<#if type=="js"||type=="css">
				<#-- 补全结尾符 -->
				<#if !result?ends_with(".${type}")>
					<#assign result="${url}.${type}">
				</#if>
			</#if>

			<#if result?starts_with("http")>
				<#-- http开头表示引入站外脚本，直接引入。 -->
				<#assign result = "${result}"/>
			<#elseif result?starts_with("${ctx}/res/${type}")>
				<#-- 符合框架约定的，按照约定引入。 -->
				<#assign result = "${provider.getForLookupPath('${result}')}"/>
			<#elseif result?starts_with("${ctx}/webjars")>
				<#-- 符合框架约定的，按照约定引入。 -->
				<#assign result = "${provider.getForLookupPath('${result}')}"/>
			<#else>
				<#-- 不符合框架约定的，不予引入。 -->
				<#assign result="">
			</#if>
		</#if>
	</#if>

	<#return result/>
</#function>

<#--
	将URL解析成增加了MD5及版本信息的URL
	@param url 待解析地址。
-->
<#function provideJsUrl url="">
	<#assign result = "${provideUrl('${url}','js')}">
	<#return result/>
</#function>

<#-- 创建引用CSS文件的标签 -->
<#macro lichkin@cssTag url="">
	<@lichkin.cssTag "${provideUrl('${url}','css')}"/>
</#macro>

<#-- 创建引用JS文件的标签 -->
<#macro lichkin@jsTag url="">
	<@lichkin.jsTag "${provideJsUrl('${url}')}"/>
</#macro>

<#-- 创建引用IMG文件的标签 -->
<#macro lichkin@imgTag  url,alt="",width="",height="",id="",class="",style="">
	<@lichkin.imgTag "${provideUrl('${url}','img')}","${alt}","${width}","${height}","${id}","${class}","${style}"/>
</#macro>
