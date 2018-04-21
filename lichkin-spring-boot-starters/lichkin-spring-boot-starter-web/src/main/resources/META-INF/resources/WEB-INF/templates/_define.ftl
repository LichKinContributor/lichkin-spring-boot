<#-- 只做定义 -->

<#-- 引入宏文件 -->
<#import "/spring.ftl" as spring/>
<#import "/lichkin.ftl" as lichkin/>

<#-- 参数定义 -->
<#assign
  ctx=request.getContextPath()
  requestUri=springMacroRequestContext.requestUri
  mappingUri=requestUri?substring(0,requestUri?index_of('.html'))
>

<#-- 引入宏文件，此处的宏需要依赖于环境，不能使用import。 -->
<#include "/lichkin-static-resource-provider.ftl"/>
