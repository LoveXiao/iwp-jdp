<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="nodesLevel" type="java.lang.String" required="false" description="菜单展开层数"%>
<%@ attribute name="nameLevel" type="java.lang.String" required="false" description="返回名称关联级别"%>
<%@ attribute name="divClass" type="java.lang.String" required="false" description="div css样式"%>
<div class="${divClass}">
	<div class="input-group">
		<input id="${id}Id" name="${name}" class="${cssClass}" type="hidden" value="${value}"${disabled eq 'true' ? ' disabled=\'disabled\'' : ''}/>
		<input id="${id}Name" name="${labelName}" readonly="readonly" type="text" value="${labelValue}" maxlength="100"${disabled eq "true"? " disabled=\"disabled\"":""}"
			class="${cssClass}" style="${cssStyle}"/>
			<span class="input-group-btn">
	       		 <button id="${id}Button" class="btn btn-default ${disabled eq 'true' ? ' disabled' : ''}" type="button" onclick="showTree()"><i class="icon-search"></i></button>
	      </span>
	</div>
</div>
<script type="text/javascript">
 	function showTree(){
	 if ($("#${id}Id").attr("disabled")){
			return true;
		}
     var nameLevel = ${nameLevel eq null ? "1" : nameLevel};
		top.dialog({
			url: "${ctx}/tag/treeselect?url="+encodeURIComponent("${url}")+"&module=${module}&checked=${checked}&extId=${extId}&nodesLevel=${nodesLevel}&selectIds="+$("#${id}Id").val(),
			title: '选择${title}',
			width: 300,
			height: 420,
			//data: $('#input').val(), // 给 iframe 的数据
			ok: function () {
		    	var tree = this.iframeNode.contentWindow.tree;
		    	var ids = [], names = [], nodes = [];
				if ("${checked}" == "true"){
					nodes = tree.getCheckedNodes(true);
				}else{
					nodes = tree.getSelectedNodes();
				}
				for(var i=0; i<nodes.length; i++) {
					if ("${checked}" == "true" && nodes[i].isParent){
						continue; // 如果为复选框选择，则过滤掉父节点
					}
					if ("${notAllowSelectRoot}" =="true" && nodes[i].level == 0){
						dialog({content:"不能选择根节点（"+nodes[i].name+"）请重新选择。"}).show();
						return false;
					}
					if ( "${notAllowSelectParent}" == "true" && nodes[i].isParent){
						dialog({content:"不能选择父节点（"+nodes[i].name+"）请重新选择。"}).show();
						return false;
					}
					//${not empty module && selectScopeModule} && 
					if (nodes[i].module == ""){
						dialog({content:"不能选择公共模型（"+nodes[i].name+"）请重新选择。"}).show();
						return false;
					}else if ("${module}" !="" && nodes[i].module != "${module}"){
						dialog({content:"不能选择当前栏目以外的栏目模型，请重新选择。"}).show();
						return false;
					}
					ids.push(nodes[i].id);
                 var t_node = nodes[i];
                 var t_name = "";
                 var name_l = 0;
                 do{
                     name_l++;
                     t_name = t_node.name + " " + t_name;
                     t_node = t_node.getParentNode();
                 }while(name_l < nameLevel);
					names.push(t_name);
					if("${checked}" != "true")
						break; // 如果为非复选框选择，则返回第一个选择  
				}
				$("#${id}Id").val(ids);
				$("#${id}Name").val(names);
		    },
			cancel:function(){},
			okValue: '确定',
			cancelValue: '取消',
			/* onclose: function () {
				that.close().remove();	
			}, */
			/* oniframeload: function () {
			} */
		}).showModal();
		//return false;
 	};

	/* $("#${id}Button").click(function (){
	}); */

</script>
