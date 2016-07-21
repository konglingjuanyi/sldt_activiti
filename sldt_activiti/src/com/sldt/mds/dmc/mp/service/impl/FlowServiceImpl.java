package com.sldt.mds.dmc.mp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.dao.FlowDao;
import com.sldt.mds.dmc.mp.dao.MyDeclareDao;
import com.sldt.mds.dmc.mp.dao.MyInitDeclareDao;
import com.sldt.mds.dmc.mp.service.FlowService;
import com.sldt.mds.dmc.mp.util.ObjectToString;
import com.sldt.mds.dmc.mp.vo.MyDeclareVO;
import com.sldt.mds.dmc.mp.vo.MyInitDeclareVO;

@Service(value="flowService")
public class FlowServiceImpl implements FlowService{
	
	private static Log log = LogFactory.getLog(FlowServiceImpl.class);
	
	@Resource(name="flowDao")
	private FlowDao flowDao;
	
	@Resource(name="myDeclareDao")
	private MyDeclareDao myDeclareDao;
	
	@Resource(name="myInitDeclareDao")
	private MyInitDeclareDao myInitDeclareDao;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Override
	public ProcessInstance startInitFlow(MyInitDeclareVO myInitDeclareVO, Map<String, Object> variables) {
		String dbCode = myInitDeclareVO.getDbCode();
		String whereSql = "AND M.DB_CODE = '"+dbCode+"' ";
		List<Map<String,Object>> list = flowDao.getMetaSysInventory(whereSql);
		if(list.size() == 1){
			myInitDeclareVO.setDbChName(ObjectToString.retResult(list.get(0).get("DBCHNAME")));
			myInitDeclareVO.setDbUser(ObjectToString.retResult(list.get(0).get("DB_USER")));
			myInitDeclareVO.setDbDesc(ObjectToString.retResult(list.get(0).get("DB_DESC")));
		}
		myInitDeclareDao.saveInitDeclare(myInitDeclareVO);
		String businessKey = myInitDeclareVO.getInitDecId();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("dmc_init", businessKey, variables);
		String processInstanceId = processInstance.getId();
		myInitDeclareVO.setActId(processInstanceId);
		myInitDeclareDao.updateInitDeclare(myInitDeclareVO);
		return processInstance;
	}

	@Override
	public List<MyDeclareVO> taskList(String userId) {
		List<MyDeclareVO> result = new ArrayList<MyDeclareVO>();
		Integer count = 0;
		List<Task> tasks = new ArrayList<Task>();
		List<MyDeclareVO> myDeclares = myDeclareDao.getMyDeclare("ORDER BY ALT_OPER_DATE ");
		tasks.addAll(taskService.createTaskQuery().processDefinitionKey("dmc").taskAssignee(userId).active().orderByTaskId().desc().list());
		tasks.addAll(taskService.createTaskQuery().processDefinitionKey("dmc").taskCandidateUser(userId).active().orderByTaskId().desc().list());
		for(MyDeclareVO md : myDeclares){
			for(Task task : tasks){
				if(md.getActId().equals(task.getProcessInstanceId())){
					count++;
					md.setRn(String.valueOf(count));
					md.setTaskId(task.getId());
					md.setName(task.getName());
					md.setAssignee(task.getAssignee());
					result.add(md);
				}
			}
		}
		return result;
	}

	@Override
	public List<MyInitDeclareVO> initTaskList(String userId) {
		List<MyInitDeclareVO> result = new ArrayList<MyInitDeclareVO>();
		Integer count = 0;
		List<Task> tasks = new ArrayList<Task>();
		List<MyInitDeclareVO> myInitDeclares = myInitDeclareDao.getMyInitDeclare("ORDER BY ALT_OPER_DATE ");
		tasks.addAll(taskService.createTaskQuery().processDefinitionKey("dmc_init").taskAssignee(userId).active().orderByTaskId().desc().list());
		tasks.addAll(taskService.createTaskQuery().processDefinitionKey("dmc_init").taskCandidateUser(userId).active().orderByTaskId().desc().list());
		for(MyInitDeclareVO mid : myInitDeclares){
			for(Task task : tasks){
				if(mid.getActId().equals(task.getProcessInstanceId())){
					count++;
					mid.setRn(String.valueOf(count));
					mid.setTaskId(ObjectToString.retResult(task.getId()));
					mid.setName(ObjectToString.retResult(task.getName()));
					mid.setAssignee(ObjectToString.retResult(task.getAssignee()));
					result.add(mid);
				}
			}
		}
		return result;
	}

