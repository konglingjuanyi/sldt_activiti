<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var context = '<%=request.getContextPath()%>';
	var limit = 10;
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>数据管控应用门户-待办事项</title>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/flow.css">
<script type="text/javascript" src="<%=request.getContextPath() %>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/flow/flow.js"></script>
</head>

<body style="background-color:#F5F5F5;">
	<div>
   		<div id="tableDiv">
   			<table id="flowTable" class="data_table_flow">
   				<thead>
					<tr style="display:block">
		 				<th style="display:none"></th>
		  				<th style="width: 80px;">序号</th>
		   				<th style="width: 120px;">流程名称</th>
						<th style="width: 120px;">名称</th>
						<th style="width: 100px;">提交用户</th>
						<th style="width: 150px;">提交时间</th>
						<th style="width: 150px;">当前节点</th>
						<th style="width: 270px;">描述</th>
						<th style="width: 80px;">操作</th>
					</tr>
				</thead>
   			</table>
   			<div id="flowTable_div">
   				<table class="data_table_flow_tbody" id="flowTable_tBody"></table>
   			</div>
   		</div>
	</div>
</body>
</html>