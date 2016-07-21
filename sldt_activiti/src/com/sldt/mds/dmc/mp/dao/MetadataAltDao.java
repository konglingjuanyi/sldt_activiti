package com.sldt.mds.dmc.mp.dao;

import java.util.List;
import java.util.Map;

import com.sldt.framework.common.PageResults;

public interface MetadataAltDao {
	/**
	 * 根据参数获取我的变更
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getNewAltMeta(PageResults page);
	
	public List<Map<String, Object>> getDecAltSumByDbId(PageResults page);
  
	public List<Map<String, Object>> getAltSumByDbId(PageResults page);
	public void insUploadInfo(PageResults page);
	//插入变更记录库中
	public void insAltMetaItem(PageResults page);
	//更新上传记录信息
	public void updUploadInfo(PageResults page);
	//全量比对出变更元数据对象
	public Map<String, Object> analyseAltMetaFull(String tmpId, String dbId);
	//全量比对出上传变更模块元数据对象
	public List<Map<String, Object>> analyseAltModuleMetaFull(String tmpId,String dbId);
	//全量比对出上传变更表级元数据对象
	public List<Map<String, Object>> analyseAltTableMetaFull(String tmpId,String dbId);
	//全量比对出上传变更字段元数据对象
	public List<Map<String, Object>> analyseAltColumnMetaFull(String tmpId,String dbId);
	//删除
	public void delMetaInfoTmp(String tmpId);
	
	/**
	 * 删除上传记录信息
	 * @param params
	 * @return
	 */
	public void delUploadInfo(PageResults page);
	
	/**
	 * 获取excel上传信息
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getUploadList(PageResults page);
	//根据参数获取变更明细数据
	public List<Map<String, Object>> getAltMetaItem(PageResults page);
	//删除变更明细数据
	public void delAltMetaDetail(PageResults page); 
	//更新变更明细状态
	public void updAltMetaSts(PageResults page); 
	/**
	 * 查看变更分析明细数据
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> showAltMetaDetail(PageResults page);
}
