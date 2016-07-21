<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
var dbId = '<%=request.getParameter("dbId")%>';
var optId = '';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>数据管控应用门户-在线编辑</title>
<link href="<%=request.getContextPath()%>/page/mp/css/metadataChange.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/frame.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/form.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/version.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/common.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/edit.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/metaAlteration.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/jquery.contextMenu.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/common/util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.contextMenu.js"></script>
<link href="<%=request.getContextPath()%>/page/mp/css/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/page/mp/sysnav/edit.js"></script>
<style type="text/css">  
<!--  
html,body {height:100%; margin:0px; font-size:12px;}  
  
.mydiv {  
background-color: #FFFFFF;  
border: 1px solid #e0e0e0;  
text-align: center;  
line-height: 25px;  
font-size: 13px;  
font-weight: bold;  
z-index:9999999;  
width: 150px;  
height: 200px;  
bottom:0px;
left:88%;/*FF IE7*/  
margin-top:0px;  
position:fixed!important;/*FF IE7*/  
position:absolute;/*IE6*/  
  
_top:       expression(eval(document.compatMode &&  
            document.compatMode=='CSS1Compat') ?  
            documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 :/*IE6*/  
            document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2);/*IE5 IE5.5*/  
  
}  
-->   
</style>  
</head>

<body style="background-color: #F5F5F5;" >
<div id="popDiv"  class="mydiv" >
	<div id="divName" style="cursor: pointer;">元数据变更记录</div>
	<div id="altCount" style="text-align: left;margin: 5px;">
	
	</div>
	<div><a onclick="saveAlt()" style="cursor: pointer;">查看详情</a></div>
