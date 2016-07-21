package com.sldt.activiti.process.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;

@Controller
@RequestMapping("/activiti/processInstance.do")
public class ProcessInstanceController {

	private static Log log = LogFactory.getLog(ProcessInstanceController.class);

	@Resource
	private ProcessEngineImpl processEngine;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private RepositoryServiceImpl repositoryService;
	@Resource
	private HistoryService historyService;

	/**
	 * 流程实例的Id
	 */
	private String id;
	private String definitionId;
	
	protected String[] ids;
	/**
	 * 查询条件
	 */
	protected String definitionKey;
	
	/**
	 * 分页查询
	 * @return
	 */
	@RequestMapping(params="method=getProcessInstanceByPage")
	@ResponseBody
	public Object getProcessInstanceByPage(HttpServletRequest request, HttpServletResponse response) {
		PageResults page = ControllerHelper.buildPage(request);
		
		ProcessInstanceQuery query = runtimeService
				.createProcessInstanceQuery();
		if (null != definitionKey && !"".equals(definitionKey.trim())) {
			query.processDefinitionKey(definitionKey);
		}

		
		long rowCount = query.count();
		List<?> result = query.listPage(page.getStartIndex(), page.getPageSize());
		
		page.setTotalRecs((int)rowCount);
		page.setQueryResult(result);
		
		return ObjectToJSON.returnResult(result, page);
	}
	
	/**
	 * 流程实例删除
	 * @return
	 */
	@RequestMapping(params="method=deleteProcessInstance")
	@ResponseBody
	public String deleteProcessInstance(HttpServletRequest request, HttpServletResponse response) {
		for (String id : ids) {
			runtimeService.deleteProcessInstance(id, "删除原因测试");
		}
		return "listAction";
	}
	
	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getDefinitionKey() {
		return definitionKey;
	}

	public void setDefinitionKey(String definitionKey) {
		this.definitionKey = definitionKey;
	}	

	public void setId(String id) {
		this.id = id;
	}
	
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

}
