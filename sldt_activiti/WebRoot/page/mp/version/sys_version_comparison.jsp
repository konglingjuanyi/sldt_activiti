<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String dbName = request.getParameter("dbName");
String dbId = request.getParameter("dbId");
String initDbId=dbId;
/* if(dbId.endsWith("_UAT")){
   dbId=dbId.substring(0,dbId.length()-4);
} */
String Col_1 = request.getParameter("Col_1");
String Col_2 = request.getParameter("Col_2");
String Col_3 = request.getParameter("Col_3");
String c1_Name = request.getParameter("c1_Name");
String c2_Name = request.getParameter("c2_Name");
String c3_Name = request.getParameter("c3_Name");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
var context = "<%=request.getContextPath()%>";
var dbId = "<%=dbId%>";
var initDbId="<%=initDbId%>";
var sysName = "<%=dbName%>";
var Col_1 = "<%=Col_1%>";
var Col_2 = "<%=Col_2%>";
var Col_3 = "<%=Col_3%>";
var Col_4 = '';
var Col_5 = '';

var c1_Name = "<%=c1_Name%>";
var c2_Name = "<%=c2_Name%>";
var c3_Name = "<%=c3_Name%>";
</script>
<title>数据管控应用门户-版本比对</title>
<link href="<%=request.getContextPath()%>/page/mp/css/frame.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/common.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/page/mp/css/sys_version_comparison.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"  src="<%=request.getContextPath()%>/public/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/common/util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/version/sys_version_comparison.js"></script>
</head>
<jsp:include page="/page/mp/version/version_comp_chooseVer.jsp" flush="true" />
<body style="background-color:#F5F5F5;">
 <div class="gf_all">
    <!-- 中间 -->
    <div class="center" >
        <div class="center_content">
            <div class="top">
                 <jsp:include page="/page/mp/version/version_comp_topInfo.jsp" flush="true" />
            </div>
            <!-- 数据库信息 -->
             <div class="title_div">
                <div class="version_cmp">
                    <div class="xd-name">数据库信息</div>
                    <div class="cmp_font_07"><a href="#" id="toggle" class="cmp_font_09"><img src="<%=request.getContextPath()%>/page/mp/imgs/gf_vcomp_shouqi.png"/>&nbsp;收起</a></div>
                </div>
             </div>
             <div class="dataBaseInfo">
                  <jsp:include page="/page/mp/version/version_comp_dataBaseInfo.jsp" flush="true" />
             </div>
             <!-- 模式信息 -->
             <div class="title_div" id="schema_title_div">
             <div class="version_cmp">
                <div class="xd-name" id="schema_title_name">模式信息</div>
                <div class="cmp_font_07" ><a href="#" id="toggle2" class="cmp_font_09"><img src="<%=request.getContextPath()%>/page/mp/imgs/gf_vcomp_shouqi.png"/>&nbsp;收起</a></div>
             </div>
             </div>
             <div class="schema">
                  <jsp:include page="/page/mp/version/version_comp_schemaInfo.jsp" flush="true" />
             </div>
             <!-- 模块信息 -->
             <div class="title_div">
                <div class="version_cmp">
                <div class="xd-name" id="module_title_name">模块信息</div>
                <div class="cmp_font_07" ><a href="#" id="toggle3" class="cmp_font_09"><img src="<%=request.getContextPath()%>/page/mp/imgs/gf_vcomp_shouqi.png"/>&nbsp;收起</a></div>
                </div>
        </div>
        <div class="module">
            <jsp:include page="/page/mp/version/version_comp_moduleInfo.jsp" flush="true" />
        </div>
        <!-- 表信息 -->
        <div class="title_div">
            <div class="version_cmp">
                <div class="xd-name" id="table_title_name">表信息</div>
                <div class="cmp_font_07" ><input name="noSameTable" type="checkbox" value="noSameTable" class="isTabChange" />&nbsp;忽略相同条目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="toggle4" class="cmp_font_09"><img src="<%=request.getContextPath()%>/page/mp/imgs/gf_vcomp_shouqi.png"/>&nbsp;收起</a></div>
            </div>
        </div>
        <div class="table">
            <jsp:include page="/page/mp/version/version_comp_tableInfo.jsp" flush="true" />
        </div>
         <!-- 字段信息 -->
        <div class="title_div">
            <div class="version_cmp">
                <div class="xd-name" id="column_title_name">字段信息</div>
                <div class="cmp_font_07" ><input name="noSameCol" type="checkbox" value="noSameCol" class="isColChange" />&nbsp;忽略相同条目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="toggle5" class="cmp_font_09"><img src="<%=request.getContextPath()%>/page/mp/imgs/gf_vcomp_shouqi.png"/>&nbsp;收起</a></div>
            </div>
        </div>
        <div class="column">
             <jsp:include page="/page/mp/version/version_comp_columnInfo.jsp" flush="true" />
        </div>
    </div>
 </div>
 </div>
</body>
</html>