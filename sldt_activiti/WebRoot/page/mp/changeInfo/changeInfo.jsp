<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
var dbId = '<%=request.getParameter("dbId")==null?"":request.getParameter("dbId")%>';
var optId = '<%=request.getParameter("optId")==null?"":request.getParameter("optId")%>';
var dbName = '<%=request.getParameter("dbName")==null?"":request.getParameter("dbName")%>';
var altSts = "2";
var limit = 10;
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>数据管控应用门户-元数据申报</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/changeInfo/changeInfo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/common/util.js"></script>

<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/public/layer/skin/layer.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/changeInfo.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/selected.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/frame.css">
</head>

<body style="background-color:#F5F5F5;">
<div class="gf_all">
		<div style="padding-top: 80px;"></div>
		<div class="tiledcenter">
		<div id="change_02" style="margin-top:20px;font-size: 12px;height: 0px;padding-bottom: 0px;padding-top: 0px;">
			<div id="c" style="float: left;">
				<div id="sysName" style="float: left;font-size: 18px;">系统名称</div>
				<div id="a" style="float: left;padding-left: 40px;padding-top: 5px;">近期已投产版本：</div>
				<div id="jqVerDate" style="float: left;padding-top: 5px;">
					无
				</div>
				<div id="a" style="float: left;padding-left: 40px;padding-top: 5px;">变更申报版本：</div>
				<div id="altVerDate" style="float: left;padding-top: 5px;">
					无
				</div>
				<div style="float: left; padding-left: 40px;padding-top: 5px;">开发中版本：</div>
				<div id="deveVerDate" style="float: left;padding-top: 5px;">无</div>
				<div id="a" style="float: left; padding-left: 40px;padding-top: 5px;">远期版本：</div>
				<div id="forVerDate" style="float: left;padding-top: 5px;">无</div>
				<div style="clear: both"></div>
				<!--这个层很有用，必须要，否则可能不兼容。-->
			</div>
		</div>
		<div>
		<div id="changeInfo">
			<div id="searchDiv">
				<span style="top: 2px;position: relative;font-size: 14x">分类：&nbsp;</span><select id="classId" class="searchCondition" >
							<option value="">请选择</option>
							<option value="DATABASE">数据库</option>
							<option value="SCHEMA">模式</option>
							<option value="MODULE">模块</option> 	
							<option value="TABLE">表</option>
							<option value="COLUMN">字段</option>
							<option value="INTFILE">表（文件）引用</option>
							<option value="COLCODE">字段落地代码</option>
					   </select> 
				<span style="top: 2px;position: relative;font-size: 14x">类型：&nbsp;</span><select id="altType" class="searchCondition">
						<option value="">请选择</option>
						<option value="I">新增</option>
						<option value="D">删除</option>
						<option value="U">修改</option> 
					</select>
				<span style="top: 2px;position: relative;font-size: 14x">变更批次：&nbsp;</span><select id="altBatch" class="searchCondition">
						<option value="">请选择</option>
					</select>	
					<img id="searchBtn" src="<%=request.getContextPath()%>/page/mp/imgs/gf_search.png" style="position: relative;top: 10px;left:5px;cursor: pointer;">
<!-- 				<input id="searchBtn" type="button" value="查询"/>
 -->				<div id="search_page">
						<div class="search_page_left_ci" id="tableLastPage" style="cursor: pointer;" ></div>
						<div class="search_page_mid_ci" id="tablePages">0/0</div>
						<div class="search_page_right_ci" id="tableNextPage" style="cursor: pointer;" ></div>
					</div>
			</div> 	
			<div>
				<table id="changeInfo_table" class="data_table_ci">
					<thead>
						<tr>
							<th style="width: 30px;"></th>
							<th style="display:none"></th>
							<th style="width: 40px;">序号</th>
							<th style="width: 100px;">变更项目</th>
							<th style="width: 60px;">变更前</th>
							<th style="width: 60px;">变更后</th>
							<th style="width: 40px;">方式</th>
							<th style="width: 40px;">分类</th>
							<th style="width: 40px;">类型</th>
							<th style="width: 60px;">变更批次</th>
							<th style="width: 78px;">变更时间</th>
							<th style="width: 60px;">变更人</th>
						</tr>
					</thead>
				</table>
				<div id="changeInfo_table_tdiv">
					<table id="changeInfo_table_tbody" class="data_table_ci_tbody">
					</table>
				</div>
			</div>
		</div>
		<div id="arrow" align="center">
			<div id="arrowDiv">
				<input id="add" class="arrowInput" type="button" value=">"/>
				<input id="addAll" class="arrowInput" type="button" value=">>"/>
				<input id="removeAll" class="arrowInput" type="button" value="<<"/>
				<input id="remove" class="arrowInput" type="button" value="<"/>
			</div>
		</div>
		<div id="selectedInfo">
			<div id="btnDiv">
				<!-- <input id="reportBtn" type="button" value="申报"/> -->
				<img id="reportBtn" src="<%=request.getContextPath() %>/page/mp/imgs/gf_rep.png" style="cursor: pointer;"/>
			</div>
			<div id="selected_div">
				<table id="selected_table">
					<tr>
						<th style="width: 30px;"></th>
						<th style="display:none"></th>
						<th style="width: 50px;">序号</th>
						<th>变更项目</th>
					</tr>
				</table>
			</div>
		</div>
		<div class="clear"></div>
	</div>
   </div>
 </div>
</body>
<script type="text/javascript">

$("#sysName").html("<%=request.getParameter("dbName")==null?"":request.getParameter("dbName")%>");
$("#jqVerDate").html("<%=request.getParameter("jqVerDate")==null?"":request.getParameter("jqVerDate")%>");
$("#deveVerDate").html("<%=request.getParameter("deveVerDate")==null?"":((String)request.getParameter("deveVerDate")).replaceAll("\"", "\\\\\"")%>");
$("#targetVerDate").html("<%=request.getParameter("targetVerDate")==null?"":((String)request.getParameter("targetVerDate")).replaceAll("\"", "\\\\\"")%>");
$("#forVerDate").html("<%=request.getParameter("forVerDate")==null?"":((String)request.getParameter("forVerDate")).replaceAll("\"", "\\\\\"")%>");
$("#altVerDate").html("<%=request.getParameter("optId")==null || "".equals(request.getParameter("optId")) ?" <a style=\\\"cursor: pointer;\\\" onclick=\\\"reqAltMetaDet('','','','')\\\">ALL</a> ":((String)request.getParameter("optId"))%>");

</script>
</html>