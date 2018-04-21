<#macro html@attr name,value=""><#if value!=""> ${name}="${value}"</#if></#macro>
<#macro html@id value=""><@html@attr "id","${value}"/></#macro>
<#macro html@name value=""><@html@attr "name","${value}"/></#macro>
<#macro html@class value=""><@html@attr "class","${value}"/></#macro>
<#macro html@style value=""><@html@attr "style","${value}"/></#macro>
<#macro html@value value=""><@html@attr "value","${value}"/></#macro>

<#-- 创建引用CSS文件的标签 -->
<#macro cssTag url>
	<link rel="stylesheet" type="text/css" href="${url}" />
</#macro>

<#-- 创建引用JS文件的标签 -->
<#macro jsTag url>
	<script type="text/javascript" src="${url}"></script>
</#macro>

<#-- 创建引用IMG文件的标签 -->
<#macro imgTag url,alt="",width="",height="",id="",class="",style="">
	<img src="${url}"<@html@attr "alt","${alt}"/><@html@attr "width","${width}"/><@html@attr "height","${height}"/><@html@id "${id}"/>class="lichkin-img ${class}"<@html@style "${style}"/> />
</#macro>

<#-- 创建script的标签 -->
<#macro scriptTag>
	<script type="text/javascript">
		<#nested "content"/>
	</script>
</#macro>
