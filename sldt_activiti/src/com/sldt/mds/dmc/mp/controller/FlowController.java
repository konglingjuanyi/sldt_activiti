package com.sldt.mds.dmc.mp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.cas.dmp.ModuleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sldt.framework.common.CommonUtil;
import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.framework.common.PageVo;
import com.sldt.mds.dmc.mp.service.CheckInfoService;
import com.sldt.mds.dmc.mp.service.DeclareService;
import com.sldt.mds.dmc.mp.service.FlowService;
import com.sldt.mds.dmc.mp.service.MetadataAltService;
import com.sldt.mds.dmc.mp.service.MetadataVerService;
import com.sldt.mds.dmc.mp.service.MyDeclareService;
import com.sldt.mds.dmc.mp.service.MyInitDeclareService;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;
import com.sldt.mds.dmc.mp.util.ObjectToString;
import com.sldt.mds.dmc.mp.vo.CheckInfo;
import com.sldt.mds.dmc.mp.vo.DeclareVO;
import com.sldt.mds.dmc.mp.vo.MyDeclareVO;
import com.sldt.mds.dmc.mp.vo.MyInitDeclareVO;

@Controller
@RequestMapping("/flow.do")
public class FlowController {
	
	private static Log log = LogFactory.getLog(FlowController.class);
	
	@Resource(name="metaAltService")
	public MetadataAltService metaAltService;
	
	@Resource(name="checkInfoService")
	public CheckInfoService checkInfoService;
	
	@Resource(name="declareService")
	public DeclareService declareService;
	
	@Resource(name="flowService")
	public FlowService flowService;
	
	@Resource(name="myDeclareService")
	public MyDeclareService myDeclareService;
	
	@Resource(name="metaVerService")
	public MetadataVerService metaVerService;
	
	@Resource(name="myInitDeclareService")
	public MyInitDeclareService myInitDeclareService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private ProcessEngineFactoryBean processEngine;
	
	@RequestMapping(params="method=getMyAltMetaByPage")
	@ResponseBody
	public Object getMyAltMetaByPage(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String,Object>> list = metaAltService.getAltMetaItem(page);
		for (Map<String, Object> obj : list) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.returnResult(list,page);
	}
	
	@RequestMapping(params="method=startInitFlow")
	@ResponseBody
	public Object startInitFlow(HttpServletRequest request, String dbId, String decDesc, String instanceId, String initDecName,String assignee){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		Map<String,String> msg=null;
		if(StringUtils.isNotEmpty(userId)){
			MyInitDeclareVO myInitDeclareVO = new MyInitDeclareVO();
			String uuid = CommonUtil.getUUID();
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			myInitDeclareVO.setInitDecId(uuid);
			myInitDeclareVO.setAltOperDate(now);
			myInitDeclareVO.setAltUser(userId);
			myInitDeclareVO.setDbCode(dbId);
			myInitDeclareVO.setAltReason(decDesc);
			myInitDeclareVO.setActSts("1");
			myInitDeclareVO.setInstanceId(instanceId);
			myInitDeclareVO.setInitDecName(initDecName);
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("firsthecker", assignee);
			ProcessInstance processInstance = flowService.startInitFlow(myInitDeclareVO, variables);
			msg = new HashMap<String, String>();
			if(processInstance!=null&&processInstance.getProcessInstanceId()!=null){
				msg.put("success", "true");
				msg.put("msg", "申请成功，目前已进入系统版本覆盖申请流程！");
			}else{
				msg.put("success", "false");
				msg.put("msg", "申请失败，出现故障，请联系平台管理员！");
			}
		}
		return ObjectToJSON.returnResult(msg,null);
	}
	
	@RequestMapping(params="method=getDynamicFlow")
	@ResponseBody
	public Object getDynamicFlow(){
		String whereSql = "ORDER BY AUDIT_TIME DESC ";
		List<CheckInfo> checkInfos = checkInfoService.getCheckInfo(whereSql);
		return ObjectToJSON.returnResult(checkInfos, null);
	}
	
	@RequestMapping(params="method=declareList")
	@ResponseBody
	public PageVo getDeclareList(String decName, String actSts, HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(StringUtils.isNotBlank(decName)){
			decName = URLDecoder.decode(decName, "utf-8");
		}
		PageResults page = ControllerHelper.buildPage(request);
		List<DeclareVO> userList = declareService.getList(page, decName, actSts);
		PageVo pv = new PageVo(page.getPageSize(), page.getCurrPage(), page.getTotalRecs());
		pv.setRows(userList);
		return pv;
	}
	
