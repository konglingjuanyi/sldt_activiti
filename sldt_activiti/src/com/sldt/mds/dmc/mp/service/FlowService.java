package com.sldt.mds.dmc.mp.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.vo.MyDeclareVO;
import com.sldt.mds.dmc.mp.vo.MyInitDeclareVO;

public interface FlowService {
	
	/**
	 * 启动初始化流程
	 * @param myDeclareVO
	 * @param variables
	 * @return
	 */
	public ProcessInstance startInitFlow(MyInitDeclareVO myInitDeclareVO, Map<String, Object> variables);
	
	/**
	 * 变更申报任务列表
	 * @param userId
	 * @return
	 */
	public List<MyDeclareVO> taskList(String userId);
	
	/**
	 * 初始化任务列表
	 * @param userId
	 * @return
	 */
	public List<MyInitDeclareVO> initTaskList(String userId);
	
	/**
	 * 更新变更状态
	 * @param altSts
	 * @param whereSql
	 */
	public void updateAlterationAltsts(String altSts, String whereSql);
	/**
	 * 获取用户
	 * @param whereSql
	 * @return
	 */
	public List getUsers(String whereSql);
    /**
     * 获取此用户大部门审核人
     * @param userId
     * @return
     */
	public List getAssignee(String userId);
	/**
	 * 启动流程
	 * @param myDeclareVO
	 * @param variables
	 * @return
	 */
	public ProcessInstance startFlow(MyDeclareVO myDeclareVO,
			Map<String, Object> variables);
	/**
	 * 根据参数获取变更明细数据
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getAltMetaItem(PageResults page);
	
	public List getAltertionImpact(PageResults page);
	/**
	 * 获取表级接口（文件）使用关系（变更）
	 * @param page
	 * @return
	 */
	public List getMetaIntFileDec(PageResults page);
	/**
	 * 获取字段落地代码
	 * @param page
	 * @return
	 */
	public List getMetaIntColCodeDec(PageResults page);
	/**
	 * 获取申报流水信息
	 * @param decId
	 * @return
	 */
	public List<MyDeclareVO> getDeclareInfo(String decId);
	/**
	 * 获取初始化申报流水信息
	 * @param initDecId
	 * @return
	 */
	public List<MyInitDeclareVO> getInitDeclareInfo(String initDecId);
}
