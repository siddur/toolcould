<%@page import="siddur.common.miscellaneous.Paging"%>
<%@page import="siddur.tool.cloud.action.ToolAction"%>
<%@page import="siddur.tool.core.IToolWrapper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<s:site>
<jsp:attribute name="headPart">
<link rel="stylesheet" type="text/css" href="/toolcloud/css/tag.css" />
<style>
	.list{
		margin:7px;
		width:225px;
		height:150px;
		overflow: hidden;
		background-color: #CDCDCD;
		box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.5) ;
		padding:3px;
	}
	
	.list:hover{
		cursor:pointer;
	}
	
	.paging{
		clear:both;
	}
}
</style>
<script>
	var url = "/toolcloud/ctrl/tool/list";
	var onlyForMine = function(obj){
		if(obj.checked){
			url = "/toolcloud/ctrl/tool/mine";
		}else{
			url = "/toolcloud/ctrl/tool/list";
		}
		location.href = url;
	}
</script>
</jsp:attribute>
<jsp:body>
<div class="screen">
	<div style="text-align:right">
		<input type="checkbox" id="mytoolsck" onchange="onlyForMine(this)" <c:if test="${mine}">checked="checked"</c:if>>
		<label for="mytoolsck" style="font-size: 15px;">仅显示我发布的工具</label>
	</div>
	<c:forEach var="t" items="${paging.data}">
		<div class="list left_float" onclick="location.href='/toolcloud/ctrl/tool/detail?toolId=${t.descriptor.pluginID}';">
			<s:tool_detail updatable="${editable}" toolDescriptor="${t.descriptor}"></s:tool_detail>
		</div>
	</c:forEach>
	<s:paging pageIndex="${paging.pageIndex}" pageSize="${paging.pageSize}" total="${paging.total}"/>
</div>
</jsp:body>
</s:site>
