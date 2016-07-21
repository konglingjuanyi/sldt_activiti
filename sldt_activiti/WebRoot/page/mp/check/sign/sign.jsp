<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>会签人选择</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/check/sign/sign.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/check/common.js"></script>

<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/public/layer/skin/layer.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/check/sign/sign.css">

</head>

<body style="background-color:#FFFFFF;" topmargin="0" leftmargin="0">
	<div id="sign" style="margin: 10px;">
		<div id="search">
			<div id="searchDiv">
				人名搜索：<input id="userId" type="text" style="height: 22px;line-height: 22px;"/>
			</div>
			<img style="cursor: pointer;position: relative;left: 250px;top: -25px;" id="searchUser" src="<%=request.getContextPath() %>/page/mp/imgs/gf_search.png">
		</div>
		<div id="left">
			<div class="leftDiv">
				可选用户列表
			</div>
			<div class="leftDiv2">
				<select multiple="multiple" id="selectable"></select>
			</div>
		</div>
	
		<div id="arrow">
			<div id="arrowDiv" align="center">
				<input id="add" class="arrowInput" type="button" value=">"/><br/>
				<input id="addAll" class="arrowInput" type="button" value=">>"/><br/>
				<input id="removeAll" class="arrowInput" type="button" value="<<"/><br/>
				<input id="remove" class="arrowInput" type="button" value="<"/>
			</div>
		</div>
		<div id="right">
			<div class="rightDiv">
				已选用户列表
			</div>
			<div class="rightDiv2">
				<select id="selected" multiple="multiple">
				</select>
			</div>
		</div>
	
		<div id="btnDiv" >
			<div style="border: 0px solid red;margin-top: 10px;">
				<font id="submit">
					<span style="position: absolute;cursor: pointer;border:0;font-size: 13px;color: #ffffff;margin-top: 5px;margin-left: 12px">确定</span>
					<img style="border:0;cursor: pointer;" src="<%=request.getContextPath() %>/page/mp/imgs/gf_button_img.png">
				</font>
				&nbsp;&nbsp;
				<a href="#" id="cancel">
					<span style="position: absolute;font-size: 13px;color: #ffffff;margin-top: 5px;margin-left: 12px">取消</span>
					<img style="margin-top: 0px;border: 0px;" id="cancel" src="<%=request.getContextPath() %>/page/mp/imgs/gf_button_img.png">
				</a>
			</div>
		</div>
	</div>
		
</body>
</html>