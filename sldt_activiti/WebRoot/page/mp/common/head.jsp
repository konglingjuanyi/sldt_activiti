<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- <%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %> --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/public/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/public/fontAwesome/css/font-awesome.min.css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/public/fontAwesome/css/font-awesome-ie7.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/public/sunlineUI/css/sunline.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/public/bootstrap/js/bootstrap.min.js"></script>

<!-- head 页面内容  -->
    <div class="header">
        <div class="header-in">
            <div class="header-left fl">
                <h1>
                    <a href="javacript:void(0)"><img src="<%=request.getContextPath()%>/public/sunlineUI/img/logo.png" alt="" /></a>
                </h1>
            </div>
            <div class="header-right fr">
                <div class="Hright-top clear">
                    <ul class="fr">
                        <li>欢迎访问，<font color="red"><!-- <shiro:principal/> --></font> ！</li>
                        <li><a class="modify" href="javascript:void(0)">修改密码</a></li>
                        <li><a class="cancellation" href="<%=request.getContextPath()%>/../ssm/logout">注销</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="nav">
        <div class="nav-in">
        	<ul>
            	<%-- <c:if test="${rootMenus.childs!=null }">
                	<c:forEach items="${rootMenus.childs }" var="rootM" varStatus="vs">
                    	<c:if test="${vs.index==0}">
                        	<li><a class="cur" id="rootMenu_${rootM.menuId}" href="javascript:void(0)" url="${rootM.menuUrl}">${rootM.menuName }</a></li>
                        </c:if>
                        <c:if test="${vs.index!=0}">
                        	<li><a id="rootMenu_${rootM.menuId}" href="javascript:void(0)" url="${rootM.menuUrl}">${rootM.menuName }</a></li>
                        </c:if>
                    </c:forEach>
                </c:if> --%>
                <li><a id="nav_1" class="cur" href="javascript:void(0)" url="page/mp/home/home_content.jsp">首页</a></li>
                <li><a id="nav_2" class="" href="javascript:void(0)" url="page/mb/dmv/DataMapView.jsp">数据地图</a></li>
                <li><a id="nav_3" class="" href="javascript:void(0)" url="page/mp/metaAlt/metaAlteration.jsp">元数据变更</a></li>
                <li><a id="nav_4" class="" href="javascript:void(0)" url="page/mp/version/all_sys_version_view.jsp">元数据版本</a></li>
                <li><a id="nav_5" class="" href="javascript:void(0)" url="page/mp/flow/declareList.jsp">元数据流程</a></li>
                <li><a id="nav_6" class="" href="javascript:void(0)" url="../../page/activiti/process/model/modelManage.jsp">模型管理</a></li>
            </ul>
        </div>
    </div>
