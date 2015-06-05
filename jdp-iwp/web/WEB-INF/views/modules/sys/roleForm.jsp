<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html><html>
<head>
	<title>角色信息</title>
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
					name: {remote: "${ctx}/sys/role/checkRolename?oldrolename=" + encodeURIComponent('${role.name}')},
				},
				messages: {
					name: {remote: "角色名称已存在"},
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/role/">角色列表</a></li>
		<li class="active"><a href="${ctx}/sys/role/form?recid=${role.recid}">角色<shiro:hasPermission name="sys:role:edit">${not empty role.recid?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:role:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form modelAttribute="role" action="${ctx}/sys/role/save" method="post"class="form-horizontal" id="inputForm">
		<form:hidden path="recid"/>
		<tags:message content="${message}"/>
	    <div class="form-group">
          <label class="col-sm-2 control-label" for=menuIds>权限：</label >
           <tags:treeselect id="menuIds" name="menuIds" value="${role.menuIds}" labelName="${role.menuNames}" labelValue="${role.menuNames}"
			title="菜单" url="/sys/menu/treeData" cssClass="form-control required" checked="true" divClass="col-sm-6"/>
        </div>
		<div class="form-group">
          <label class="col-sm-2 control-label" for="name">角色名称：</label >
          <div class="col-sm-6">
            <form:input path="name" htmlEscape="false" class="form-control required" maxlength="50" placeholder="角色名称" />
          </div>
        </div>
		<div class="form-group">
          <label class="col-sm-2 control-label" for="nameMng">角色别名：</label >
          <div class="col-sm-6">
            <form:input path="nameMng" htmlEscape="false" class="form-control required" maxlength="50" placeholder="角色别名" />
          </div>
        </div>
		<div class="form-group" style="margin-top:8px;">
			<div class="col-sm-offset-2 col-sm-6">
			<!-- <shiro:hasPermission name="sys:role:edit">  </shiro:hasPermission> -->
				<form:button id="btnSubmit" class="btn btn-primary " type="submit" >保存</form:button>&nbsp;
				<form:button id="btnCancel" class="btn" type="button" onclick="history.go(-1)">返 回</form:button>
			</div>
		</div>
	</form:form>
</body>
</html>