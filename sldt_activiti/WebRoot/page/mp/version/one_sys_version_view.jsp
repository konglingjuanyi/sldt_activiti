<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
String sysName = request.getParameter("dbName");
String dbId = request.getParameter("dbId");
%>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
var dbId = '<%=dbId%>';
var sysName = '<%=sysName%>';
</script>
<title>数据管控应用门户-元数据版本</title>
<link href="<%=request.getContextPath() %>/page/mp/css/metadataChange.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/page/mp/css/version_view.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/page/mp/css/common.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/page/mp/css/frame.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/page/mp/css/edit.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/page/mp/css/metaAlteration.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/public/layer/skin/layer.css" rel="stylesheet" type="text/css">
<script type="text/javascript"  src="<%=request.getContextPath()%>/public/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/version/version_view.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/common/util.js"></script>
<style type="text/css">
.xubox_shade, .xubox_layer{
    position: fixed;
}
.xubox_layer{
top:300px !important;
}
</style>
</head>
<body style="background-color:#F5F5F5;">
    <div class="gf_all">
    <!-- 中间 -->
    <div class="center" >
    <!-- 左边 -->
     <div class="center_left_content">
      <div id="change_02">【<%=sysName %>】版本浏览</div>
      <div class="change_font_04">
    <!-- 元数据变更通过版本发布合理管理元数据。通过版本浏览查看元数据的版本概要。 -->
      </div>
    <div id="sysVer">
        <!-- 分割线 -->
     </div> 
     <div class="one_sys_version"><!-- 版本信息 --></div>
    
    </div> 
      <!-- 右边 -->
       <div class="center_right_content">
       <div id="change_42">
        <div id="change_43"><img src="<%=request.getContextPath() %>/page/mp/imgs/ny19.png"></div>
        <div id="change_44"></div> 
         </div>
       <div id="change_45">
        <div id="change_46">系统统计</div>
        <div id="change_47">浏览次数 : 83次<br>
                            版本次数 : 55次<br>最近更新 : 2014-12-2
        </div>
      </div>  
         </div>
        </div> 
        </div>
</body>
<script type="text/javascript">
    var titleDiv = getTitle("","【<%=sysName %>】版本查看");
    $("#sysVer").html(titleDiv);
</script>
 <script type="text/javascript">
    <!--

$.ajax({
    url:context+"/metadata.do?method=getSysMetadataVersion",
    method: 'POST',
    async:false,//同步
    dataType :'json',
    data:{dbId:dbId,sysName:sysName},
    success: function(response) {
    var _dateques = response.data;
    //debugger;
    var ver = getSysAllVersion(_dateques);
    $(".one_sys_version").html(ver); 
    clickCmpVersion();
    },fail : function(){
        alert("load fail!");
    }
});

$(document).ready(function(){
    $(".moreVer").click(function() {
        var id = $(this)[0].id;
        //debugger;
        $(this).html($(".more_ver_"+id).is(":hidden") ? "收起"+"" : "更多历史版本"+"");
        $(".more_ver_"+id).slideToggle("slow"); 
    }); 
        
    });

    //-->
    </script>
</html>