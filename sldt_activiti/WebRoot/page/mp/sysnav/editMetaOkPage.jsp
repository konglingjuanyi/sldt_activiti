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
<title>系统介绍</title>

<script type="text/javascript"src="<%=request.getContextPath()%>/public/jquery/jquery.js"></script>
<script type="text/javascript"src="<%=request.getContextPath()%>/page/mp/common/util.js"></script>

<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/page/mp/css/edit.css">
</head>

<body style="background-color: #FFFFFF;" onload="loadData()">
<div class="altItemcontent">
 	<div id="change_03">
		    <div id="change_05">基本信息</div>
		  </div>
<div id="change_04" style="padding-bottom: 50px;">
	<div class="wb" style="margin-left: 20px;">
	<ul class="list1" id='db_list_ul'>
	 <li class="cl1">元数据代码：</li> 
	 <li class="cl2"></li> 
	  <li class="cl3" >元数据名称：</li> 
	 <li  class="cl4" id='dbchname' ></li>
	 <li  class="cl3">生效时间：</li> 
	  <li  class="cl4"></li> 
	  <li  class="cl1">所属开发科室：</li>  
	 <li  class="cl2" id='dept' ></li>
	 <li  class="cl3">DBA：</li>
	 <li  class="cl4" id='dba' ></li>
	  <li  class="cl1">开发商：</li>  
	 <li  class="cl2" id='proMFac' ></li>
	 <li  class="cl3">开发负责人：</li>
	 <li  class="cl4" id='devloper'></li>
	 <li class="cl3">元数据类型：</li>  
	 <li  class="cl4">系统</li> 
	 <li  class="cl1">功能简介：</li>  
	  <li  class="tiledcl5" id='remark'></li>
	  </ul>  
	 </div> 
</div>
	<div>
		<table class="data_table_alt_detail" style="width: 780px;">
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
			<tbody id="altModuleItem">
				
			</tbody>
		</table>
	</div>
	<div style="padding-top: 50px;">
		<table class="data_table_alt_detail" style="width: 780px;">
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
			<tbody id="altTableItem">
			</tbody>
		</table>
	</div>
	<div style="padding-top: 50px;">
		<table class="data_table_alt_detail" style="width: 780px;">
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
			<tbody id="altColumnItem">
			</tbody>
		</table>
	</div>
</div>
</body>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/page/mp/sysnav/editMetaOkPage.js"></script>
</html>