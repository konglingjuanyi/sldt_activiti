<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
var taskId = '<%=request.getParameter("taskId")%>';
var decId = '<%=request.getParameter("decId")%>';
var assignee = '<%=request.getParameter("assignee")%>';
var limit = 5;
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>架构办审批</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/check/approver.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/check/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/common/util.js"></script>

<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/public/layer/skin/layer.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/check/common.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/check/approver.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath( )%>/page/mp/css/metadataChange.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/common.css">
<%-- <LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/sys.css"> --%>
</head>

<body style="background-color:#FFFFFF;" topmargin='0' leftmargin='0'>
<div style="padding: 10px;">
	<div id="top" style="padding-bottom: 10px;">架构办审批</div>
	<div id="top_color"><hr color="#ed1a2e"/></div>
	<div id="handle">
		<div id='change_04' style="height: 60px;">
			<div class="wb" style="width: 100px;">
				<ul class="list1" id="list1"></ul>
			</div>
		</div>
		<div> 	
			<div class="search_page_left_check" id="tableLastPage" style="cursor: pointer;margin-left: 800px;" ></div>
			<div class="search_page_mid_check" id="tablePages">0/0</div>
			<div class="search_page_right_check" id="tableNextPage" style="cursor: pointer;" ></div>
			<div class="clear"></div>
		</div>
		<div id="tableDiv">
			<table id="handleTable" class="data_table">
				<tr>
					<th style="display:none"></th>
					<th>序号</th>
					<th>变更项目</th>
					<th>变更前</th>
					<th>变更后</th>
					<th>方式</th>
					<th>分类</th>
					<th>类型</th>
					<th>变更批次</th>
					<th>变更时间</th>
					<th>变更人</th>
				</tr>
			</table>
		</div>
		
		<div id="search_page_ai"></div>
		
		<div id="ai_table_div" style='margin-left: 0'>
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
		
		<!-- <div id="search_page_cc"></div>
		
		<div id="cc_table_div" style='margin-left: 0'>
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
		
		<div id="search_page_if"></div>
		
		<div id="if_table_div" style='margin-left: 0'>
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
		
		<form id="checkForm" action="">
			<div id="btnDiv" align="center" style="margin: 0;">
				<table class="msg_table">
					<tr>
						<td class="msg_table_odd2" style="height:20px;">审批结果</td>
						<td class="msg_table_even2" style="text-align: left;height:30px;">
							<div style="float: left;padding-left: 20px;">
								<input name="checkResult" type="radio" value="0"/>通过
							</div>
							<div style="float: left;padding-left: 20px;">
								<input name="checkResult" type="radio" value="1"/>不通过
							</div>
							<div style="padding-left: 145px;">
								<input name="checkResult" type="radio" value="2"/>全行评审	
							</div>	
						</td>
					</tr>
					<tr>
						<td class="msg_table_odd2" style="height:20px;">审批意见</td>
						<td class="msg_table_even2" style="height:20px;">
							<textarea style="width: 663px;height: 30px; line-height: 30px;"></textarea>
						</td>
					</tr>
				</table>
			</div>
		</form>
		<div style="margin: 0;">
			<table class="msg_table">
				<tr>
					<td class="msg_table_odd2">审核历史</td>
					<td class="msg_table_even2">
						<textarea id="checkInfo" style="width: 663px; height: 150px;" disabled="disabled"></textarea>
					</td>
				</tr>
			</table>
		</div>
	</div>
		<div align="center" class="btnDiv"/>
			<input id="submit" class="but" type="button" value="&nbsp;确 认 "/>
			<input id="reset" class="but" type="button" value=" &nbsp;重 置 "/>
			<input id="close" class="but" type="button" value=" &nbsp;关 闭 "/>
		</div>
</div>	
</body>
</html>