<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
 var context = '<%=request.getContextPath()%>';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>元数据版本</title>
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
<body style = "background-color:#F5F5F5; ">
    <div class="gf_all">
    <%-- <div class="top">
           <jsp:include page="/page/common/head.jsp" flush="true" />
        </div> --%>
        <!-- 中间 -->
        <div class="center">
            <!-- 左边 -->
            <div class="center_left_content">
            <div id="change_02">版本浏览</div>
            <div class="change_font_04">
                                        元数据变更通过版本发布合理管理元数据。通过版本浏览查看元数据的版本概要。
            </div>
            <div id="myVer"><!-- 我的系统分割线 --></div>
            <div id="my_system_version"><!-- 我的系统版本 --></div>
            <div id="allVer"><!-- 所有系统分割线 --></div>
            <div id="allVerSys" style="margin-left:30px;">
                <table width="98%" id="all_sys_tab1">
                <!-- 默认显示 -->
                </table>
                <!-- 收缩展开效果 -->
                <div class="more_sys">
                <table width="98%" id="all_sys_tab2">
                <!-- 展开更多 -->
                </table>
                </div>
            </div>
            <br><br>
            </div>
            <!-- 右边 -->
            <div class="center_right_content">
                <div id="change_42">
                    <div id="change_43"><img src="../imgs/ny19.png"></div>
                    <div id="change_44"></div>
                </div>
                <div id="change_45">
                    <div id="change_46">系统统计</div>
                    <div id="change_47">浏览次数：83次<br>
                                                                                                     版本次数 : 55次<br>最近更新 : 2014-12-2
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">
	var titleDiv = getTitle(1,"我的系统");
	$("#myVer").html(titleDiv);
	var titleDiv2 = getTitle(2,"所有系统");
	$("#allVer").html(titleDiv2);
	function showHideSys(){
        $(".box").html($(".more_sys").is(":hidden") ? "<img src=\""+context+"/page/mp/imgs/gf_vcomp_shouqi.png\"/><font style=\"font-size:13px;\">&nbsp;收起</font>"+"" : "<img src=\""+context+"/page/mp/imgs/gf_vcomp_zhankai.png\"/><font style=\"font-size:13px;\">&nbsp;展开更多</font>"+"");
        $(".more_sys").slideToggle("slow");
   }
	/**
	 * 我的系统
	 */
	 getMySysAllVer();

	/**
	 * 全部系统
	 */
	 getAllMetadataDatabase();
	
	 clickCmpVersion();
	 
	 $(document).ready(function(){
         $(".moreVer").click(function() {
            var id = $(this)[0].id;
            //debugger;
            $(this).html($(".more_ver_"+id).is(":hidden") ? "<img src=\""+context+"/page/mp/imgs/gf_vcomp_shouqi.png\"/><font style=\"font-size:13px;\">&nbsp;收起</font>"+"" : "<img src=\""+context+"/page/mp/imgs/gf_vcomp_zhankai.png\"/><font style=\"font-size:13px;\">&nbsp;更多历史版本</font>"+"");
            $(".more_ver_"+id).slideToggle("slow"); 
        }); 
    });
</script>
</html>