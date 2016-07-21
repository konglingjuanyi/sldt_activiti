<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK  rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/frame.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/flip/jquery.flip.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/layer/layer.min.js"></script>
</head>
<body style="background-color:#FFFFFF; margin-top: 0px; margin-bottom: 0px; margin-left: 0px; margin-right: 0px;">

	<div id="CGBx20home-03">
		<%@ include file="/page/mp/home/sys_nav.jsp" %>
  	</div>
	
	<div id="CGBx20home-04">
		<%@ include file="/page/mp/version/version_home.jsp" %>
	</div>

	<div id="CGBx20home-05">
		<%@ include file="/page/mp/home/metadata_user.jsp" %>
	</div>
	
	<div id="CGBx20home-06">
		<%@ include file="/page/mp/home/sys_link.jsp" %>
	</div>
	
</body>
</html>