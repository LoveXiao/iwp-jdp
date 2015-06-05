<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html><html>
<head>
	<title>角色信息</title>
	<script type="text/javascript">
		$(document).ready(function() {
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/stay/hotel/">酒店列表</a></li>
		<li class="active"><a href="${ctx}/stay/hotel/form?recid=${hotel.recid}">酒店<shiro:hasPermission name="stay:hotel:edit">${not empty hotel.recid?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="stay:hotel:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form modelAttribute="hotel" action="${ctx}/stay/hotel/save" method="post" class="form-horizontal" role="form" id="inputForm">
		<form:hidden path="recid"/>
		<tags:message content="${message}"/>
		<div class="form-group">
          <!-- Text input-->
          <label class="col-sm-2 control-label" for="name">酒店名称：</label >
          <div class="col-sm-6">
            <form:input path="name" htmlEscape="true" class="form-control required" maxlength="50" placeholder="酒店名称" />
          </div>
        </div>
    <div class="form-group">
          <!-- Text input-->
          <label class="col-sm-2 control-label" for="corporation">酒店法人：</label>
          <div class="col-sm-6">
             <form:input path="corporation" htmlEscape="false" class="form-control  required"  maxlength="50" placeholder="酒店法人" />
          </div>
    </div>
    <div class="form-group">
          <!-- Text input-->
          <label class="col-sm-2 control-label" for="address">酒店地址：</label>
          <div class="col-sm-6">
            <form:input path="address" htmlEscape="false" class="form-control required" maxlength="200" placeholder="酒店地址" />
          </div>
     </div>
    <div class="form-group">
          <!-- Text input-->
          <label class="col-sm-2 control-label" for="phone">联系电话：</label>
          <div class="col-sm-6">
            <form:input path="phone" htmlEscape="false" class="form-control  required" maxlength="20" placeholder="联系电话" />
          </div>
      </div>
    <div class="form-group">
          <!-- Text input-->
          <label class="col-sm-2 control-label" for="flexdLine">固定电话：</label>
          <div class="col-sm-6">
            <form:input path="flexdLine" htmlEscape="false" class="form-control  required" maxlength="20" placeholder="固定电话" />
          </div>
      </div>
          <div class="form-group">
          <!-- Text input-->
          <label class="col-sm-2 control-label" for="level">酒店等级：</label>
          <div class="col-sm-6">
            <form:input path="level" htmlEscape="false" class="form-control  required" maxlength="20" placeholder="酒店星级 "/>
          </div>
      </div>
          <div class="form-group">
          <!-- Text input-->
          <label class="col-sm-2 control-label" for="description">酒店简介：</label>
          <div class="col-sm-6">
            <form:textarea  path="description" htmlEscape="false" class="form-control  required" maxlength="200" placeholder="酒店简介" />
          </div>
      </div>
		
		<div class="form-group" style="margin-top:8px;">
			<div class="col-sm-offset-2 col-sm-6">
			<!-- <shiro:hasPermission name="sys:hotel:edit">  </shiro:hasPermission> -->
				<form:button id="btnSubmit" class="btn btn-primary" type="submit" >保存</form:button>&nbsp;
				<form:button id="btnCancel" class="btn" type="button" onclick="history.go(-1)">返 回</form:button>
			</div>
		</div>
	</form:form>
</body>
</html>