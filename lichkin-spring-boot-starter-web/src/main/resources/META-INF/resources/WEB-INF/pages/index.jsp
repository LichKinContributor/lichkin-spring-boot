<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html id="lichkin-html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<link href="${ctx}/res/img/favicon.ico" type="image/x-icon" rel="shortcut icon">
<style type="text/css">
.companyName {
  font-size: 16pt;
  color: #FFFFFF;
  padding-top: 16pt;
  padding-bottom: 16pt;
}

.copyright {
  font-size: 10pt;
  color: #67AEE6;
}

#topDiv {
  background-color: #67AEE6;
  text-align: center;
}

#centerDiv {
  text-align: center;
  vertical-align: top;
}

#bottomDiv {
  background-color: #E6F4FF;
  text-align: center;
  padding-top: 10pt;
  padding-bottom: 10pt;
}

#logo {
  width: 500px;
  height: 500px;
}
</style>
<script type="text/javascript">
  var onBodyloaded = function() {
    var w = document.documentElement.clientWidth;
    var h = document.documentElement.clientHeight;
    var topHeight = document.getElementById('topDiv').offsetHeight;
    var bottomHeight = document.getElementById('bottomDiv').offsetHeight;
    var size = h - topHeight - bottomHeight;
    if (w > h) {
      document.getElementById('logo').style.width = document.getElementById('logo').style.height = size + 'px';
    } else {
      document.getElementById('centerDiv').style.height = size + 'px';
      document.getElementById('logo').style.width = document.getElementById('logo').style.height = w + 'px';
      document.getElementById('logo').style.marginTop = (size - w) / 2 + 'px';
    }
  };
</script>
</head>
<body id="lichkin-body" class="lichkin-body" onload="onBodyloaded();">
  <div id="topDiv">
    <div class="companyName">苏州鑫宏利业信息科技有限公司</div>
  </div>
  <div id="centerDiv">
    <img id="logo" src="/res/img/favicon.ico" class="lichkin-img" />
    <div id="bottomDiv">
      <div class="copyright">©2014-2018</div>
      <div class="copyright">SuZhou LichKin Information Technology Co., Ltd.</div>
    </div>
  </div>
</body>
</html>
