<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
z-index:9;  
width: 150px;  
height: 100px;  
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
<script type="text/javascript">

var context = '<%=request.getContextPath()%>';
var dbId = "<%=request.getParameter("dbId")%>";
var optId = "<%=request.getParameter("optId")%>";
var verType = "<%=request.getParameter("verType")%>";
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据管控应用门户-系统详情</title>
<link href="<%=request.getContextPath()%>/page/mp/css/metadataChange.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/common/util.js"></script>
</head>
<body style="background-color: #F5F5F5;">
    <div id="popDiv" class="mydiv">
                系统管理
       <div style="margin: 10px;border: 0px solid red;overflow: hidden;" id="sysMgr">
            <div id='editSys' style="float: left;padding: 0px;"></div>
            <div id='compSys' style="float: left;padding-left: 10px;"></div>
            <div style="float: left;padding-left: 10px;" id="downExcel"></div>
       </div>
    </div>
    <div class="gf_all">
        <!-- 中间 -->
        <div class="center">
            <!-- 左边 -->
            <div class="center_left_content">
                <div id="change_02">无系统信息</div>
                <div id='change_02_desc' class="change_font_04"></div>
                <div id="change_03">
                  <div id="change_05">基本信息</div>
                </div>
                <div id="change_04"></div>
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
    <div class="tiledcenter">
        <jsp:include page="/page/mp/version/sysDetailVersion.jsp" flush="true" />
    </div>
  </div>
</body>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/version/sysVersion.js"></script>
</html>