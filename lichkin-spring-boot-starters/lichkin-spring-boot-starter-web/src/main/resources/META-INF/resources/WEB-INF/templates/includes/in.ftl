<#assign
  ctx=request.getContextPath()
  res="${ctx}/res"
  js="${res}/js"
  css="${res}/css"
  img="${res}/img"
  webjars="${ctx}/webjars"
>

<link href="${img}/favicon.ico" type="image/x-icon" rel="shortcut icon">

<script type="text/javascript">
  var _CTX = '${ctx}', _RES = '${res}', _JS = '${js}', _CSS = '${css}', _IMG = '${img}', _WEBJARS = '${webjars}';
</script>
<script type="text/javascript" src="${webjars}/lichkin${compressSuffix}.js?_$=${requestSuffix}"></script>