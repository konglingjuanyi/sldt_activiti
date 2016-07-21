<%@ page contentType="text/html; charset=utf-8"%>
<%
	request.getSession().invalidate();
	response.sendRedirect(request.getContextPath()+"/frame.do?method=mpMainPage");
%>