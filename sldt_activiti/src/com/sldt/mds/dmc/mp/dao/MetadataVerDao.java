package com.sldt.mds.dmc.mp.dao;

import java.util.List;
import java.util.Map;

import com.sldt.framework.common.PageResults;

public interface MetadataVerDao {
	
	/**
	 * 根据参数获取当前最新版本号
	 * @param params
	 * @return
	 */
	public Map<String,Object> getCurrVerMap(PageResults page);
	/**
	 * 根据参数对象获取当前我的版本元数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getMyVersionMeta(PageResults page);
	/**
	 * 根据参数对象获取最新的版本元数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getNewVersionMeta(Map<String,String> params);
	/**
	 * 根据参数获取历史版本号
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getHisVerList(PageResults page);
	/**
	 * 获取初始化版本
	 * @param dbId
	 */
	public Map<String,Object> getInitVer(String dbId);
	/**
	 * 获取当前开发版本
	 * @param page
	 * @return
	 */
	public Map<String, Object> getCurDeveVer(PageResults page);
	/**
	 * 根据参数获取模板级元数据汇总
	 * @param page
	 * @return
	 */
	public int getVerSchemaMetaCount(PageResults page);
	/**
	 * 根据参数获取模块级元数据汇总
	 * @param params
	 * @return
	 */
	public int getVerModuleMetaCount(PageResults page);
	/**
	 * 根据对象获取表级元数据汇总
	 * @param params
	 * @return
	 */
	public int getVerTableMetaCount(PageResults page);
	/**
	 * 根据参数获取字段级元数据汇总
	 * @param params
	 * @return
	 */
	public int getVerColumnMetaCount(PageResults page);
	/**
	 * 根据参数获取开发及远期版本号
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getForAndDeveVerList(PageResults page);
	
	/**
	 * 根据参数获取数据库级元数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getVerDataBaseMeta(PageResults page);
	
	/**
	 * 根据参数获取模式级元数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getVerSchemaMeta(PageResults page);
	
	/**
	 * 根据参数获取模块级元数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getVerModuleMeta(PageResults page);
	
	/**
	 * 根据对象获取表级元数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getVerTableMeta(PageResults page);
	
	/**
	 * 根据参数获取字段级元数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getVerColumnMeta(PageResults page);
	
	/**
	 * 根据参数获取字段落地元数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getVerColCodeMeta(PageResults page);
	
	/**
	 * 根据参数获取接口文件元数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getVerIntFileMeta(PageResults page);
	
	/**
	 * excel导入到临时表中
	 * @return
	 */
	public void insMetaTmpByExcData(PageResults page);
	
	/**
	 * 查询投产日历
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getMetaVerDate(Map<String, String> params);
	/**
	 * 根据参数获取目标版本批次数据
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getCurrAltBatch(PageResults page);
	/**
	 * 获取快照schema信息
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getProVerSchemaMeta(PageResults page);
	/**
	 * 快照库表信息
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getProVerTableMeta(PageResults page);
	/**
	 * 快照字段信息
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getProVerColumnMeta(PageResults page);
	/**
	 * 比对的字段信息
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getCompAllVersionColumnList(PageResults page);
	/**
	 * 比对的全部表
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getCompAllVersionTableList(PageResults page);
	/**
	 * 获取字段落地代码数据
	 * @return
	 */
	public List<Map<String,Object>> getColCodeData(PageResults page);
	/**
	 * 获取生产库版本信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getProVersionInfo(
			Map<String, String> params);
	
	int getProVerColumnMetaCount(PageResults page);
	/**
	 * 统计快照表
	 * @param page
	 * @return
	 */
	int getProVerTableMetaCount(PageResults page);
	/**
	 * 统计快照schema
	 * @param page
	 * @return
	 */
	int getProVerSchemaMetaCount(PageResults page);
	/**
	 * 统计快照模块
	 * @param page
	 * @return
	 */
	int getProVerModuleMetaCount(PageResults page);
	/**
	 * 获取文件接口/程序引用关系数据
	 * @return
	 */
	public List<Map<String,Object>> getFileIntData(PageResults page);
	/**
	 * 生产数据库信息
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getProVersionDbInfo(Map<String, String> params);
	/**
	 * 生产模式信息快照
	 * @param dbId
	 * @param optId 
	 * @return
	 */
	List<Map<String, Object>> getVerSchemaMetaSna(String dbId, String optId);
	/**
	 * 需要对比的版本的schema
	 * 
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getCompAllVersionSchemaList(PageResults page);
	/**
	 * 版本比对的模块列表信息
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getCompAllVersionModuleList(PageResults page);
	/**
	 * 取快照表模块信息
	 * @param dbId
	 * @param optId 
	 * @return
	 */
	List<Map<String, Object>> getVerModuleMetaSna(String dbId, String optId);
	/**
	 * 当流程结束后将审批过的变更元数据信息进行拉链关链操作
	 * @param decId
	 * @return
	 */
	public List<Map<String,Object>> getTarOptDate(String decId);
	/**
	 * 当流程结束后将审批过的变更元数据信息进行拉链关链操作
	 * @param page
	 */
	public void updVerMetaByDecId(PageResults page);
	/**
	 * 当流程结束后将审批过的变更元数据插入到版本库中
	 * @param page
	 */
	public void insVerMetaByDecId(PageResults page);
	
	/**
	 * 处理变更版本库冲突问题
	 * @param page
	 */
	public void updALtStsCt(PageResults page);
	
	/**
	 * 删除变更版本库冲突问题
	 * @param page
	 */
	public void delMetaALtCt(PageResults page);
	
	/**
	 * 回滚变更版本库冲突问题
	 * @param page
	 */
	public void rollbakMetaALtCt(PageResults page);
	
	/**
	 * 初始化系统数据
	 * @param dbCode
	 */
	public void initMetadata(String dbCode);
}