	@RequestMapping(params="method=taskList")
	@ResponseBody
	public Object taskList(HttpServletRequest request){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		List<MyDeclareVO> myDeclares = flowService.taskList(userId);
		return ObjectToJSON.returnResult(myDeclares, null);
	}
	
	@RequestMapping(params="method=initTaskList")
	@ResponseBody
	public Object initTaskList(HttpServletRequest request){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		List<MyInitDeclareVO> myInitDeclares = flowService.initTaskList(userId);
		return ObjectToJSON.returnResult(myInitDeclares, null);
	}
	
	@RequestMapping(params="method=claim")
	@ResponseBody
	public Object claim(HttpServletRequest request, String taskId){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		taskService.claim(taskId, userId);
		return ObjectToJSON.returnResult("签收成功！", null);
	}
	
	@RequestMapping(params="method=complete")
	@ResponseBody
	public Object complete(HttpServletRequest request, String taskId, String decId, String node, String checkResult, String checkOpinion,String assignee){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		if(assignee == ""){
			claimTask(taskId, userId);
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		String actId = "";
		String whereSql = "";
		CheckInfo checkInfo = new CheckInfo();
		if(StringUtils.isNotBlank(decId)){
			whereSql = "AND DEC_ID = '"+decId+"' ";
		}
		List<MyDeclareVO> myDeclareVOs = myDeclareService.getMyDeclare(whereSql);
		MyDeclareVO myDeclare = null;
		if(myDeclareVOs.size() == 1){
			actId = myDeclareVOs.get(0).getActId();
			myDeclare = myDeclareVOs.get(0);
		}
		if("0".equals(checkResult)){
			variables.put(node, 0);
			checkInfo.setAuditResult("通过");
		}else if("1".equals(checkResult)){
			variables.put(node, 1);
			checkInfo.setAuditResult("不通过");
		}else if("2".equals(checkResult)){
			variables.put(node, 2);
			checkInfo.setAuditResult("全行评审");
		}
		if("firsthecker".equals(node)){
			if("1".equals(checkResult)){
				updateAlterationAltsts("2", decId);
				myDeclare.setActSts("4");
				myDeclare.setLastModifier(userId);
				myDeclare.setLastModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				whereSql = "AND DEC_ID = '"+myDeclare.getDecId()+"' ";
				myDeclareService.update(myDeclare, whereSql);
			}
		}else if("approver".equals(node) || "appraisal".equals(node)){
			if("0".equals(checkResult)){
				myDeclare.setActSts("2");
				myDeclare.setLastModifier(userId);
				myDeclare.setLastModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				whereSql = "AND DEC_ID = '"+myDeclare.getDecId()+"' ";
				PageResults page = ControllerHelper.buildPage(request);
				page.setParameter("decId", decId);
				//完成变更申报后进行版本更新
				metaVerService.insVerMetaByDecId(page);
				updateAlterationAltsts("0", decId);
				myDeclareService.update(myDeclare, whereSql);
			}
		}
		// TODO Auto-generated method stub
		checkInfo.setDepartment("");
		checkInfo.setActId(actId);
		checkInfo.setAuditor(userId);
		checkInfo.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		checkInfo.setAuditOpinion(checkOpinion);
		if(StringUtils.isBlank(node)){
			node = "sign";
		}
		taskService.complete(taskId, variables);
		checkInfo.setNode(node);
		checkInfoService.save(checkInfo);
		return ObjectToJSON.returnResult("办理成功！", null);
	}
	
	@RequestMapping(params="method=completeForInit")
	@ResponseBody
	public Object completeForInit(HttpServletRequest request, String taskId, String initDecId, String node, String checkResult, String checkOpinion, String assignee){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		if(assignee == ""){
			claimTask(taskId, userId);
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		String actId = "";
		String whereSql = "";
		CheckInfo checkInfo = new CheckInfo();
		if(StringUtils.isNotBlank(initDecId)){
			whereSql = "AND INIT_DEC_ID = '"+initDecId+"' ";
		}
		List<MyInitDeclareVO> myInitDeclares = myInitDeclareService.getMyInitDeclare(whereSql);
		MyInitDeclareVO myInitDeclareVO = null;
		if(myInitDeclares.size() > 0){
			actId = myInitDeclares.get(0).getActId();
			myInitDeclareVO = myInitDeclares.get(0);
		}
		if("0".equals(checkResult)){
			variables.put(node, 0);
			checkInfo.setAuditResult("通过");
		}else if("1".equals(checkResult)){
			variables.put(node, 1);
			checkInfo.setAuditResult("不通过");
		}
		
		if(myInitDeclareVO != null){
			if("firstCheck".equals(node)){
				if("1".equals(checkResult)){
					myInitDeclareVO.setActSts("3");
					myInitDeclareVO.setLastModifier(userId);
					myInitDeclareVO.setLastModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					myInitDeclareService.update(myInitDeclareVO);
				}
			}else if("rechecker".equals(node)){
				if("0".equals(checkResult)){
					myInitDeclareVO.setActSts("2");
					myInitDeclareVO.setLastModifier(userId);
					myInitDeclareVO.setLastModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					//完成审批后初始化数据
					metaVerService.initMetadata(myInitDeclareVO.getDbCode());
					myInitDeclareService.update(myInitDeclareVO);
				}
			}
		}
		taskService.complete(taskId, variables);
		// TODO Auto-generated method stub
		checkInfo.setDepartment("");
		checkInfo.setActId(actId);
		checkInfo.setAuditor(userId);
		checkInfo.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		checkInfo.setAuditOpinion(checkOpinion);
		checkInfo.setNode(node);
		checkInfoService.save(checkInfo);
		return ObjectToJSON.returnResult("办理成功！", null);
	}
	
	@RequestMapping(params="method=completeForSign")
	@ResponseBody
	public Object completeForSign(HttpServletRequest request, String taskId, String decId, String node, String checkResult, 
			String checkOpinion, String feedbacUsers,String assignee){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		if(assignee == ""){
			claimTask(taskId, userId);
		}
		Map<String, Object> variables = new HashMap<String, Object>();
		CheckInfo checkInfo = new CheckInfo();
		String actId = "";
		String whereSql = "";
		if(StringUtils.isNotBlank(decId)){
			whereSql = "AND DEC_ID = '"+decId+"' ";
		}
		List<MyDeclareVO> myDeclareVOs = myDeclareService.getMyDeclare(whereSql);
		MyDeclareVO myDeclare = null;
		if(myDeclareVOs.size() == 1){
			actId = myDeclareVOs.get(0).getActId();
			myDeclare = myDeclareVOs.get(0);
		}
		if("1".equals(checkResult)){
			String[] feedbacUser = feedbacUsers.split(","); 
			List<String> feedbacUser_ = Arrays.asList(feedbacUser);
			variables.put(node, 1);
			variables.put("feedbacUsers", feedbacUser_);
			checkInfo.setAuditResult("有影响");
		}else if("0".equals(checkResult)){
			variables.put(node, 0);
			checkInfo.setAuditResult("无影响");
			myDeclare.setActSts("2");
			myDeclare.setLastModifier(userId);
			myDeclare.setLastModifyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			whereSql = "AND DEC_ID = '"+myDeclare.getDecId()+"' ";
			PageResults page = ControllerHelper.buildPage(request);
			page.setParameter("decId", decId);
			//完成变更申报后进行版本更新
			metaVerService.insVerMetaByDecId(page);
			myDeclareService.update(myDeclare, whereSql);
			updateAlterationAltsts("0", decId);
		}
		taskService.complete(taskId, variables);
		// TODO Auto-generated method stub
		checkInfo.setDepartment("");
		checkInfo.setActId(actId);
		checkInfo.setAuditor(userId);
		checkInfo.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		checkInfo.setAuditOpinion(checkOpinion);
		checkInfo.setNode(node);
		checkInfoService.save(checkInfo);
		return ObjectToJSON.returnResult("办理成功！", null);
	}
	
	private void claimTask(String taskId, String userId){
		taskService.claim(taskId, userId);
	}
	
	private void updateAlterationAltsts(String altSts,String decId){
		if(StringUtils.isNotBlank(decId)){
			String altIds = "";
			String whereSql = "AND DECLARE_ID = '"+decId+"' ";
			List<Map<String,Object>> decDetailedList = myDeclareService.getDecDetailed(whereSql);
			if(decDetailedList.size() > 0){
				for(Map<String, Object> map : decDetailedList){
					altIds += String.valueOf(map.get("ALTID"))+",";
				}
				altIds = altIds.substring(0, altIds.lastIndexOf(","));
				whereSql = "AND A.ALT_ID IN ("+ObjectToString.loadSql(altIds)+") ";
				flowService.updateAlterationAltsts(altSts, whereSql);
			}
		}
	}
	
	@RequestMapping(params="method=getAssignee")
	@ResponseBody
	public Object getAssignee(HttpServletRequest request){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		List<Map<String, Object>> users = flowService.getAssignee(userId);
		return ObjectToJSON.returnResult(users, null);
	}
	
	/**
	 * 获取审核日志
	 * @param actId
	 * @return
	 */
	@RequestMapping(params="method=getHistory")
	@ResponseBody
	public Object getHistory(String actId){
		List<CheckInfo> checkInfos = new ArrayList<CheckInfo>();
		if(StringUtils.isNotBlank(actId)){
			String whereSql = "AND C.ACT_ID = '"+actId+"' ORDER BY C.AUDIT_TIME DESC ";
			checkInfos = checkInfoService.getCheckInfo(whereSql);
		}
		return ObjectToJSON.retResult(checkInfos, null);
	}
	
	@RequestMapping(params="method=process-instance")
	public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response)
	          throws Exception {
		InputStream resourceAsStream = null;
	    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	    
	    ProcessEngineConfigurationImpl processEngineConfigurationImpl = this.processEngine.getProcessEngineConfiguration();
	    Context.setProcessEngineConfiguration(processEngineConfigurationImpl);
	    
	    BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
	    
	    resourceAsStream =  ProcessDiagramGenerator.generateDiagram(bpmnModel,"png", runtimeService.getActiveActivityIds(processInstanceId));
	    
	    byte[] b = new byte[1024];
	    int len = -1;
	    response.setCharacterEncoding("utf-8");
	    while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
	    	response.getOutputStream().write(b, 0, len);
	    }
	}
	
