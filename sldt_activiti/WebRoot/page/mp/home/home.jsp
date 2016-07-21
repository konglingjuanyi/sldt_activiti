<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据管控应用门户</title>
<LINK  rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/frame.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/sunlineUI/js/appClient.js"></script>
<script type="text/javascript">
	var context = "<%=request.getContextPath()%>";
</script>
</head>
<body>
	<table style="width: 1347px">
		<tr>
			<td>
				<div>
					<jsp:include page="/page/mp/common/head.jsp" flush="true" />
				</div>
			</td>
		</tr>
	
		<tr>
			<td>
				<div id="home_content_div">
				 	<iframe id="mainArea" style="height: 2000px !important;" name="mainArea" src="<%=request.getContextPath()%>/page/mp/home/home_content.jsp" width="100%"  
			 				frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" 
			 				allowtransparency="yes" onLoad="iFrameHeight('mainArea')">
			 		</iframe> 
		           <!--   <jsp:include page="/page/mp/home/home_content.jsp" flush="true" />-->
			 		
				</div>
			</td>
		</tr>
		
		<tr>
			<td>
				<div id="home_foot_div">
					<jsp:include page="/page/mp/common/foot.jsp" flush="true" />
				</div>
			</td>
		</tr>
	</table>
</body>
 <script type="text/javascript" src="<%=request.getContextPath()%>/page/mp/home/home.js"></script>
 </html>