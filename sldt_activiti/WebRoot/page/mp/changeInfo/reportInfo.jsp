<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%-- <%
String altIds = (String) request.getAttribute("altIds");
%>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
var altIds = "<%=altIds%>";
var dbId = '<%=request.getAttribute("dbId")==null?"":request.getAttribute("dbId")%>';
var optId = '<%=request.getAttribute("optId")==null?"":request.getAttribute("optId")%>';
var altSts = "2";
var limit = 4;
</script> --%>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
var altIds = '<%=request.getParameter("altIds")%>';
var dbId = '<%=request.getParameter("dbId")==null?"":request.getParameter("dbId")%>';
var optId = '<%=request.getParameter("optId")==null?"":request.getParameter("optId")%>';
var altSts = "2";
var limit = 4;
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>申报信息</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/changeInfo/reportInfo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/common/util.js"></script>

<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/public/layer/skin/layer.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/reportInfo.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath( )%>/page/mp/css/metadataChange.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/common.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/sys.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/check/common.css">
</head>

<body style="background-color:#FFFFFF;" topmargin='0' leftmargin='0'>
<div style="margin: 10px;">
	<div id="top" style=""><div style="font-size: 20px;font-family:宋体;margin-top: 20px;">变更申报单</div></div>
	<div><hr color="#ed1a2e"/></div> 
	<div id="reportInfo">
		<!-- <div id="reportInfoDiv">申报信息</div> -->
		<div id="formDiv">
			<form id="reportForm" action="">
				<div id='change_04' style="height: 60px;">
					<div class="wb" style="width: 100px;">
						<ul class="list1" id="list1">
							<li class="cl1" style="height: 30px;">申报名称：</li>
							<li class="cl2" style="height: 30px;padding-left: 0px;"><input style="height: 25px;width: 200px;line-height: 25px;" id="decName" type="text"/></li>
							<li class="cl1" style="height: 30px;"></li>
							<li class="cl2" style="height: 30px;"></li>
							<li class="cl3" style="height: 60px;">申报描述：</li>
							<li class="cl4" style="height: 60px;padding-left: 0px;width: 80%">
								<textarea  id="decDesc" style="height:57px;width: 700px;"></textarea>
							</li>
							<li class="cl1" style="height: 30px;">提交给：</li>
							<li class="cl2" style="height: 30px;padding-left: 0px;">
								<select style="height: 20px;width: 205px;" id="assignee"></select>
							</li>
							<li class="cl1" style="height: 30px;"></li>
							<li class="cl2" style="height: 30px;"></li>
							<li class="cl1" style="height: 30px;">变更申报类型：</li>
							<li class="cl2" style="height: 30px;padding-left: 0px;">
								<select style="height: 20px;width: 205px;" id="decType">
									<option value="1">需求开发设计</option>
									<option value="2">测试补丁修改</option>
									<option value="3">生成补丁修改</option>
								</select>
							</li>
							<li class="cl1" style="height: 30px;"></li>
							<li class="cl2" style="height: 30px;"></li>
						</ul>
					</div>
				</div>	
			</form>
		</div>
		<div id="search_page" style="padding-top:65px;position: relative;overflow: hidden;float: right;margin-top: -10px;margin-right: 30px;">
			<div class="search_page_left" id="tableLastPage" style="cursor: pointer;" ></div>
			<div class="search_page_right" id="tableNextPage" style="cursor: pointer;" ></div>
			<div class="search_page_mid" id="tablePages">0/0</div>
		</div>
		
		<div id="sysName" style="float: left;font-size: 18px; margin-top:15px;">系统名称</div>
		
		<div id="tableDiv" style="float: left;margin-top: 0px;height: 230px;">
			<table id="reportInfo_table" class="data_table">
				<tr>
					<th style='width:60px'>序号</th>
					<th style='width:140px'>变更项目</th>
					<th style='width:70px'>变更前</th>
					<th style='width:70px'>变更后</th>
					<th style='width:60px'>方式</th>
					<th style='width:90px'>分类</th>
					<th style='width:60px'>类型</th>
					<th style='width:90px'>变更批次</th>
					<th style='width:140px'>变更时间</th>
					<th style='width:80px'>变更人</th>
				</tr>
			</table>
		</div>
		
		<div id="search_page_ai" style="padding-top:25px;position: relative;overflow: hidden;float: right;margin-top: -10px;margin-right: 30px;"></div>
		
		<div id="ai_table_div" style="float: left;margin-top: 0px;margin-left: 0;height: 230px;overflow: auto;">
			<table id="reportInfo_table_ai" class="data_table">
				<tr>
					<th>序号</th>
					<th>影响系统</th>
					<th>相关影响表总数</th>
					<th>影响系统关联人</th>
					<th>分析依据类型</th>
				</tr>
			</table>
		</div>
		
		<!-- <div id="search_page_cc" style="padding-top:25px;position: relative;overflow: hidden;float: right;margin-top: -10px;margin-right: 30px;"></div>
		
		<div id="cc_table_div" style="float: left;margin-top: 0px;margin-left: 0;height: 230px;overflow: auto;">
			<table id="reportInfo_table_cc" class="data_table">
				<tr>
					<th>序号</th>
					<th>数据库标识</th>
					<th>模式名</th>
					<th>模块名</th>
					<th>表英文名</th>
					<th>字段英文名</th>
					<th>代码中文名</th>
					<th>代码描述</th>
					<th>是否有效</th>
					<th>生效日期</th>
					<th>无效日期</th>
					<th>数据标准引用</th>
				</tr>
			</table>
		</div>
		
		<div id="search_page_if" style="padding-top:25px;position: relative;overflow: hidden;float: right;margin-top: -10px;margin-right: 30px;"></div>
		
		<div id="if_table_div" style="float: left;margin-top: 0px;margin-left: 0;height: 230px;overflow: auto;">
			<table id="reportInfo_table_if" class="data_table">
				<tr>
					<th>序号</th>
					<th>数据库标识</th>
					<th>引用系统标识</th>
					<th>引用模式名</th>
					<th>引用模块名</th>
					<th>引用表英文名</th>
					<th>调用接口交易</th>
					<th>接口实现</th>
					<th>依赖类型</th>
				</tr>
			</table>
		</div> -->
		<div class="clear"></div>
	</div>
		<div id="btnDiv" align="center">
 			<img style="margin-top: 15px;cursor: pointer;" id="start" src="<%=request.getContextPath() %>/page/mp/imgs/gf_submit.png">
			&nbsp;&nbsp;
			<img style="margin-top: 15px;cursor: pointer;" id="reset" src="<%=request.getContextPath() %>/page/mp/imgs/gf_reset.png">
			&nbsp;&nbsp;
			<img style="margin-top: 15px;cursor: pointer;" id="cancel" src="<%=request.getContextPath() %>/page/mp/imgs/gb3.png">
		</div>
</div>
</body>
<script type="text/javascript">
$("#sysName").html("<%=request.getAttribute("dbName")==null?"":request.getAttribute("dbName")%>");
</script>
</html>