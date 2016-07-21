<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	var context = '<%=request.getContextPath()%>';
	var dbId = '<%=request.getParameter("dbId")%>';
	var optId = '';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据管控应用门户-表级接口（文件）使用关系</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/page/mp/css/frame.css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/page/mp/css/common.css" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/public/fontAwesome/css/font-awesome.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/public/fontAwesome/css/font-awesome-ie7.min.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/public/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/public/bootstrap/css/bootstrap.min.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jqgrid/js/jquery.jqGrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/jqgrid/js/i18n/grid.locale-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/layer/layer.js"></script>




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

.ui-button-text-icon-primary .ui-button-icon-primary,.ui-button-text-icons .ui-button-icon-primary,.ui-button-icons-only .ui-button-icon-primary
    {
    left: 0.3em;
}

.ui-button-text-icon-primary .ui-button-text {
    padding: 0.4em 0.8em 0.4em 1.7em;
}

.ui-corner-all-12,.ui-corner-top-12,.ui-corner-left-12,.ui-corner-tl-12
    {
    -moz-border-radius-topleft: 12px;
    -webkit-border-top-left-radius: 12px;
    -khtml-border-top-left-radius: 12px;
    border-top-left-radius: 12px;
}

.ui-corner-all-12,.ui-corner-top-12,.ui-corner-right-12,.ui-corner-tr-12
    {
    -moz-border-radius-topright: 12px;
    -webkit-border-top-right-radius: 12px;
    -khtml-border-top-right-radius: 12px;
    border-top-right-radius: 12px;
}

.ui-corner-all-12,.ui-corner-bottom-12,.ui-corner-left-12,.ui-corner-bl-12
    {
    -moz-border-radius-bottomleft: 12px;
    -webkit-border-bottom-left-radius: 12px;
    -khtml-border-bottom-left-radius: 12px;
    border-bottom-left-radius: 12px;
}

.ui-corner-all-12,.ui-corner-bottom-12,.ui-corner-right-12,.ui-corner-br-12
    {
    -moz-border-radius-bottomright: 12px;
    -webkit-border-bottom-right-radius: 12px;
    -khtml-border-bottom-right-radius: 12px;
    border-bottom-right-radius: 12px;
}

/* 自动完成的loading图片 */
.ui-loading {
    background: url('../imgs/ajax/loading.gif') right center no-repeat;
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

.pagination ul>li>a:hover,.pagination ul>.active>a,.pagination ul>.active>span
    {
    background-color: #f5f5f5;
}

.pagination ul>.active>a,.pagination ul>.active>span {
    color: #999999;
    cursor: default;
}

.pagination ul>.disabled>span,.pagination ul>.disabled>a,.pagination ul>.disabled>a:hover
    {
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

.ui-dialog .ui-dialog-titlebar-close,.ui-jqdialog .ui-dialog-titlebar-close,.ui-dialog .ui-jqdialog-titlebar-close,.ui-jqdialog .ui-jqdialog-titlebar-close
    {
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
</style>
<script type="text/javascript">
	var dbId = "<%= request.getParameter("dbId")%>";
	var optId = "<%= request.getParameter("optId")%>";
</script>
</head>
<body style="background-color: #F5F5F5;">
<div class="gf_all">
    <div style="padding-top: 80px;"></div>
    <div class="tiledcenter">
        <div style="padding-top: 20px;padding-left: 20px; font-weight: bolder;font-size: 18px;">
                         我的引用系统
        </div>
        <div style="padding-top: 20px;padding-left:30px;">
            <span>引用系统标识：<input type="text" style="height: 25px; width: 150px;"  id="my_refDbId"  placeholder="请输入引用系统标识"></span>&nbsp;&nbsp;&nbsp;&nbsp;
            <span>引用模式名：<input type="text" width="100"  style="height: 25px; width: 150px;"  id="my_refSchema"  placeholder="请输入引用模式名"></span>&nbsp;&nbsp;&nbsp;&nbsp;
            <span>引用模块名：<input type="text" width="100"  style="height: 25px; width: 150px;"  id="my_refModule"  placeholder="请输入引用模块名"></span>&nbsp;&nbsp;&nbsp;&nbsp;
            <span>引用表名：<input type="text" width="100"  style="height: 25px; width: 150px;"  id="my_refTable"  placeholder="请输入引用表名"></span>
            <br/>
            <br/>
            <span>接口/功能名称：<input type="text" width="150"  style="height: 25px; width: 150px;"  id="my_refFuncName"  placeholder="请输入接口/功能名称"></span>&nbsp;&nbsp;&nbsp;&nbsp;
            <span >依赖关系类型：
                 <select id="my_cType" style="height: 25px;width: 145px;">
                   <option value='' selected>--不限--</option>
                   <option value='1'>程序依赖</option>
                   <option value='0'>数据表依赖</option>
                 </select>
             </span>
             <img onclick="findFileInt('0')" style="cursor: pointer; margin-left: 50px;" src="<%=request.getContextPath()%>/page/mp/imgs/gf_search.png">
        </div>
        <div style="padding-top: 10px;">
            <table id="grid-table" ></table>
             <div id="grid-pager"></div>
        </div>
        <div style="padding-top: 20px;padding-left: 20px; font-weight: bolder;font-size: 18px;">
                             引用我的系统
        </div>
        <div style="padding-top: 20px;padding-left: 30px;">
             <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统标识：<input type="text" style="height: 25px; width: 150px;"  id="refDbId"  placeholder="请输入引用系统标识"></span>&nbsp;&nbsp;&nbsp;&nbsp;
             <span>引用模式名：<input type="text" width="100"  style="height: 25px; width: 150px;"  id="refSchema"  placeholder="请输入引用模式名"></span>&nbsp;&nbsp;&nbsp;&nbsp;
             <span>引用模块名：<input type="text" width="100"  style="height: 25px; width: 150px;"  id="refModule"  placeholder="请输入引用模块名"></span>&nbsp;&nbsp;&nbsp;&nbsp;
             <span>引用表名：<input type="text" width="100"  style="height: 25px; width: 150px;"  id="refTable"  placeholder="请输入引用表名"></span>
             <br/>
             <br/>
             <span>接口/功能名称：<input type="text" width="150"  style="height: 25px; width: 150px;"  id="refFuncName"  placeholder="请输入接口/功能名称"></span>&nbsp;&nbsp;&nbsp;&nbsp;
             <span >依赖关系类型：
                  <select id="cType" style="height: 25px;width: 145px;">
                    <option value='' selected>--不限--</option>
                    <option value='1'>程序依赖</option>
                    <option value='0'>数据表依赖</option>
                  </select>
              </span>
              <img onclick="findFileInt('1')" style="cursor: pointer; margin-left: 50px;" src="<%=request.getContextPath()%>/page/mp/imgs/gf_search.png">
       </div>
       <div style="padding-top: 10px;">
           <table id="grid-table_ref" ></table>
            <div id="grid-pager_ref"></div>
       </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/page/mp/version/pFileIntPage.js"></script>
</html>