<#-- HTML文档基本结构，使用宏定义模板后，格式会相当完美。 -->
<#macro html>

<#-- DOCTYPE定义 -->
<#nested "DOCTYPE"/>
<html id="lichkin-html">
	<head>
		<#-- 文档编码、兼容模式、屏幕缩放等定义。 -->
		<#nested "meta"/>
		<#-- 图标、样式文件等引入。 -->
		<#nested "link"/>
		<#-- 显示的样式定义，虽然应该将样式都写入到css文件中，但是有些时候使用style还是很有必要的。 -->
		<#nested "style"/>
	</head>
		<#-- 自定义body的属性，可能会用到style、class、onXxx等。 -->
	<body id="lichkin-body" class="lichkin-body" <#nested "body-attributes"/>>
		<#-- HTML文本body以内的内容 -->
		<#nested "body-content"/>
		<#-- 脚本代码 -->
		<script id="lichkin-javascript-contents-before-links" type="text/javascript">
			<#nested "javascript-contents-before-links"/>
		</script>
		<#-- 脚本文件引入，此处引用可减少脚本文件的DOM操作代码。 -->
		<#nested "javascript-links"/>
		<#-- 脚本代码 -->
		<script id="lichkin-javascript-contents-after-links" type="text/javascript">
			<#nested "javascript-contents-after-links"/>
		</script>
	</body>
</html>

</#macro>