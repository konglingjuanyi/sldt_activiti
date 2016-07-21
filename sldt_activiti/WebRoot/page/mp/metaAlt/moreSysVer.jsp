<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据管控应用门户-历史变更</title>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/morever/style.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/frame.css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/page/mp/css/metadataChange.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/public/jquery/jquery.js"></script>

<script type="text/javascript">
var context = '<%=request.getContextPath()%>';
var dbId = "<%=request.getParameter("dbId")%>";
var dbName = "<%=request.getParameter("dbName")%>";
</script>
</head>
<body style="background-color: #F5F5F5;">
    <div class="gf_all">
        <div class="center">
            <!-- 左边 -->
            <div class="center_left_content">
            <!-- 目录 -->
            <div id="sys_ver_list_Div">
                <div class="content">
                    <div class="wrapper">
                        <div class="light">
                            <i></i>
                        </div>
                        <div class="main">
                            <h1 class="title" id="sysTitle"></h1>
                            <div id="content_time">
                                <div class="year">
                                    <h2>
                                        <a href="#">2014-11<i></i></a>
                                    </h2>
                                    <div class="list">
                                        <ul>
                                            <li class="cls" id="version_li0">
                                                <p class="date">15</p>
                                                <p class="intro">正式投产使用</p>
                                                <p class="version">&nbsp;</p>
                                                <div class="more">
                                                    <p>系统正式建立</p>
                                                </div>
                                             </li>
                                             <li class="cls" id="version_li1">
                                                <p class="date">26</p>
                                                <p class="intro" onmouseover="myMouseUp('1')"
                                                    onmouseout="myMouseOut('1')">全行推广</p>
                                                 <p class="version">&nbsp;</p>
                                                 <div class="more">
                                                    <p>新一代核心银行系统全行推广</p>
                                                 </div>
                                             </li>
                                        </ul> 
                                    </div>
                                </div>
                                <div class="year">
                                    <h2>
                                        <a href="#">2014-12<i></i></a>
                                    </h2>
                                    <div class="list">
                                        <ul>
                                            <li class="cls">
                                                <p class="date">20</p>
                                                <p class="intro">bug修复</p>
                                                <p class="version">&nbsp;</p>
                                                <div class="more">
                                                    <p>新一代核心银行系统全行推广</p>
                                                </div>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
    </div>
    <div class="center_right_content">
        <div id="change_42">
            <div id="change_43">
                <img src="../imgs/ny19.png">
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
</div>
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/page/mp/metaAlt/moreSysVer.js"></script>
</html>