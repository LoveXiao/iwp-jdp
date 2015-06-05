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
		<!-- onclick='jump("mainFrame","${ctx}/sys/user/")'-->
		<li class="active"><a href="${ctx}/stay/hotel/">酒店列表</a></li>
		<shiro:hasPermission name="stay:hotel:edit"><li>
		<a href="${ctx}/stay/hotel/form" >酒店添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="hotel" action="${ctx}/stay/hotel/" method="post" role="form" class="form-horizontal breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${hotel.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${hotel.pageSize}"/>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="name">酒店名称：</label>
			<div class="col-sm-4">
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control" placeholder="酒店名称"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="address">酒店地址：</label>
			<div class="col-sm-4">
				<form:input path="address" htmlEscape="false" maxlength="200" class="form-control" placeholder="酒店地址"/>
			&nbsp;
			</div>
			<div class="col-sm-1">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
			</div>
		</div>
	</form:form>
	<div>
		<table id="contentTable" class="table table-striped table-condensed table-bordered">
			<thead><tr><th>店&nbsp;&nbsp;&nbsp;名</th><th>地址</th><th>法人</th><th>联系电话</th><th>固话</th><th>星级</th><th>操作</th></tr></thead>
			<tbody>
			<c:forEach items="${hotels.content}" var="hotel">
				<tr>
					<td><a href="${ctx}/stay/hotel/form?recid=${hotel.recid}">${hotel.name}</a></td>
					<td>${hotel.address}</td>
					<td>${hotel.corporation}</td>
					<td>
						${hotel.phone}
					</td>
						<td>
						${hotel.flexdLine}
					</td>
						<td>
						${hotel.level}
						</td>
					<td>
						<shiro:hasPermission name="stay:hotel:edit">
							<a href="${ctx}/stay/hotel/form?recid=${hotel.recid}">修改</a>
						</shiro:hasPermission>
						<shiro:lacksPermission name="stay:hotel:edit">
							<a href="${ctx}/stay/hotel/form?recid=${hotel.recid}">查看</a>
						</shiro:lacksPermission>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<tags:pagination page="${hotels}" paginationSize="2"  pageSize="${hotel.pageSize}"/>
</body>
</html>