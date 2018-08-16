<#include "/_define.ftl"/>

<#macro html type="",css=true,js=false,i18nJs=true,i18nJsAddition=true,iconsJs=true,iconsJsAddition=true>
	<#include "html-lichkin-simple.ftl"/>

	<@html type=type css=css js=js i18nJs=i18nJs i18nJsAddition=i18nJsAddition iconsJs=iconsJs iconsJsAddition=iconsJsAddition;section>
		<#if section="link-after-self">
			<@lichkin@cssTag url="/res/css/admin/index-addition" />
		</#if>
		<#if section="body-content">
			<#nested "body-content"/>
		</#if>
		<#if section="javascript-links">
			<@lichkin@jsTag url="/webjars/spark-md5/spark-md5" />
		</#if>
		<#if section="javascript-contents-after-links">
			<#nested "javascript-contents-after-links-bofore-init"/>
$('<img src="' + _CTX + '/res/img/index/logo.png" />').appendTo('.logoBox');

LK.UI.text({
  $appendTo : $('.copyrightBox'),
  original : true,
  text : $.LKGetI18N('copyright') + ' &copy;' + $.LKGetI18N('COPYRIGTH') + ' 2018-' + new Date().getFullYear()
});

$('.systemName').html($.LKGetI18N('systemName'));

var $loginName = $('input[name=loginName]');
$loginName.attr('placeholder', $.LKGetI18N('NoLoginName'));

var $pwd = $('input[name=pwd]');
$pwd.attr({
  'placeholder' : $.LKGetI18N('NoPassword'),
  'type' : 'password'
});

$loginBtn.click(function(e) {
  e.preventDefault();
  var loginName = $loginName.val();
  var pwd = $pwd.val();
  if (loginName == '') {
    LK.alert('NoLoginName');
    return;
  }
  if (pwd == '') {
    LK.alert('NoPassword');
    return;
  }
  LK.ajax({
    url : '/Admin/AccountLogin',
    data : {
      loginName : loginName,
      pwd : SparkMD5.hash(pwd)
    },
    success : function() {
      beforeToHome();
      setTimeout(function() {
        window.location.href = _CTX + "/admin/home" + _MAPPING_PAGES
      }, 1000);
    }
  });
});
			<#nested "javascript-contents-after-links-after-init"/>
		</#if>
	</@html>

</#macro>