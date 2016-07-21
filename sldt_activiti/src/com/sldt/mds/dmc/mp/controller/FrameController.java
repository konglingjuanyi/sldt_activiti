package com.sldt.mds.dmc.mp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frame.do")
public class FrameController {

	@RequestMapping(params="method=mpMainPage")
	public String mpMainPage(){
		return "mp/home/home";
	}
	
	@RequestMapping(params="method=reqEditOkMetaPage")
	public String reqEditOkMetaPage(HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("dbId",request.getParameter("dbId"));
		return "mp/sysnav/editMetaOkPage";
	}
	
	@RequestMapping(params="method=handle")
	public String handle(HttpServletRequest request,HttpServletResponse response){
		String taskId = (String) request.getParameter("taskId");
		String decId = (String) request.getParameter("decId");
		String node = (String) request.getParameter("node");
		String assignee = (String) request.getParameter("assignee");
		request.setAttribute("taskId", taskId);
		request.setAttribute("decId", decId);
		request.setAttribute("assignee", assignee);
		String location = "";
		if("firsthecker".equals(node)){
			location = "mp/check/firsthecker";
		}else if("rechecker".equals(node)){
			location = "mp/check/rechecker";
		}else if("analyzer".equals(node)){
			location = "mp/check/analyzer";
		}else if("feedbac".equals(node)){
			location = "mp/check/feedbac";
		}else if("approver".equals(node)){
			location = "mp/check/approver";
		}else if("appraisal".equals(node)){
			location = "mp/check/appraisal";
		}
		return location;
	}
	
	@RequestMapping(params="method=handleForInit")
	public String handleForInit(HttpServletRequest request,HttpServletResponse response){
		String taskId = (String) request.getParameter("taskId");
		String initDecId = (String) request.getParameter("initDecId");
		String node = (String) request.getParameter("node");
		String assignee = (String) request.getParameter("assignee");
		request.setAttribute("taskId", taskId);
		request.setAttribute("initDecId", initDecId);
		request.setAttribute("assignee", assignee);
		String location = "";
		if("firstCheck".equals(node)){
			location = "mp/initCheck/firsthecker";
		}else if("rechecker".equals(node)){
			location = "mp/initCheck/rechecker";
		}
		return location;
	}
	
	@RequestMapping(params="method=reqSignPage")
	public String reqSignPage(HttpServletRequest request,HttpServletResponse response){
		return "mp/check/sign/sign";
	}
	
}
