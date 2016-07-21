package com.sldt.activiti.process.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.query.NativeQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sldt.activiti.process.common.ProcessVariableEnum;
import com.sldt.activiti.process.domain.Job;
import com.sldt.activiti.process.domain.User;
import com.sldt.activiti.process.service.IBaseService;
import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;

@Controller
@RequestMapping("/activiti/processTask.do")
public class ProcessTaskController {

	private static Log log = LogFactory.getLog(ProcessTaskController.class);

	@Resource
	private TaskService taskService;
	@Resource
	private RepositoryServiceImpl repositoryService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private IBaseService baseService;
	@Resource
	private ManagementService managementService;

	/**
	 * 查询条件
	 */
	private String taskId;
	private String taskName;
	
	protected String[] ids;
	/**
	 * 查询条件
	 */
	protected String definitionKey;
	
	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getDefinitionKey() {
		return definitionKey;
	}

	public void setDefinitionKey(String definitionKey) {
		this.definitionKey = definitionKey;
	}	
	
	/**
	 * @MethodName: delete
	 * @Description: 删除待办任务
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params="method=deleteProcessTask")
	@ResponseBody
	public String deleteProcessTask(HttpServletRequest request, HttpServletResponse response) {
		for (String id : ids) {
			taskService.deleteTask(id, true);
		}
		return "listAction";
	}	
	
	/**
	 * @MethodName: findPage
	 * @Description: 分页查询
	 * @return
	 */
	@RequestMapping(params="method=getProcessTaskByPage")
	@ResponseBody
	public Object getProcessTaskByPage(HttpServletRequest request, HttpServletResponse response) {
		PageResults page = ControllerHelper.buildPage(request);
		
		//伪代码
		//select res.* 任务表 res left join 任务的候选人表 i on... where 
			//res.assginee = 当前用户 or 
		    //i.user_id_ = 当前用户 or
		    //i.group_id_ in 当前用户职位列表
		//in 不能使用参数
		StringBuilder sql = new StringBuilder("select DISTINCT RES.* from "
				+ managementService.getTableName(Task.class) + " RES left join "
				+ managementService.getTableName(IdentityLinkEntity.class) + " I on I.TASK_ID_ = RES.ID_ "
				+ " where RES.ASSIGNEE_ = #{assignee} "
				+ " or I.USER_ID_ = #{candidateUser} "
				+ " or I.GROUP_ID_ IN  ");
		//当前用户
		//User currentUser = baseService.get(User.class, SecurityHolder.getCurrentUser().getId());
		User currentUser = baseService.get(User.class, "admin");
		
		//得到当前用户的职位列表
		StringBuilder candidateGroups = new StringBuilder("(");
		for (Job j : currentUser.getJobs()) {
			candidateGroups.append("'" + j.getJobName() + "',");
		}
		if (candidateGroups.length() > 1) {
			candidateGroups.deleteCharAt(candidateGroups.length() - 1);
		}
		candidateGroups.append(")");
		sql.append(candidateGroups);
		
		
		//使用nativerQuery可以直接查询标准sql语句,直接操作底层所有的activiti的表
		NativeQuery query = taskService.createNativeTaskQuery()
			.sql(sql.toString())
			.parameter("assignee", currentUser.getUserName())
			.parameter("candidateUser", currentUser.getUserName());
		//查询一页数据
		List<Task> tasks = query.listPage(page.getStartIndex(), page.getPageSize());
		//查询总行数
		
		/*
		  select count(*) form (
		    select res.* 任务表 res left join 任务的候选人表 i on... where 
		    res.assginee = 当前用户 or 
	        i.user_id_ = 当前用户 or
	        i.group_id_ in 当前用户职位列表
	        //查询一页数据的语句，作为子查询，查询总行数
	      )
	    */
		
		long rowCount = query.sql("select count(*) from (" + sql.toString() + ") t1").count();
		List<Map<String, Object>> map = doResult(tasks);
		
		page.setTotalRecs((int)rowCount);
		page.setQueryResult(map);
		
		return ObjectToJSON.returnResult(map, page);
	}

	/**
	 * 处理代办任务的返回数据，因为单独返回task数据，页面不知道是请假，还是报销
	 *   任务名称=task.getName
	 *   开始时间=task.getStartTime
	 *   申请人= 任务变量requestUser.userName
	 *   标题= 任务变量(业务数据,比如请假单).tile
	 *   流程名称=查询任务的流程定义.name
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> doResult(List<Task> list) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Task task : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			//业务数据,在启动流程时，存放到流程引擎中的变量
			Object model = taskService.getVariable(task.getId(),
					ProcessVariableEnum.model.toString());
			//申请人,在启动流程时，存放到流程引擎中的变量
			Object requestUser = taskService.getVariable(task.getId(),
					ProcessVariableEnum.requestUser.toString());			
			map.put("model", model);
			map.put("requestUser", requestUser);
			map.put("task", task);
			//查询任务所有的流程定义,得到流程名称(请假流程，报销流程)
			ProcessDefinition processDefinition = repositoryService
					.createProcessDefinitionQuery()
					.processDefinitionId(task.getProcessDefinitionId())
					.singleResult();
			//流程名称
			map.put("processName", processDefinition.getName());
			result.add(map);
		}
		return result;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
