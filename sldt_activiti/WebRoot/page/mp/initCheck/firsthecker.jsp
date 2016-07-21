<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
var taskId = '<%=request.getAttribute("taskId")%>';
var initDecId = '<%=request.getAttribute("initDecId")%>';
var assignee = '<%=request.getParameter("assignee")%>';
var limit = 5;
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>版本初始化初审</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/initCheck/firsthecker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/initCheck/common.js"></script>

<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/public/layer/skin/layer.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/init_common.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath( )%>/page/mp/css/metadataChange.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/common.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/sys.css">
</head>

<body style="background-color:#FFFFFF;" topmargin='0' leftmargin='0'>  
<div style="padding: 10px;">
	<div id="top" style="padding-bottom: 10px;">版本初始化初审</div>
	<div id="top_color"><hr color="#ed1a2e"/></div>
	<div id="handle">
		<div id='change_04' style="height: 60px;">
			<div class="wb" style="width: 100px;">
				<ul class="list1" id="list1"></ul>
			</div>
		</div>
		<div id='change_04' style="height: 60px;">
			<div class="wb" style="width: 100px;">
				<ul class="list1" id="list2"></ul>
			</div>
		</div>
		<form id="checkForm" action="">
			<div id="btnDiv" align="center" style="margin: 10px;">
				<table class="msg_table">
					<tr>
						<td class="msg_table_odd2">审核结果</td>
						<td class="msg_table_even2">
							<div style="float: left;padding-left: 20px;">
								<input name="checkResult" type="radio" value="0"/>通过
							</div>
							<div style="float: left;padding-left: 20px;">
								<input name="checkResult" type="radio" value="1"/>不通过
							</div>		
						</td>
					</tr>
					<tr>
						<td class="msg_table_odd2">审核意见</td>
						<td class="msg_table_even2">
							<textarea style="width: 663px;height: 40px;line-height: 35px;"></textarea>
						</td>
					</tr>
				</table>
			</div>
		</form>
		<div style="margin: 10px;">
			<table class="msg_table">
				<tr>
					<td class="msg_table_odd2">审核历史</td>
					<td class="msg_table_even2">
						<textarea id="checkInfo" style="width: 663px; height: 150px;" disabled="disabled"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div align="center" class="btnDiv"/>
			<input id="submit" class="but" type="button" value="&nbsp;确 认 "/>
			<input id="reset" class="but" type="button" value=" &nbsp;重 置 "/>
			<input id="close" class="but" type="button" value=" &nbsp;关 闭 "/>
		</div>
	</div>
</div>	
</body>
</html>