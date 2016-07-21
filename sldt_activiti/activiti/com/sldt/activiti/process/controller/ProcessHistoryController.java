package com.sldt.activiti.process.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;

@Controller
@RequestMapping("/activiti/processHistory.do")
public class ProcessHistoryController {

	private static Log log = LogFactory.getLog(ProcessHistoryController.class);
	
	@Resource
	private HistoryService historyService;
	
	protected String[] ids;
	/**
	 * 查询条件
	 */
	protected String definitionKey;
	
	/**
	 * 分页查询
	 * @return
	 */
	@RequestMapping(params="method=getProcessHistoryByPage")
	@ResponseBody
	public Object getProcessHistoryByPage(HttpServletRequest request, HttpServletResponse response) {
		PageResults page = ControllerHelper.buildPage(request);
		
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		query = query.finished();
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
	 * @MethodName: delete
	 * @Description: 删除
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params="method=deleteProcessHistory")
	@ResponseBody
	public String deleteProcessHistory(HttpServletRequest request, HttpServletResponse response) {
		for (String id : ids) {
			historyService.deleteHistoricProcessInstance(id);
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

	
}