</div>
	<div class="gf_all">
		<!-- 中间 -->
		<div class="center">
			<!-- 左边 -->
			<div class="center_left_content">

				<div id="change_02">无系统信息</div>
				<div id='change_02_desc' style="float: left;" class="change_font_04"></div>
				<div id='saveMeta' style="float: right;padding-right: 20px; padding-bottom: 10px;">
				<a style="cursor: pointer;" onclick="saveAlt()">
				<img src="../imgs/bc-r.png" title="确认当前元数据修改记录信息" onmouseover="this.src='../imgs/bc_r_1.png';" onmouseout="this.src='../imgs/bc-r.png';" /></a>
				</div>
				<div id="change_03">
					<div id="change_05">基本信息</div>
				</div>
				<!-- 基本信息详情 -->
				<div id="change_04"></div>
			</div>
			<!-- 右边 -->
			<div class="center_right_content">
				<div id="change_42">
					<div id="change_43">
						<img src="<%=request.getContextPath()%>/page/mp/imgs/ny19.png">
					</div>
					<div id="change_44"></div>
				</div>
				<div id="change_45">
					<div id="change_46">系统统计</div>
					<div id="change_47">
						浏览次数 : 83次<br> 版本次数 : 55次<br>最近更新 : 2014-12-2
					</div>
				</div>
			</div>
		</div>

		<div class="tiledcenter">
			<div id="sys_ver_list_Div">
				<div class="sys_verDiv4">
					<div class="catalogue1">
						<div class="catalogue2">
							<div class="catalogue21">目录</div>
						</div>
						<div class="catalogue3">
							<ul class="ulverstyle">
								<li class="dl1">1 模式信息</li>
								<li class="dl1">2 模块信息</li>
								<li class="dl1">3 表级信息</li>
								<li class="dl1">4 字段信息</li>
								<li class="dl2">&nbsp;<a href="#schemaInfoId"> 模式基本信息</a></li>
								<li class="dl2">&nbsp;<a href="#moduleInfoId"> 模块基本信息</a></li>
								<li class="dl2">&nbsp;<a href="#tableInfoId"> 表级基本信息</a></li>
								<li class="dl2">&nbsp;<a href="#columnInfoId"> 字段基本信息</a></li>
							</ul>
						</div>
					</div>
				</div>

				<!-- 版本概要 -->
				<div class="sysDiv5" id="schemaInfoId">
					<div class="xd">
						<div class="xd-1">1</div>
					</div>
					<div class="xd-name">模式信息</div>
					<div class="apDiv-line"></div>
				</div>

				<div class="sys_verDiv6" id="verSchemaData">
					<div class="apDiv-bg">
						<table class="x-bg">
							<tr>
								<th scope="row" style="width: 100px;">元数据代码</th>
							</tr>
							<tr>
								<th scope="row">元数据名称</th>
								<td abbr=""></td>
							</tr>
						</table>
					</div>
				</div>
				<!-- 版本概要 -->
				<div class="sysDiv5" id="moduleInfoId">
					<div class="xd">
						<div class="xd-1">2</div>
					</div>
					<div class="xd-name">模块信息</div>
					<div class="apDiv-line"></div>
				</div>
				<div class="sys_verDiv7">
				<div class="sys_verDiv8">
				
					<div class="sys_verDiv7_2">
						<table class="data_table" style="width: 100%;">
							<thead>
								<tr>
									<th>模块名</th>
									<th>模块中文名称</th>
									<th>开发负责联系人</th>
									<th>SA</th>
									<th>模块识别表达式</th>
									<th>模式描述</th>
									<th>总表数</th>
								</tr>
							</thead>

							<tbody id="verModuleData">
							</tbody>
						</table>
					</div>
				</div>


				<!-- 版本概要 -->
				<div class="sysDiv5" id="tableInfoId">
					<div class="xd">
						<div class="xd-1">3</div>
					</div>
					<div class="xd-name">表级信息</div>
					<div class="apDiv-line"></div>
				</div>
				<div class="sys_verDiv8">
					<div class="sys_verDiv8_1">
						<div id="search">
							<div id="tablePageDiv">

								<div class="search_tab_schema_div">模式选择：</div>
								<div class="search_schema_bg_div">
									<div>
										<select name="search_value" class="search_select_input"></select>
									</div>
								</div>

								<div class="search_tab_module_div">模块选择：</div>
								<div class="search_module_bg_div">
									<div>
										<select name="search_value" class="search_select_input"></select>
									</div>
								</div>

								<div class="search_table_col_div">表级数据查询：</div>
								<div class="search_table_bg_div">
									<div>
										<input type="text" name="search_value" class="search_input">
									</div>
									<div class="search_table_btn_div" style="cursor: pointer;"
										id="submit"></div>
								</div>

								<div class="search_page_left"  style="pcursor: pointer;margin-left: 960px;"
									id="tableLastPage"></div>
								<div class="search_page_mid" id="tablePages">1/150000</div>
								<div class="search_page_right" id="tableNextPage"
									style="cursor: pointer;"></div>
							</div>
						</div>
					</div>

					<div class="sys_verDiv8_2" >
						<table class="data_table" style="width: 100%;">
							<thead>
								<tr>
									<th style="width: 15%">表英文名</th>
									<th style="width: 10%">表中文名</th>
									<th style="width: 10%">所属表空间</th>
									<th style="width: 15%">主键字段</th>
									<th style="width: 10%">外键字段</th>
									<th style="width: 5%">是否关键表</th>
									<th style="width: 10%">生命周期说明</th>
									<th style="width: 15%">描述</th>
									<th style="width: 10%">上下文</th>
								</tr>
							</thead>

							<tbody id="verTableData">
							</tbody>
						</table>

					</div>
				</div>

				<!-- 版本概要 -->
				<div class="sysDiv5" id="columnInfoId">
					<div class="xd">
						<div class="xd-1">4</div>
					</div>
					<div class="xd-name">字段级信息</div>
					<div class="apDiv-line"></div>
				</div>
				<div class="sys_verDiv9" style="padding: 0 0 40px 0;">
					<div class="sys_verDiv9_1">
						<div id="search">
							<div id="columnPageDiv" name="columnPageDiv">
								<div class="search_col_schema_div">模式选择：</div>
								<div class="search_col_schema_bg_div">
									<div>
										<select name="search_value" class="search_col_select_input"></select>
									</div>
								</div>

								<div class="search_col_module_div">模块选择：</div>
								<div class="search_col_module_bg_div">
									<div>
										<select name="search_value" class="search_col_select_input"></select>
									</div>
								</div>
								
								<div class="search_col_edit_page_left" style="cursor: pointer;"></div>
								<div class="search_col_edit_page_right" style="cursor: pointer;"></div>
								<div class="search_col_edit_page_mid">0/0</div>

								<div class="search_col_table_div">表级数据查询：</div>
								<div class="search_col_table_bg_div">
									<div>
										<input type="text" name="search_value" class="search_input">
									</div>
								</div>

								<div class="search_col_div">字段级数据查询：</div>
								<div class="search_bg_div">
									<div>
										<input type="text" id="sysVerColumnSearch"
											name="sysVerColumnSearch" class="search_input">
									</div>
									<div class="search_btn_div" style="cursor: pointer;"
										id="submit"></div>
								</div>

								<div class="table_add" style="padding-top: 8px;"><input type="button" onclick="" value="新增字段级元数据"/></div>
							</div>
						</div>
					</div>
					<div class="sys_verDiv9_2" style="padding: 50px 0 0 0;">
						<table class="data_table" style="width: 100%;">
							<thead>
								<tr>
									<th>字段英文名</th>
									<th>字段中文名</th>
									<th>字段类型</th>
									<th width="50px">字段序号</th>
									<th width="50px">主键</th>
									<th width="50px">是否允许空值</th>
									<th width="50px">是否代码字段</th>
									<th width="50px">是否有索引</th>
									<th>引用代码表</th>
									<th>字段描述</th>
									<th width="20%">上下文</th>
								</tr>
							</thead>
							<tbody id="verColumnData">
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
</body>

<script type="text/javascript" src="<%=request.getContextPath()%>/page/mp/version/sysMenu.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/page/mp/sysnav/editMetaPage.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/page/mp/sysnav/editMetaPageDetail.js"></script>
<script type="text/javascript">
onloadDiv();
function onloadDiv(){
	$("#altCount").hide(); 
	$("#popDiv").css("height","25px"); 
	$("#popDiv").css("opacity","0.3"); 
}

$("#popDiv").mouseenter(function(){
    //鼠标移入
    $("#altCount").show();
    $("#popDiv").css("height","200px"); 
	$(this).css("opacity","1"); 
}).mouseleave(function(){
    //鼠标移出
    $("#altCount").hide(); 
    $("#popDiv").css("height","25px"); 
	$(this).css("opacity","0.3"); 
});
setDivAltCount();
</script>
</html>