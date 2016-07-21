<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/page/mp/css/sys_link.css">
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/home/sys_link.js"></script>
<title>系统列表</title>
<script type="text/javascript">
       var context = '<%=request.getContextPath()%>';
</script>
</head>
<body>
	<div class="sys1">
		<div class="sys2">
			<img src="../imgs/gf_60.png" style="vertical-align: middle;">系统链接
		</div>
		<div class="sys3">
			<img src="../imgs/links_1.png" style="vertical-align: middle;">一级重要系统
		</div>
		<div id="sys_list_1_div"></div>
		<div id="sys_list_line1_div"></div>
		<div class="sys4">
			<img src="../imgs/links_1.png" style="vertical-align: middle;">一级次要系统
		</div>
		<div id="sys_list_2_div"></div>
		<div id="sys_list_line2_div"></div>
		<div class="sys5">
			<img src="../imgs/links_1.png" style="vertical-align: middle;">二级及其他系统
		</div>
		<div id="sys_list_3_div"></div>
	</div>
</body>
<script type="text/javascript">
	getSystemItem();
</script>
</html>