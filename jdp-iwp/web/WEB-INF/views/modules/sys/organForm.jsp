<%@page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>单位管理</title>
	<style type="text/css"> 
		.controls{ 
		     display:inline-block;
		} 
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {remote: "${ctx}/sys/organ/checkorganname?oldorganname=" + encodeURIComponent('${organ.name}')},
				},
				messages: {
					name: {remote: "单位名称已存在"},
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			}); 
		});
	</script>
</head>
<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/organ/">单位列表</a></li>
		<li class="active">
			<a href="${ctx}/sys/organ/form">单位<shiro:hasPermission name="sys:organ:edit">添加</shiro:hasPermission>
			<shiro:lacksPermission name="sys:organ:view">查看</shiro:lacksPermission></a>
		</li>
	</ul><br/>
	<form:form modelAttribute="organ" action="${ctx}/sys/organ/save" method="post"class="form-horizontal" id="inputForm">
		<form:hidden path="recid"/>
		<tags:message content="${message}"/>
		<!-- Text input-->
		<div class="form-group">
          <label class="col-sm-2 control-label" for="name">单位名称：</label >
          <div class="col-sm-6">
          	<input id="oldorganname" name="oldorganname" type="hidden" value="${organ.name}"/>
            <form:input path="name" htmlEscape="false" class="form-control required" maxlength="255" placeholder="单位名称" />
          </div>
        </div>
		<!-- Text input-->
		<div class="form-group">
          <label class="col-sm-2 control-label" for="code">单位代码：</label >
          <div class="col-sm-6">
            <form:input path="code" htmlEscape="false" class="form-control required" maxlength="255" placeholder="单位代码" />
          </div>
        </div>
       	<div class="form-group">
          <label class="col-sm-2 control-label" for="code">单位排序：</label >
          <div class="col-sm-6">
            <form:input path="sort" htmlEscape="false" class="form-control required" maxlength="100" placeholder="排序" />
          </div>
        </div>
		<div class="form-group" style="margin-top:8px;">
			<div class="col-sm-offset-2 col-sm-6">
				<shiro:hasPermission name="sys:organ:edit">   
				<form:button id="btnSubmit" class="btn btn-primary" type="submit" >保存</form:button>&nbsp;
				</shiro:hasPermission>
				<form:button id="btnCancel" class="btn" type="button" onclick="history.go(-1)">返 回</form:button>
			</div>
		</div>
	</form:form>
	

</body>

</html>