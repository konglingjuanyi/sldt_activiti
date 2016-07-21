package com.sldt.mds.dmc.mp.service;

import java.util.List;
import java.util.Map;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.vo.MetadataColumnVO;
import com.sldt.mds.dmc.mp.vo.MetadataDatabaseVO;
import com.sldt.mds.dmc.mp.vo.MetadataModuleVO;
import com.sldt.mds.dmc.mp.vo.MetadataSchemaVO;
import com.sldt.mds.dmc.mp.vo.MetadataTableVO;

public interface MetadataVerService {

	/**
	 * 根据参数获取当前最新版本号
	 * @param page
	 * @return
	 */
	public Map<String, Object> getCurrVerMap(PageResults page);
	/**
	 * 根据参数对象获取当前我的版本元数据
	 * @param params
	 * @return
	 */
	public List<MetadataDatabaseVO> getMyVersionMeta(PageResults page);
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
	public List<MetadataDatabaseVO> getVerDataBaseMeta(PageResults page);
	
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
	 * 获取快照schema 信息
	 * @param page
	 * @return
	 */
	public List<MetadataSchemaVO> getProVerSchemaMeta(PageResults page);
	/**
	 * 根据参数获取模式级元数据
	 * @param params
	 * @return
	 */
	public List<MetadataSchemaVO> getVerSchemaMeta(PageResults page);
	public List<MetadataModuleVO> getProVerModuleMeta(PageResults page);

	/**
	 * 根据参数获取模块级元数据
	 * @param params
	 * @return
	 */
	public List<MetadataModuleVO> getVerModuleMeta(PageResults page);
	/**
	 * 快照库表列表
	 * @param page
	 * @return
	 */
	public List<MetadataTableVO> getProVerTableMeta(PageResults page);
	/**
	 * 根据对象获取表级元数据
	 * @param params
	 * @return
	 */
	public List<MetadataTableVO> getVerTableMeta(PageResults page);
	/**
	 * 快照库字段信息
	 * @param page
	 * @return
	 */
	public List<MetadataColumnVO> getProVerColumnMeta(PageResults page);
	/**
	 * 根据参数获取字段级元数据
	 * @param params
	 * @return
	 */
	public List<MetadataColumnVO> getVerColumnMeta(PageResults page);
	/**
	 * 版本比对字段列表
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getCompAllVersionColumn(PageResults page);
	/**
	 * 查找需要比对的版本的表信息
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getComptableAutoComplete(PageResults page);
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
	
	/**
	 * 统计字段
	 * @param page
	 * @return
	 */
	public int getProVerColumnMetaCount(PageResults page);
	/**
	 * 统计快照表
	 * @param page
	 * @return
	 */
	public int getProVerTableMetaCount(PageResults page);
	/**\
	 * 统计schema
	 * @param page
	 * @return
	 */
	public int getProVerSchemaMetaCount(PageResults page);
	/**
	 * 统计快照模块
	 * @param page
	 * @return
	 */
	public int getProVerModuleMetaCount(PageResults page);
	/**
	 * 获取文件接口/程序引用关系数据
	 * @return
	 */
	public List<Map<String,Object>> getFileIntData(PageResults page);
	/**
	 * 取生产数据库信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getProVersionDbInfo(
			Map<String, String> params);
	/**
	 * 生产模式信息快照
	 * @param dbId
	 * @param optId 
	 * @return
	 */
	public List<MetadataSchemaVO> getVerSchemaMetaSna(String dbId, String optId);
	/**
	 * 模式比较
	 * @param allVerSchemaList 全部模式列表
	 * @param list1 比较模式1
	 * @param list2 比较模式2
	 * @return
	 */
	public List compSchema(List<MetadataSchemaVO> allVerSchemaList,
			List<MetadataSchemaVO> list1, List<MetadataSchemaVO> list2);
	/**
	 * 获取需要对比的版本的schema
	 * @param page
	 * @return
	 */
	public List<MetadataSchemaVO> getCompAllVersionSchemaList(PageResults page);
	/**
	 * 版本比对全部模块列表信息
	 * @param page
	 * @return
	 */
	public List<MetadataModuleVO> getCompAllVersionModuleList(PageResults page);
	/**
	 * 取快照模块
	 * @param dbId
	 * @param optId 
	 * @return
	 */
	public List<MetadataModuleVO> getVerModuleMetaSna(String dbId, String optId);
	/**
	 * 模块比较
	 * @param allList
	 * @param list
	 * @param list2
	 * @return
	 */
	public List compModule(List<MetadataModuleVO> allList,
			List<MetadataModuleVO> list, List<MetadataModuleVO> list2);
	/**
	 * 比对的全部表
	 * @param page
	 * @return
	 */
	public List getCompAllVersionTableList(PageResults page);
	/**
	 * 比对的全部字段信息分页
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getCompAllVersionColumnList(PageResults page);
	/**
	 * 当流程结束后将审批过的变更元数据插入到版本库中
	 * @param page
	 */
	public void insVerMetaByDecId(PageResults page);
	/**
	 * 初始化系统数据
	 * @param dbCode
	 */
	public void initMetadata(String dbCode);
}