	@Override
	public void updateAlterationAltsts(String altSts, String whereSql) {
		flowDao.updateAlterationAltsts(altSts, whereSql);
	}

	@Override
	public List getAssignee(String userId) {
		String whereSql = "";
		String tUnitid = "3003";//后期补充获取机构
		if(StringUtils.isNotBlank(tUnitid)){
			whereSql += "AND ORG_ID = '"+tUnitid+"' ";
		}
		List<Map<String, Object>> unitUsers = flowDao.getUnitUser(whereSql);//后期SM提供REST接口，由系统管理获取
		String userIds = "";
		for(Map<String, Object> unitUser : unitUsers){
			userIds += unitUser.get("USER_ID")+",";
		}
		if(StringUtils.isNotBlank(userIds)){
			whereSql += "AND T.USER_ID IN("+ObjectToString.loadSql(userIds)+") ";
		}
		whereSql += "AND A.GROUP_ID_ = 'firsthecker' ";
		return flowDao.getUsers(whereSql);
	}

	@Override
	public ProcessInstance startFlow(MyDeclareVO myDeclareVO,
			Map<String, Object> variables) {
		 myDeclareDao.saveDeclare(myDeclareVO);
		 String businessKey = myDeclareVO.getDecId();
			
		 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("dmc", businessKey, variables);
			
		 String processInstanceId = processInstance.getId();
			
		 myDeclareVO.setActId(processInstanceId);
	   	 myDeclareVO.setActSts("1");
			
		 String whereSql = "AND DEC_ID = '"+myDeclareVO.getDecId()+"' ";
		 myDeclareDao.update(myDeclareVO, whereSql);
			
		 return processInstance;
	}

	@Override
	public List<Map<String, Object>> getAltMetaItem(PageResults page) {
		return flowDao.getAltMetaItem(page);
	}

	@Override
	public List getAltertionImpact(PageResults page) {
		return flowDao.getAltertionImpact(page);
	}

	@Override
	public List getMetaIntFileDec(PageResults page) {
		return flowDao.getMetaIntFileDec(page);
	}

	@Override
	public List getMetaIntColCodeDec(PageResults page) {
		return flowDao.getMetaIntColCodeDec(page);
	}

	@Override
	public List<MyDeclareVO> getDeclareInfo(String decId) {
		String whereSql = "";
		List<MyDeclareVO> list = new ArrayList<MyDeclareVO>();
		if(StringUtils.isNotBlank(decId)){
			whereSql = "AND DEC_ID = '"+decId+"' ";
		}
		List<MyDeclareVO> myDeclares = myDeclareDao.getMyDeclare(whereSql);
		if(myDeclares.size() > 0){
			MyDeclareVO myDeclareVO = myDeclares.get(0);
			String actId = myDeclareVO.getActId();
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(actId).list();
			if(tasks.size() > 0){
				Task task = tasks.get(0);
				myDeclareVO.setTaskId(ObjectToString.retResult(task.getId()));
				myDeclareVO.setName(ObjectToString.retResult(task.getName()));
				myDeclareVO.setAssignee(ObjectToString.retResult(task.getAssignee()));
				list.add(myDeclareVO);
			}
		}
		return list;
	}

	@Override
	public List getUsers(String whereSql) {
		return flowDao.getUsers(whereSql);
	}

	@Override
	public List<MyInitDeclareVO> getInitDeclareInfo(String initDecId) {
		String whereSql = "";
		List<MyInitDeclareVO> list = new ArrayList<MyInitDeclareVO>();
		if(StringUtils.isNotBlank(initDecId)){
			whereSql = "AND INIT_DEC_ID = '"+initDecId+"' ";
		}
		List<MyInitDeclareVO> myInitDeclares = myInitDeclareDao.getMyInitDeclare(whereSql);
		if(myInitDeclares.size() > 0){
			MyInitDeclareVO myInitDeclareVO = myInitDeclares.get(0);
			String actId = myInitDeclareVO.getActId();
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(actId).list();
			if(tasks.size() > 0){
				Task task = tasks.get(0);
				myInitDeclareVO.setTaskId(ObjectToString.retResult(task.getId()));
				myInitDeclareVO.setName(ObjectToString.retResult(task.getName()));
				myInitDeclareVO.setAssignee(ObjectToString.retResult(task.getAssignee()));
				list.add(myInitDeclareVO);
			}
		}
		return list;
	}

}
