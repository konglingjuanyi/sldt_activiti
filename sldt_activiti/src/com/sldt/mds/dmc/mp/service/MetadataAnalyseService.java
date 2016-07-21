package com.sldt.mds.dmc.mp.service;

import java.util.List;
import java.util.Map;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.vo.AnalyseFlow;


public interface MetadataAnalyseService {
	//删除受影响方数据
	public void delImpactSide(PageResults page);
	//获取当前影响分析的instanceID
	public List<Map<String, Object>> getInstanceId(PageResults page);
	/**
	 * 获取当前系统的namespace
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getSNADBNamespace(PageResults page);
	/**
	 * 获取当前元数据ID的影响方
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getImpactSide(PageResults page);
	/**
	 * 获取当前元数据ID所影响的系统
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getImpactSys(PageResults page); 
	/**
	 * 获取当前元数据ID所影响的系统
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getImpactTable(PageResults page); 
	/**
	 * 获取当前元数据ID的影响分析线条
	 * @param page
	 * @return
	 */
	public List<AnalyseFlow> getAnalyseFlow(PageResults page);
	//插入受影响方数据
	public void insImpactSide(PageResults page);

}
