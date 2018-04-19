<#include "/includes/in.ftl">
<link rel="stylesheet" type="text/css" href="${webjars}/lichkin-app${compressSuffix}.css?_$=${requestSuffix}">
<script type="text/javascript" src="${webjars}/lichkin-app${compressSuffix}.js?_$=${requestSuffix}"></script>
<script type="text/javascript">
  // 使用WebViewJavascriptBridge时，需先监听事件初始化。
  document.addEventListener('WebViewJavascriptBridgeReady', function onBridgeReady(event) {
    event.bridge.init();
  }, false);
</script>