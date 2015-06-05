<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预警信息</title>
	<%@ include file="/WEB-INF/views/include/ueditor.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
	       var ue = UE.getEditor('editor');
	       ue.ready(function(){
		    	UE.getEditor('editor').setContent('${warn.content}');    
			});
			$("#name").focus();
			$("#inputForm").validate({
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
	<!-- 
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/warnInfo/warn/">预警信息列表</a></li>
		<li class="active"><a href="${ctx}/warnInfo/warn/form?recid=${warn.recid}">预警信息<shiro:hasPermission name="warnInfo:warn:edit">${not empty warn.recid?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="warnInfo:warn:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	 -->
	<form:form modelAttribute="warn" action="${ctx}/warnInfo/warn/save" method="post" class="form-horizontal" role="form" id="inputForm">
		<form:hidden path="recid"/>
		<tags:message content="${message}"/>
		<div class="form-group">
          <!-- Text input-->
          <label class="col-sm-2 control-label" for="title">预警标题：</label >
          <div class="col-sm-6">
            <form:input path="title" htmlEscape="false" class="form-control  required" maxlength="50" placeholder="预警标题" />
          </div>
        </div>
    <div class="form-group">
          <!-- Text input-->
          <label class="col-sm-2 control-label" for="author">创建人：</label>
          <div class="col-sm-6">
             <form:input path="author" htmlEscape="false" class="form-control  required"  maxlength="50" placeholder="创建人" />
          </div>
    </div>
  	<div class="form-group">
		<label class="col-sm-2 control-label" for="issueClient">发布平台:</label>
		<div class="col-sm-6">
			<form:checkboxes path="issueClient" items="${allIssueClient}" itemLabel="label" itemValue="recid" htmlEscape="false" class="required"/>
		</div>
	</div>
    <div class="form-group">
    	<label class="col-sm-2 control-label" for="content">发布内容:</label>
    	
    	 <script id="editor" type="text/plain" class ="col-sm-10 edui-default" style="width:880px;height:350px;" name="content"></script>

     </div>
	<div class="form-group">
		<!-- <shiro:hasPermission name="sys:warn:edit">  </shiro:hasPermission> -->
		<div class="col-sm-offset-2 col-sm-10">
			<form:button id="btnSubmit" class="btn btn-primary" type="submit" >保存</form:button>&nbsp;
			<form:button id="btnCancel" class="btn" type="button" onclick="history.go(-1)">返 回</form:button>
		</div>
	</div>
	</form:form>
</body>
</html>