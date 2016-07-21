package com.sldt.mds.dmc.mp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	public static final String AUTH_KEY = UserAuthFilter.class.getName() + "$AUTHENTICATION";
			
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain fc) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		//获取访问路径
		String path = req.getServletPath();
		if(req.getQueryString()!=null&&req.getQueryString().equals("method=login")&&path.equals("/frame.do")){
			fc.doFilter(req, resp);
		}else if(path.endsWith("login.jsp")//如果访问的只是login.jsp
				||path.endsWith(".js")||path.endsWith(".css")||path.endsWith(".jpg")||path.endsWith(".png")
				){
			//直接放行
			fc.doFilter(req, resp);
		}else{
			Object obj = req.getSession().getAttribute(UserAuthFilter.AUTH_KEY);
			if( obj == null ){
				resp.sendRedirect("/dmc/login.jsp");
			}else{
				fc.doFilter(req, resp);
			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
