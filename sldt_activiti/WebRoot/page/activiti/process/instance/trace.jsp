<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<title>流程跟踪</title>
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-styles.jsp" %>
	<script type="text/javascript" src="${ctx}/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${ctx }/js/edit.js"></script>
</head>

<body>

	<div id="navigatorDiv">
	  	<button type="button" id="backButton" class="button positive">
    		<img src="${ctx}/js/easyui/themes/icons/undo.png" alt=""/> 返回
  	  	</button>
	</div>
	
	<div id="editDiv">
		<fieldset>
		<legend><small>流程跟踪</small></legend>
		<table >
			<tr>
				<td >
				  <!-- 显示动态流程跟踪图 -->
				  <img alt="..." src="${ctx }/process/instance!tracePicture.action?processInstanceId=${param.processInstanceId}&businessKey=${param.businessKey}&definitionKey=${param.definitionKey}">	  
				</td>
			</tr>
		</table>
		</fieldset>
	</div>

</body>
</html>
