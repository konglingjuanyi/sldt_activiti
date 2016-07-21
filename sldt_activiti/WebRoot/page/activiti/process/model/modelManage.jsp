<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var context = '<%=request.getContextPath()%>';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据管控应用门户-我的变更申报</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/public/fontAwesome/css/font-awesome.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/public/jqgrid/css/ui.jqgrid.css" />
<%-- <link rel="stylesheet" href="<%=request.getContextPath()%>/public/boot_awesome/ace.min.css" /> --%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/public/bootstrap/css/bootstrap.min.css" />
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/declarelist.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/frame.css">
<!-- 调整表头样式 --->
<style type="text/css">
.ui-jqgrid .ui-jqgrid-labels {
	border-bottom: 0;
	background: #FFE7DA;
}

.template {
	display: none;
}

td.label {
	text-align: right;
	font-weight: bold;
}

textarea {
	height: 100px;
}

.ui-button .ui-button-text {
	line-height: 1;
}

.ui-buttonset .ui-button-text-only .ui-button-text {
	padding: .4em .5em;
}

.ui-button-text-icon-primary .ui-button-icon-primary,.ui-button-text-icons .ui-button-icon-primary,.ui-button-icons-only .ui-button-icon-primary {
	left: 0.3em;
}

.ui-button-text-icon-primary .ui-button-text {
	padding: 0.4em 0.8em 0.4em 1.7em;
}

.ui-corner-all-12,.ui-corner-top-12,.ui-corner-left-12,.ui-corner-tl-12 {
	-moz-border-radius-topleft: 12px;
	-webkit-border-top-left-radius: 12px;
	-khtml-border-top-left-radius: 12px;
	border-top-left-radius: 12px;
}

.ui-corner-all-12,.ui-corner-top-12,.ui-corner-right-12,.ui-corner-tr-12 {
	-moz-border-radius-topright: 12px;
	-webkit-border-top-right-radius: 12px;
	-khtml-border-top-right-radius: 12px;
	border-top-right-radius: 12px;
}

.ui-corner-all-12,.ui-corner-bottom-12,.ui-corner-left-12,.ui-corner-bl-12 {
	-moz-border-radius-bottomleft: 12px;
	-webkit-border-bottom-left-radius: 12px;
	-khtml-border-bottom-left-radius: 12px;
	border-bottom-left-radius: 12px;
}

.ui-corner-all-12,.ui-corner-bottom-12,.ui-corner-right-12,.ui-corner-br-12 {
	-moz-border-radius-bottomright: 12px;
	-webkit-border-bottom-right-radius: 12px;
	-khtml-border-bottom-right-radius: 12px;
	border-bottom-right-radius: 12px;
}

/* 自动完成的loading图片 */
.ui-loading {
	background: url('../images/ajax/loading.gif') right center no-repeat;
	padding-left: 18px;
}

.error,.alert,.notice,.success,.info {
	padding: auto !important;
}

/* 解决和jquery ui tooltip冲突问题 */
.qtip {
	padding: 0px !important;
}

.pagination {
	margin: 20px 0;
}

