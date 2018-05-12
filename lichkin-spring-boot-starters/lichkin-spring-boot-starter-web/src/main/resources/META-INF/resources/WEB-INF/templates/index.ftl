<#include "/macro/html-lichkin.ftl"/>

<@html ;section>
	<#if section="link">
		<#-- 引入对应的样式文件 -->
		<@lichkin@cssTag/>
	</#if>
	<#if section="style">
	</#if>
	<#if section="body-attributes">style="background-color:#ffffff;"</#if>
	<#if section="body-content">
	  <div id="topDiv">
	    <div class="companyName">苏州鑫宏利业信息科技有限公司</div>
	  </div>
	  <div id="centerDiv">
	  	<@lichkin@imgTag id="logo" url="/res/img/favicon.ico"/>
	  </div>
	  <div id="bottomDiv">
	    <div class="copyright">©2014-2018</div>
	    <div class="copyright">SuZhou LichKin Information Technology Co., Ltd.</div>
	  </div>
	</#if>
	<#if section="javascript-links">
		<#-- 引入对应的脚本文件 -->
		<@lichkin@jsTag/>
	</#if>
	<#if section="javascript-contents">
	</#if>
</@html>