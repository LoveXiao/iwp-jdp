<%@page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>用户管理</title>
	<style type="text/css">
		.sort{color:#0663A2;cursor:pointer;}
		.controls{ 
		     display:inline-block;
		}
	</style>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			if(s != null && s != "")
				$("#pageSize").val(s);
			$("#searchForm").submit();
    		return false;
   		}
		$(document).ready(function() {
			$("#btnImport").click(function(){
				$("#importBox").removeClass("hide");
			});
			$("#btnExport").click(function(){
				var d = dialog({
				    title: '系统提示',
				    content: '确认要导出用户数据吗？',
				    okValue: '确定',
				    ok: function () {
				    	$("#searchForm").attr("action","${ctx}/sys/user/export");
						$("#searchForm").submit();
						$("#searchForm").attr("action","${ctx}/sys/user/");
				    },
				    cancelValue: '取消',
				    cancel: function () {}
				});
				d.show();
			});
		});
	</script>
</head>
<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/sys/user/import" method="post" enctype="multipart/form-data"
			style="padding-left:20px;text-align:center;" class="form-search" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/sys/user/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs"> 
		<!-- onclick='jump("mainFrame","${ctx}/sys/user/")'-->
		<li class="active"><a href="${ctx}/sys/user/">用户列表</a></li>
		<shiro:hasPermission name="sys:user:edit"><li>
		<!-- onclick='jump("mainFrame","${ctx}/sys/user/form")' -->
		<a href="${ctx}/sys/user/form" >用户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/" method="post" class="breadcrumb form-search form-horizontal">
		<input id="pageNo" name="pageNo" type="hidden" value="${user.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${user.pageSize}"/>
	   <div class="form-group">
			<label class="col-sm-1 control-label" for="organ.recid" >归属部门：</label>
			<tags:treeselect id="organ" name="organ.recid" value="${user.organ.recid}" labelName="organ.name" labelValue="${user.organ.name}" 
				title="部门" url="/sys/organ/treeData" cssClass="form-control" allowClear="true" checked="true" divClass="col-sm-3"/>
			<label class="col-sm-1 control-label" for="username">登录名：</label>
       		<div class="col-sm-3">
				<form:input path="username" htmlEscape="false" maxlength="50" class="form-control" placeholder="输入用户登陆名"/>
			</div>
		</div>
	  	<div class="form-group">
       		<label class="col-sm-1 control-label" for="username">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</label>
       		<div class="col-sm-7">
       			<div class="input-group">
					<form:input path="name" htmlEscape="false" maxlength="50" class="form-control" placeholder="输入用户姓名"/>
					<span class="input-group-btn">
		       			 &nbsp;<button id="btnSubmit" class="btn btn-primary " type="submit" value="查询">查询</button>
		       			 &nbsp;<button id="btnExport" class="btn btn-primary " type="button" value="导出">导出</button>
		       			 &nbsp;<button id="btnImport" class="btn btn-primary " type="button" value="导入">导入</button>
		       		 </span>
				</div>
			</div>
		</div>
	</form:form>
	<div>
		<table id="contentTable" class="table table-striped table-condensed table-bordered">
			<thead><tr><th>姓&nbsp;&nbsp;&nbsp;名</th><th>登陆名</th><th>部门</th><th>所属角色</th><th>操作</th></tr></thead>
			<tbody>
			<c:forEach items="${users.content}" var="user">
				<tr>
					<td><a href="${ctx}/sys/user/form?recid=${user.recid}">${user.name}</a></td>
					<td>${user.username}</td>
					<td>${user.organ.name}</td>
					<td>
						<c:forEach items="${user.roleList}" var ="role">
						 ${role.nameMng}&nbsp;
						</c:forEach>
					</td>
					<td>
						<shiro:hasPermission name="sys:user:edit">
							<a href="${ctx}/sys/user/delete?recid=${user.recid}">删除</a>  
							<a href="${ctx}/sys/user/form?recid=${user.recid}">修改</a>
						</shiro:hasPermission>
						<shiro:lacksPermission name="sys:user:view">
							<a href="${ctx}/sys/user/form?recid=${user.recid}">查看</a>
						</shiro:lacksPermission>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<tags:pagination page="${users}" paginationSize="2" pageSize="${user.pageSize}"/>
</body>
</html>