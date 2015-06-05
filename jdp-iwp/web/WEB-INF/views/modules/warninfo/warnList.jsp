<%@page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>酒店管理</title>
	<style type="text/css">
		.sort{color:#0663A2;cursor:pointer;}
	</style>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			if(s != null && s != "")
				$("#pageSize").val(s);
			$("#searchForm").submit();
    		return false;
   		}
	</script>
</head>
<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<ul class="nav nav-tabs"> 
	 
		<li class="active"><a href="${ctx}/warnInfo/warn/">预警信息列表</a></li>
		<!-- 
		<shiro:hasPermission name="warnInfo:warn:edit"><li>
		<a href="${ctx}/warnInfo/warn/form" >预警信息添加</a></li></shiro:hasPermission>
		-->
	</ul>
	<form:form id="searchForm" modelAttribute="warn" action="${ctx}/warnInfo/warn/" method="post" role="form" class="form-horizontal breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${warn.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${warn.pageSize}"/>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="title">预警标题：</label>
			<div class="col-sm-4">
				<form:input path="title" htmlEscape="false" maxlength="50" class="form-control" placeholder="预警标题"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="author">创建人：</label>
			<div class="col-sm-4">
				<form:input path="author" htmlEscape="false" maxlength="200" class="form-control" placeholder="创建人"/>
			&nbsp;
			</div>
			<div class="col-sm-1">
			<form:button id="btnSubmit" class="btn btn-primary" type="submit" >查询</form:button>&nbsp;
				<shiro:hasPermission name="warnInfo:warn:edit">
					<a class="btn btn-primary"  href="${ctx}/warnInfo/warn/form" >预警信息添加</a>
				</shiro:hasPermission>
			</div>
		</div>
	</form:form>
	<div>
		<table id="contentTable" class="table table-striped table-condensed table-bordered">
			<thead><tr><th>预警标题</th><th>创建人</th><th>发布平台</th><th>操作</th></tr></thead>
			<tbody>
			<c:forEach items="${warns.content}" var="warn">
				<tr>
					<td><a href="${ctx}/warnInfo/warn/form?recid=${warn.recid}">${warn.title}</a></td>
					<td>${warn.author}</td>
					<td>
						<c:forEach items="${warn.issueClientDicts}" var ="dict">
						 ${dict.label}&nbsp;
						</c:forEach>
					</td>
					<td>
						<shiro:hasPermission name="warnInfo:warn:edit">
							<a href="${ctx}/warnInfo/warn/form?recid=${warn.recid}">修改</a>
						</shiro:hasPermission>
						<shiro:lacksPermission name="warnInfo:warn:edit">
							<a href="${ctx}/warnInfo/warn/form?recid=${warn.recid}">查看</a>
						</shiro:lacksPermission>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<tags:pagination page="${warns}" paginationSize="2"  pageSize="${warn.pageSize}"/>
</body>
</html>