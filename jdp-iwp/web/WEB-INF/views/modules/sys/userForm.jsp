<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css"> 
		.controls{ 
		     display:inline-block;
		} 
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#username").focus();
			$("#inputForm").validate({
				rules: {
					username: {remote: "${ctx}/sys/user/checkUsername?oldusername=" + encodeURIComponent('${user.username}')},
				},
				messages: {
					username: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
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
		<li><a href="${ctx}/sys/user/">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${user.recid}">用户<shiro:hasPermission name="sys:user:edit">${not empty user.recid?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<form:hidden path="recid"/>
		<tags:message content="${message}"/>
		<div class="form-group">
			<label class="col-sm-2 control-label" for= "organ">所属单位:</label>
			<tags:treeselect id="organ" name="organ.recid" value="${user.organ.recid}" labelName="organ.name" labelValue="${user.organ.name}"
				title="公司" url="/sys/organ/treeData" cssClass="form-control required"  divClass="col-sm-6"/>
		</div>
		<div class="form-group">
          <label class="col-sm-2 control-label" for="oldusername">登&nbsp;&nbsp;录&nbsp;名:</label >
          <div class="col-sm-6">
          		<input id="oldusername" name="oldusername" type="hidden" value="${user.username}">
				<form:input path="username" htmlEscape="false" maxlength="50" class="required username form-control required" placeholder="用户登陆名"/>
          </div>
        </div>
       	<div class="form-group">
          <label class="col-sm-2 control-label" for="name">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label >
          <div class="col-sm-6">
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required" placeholder="用户姓名" />
          </div>
        </div>
       	<div class="form-group">
          <label class="col-sm-2 control-label" for="newpassword">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:</label >
          <div class="col-sm-6">
				<input id="newpassword" name="newpassword" type="password" value="" maxlength="50" minlength="3" class="${empty user.recid?'required':''} form-control" placeholder="密码"/>
				<c:if test="${not empty user.recid}"><span class="help-inline">若不修改密码，请留空。</span></c:if>
          </div>
        </div>
	     	<div class="form-group">
          <label class="col-sm-2 control-label" for="confirmNewPassword">确认密码:</label >
          <div class="col-sm-6">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" equalTo="#newpassword" class="form-control" placeholder="确认密码" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="roleIdList">用户角色:</label>
			<div class="col-sm-6">
				<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="recid" htmlEscape="false" class="required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="remarks">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
			<div class="col-sm-6">
				<form:textarea path="remarks" maxlength="200" htmlEscape="false" class="form-control" placeholder="用户备注"/>
			</div>	
		</div>
		<div class="form-group" style="margin-top:8px;">
			<div class="col-sm-offset-2 col-sm-6">
			<!-- <shiro:hasPermission name="sys:user:edit">  </shiro:hasPermission> -->
				<form:button id="btnSubmit" class="btn btn-primary " type="submit" >保存</form:button>&nbsp;
				<form:button id="btnCancel" class="btn" type="button" onclick="history.go(-1)">返 回</form:button>
			</div>
		</div>
	</form:form>
</body>
</html>