	@RequestMapping(params="method=finishProcess")
	@ResponseBody
	public Object finishProcess(String actId,String decId,String type){
		try {
			Task task = taskService.createTaskQuery().processInstanceId(actId).singleResult();
			ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(task.getProcessDefinitionId());
			List<ActivityImpl> activitiList = def.getActivities();
			ActivityImpl endActiviti = null;
			for (ActivityImpl act : activitiList) {
				String typeName = act.getProperty("type").toString();
				if(typeName.equals("endEvent")){
					endActiviti = act;
					break;
				}
			}	
			commitProcess(task.getId(), null, endActiviti.getId());
			if("变更申报审核流程".equals(type)){
				if(StringUtils.isNotBlank(decId)){
					String whereSql = "AND DEC_ID = '"+decId+"' ";
					List<MyDeclareVO> declares = myDeclareService.getMyDeclare(whereSql);
					if(declares.size() > 0){
						MyDeclareVO declare = declares.get(0);
						declare.setActSts("5");
						myDeclareService.update(declare, whereSql);
						whereSql = "AND DECLARE_ID = '"+decId+"' ";
						List list = myDeclareService.getDecDetailed(whereSql);
						String altIds = "";
						for(int i=0;i<list.size();i++){
							Map<String,Object> map = (Map<String,Object>)list.get(i);
							altIds += map.get("ALTID")+",";
						}
						if(altIds != ""){
							altIds = altIds.substring(0, altIds.lastIndexOf(","));
							whereSql = "AND A.ALT_ID IN ("+ObjectToString.loadSql(altIds)+") ";
							flowService.updateAlterationAltsts("2", whereSql);
						}
					}
				}
			}else if("版本初始化申请审核流程".equals(type)){
				if(StringUtils.isNotBlank(decId)){
					String whereSql = "AND INIT_DEC_ID = '"+decId+"' ";
					List<MyInitDeclareVO> initDeclares = myInitDeclareService.getMyInitDeclare(whereSql);
					if(initDeclares.size() > 0){
						MyInitDeclareVO initDeclare = initDeclares.get(0);
						initDeclare.setActSts("5");
						myInitDeclareService.update(initDeclare);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ObjectToJSON.retResult("撤销成功！", null);
	}
	
	private void commitProcess(String taskId, Map<String, Object> variables, String activityId) throws Exception {
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		// 跳转节点为空，默认提交操作
		if (StringUtils.isBlank(activityId)) {
			taskService.complete(taskId, variables);
		} else {// 流程转向操作
			turnTransition(taskId, activityId, variables);
		}
	}
	
	private void turnTransition(String taskId, String activityId, Map<String, Object> variables) throws Exception {
		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);
		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);
		// 执行转向任务
		taskService.complete(taskId, variables);
	}
	
	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();
		return oriPvmTransitionList;
	}
	
	public ActivityImpl findActivitiImpl(String taskId, String activityId) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);
		// 获取当前活动节点ID
		if (StringUtils.isBlank(activityId)) {
			activityId = findTaskById(taskId).getTaskDefinitionKey();
		}
		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.toUpperCase().equals("END")) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}
		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);
		return activityImpl;
	}
	
	private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());
		if (processDefinition == null) {
			throw new Exception("流程定义未找到!");
		}
		return processDefinition;
	}
	
	/**
	 * 根据任务ID获得任务实例
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	private TaskEntity findTaskById(String taskId) throws Exception {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new Exception("任务实例未找到!");
		}
		return task;
	}
	
	@RequestMapping(params="method=startFlow")
	@ResponseBody
	public Object startFlow(HttpServletRequest request, String altIds, String decName, String decDesc, String assignee, String decType){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,String> msg = null;
		if(StringUtils.isNotEmpty(userId)){
			String uuid = CommonUtil.getUUID();
			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			MyDeclareVO myDeclareVO = new MyDeclareVO();
			myDeclareVO.setDecId(uuid);
			myDeclareVO.setDecName(decName);
			myDeclareVO.setDecDesc(decDesc);
			myDeclareVO.setAltOperDate(now);
			myDeclareVO.setAltUser(userId);
			myDeclareVO.setDecType(decType);
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("firsthecker", assignee);
			msg = new HashMap<String, String>();
			ProcessInstance processInstance = flowService.startFlow(myDeclareVO, variables);
			if(StringUtils.isNotBlank(altIds)){
				String[] alt_ids = altIds.split(",");
				for(String altId : alt_ids){
					myDeclareService.saveDecDetailed(myDeclareVO.getDecId(), altId);
				}
				String whereSql = "AND A.ALT_ID IN ("+ObjectToString.loadSql(altIds)+") ";
				flowService.updateAlterationAltsts("1", whereSql);
			}
			if(processInstance!=null&&processInstance.getProcessInstanceId()!=null){
				msg.put("success", "true");
				msg.put("msg", "提交申报成功！");
			}else{
				msg.put("success", "false");
				msg.put("msg", "提交申报失败，出现故障，请联系平台管理员！");
			}
		}
		return ObjectToJSON.retResult(msg, null);
	}
	
	@RequestMapping(params="method=getMyAltMetaForHandleByPage")
	@ResponseBody
	public Object getMyAltMetaForHandleByPage(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		String decId = (String) request.getParameter("decId");
		if(StringUtils.isNotBlank(decId)){
			String altIds = "";
			String whereSql = "AND DECLARE_ID = '"+decId+"' ";
			List<Map<String,Object>> decDetailedList = myDeclareService.getDecDetailed(whereSql);
			if(decDetailedList.size() > 0){
				for(Map<String, Object> map : decDetailedList){
					altIds += String.valueOf(map.get("ALTID"))+",";
				}
				altIds = altIds.substring(0, altIds.lastIndexOf(","));
			}
			page.setParameter("altIds", altIds);
		}
		List<Map<String,Object>> list = flowService.getAltMetaItem(page);
		for (Map<String, Object> obj : list) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.returnResult(list, page);
	}
	
	@RequestMapping(params="method=loadAltertionImpact")
	@ResponseBody
	public Object loadAltertionImpact(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String,Object>> dataList = flowService.getAltertionImpact(page);
		for (Map<String, Object> obj : dataList) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.returnResult(dataList, page);
	}
	
	@RequestMapping(params="method=loadMetaIntFileDec")
	@ResponseBody
	public Object loadMetaIntFileDec(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String,Object>> dataList = flowService.getMetaIntFileDec(page);
		for (Map<String, Object> obj : dataList) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.returnResult(dataList, page);
	}
	
	@RequestMapping(params="method=loadMetaIntColCodeDec")
	@ResponseBody
	public Object loadMetaIntColCodeDec(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String,Object>> dataList = flowService.getMetaIntColCodeDec(page);
		for (Map<String, Object> obj : dataList) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.returnResult(dataList, page);
	}
	
	@RequestMapping(params="method=getDeclareInfo")
	@ResponseBody
	public Object getDeclareInfo(String decId){
		List<MyDeclareVO> myDeclares = flowService.getDeclareInfo(decId);
		return ObjectToJSON.retResult(myDeclares, null);
	}
	
	@RequestMapping(params="method=loadAltertionImpactByDecId")
	@ResponseBody
	public Object loadAltertionImpactByDecId(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		String decId = (String) request.getParameter("decId");
		if(StringUtils.isNotBlank(decId)){
			String altIds = "";
			String whereSql = "AND DECLARE_ID = '"+decId+"' ";
			List<Map<String,Object>> decDetailedList = myDeclareService.getDecDetailed(whereSql);
			if(decDetailedList.size() > 0){
				for(Map<String, Object> map : decDetailedList){
					altIds += String.valueOf(map.get("ALTID"))+",";
				}
				altIds = altIds.substring(0, altIds.lastIndexOf(","));
			}
			page.setParameter("altIds", altIds);
		}
		List<Map<String,Object>> dataList = flowService.getAltertionImpact(page);
		for (Map<String, Object> obj : dataList) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.returnResult(dataList, page);
	}
	
	@RequestMapping(params="method=getCheckHistory")
	@ResponseBody
	public Object getCheckHistory(String decId){
		String actId = "";
		String whereSql = "";
		if(StringUtils.isNotBlank(decId)){
			whereSql = "AND DEC_ID = '"+decId+"' ";
		}
		List<MyDeclareVO> myDeclareVOs = myDeclareService.getMyDeclare(whereSql);
		if(myDeclareVOs.size() > 0){
			actId = myDeclareVOs.get(0).getActId();
		}
		whereSql = "AND C.ACT_ID = '"+actId+"' ORDER BY C.AUDIT_TIME DESC ";
		List<CheckInfo> checkInfos = checkInfoService.getCheckInfo(whereSql);
		return ObjectToJSON.retResult(checkInfos, null);
	}
	
	@RequestMapping(params="method=getUsers")
	@ResponseBody
	public Object getUsers(String userId){
		String whereSql = "AND A.GROUP_ID_ = 'feedbac' ";
		if(StringUtils.isNotBlank(userId)){
			whereSql += "AND T.USERID = '"+userId+"' ";
		}
		List<Map<String,Object>> users = flowService.getUsers(whereSql);
		return ObjectToJSON.retResult(users, null);
	}
	
	@RequestMapping(params="method=getInitDec")
	@ResponseBody
	public Object getInitDec(String initDecId){
		String whereSql = "AND INIT_DEC_ID = '"+initDecId+"' ";
		List<MyInitDeclareVO> myInitDeclares = myInitDeclareService.getMyInitDeclare(whereSql);
		return ObjectToJSON.retResult(myInitDeclares.get(0), null);
	}
	
	@RequestMapping(params="method=getInitDeclareInfo")
	@ResponseBody
	public Object getInitDeclareInfo(String initDecId){
		List<MyInitDeclareVO> myInitDeclares = flowService.getInitDeclareInfo(initDecId);
		return ObjectToJSON.retResult(myInitDeclares, null);
	}
	
	@RequestMapping(params="method=getInitCheckHistory")
	@ResponseBody
	public Object getInitCheckHistory(String initDecId){
		String actId = "";
		String whereSql = "";
		if(StringUtils.isNotBlank(initDecId)){
			whereSql = "AND INIT_DEC_ID = '"+initDecId+"' ";
		}
		List<MyInitDeclareVO> myInitDeclares = myInitDeclareService.getMyInitDeclare(whereSql);
		if(myInitDeclares.size() > 0){
			actId = myInitDeclares.get(0).getActId();
		}
		whereSql = "AND C.ACT_ID = '"+actId+"' ORDER BY C.AUDIT_TIME DESC ";
		List<CheckInfo> checkInfos = checkInfoService.getCheckInfo(whereSql);
		return ObjectToJSON.retResult(checkInfos, null);
	}
	
}