.pagination ul {
	display: inline-block;
	*display: inline;
	margin-bottom: 0;
	margin-left: 0;
	-webkit-border-radius: 4px;
	-moz-border-radius: 4px;
	border-radius: 4px;
	*zoom: 1;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
	box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.pagination ul>li {
	display: inline;
}

.pagination ul>li>a,.pagination ul>li>span {
	float: left;
	padding: 4px 12px;
	line-height: 20px;
	text-decoration: none;
	background-color: #ffffff;
	border: 1px solid #dddddd;
	border-left-width: 0;
}

.pagination ul>li>a:hover,.pagination ul>.active>a,.pagination ul>.active>span {
	background-color: #f5f5f5;
}

.pagination ul>.active>a,.pagination ul>.active>span {
	color: #999999;
	cursor: default;
}

.pagination ul>.disabled>span,.pagination ul>.disabled>a,.pagination ul>.disabled>a:hover {
	color: #999999;
	cursor: default;
	background-color: transparent;
}

.pagination ul>li:first-child>a,.pagination ul>li:first-child>span {
	border-left-width: 1px;
	-webkit-border-bottom-left-radius: 4px;
	border-bottom-left-radius: 4px;
	-webkit-border-top-left-radius: 4px;
	border-top-left-radius: 4px;
	-moz-border-radius-bottomleft: 4px;
	-moz-border-radius-topleft: 4px;
}

.pagination ul>li:last-child>a,.pagination ul>li:last-child>span {
	-webkit-border-top-right-radius: 4px;
	border-top-right-radius: 4px;
	-webkit-border-bottom-right-radius: 4px;
	border-bottom-right-radius: 4px;
	-moz-border-radius-topright: 4px;
	-moz-border-radius-bottomright: 4px;
}

.pagination-centered {
	text-align: center;
}

.ui-dialog .ui-dialog-titlebar-close,.ui-jqdialog .ui-dialog-titlebar-close,.ui-dialog .ui-jqdialog-titlebar-close,.ui-jqdialog .ui-jqdialog-titlebar-close {
	border: 0;
	background: transparent;
	opacity: .4;
	color: #d15b47;
	padding: 0;
	top: 50%;
	right: 8px !important;
	text-align: center;
	float: right;
}

.defbtn{
width:60px;
height:28px;
border-radius:5px;
border:none;
-webkit-border-radius:5px;
color:#FFF;
background-color:red;
}

li{
list-style-type:none;
}
</style>

<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jqgrid/js/jquery.jqGrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jqgrid/js/i18n/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/page/activiti/process/model/modelManage.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/page/mp/flow/workflow.js"></script>

</head>

<body style="background-color: #F5F5F5;">
	<div class="gf_all">
		<div class="tiledcenter">
			<div style="padding-top: 20px; padding-left: 10px;">
				<span>
					模型名称：<input type="text" width="300" id="name" name="name" placeholder="请输入模型名称">
				</span> 
				<img style="margin-left:20px;cursor: pointer;" id="seachModelButton" src="<%=request.getContextPath() %>/page/mp/imgs/gf_search.png">
				
				<input type="button" class="defbtn" value="添加" data-toggle="modal" data-target="addModal" style="margin-left:20px;cursor: pointer;" id="createModelButton">
				<input type="button" class="defbtn" value="删除" style="margin-left:20px;cursor: pointer;" id="deleteModelButton">
			</div>
			<div style="padding-top: 10px;">
				<table id="grid-table-model"></table>
				<div id="grid-pager-model"></div>
			</div>
		</div>
	</div>
	
	
	<div class="modal hide fade" id="addModal" tabindex="-1" role="dialog">
		<div class="modal-header"><button class="close" data-dismiss="modal">x</button>
			<h3 id="addModalLabel">添加模型</h3>
		</div>
		<form id="add-modal-form" action="<%=request.getContextPath()%>/activiti/processModel.do?method=addProcessModel" method="post">
		<div class="modal-body">
	  	  	<div id="editDiv">
				<div class="form-group">
					<div class="input-group">
						<ul class="list-group">
						<li class="list-group-item">
							<strong>名称(不支持中文)：</strong>
							<input type="text" class="form-control" style="width:380" name="name" id="name">
							<span style="color:red;" id="name-span"></span>
						</li>
						<li class="list-group-item">
							<strong>key(不支持中文)：</strong>
							<input type="text" class="form-control" style="width:380" name="key" id="key">
							<span style="color:red;" id="key-span"></span>
						</li>
						<li class="list-group-item">
							<strong>描述：</strong>
							<textarea id="description" width="500" class="form-control" name="description"></textarea>
							<span style="color:red;" id="des-span"></span>
						</li>
						<ul>
						</ul>
				</div>
			</div>
			
			<div>
				<button style="margin-left:25%" type="button" id="saveModelButton" class="button positive">
	    		<img src="<%=request.getContextPath() %>/page/mp/imgs/gf_submit.png" alt=""/>
		  	  	</button>
			  	<button type="button" id="resetModelButton" class="button positive">
		    		<img src="<%=request.getContextPath() %>/page/mp/imgs/gf_reset.png" alt=""/>
		  	  	</button>
			  	<button type="button" id="backModelButton" class="button positive">
		    		<img src="<%=request.getContextPath() %>/page/mp/imgs/gf_back.png" alt=""/>
		  	  	</button>
			</div>
		
		</div>
	</div>
	</form>
	</div>
</body>
</html>	