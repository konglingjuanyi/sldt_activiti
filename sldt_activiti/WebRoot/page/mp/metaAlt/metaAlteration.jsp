<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>元数据变更</title>
<link href="<%=request.getContextPath() %>/page/mp/css/metadataChange.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/page/mp/css/metaAlteration.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/page/mp/css/common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath() %>/public/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/public/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/common/util.js"></script>

</head>
<body style="background-color:#F5F5F5;">
    <div class="gf_all">
        <div class="center">
            <div id="alt_title">
                <div id="alt_tile_1" style="float:left;"><b>元数据变更</b></div>
                <div style="margin-top:22px;float: right;margin-right: 10px;" id="uploadExcelImg">
                    <img title='通过excel上传元数据变更信息...' style="cursor: pointer;" onclick="reqUploadPage()" src="../imgs/excel.png">
                </div>
            </div>
            <div id="alt_hr"> <hr color="#ed1a2e"></div>
            <!-- 全部表格div -->
            <div id="change_table_column_info">
                <div id="change_table_info">
                    <div id="change_16">
                         <div style="padding-top: 20px;">
                                                                    努力加载中... 
                         </div>
                     </div>
                </div>  
            </div>
       
        <!-- 右边 -->
       <div class="center_right_content1">
            <div id="change_42">
                <div id="change_43"><img src="<%=request.getContextPath() %>/page/mp/imgs/ny19.png"></div>
                <div id="change_44"></div> 
            </div>
            <div id="change_45">
                <div id="change_46">系统统计</div>
                <div id="change_47">
                </div>
            </div>  
        </div>
      </div>
    </div> 
</body>
<script type="text/javascript" src="<%=request.getContextPath() %>/page/mp/metaAlt/metaAlteration.js"></script>
</html>