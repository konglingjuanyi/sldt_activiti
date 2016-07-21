package com.sldt.mds.dmc.mp.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.fabric.xmlrpc.base.Array;
import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.service.impl.MetadataAltServiceImpl;
import com.sldt.mds.dmc.mp.service.impl.MetadataVerServiceImpl;
import com.sldt.mds.dmc.mp.util.MyApplicationContextUtil;

public class MetaAnalyeThread extends Thread {
	
	protected Logger log = Logger.getLogger(this.getClass());
	private List<Map<String, String>> altList = new ArrayList<Map<String,String>>();
	private Map<String, String> dbMap = new HashMap<String, String>();
	private String userId = "";
	private String fileName = "";
	private String currOptId;
	private String tarOptId;
	private String uploadType;
	MetadataVerServiceImpl verServices = (MetadataVerServiceImpl) MyApplicationContextUtil.getContext().getBean("metadataVerServiceImp");
	MetadataAltServiceImpl altServices = (MetadataAltServiceImpl) MyApplicationContextUtil.getContext().getBean("metadataAltServiceImp");
	
	/**
	 * 构造函数
	 * @param altList
	 * @param dbMap
	 * @param uploadType
	 * @param userId
	 * @param fileName
	 * @param curOptId
	 * @param tarOptId
	 */
	public MetaAnalyeThread(List<Map<String,String>> altList,Map<String,String> dbMap,String uploadType,String userId,String fileName,String curOptId,String tarOptId) {
		this.altList = altList;
		this.dbMap = dbMap;
		this.userId = userId;
		this.fileName = fileName;
		this.currOptId = curOptId;
		this.tarOptId = tarOptId;
		this.uploadType = uploadType;
	}
	
	@Transactional
	public void run(HttpServletRequest request){
		String tmpId = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
		try {
			log.info("开始执行元数据导入分析线程，导入类型："+uploadType+"！");
			String altBatch = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
			PageResults page = ControllerHelper.buildPage(request);
			page.setParameter("altMeta", altList);
			page.setParameter("id", tmpId);
			String operDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			
			Map<String, String> uploadInfo = new HashMap<String, String>();
			uploadInfo.put("UPLOAD_ID", tmpId);
			uploadInfo.put("UPLOAD_USER", userId);
			uploadInfo.put("UPLOAD_TIME",operDate);
			uploadInfo.put("UPLOAD_FIME_NAME", fileName);
			uploadInfo.put("UPLOAD_BATCH_ID", altBatch);
			uploadInfo.put("UPLOAD_ALT_DESC", "变更分析中");
			uploadInfo.put("UPLOAD_STS", "1");
			uploadInfo.put("UPLOAD_M", uploadType);
			page.setParameter("uploadInfo", uploadInfo);
			//插入上传文件数据
			altServices.insUploadInfo(page);
			//变更的元数据集合
			List<Map<String, String>> altMetaList = new ArrayList<Map<String,String>>();
			//变更登记数据集合
			List<Map<String, String>> altInfoList = new ArrayList<Map<String,String>>();
			//变更登记明细数据集合
			List<Map<String, String>> altDetilInfoList = new ArrayList<Map<String,String>>();
			log.info("开始分析元数据变更！");
			Map<String, List<Map<String, String>>> altMap = new HashMap<String, List<Map<String,String>>>();
			//增量
			if(uploadType.equals("0")){
				altMap = incrementAnalye(request,altMetaList, altInfoList, altDetilInfoList, operDate, altBatch,tmpId);
				altMetaList = altMap.get("altList");
				altInfoList = altMap.get("altInfoList");
				altDetilInfoList = altMap.get("altDetilInfoList");
			}else if(uploadType.equals("1")){//全量
				fullAnlye(page, altMetaList, altInfoList, altDetilInfoList, operDate, altBatch);
			}else if (uploadType.equals("2")){//删除
				delAnlye(request,altInfoList, operDate, altBatch,tmpId);
			}
			log.info("完成分析元数据变更！"+altInfoList.size());
			
			listNullToString(altMetaList);
			page.setParameter("altMetaList", altMetaList);
			page.setParameter("altInfoList", altInfoList);
			page.setParameter("altDetilInfoList", altDetilInfoList);
			altServices.insAltMetaItem(page);
			log.info("完成元数据变更历史入库操作！");
			try {
				//休眠1秒
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			Map<String,String> uploadInfo = new HashMap<String, String>();
			uploadInfo.put("UPLOAD_ALT_DESC", "导入出现异常："+e.toString().replaceAll("'", "''''"));
			uploadInfo.put("UPLOAD_STS", "2");
			uploadInfo.put("UPLOAD_STS", "2");
			PageResults page = ControllerHelper.buildPage(request);
			page.setParameter("uploadInfo", uploadInfo);
			page.setParameter("uploadId", tmpId);
			altServices.updUploadInfo(page);
			throw new RuntimeException();
		}
	}
	public Map<String, List<Map<String,String>>> incrementAnalye(HttpServletRequest request,List<Map<String,String>> altMetaList,List<Map<String,String>> altInfoList,List<Map<String,String>> altDetilInfoList,String operDate,String altBatch,String uploadId){
		String msg = "";
		int dbCount = 0 ;
		int schCount = 0;
		int tabCount = 0;
		int modCount = 0;
		int colCount = 0;
		int colCodeCount = 0;
		int intFileCount = 0;
		Map<String, List<Map<String,String>>> altMap = new HashMap<String, List<Map<String,String>>>();
		altMap = altServices.analyseAltMeta(request,userId, currOptId, tarOptId, altList,operDate,altBatch);
		altMetaList = altMap.get("altList");
		altInfoList = altMap.get("altInfoList");
		altDetilInfoList = altMap.get("altDetilInfoList");
		
		for (Map<String, String> map : altMetaList) {
			
			if(map.get("CLASSIFER_TYPE").equals("DATABASE")){
				dbCount++;
			}else if(map.get("CLASSIFER_TYPE").equals("SCHEMA")){
				schCount++;
			}else if(map.get("CLASSIFER_TYPE").equals("MODULE")){
				modCount++;
			}else if(map.get("CLASSIFER_TYPE").equals("TABLE")){
				tabCount++;
			}else if(map.get("CLASSIFER_TYPE").equals("COLUMN")){
				colCount++;
			}else if(map.get("CLASSIFER_TYPE").equals("COLCODE")){
				colCodeCount++;
			}else if(map.get("CLASSIFER_TYPE").equals("INTFILE")){
				intFileCount++;
			}
		}
		if(dbCount>0){
			msg+="系统变更 "+dbCount+" 个，";
		}
		if(schCount>0){
			msg+="模式变更 "+schCount+" 个，";
		}
		if(tabCount>0){
			msg+="表级变更 "+tabCount+" 个，";
		}
		if(modCount>0){
			msg+="模块变更 "+modCount+" 个，";
		}
		if(colCount>0){
			msg+="字段级变更 "+colCount+" 个，";
		}
		if(colCodeCount>0){
			msg+="字段落地变更 "+colCodeCount+" 个，";
		}
		if(intFileCount>0){
			msg+="文件接口变更 "+intFileCount+" 个，";
		}
		if(msg.equals("")){
			msg = "无变更影响。";
		}
		
		Map<String,String> uploadInfo = new HashMap<String, String>();
		uploadInfo.put("UPLOAD_ALT_DESC", msg);
		uploadInfo.put("UPLOAD_STS", "2");
		uploadInfo.put("UPLOAD_STS", "2");
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("uploadInfo", uploadInfo);
		page.setParameter("uploadId", uploadId);
		altServices.updUploadInfo(page);
		return altMap;
	}
	
	public void fullAnlye(PageResults page,List<Map<String,String>> altMetaList,List<Map<String,String>> altInfoList,List<Map<String,String>> altDetilInfoList,String operDate,String altBatch){
		Map<String,String> uploadInfo = new HashMap<String, String>();
		//将文件上传数据插入到数据库的临时表中
		verServices.insMetaTmpByExcData(page);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Iterator iterator = dbMap.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Map<String,Object> obj = altServices.analyseAltMetaFull(page.getParamStr("id"), key);
			list.add(obj);
			Map<String, Object> objMap = new HashMap<String, Object>();
			String msg = "【"+dbMap.get(key)+"】";
			
			if(obj.get("dbMap")!=null){
				objMap = (Map<String, Object>)obj.get("dbMap");
				msg += objMap.get("msg");
			}
			if(obj.get("schMap")!=null){
				objMap = (Map<String, Object>)obj.get("schMap");
				msg += objMap.get("msg");
			}
			if(obj.get("modMap")!=null){
				objMap = (Map<String, Object>)obj.get("modMap");
				msg += objMap.get("msg");
			}
			if(obj.get("tabMap")!=null){
				objMap = (Map<String, Object>)obj.get("tabMap");
				msg += objMap.get("msg");
			}
			if(obj.get("colMap")!=null){
				objMap = (Map<String, Object>)obj.get("colMap");
				msg += objMap.get("msg");
			}
			uploadInfo = new HashMap<String, String>();
			uploadInfo.put("UPLOAD_ALT_DESC", msg);
			uploadInfo.put("UPLOAD_STS", "2");
			uploadInfo.put("UPLOAD_STS", "2");
			page.setParameter("uploadInfo", uploadInfo);
			page.setParameter("uploadId", page.getParamStr("id"));
			altServices.updUploadInfo(page);
		}
		
		
		for (Map<String, Object> altMeta : list) {
			Map<String,Object> obj = (Map<String,Object>)altMeta.get("modMap");
			if(obj.get("obj")!=null)
				setModuleAltMeta(this.userId, this.currOptId, this.tarOptId, (List<Map<String,Object>>)obj.get("obj"), altMetaList, altInfoList, altDetilInfoList, operDate, altBatch);
			obj = (Map<String,Object>)altMeta.get("tabMap");
			if(obj.get("obj")!=null)
				setTableAltMeta(this.userId,  this.currOptId, this.tarOptId,(List<Map<String,Object>>)obj.get("obj") , altMetaList, altInfoList, altDetilInfoList, operDate, altBatch);
			obj = (Map<String,Object>)altMeta.get("colMap");
			if(obj.get("obj")!=null)
				setColumnAltMeta(this.userId,  this.currOptId, this.tarOptId,(List<Map<String,Object>>)obj.get("obj") , altMetaList, altInfoList, altDetilInfoList, operDate, altBatch);
		}
	}

	public void delAnlye(HttpServletRequest request,List<Map<String,String>> altInfoList,String operDate,String altBatch,String uploadId){
		String msg = "";
		int tabDelCount = 0;
		int modDelCount = 0;
		int colDelCount = 0;
		for (Map<String,String> map : altList) {
			//变更主键ID
			String altId = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
			String altInfoId = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
			map.put("ALT_ID", altInfoId);
			if(map.get("CLASSIFER_TYPE").equals("TABLE")){
				map.put("T_C_ID", altId);
				tabDelCount++;
			}else if(map.get("CLASSIFER_TYPE").equals("MODULE")){
				map.put("M_C_ID", altId);
				modDelCount++;
			}else if(map.get("CLASSIFER_TYPE").equals("COLUMN")){
				map.put("C_C_ID", altId);
				colDelCount++;
			}else{
				continue;
			}
			altInfoList.add(getAltInfoMap(altInfoId, map.get("DB_CODE"), currOptId, tarOptId, userId,"D",  map.get("CLASSIFER_TYPE"), altBatch, operDate));
		}
		if(tabDelCount>0){
			msg+="删除表级元数据"+tabDelCount+"个，";
		}
		if(modDelCount>0){
			msg+="删除模块元数据"+modDelCount+"个，";
		}
		if(colDelCount>0){
			msg+="删除字段级元数据"+colDelCount+"个，";
		}
		Map<String,String> uploadInfo = new HashMap<String, String>();
		uploadInfo.put("UPLOAD_ALT_DESC", msg);
		uploadInfo.put("UPLOAD_STS", "2");
		uploadInfo.put("UPLOAD_STS", "2");
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("uploadInfo", uploadInfo);
		page.setParameter("uploadId", uploadId);
		altServices.updUploadInfo(page);
	}
	/**
	 * 将list的null转换为string
	 */
	public void listNullToString(List<Map<String,String>> list) {
		for (Map<String, String> obj : list) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
	}
	public Map<String,String> getAltInfoMap(String altId,String dbId,String curVerOptId,String tarDate
			,String altUser,String altType,String classIfier,String altBatch,String operDate){
		Map<String,String> altInfoMap = new HashMap<String, String>();
		altInfoMap.put("ALT_ID", altId);
		altInfoMap.put("ALT_SYS_CODE", dbId);
		altInfoMap.put("CUR_VER_DATE_NO", curVerOptId);
		altInfoMap.put("ALT_VER_DATE_NO", tarDate);
		altInfoMap.put("ALT_OPER_DATE", operDate);
		altInfoMap.put("ALT_USER", altUser);
		altInfoMap.put("CLASSIFER_TYPE", classIfier);
		altInfoMap.put("ALT_TYPE", altType);
		altInfoMap.put("ALT_MODE", "E");
		altInfoMap.put("ALT_STS", "3");
		altInfoMap.put("ALT_BATCH", altBatch);
		return altInfoMap;
	}
	/**
	 * 创建module变更元数据对象
	 * @param tarDate
	 * @param moduleArr
	 * @param altList
	 * @param altInfoList
	 * @param altDetilInfoList
	 */
	public void setModuleAltMeta(String userId,String curOptId,String tarDate,List<Map<String, Object>> modList,List<Map<String,String>> altList,List<Map<String,String>> altInfoList,List<Map<String,String>> altDetilInfoList
			,String operDate,String altBatch){
		for (Map<String, Object> module : modList) {
			
			//变更主键ID
			String altId = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
			String altInfoId = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
			
			boolean flag = false;
			
			for (Iterator iterator = module.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(key.startsWith("MOD_")){
					if(((String)module.get("ALTTYPE")).equals("D")){
						flag = true;
						break;
					}else if(module.get(key).toString().equals("1")){
						Map<String,String> altDetailInfoMap = new HashMap<String, String>();
						altDetailInfoMap.put("ACT_META_ID", altId);
						altDetailInfoMap.put("ALT_ID", altInfoId);
						altDetailInfoMap.put("CLASSIFER_TYPE", "MODULE");
						altDetailInfoMap.put("S_VALUE", (String)module.get("S_"+key.substring("MOD_".length())));
						altDetailInfoMap.put("T_VALUE", (String)module.get("T_"+key.substring("MOD_".length())));
						altDetailInfoMap.put("ALT_COL_NAME", key.substring("MOD_".length()));
						altDetilInfoList.add(altDetailInfoMap);
						flag = true;
					}
				}
			}
			//发生了变更！！！
			if(flag){
				//记录变更数据对象
				Map<String,String> altMap = new HashMap<String, String>();
				
				altMap.put("M_C_ID", altId);
				altMap.put("ALT_ID", altInfoId);
				altMap.put("CLASSIFER_TYPE", "MODULE");
				altMap.put("DB_CODE", (String)module.get("DB_CODE"));
				altMap.put("SCHNAME",(String) module.get("SCHNAME"));
				altMap.put("MODNAME",(String) module.get("MODNAME"));
				altMap.put("MODCHNAME", (String)module.get("T_MODCHNAME"));
				altMap.put("DEPT", (String)module.get("T_DEPT"));
				altMap.put("DEPTCHARGER",(String) module.get("T_DEPTCHARGER"));
				altMap.put("DEVLOPER",(String) module.get("T_DEVLOPER"));
				altMap.put("MODPTN", (String)module.get("T_MODPTN"));
				altMap.put("OBJCNT",(String) module.get("T_OBJCNT"));
				altMap.put("REMARK",(String) module.get("T_REMARK"));
				altMap.put("SA", (String)module.get("T_SA"));
				altMap.put("STATUS", (String)module.get("T_STATUS"));
				altMap.put("TABCNT", (String)module.get("T_TABCNT"));
				altMap.put("USEDSIZE",(String) module.get("T_USEDSIZE"));
				altList.add(altMap);
				
				
				//变更登记数据对象
				Map<String,String> altInfoMap = new HashMap<String, String>();
				altInfoMap.put("ALT_ID", altInfoId);
				altInfoMap.put("ALT_SYS_CODE",(String) module.get("DB_CODE"));
				altInfoMap.put("CUR_VER_DATE_NO", curOptId);
				altInfoMap.put("ALT_VER_DATE_NO", tarDate);
				altInfoMap.put("ALT_OPER_DATE", operDate);
				altInfoMap.put("ALT_USER", userId);
				altInfoMap.put("CLASSIFER_TYPE", "MODULE");
				altInfoMap.put("ALT_TYPE",  (String)module.get("ALTTYPE"));
				altInfoMap.put("ALT_MODE", "U");
				altInfoMap.put("ALT_STS", "3");
				altInfoMap.put("ALT_BATCH", altBatch);
				altInfoList.add(altInfoMap);
			}
			
		}
	}
	
	/**
	 * 创建Table变更元数据对象
	 * @param tarDate
	 * @param tableArr
	 * @param altList
	 * @param altInfoList
	 * @param altDetilInfoList
	 */
	public void setTableAltMeta(String userId,String curOptId,String tarDate,List<Map<String, Object>> tabList,List<Map<String,String>> altList,List<Map<String,String>> altInfoList,List<Map<String,String>> altDetilInfoList
			,String operDate,String altBatch){
		
		for (Map<String, Object> table : tabList) {
			
			//变更主键ID
			String altId = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
			String altInfoId = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
			
			boolean flag = false;
			
			for (Iterator iterator = table.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(key.startsWith("MOD_")){
					if(((String)table.get("ALTTYPE")).equals("D")){
						flag = true;
						break;
					}else if(table.get(key).toString().equals("1")){
						Map<String,String> altDetailInfoMap = new HashMap<String, String>();
						altDetailInfoMap.put("ACT_META_ID", altId);
						altDetailInfoMap.put("ALT_ID", altInfoId);
						altDetailInfoMap.put("CLASSIFER_TYPE", "TABLE");
						altDetailInfoMap.put("S_VALUE", (String)table.get("S_"+key.substring("MOD_".length())));
						altDetailInfoMap.put("T_VALUE", (String)table.get("T_"+key.substring("MOD_".length())));
						altDetailInfoMap.put("ALT_COL_NAME", key.substring("MOD_".length()));
						altDetilInfoList.add(altDetailInfoMap);
						flag = true;
					}
				}
			}
			//发生了变更！！！
			if(flag){
				//记录变更数据对象
				Map<String,String> altMap = new HashMap<String, String>();
				
				altMap.put("T_C_ID", altId);
				altMap.put("ALT_ID", altInfoId);
				altMap.put("CLASSIFER_TYPE", "TABLE");
				altMap.put("DB_CODE", (String)table.get("DB_CODE"));
				altMap.put("SCHNAME",(String) table.get("SCHNAME"));
				altMap.put("MODNAME",(String) table.get("MODNAME"));
				altMap.put("TABNAME", (String) table.get("TABNAME"));
				altMap.put("TABCHNAME", (String) table.get("T_TABCHNAME"));
				altMap.put("TABSPACENAME", (String) table.get("T_TABSPACENAME"));
				altMap.put("PKCOLS", (String) table.get("T_PKCOLS"));
				altMap.put("FKCOLS", (String) table.get("T_FKCOLS"));
				altMap.put("FKTABLENAME", (String) table.get("T_FKTABLENAME"));
				altMap.put("ROWCOUNT", (String) table.get("T_ROWCOUNT"));
				altMap.put("IMPFLAG", (String) table.get("T_IMPFLAG"));
				altMap.put("REMARK", (String) table.get("T_REMARK"));
				altMap.put("ZIPDESC", (String) table.get("T_ZIPDESC"));
				altMap.put("LCYCDESC", (String) table.get("T_LCYCDESC"));
				altMap.put("PCNT", (String) table.get("T_PCNT"));
				altMap.put("TSIZE", (String) table.get("T_TSIZE"));
				altMap.put("CRTDATE", (String) table.get("T_CRTDATE"));
				altMap.put("MODIYDATE", operDate);
				altList.add(altMap);
				
				
				//变更登记数据对象
				Map<String,String> altInfoMap = new HashMap<String, String>();
				altInfoMap.put("ALT_ID", altInfoId);
				altInfoMap.put("ALT_SYS_CODE",(String) table.get("DB_CODE"));
				altInfoMap.put("CUR_VER_DATE_NO", curOptId);
				altInfoMap.put("ALT_VER_DATE_NO", tarDate);
				altInfoMap.put("ALT_OPER_DATE", operDate);
				altInfoMap.put("ALT_USER", userId);
				altInfoMap.put("CLASSIFER_TYPE", "TABLE");
				altInfoMap.put("ALT_TYPE",  (String)table.get("ALTTYPE"));
				altInfoMap.put("ALT_MODE", "U");
				altInfoMap.put("ALT_STS", "3");
				altInfoMap.put("ALT_BATCH", altBatch);
				altInfoList.add(altInfoMap);
			}
			
		}
	}
	
	/**
	 * 创建Column变更元数据对象
	 * @param tarDate
	 * @param moduleArr
	 * @param altList
	 * @param altInfoList
	 * @param altDetilInfoList
	 */
	public void setColumnAltMeta(String userId,String curOptId,String tarDate,List<Map<String, Object>> colList,List<Map<String,String>> altList,List<Map<String,String>> altInfoList,List<Map<String,String>> altDetilInfoList
			,String operDate,String altBatch){
		
		for (Map<String, Object> column : colList) {
			
			//变更主键ID
			String altId = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
			String altInfoId = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
			
			boolean flag = false;
			
			for (Iterator iterator = column.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(key.startsWith("MOD_")){
					if(((String)column.get("ALTTYPE")).equals("D")){
						flag = true;
						break;
					}else if(column.get(key).toString().equals("1")){
						Map<String,String> altDetailInfoMap = new HashMap<String, String>();
						altDetailInfoMap.put("ACT_META_ID", altId);
						altDetailInfoMap.put("ALT_ID", altInfoId);
						altDetailInfoMap.put("CLASSIFER_TYPE", "COLUMN");
						altDetailInfoMap.put("S_VALUE", (String)column.get("S_"+key.substring("MOD_".length())));
						altDetailInfoMap.put("T_VALUE", (String)column.get("T_"+key.substring("MOD_".length())));
						altDetailInfoMap.put("ALT_COL_NAME", key.substring("MOD_".length()));
						altDetilInfoList.add(altDetailInfoMap);
						flag = true;
					}
				}
			}
			//发生了变更！！！
			if(flag){
				//记录变更数据对象
				Map<String,String> altMap = new HashMap<String, String>();
				
				altMap.put("C_C_ID", altId);
				altMap.put("ALT_ID", altInfoId);
				altMap.put("CLASSIFER_TYPE", "COLUMN");
				altMap.put("DB_CODE", (String)column.get("DB_CODE"));
				altMap.put("SCHNAME",(String) column.get("SCHNAME"));
				altMap.put("MODNAME",(String) column.get("MODNAME"));
				altMap.put("TABNAME", (String) column.get("TABNAME"));
				altMap.put("COLNAME", (String)column.get("COLNAME"));
				altMap.put("COLCHNAME", (String)column.get("T_COLCHNAME"));
				altMap.put("COLTYPE", (String)column.get("T_COLTYPE"));
				altMap.put("COLSEQ", (String)column.get("T_COLSEQ"));
				altMap.put("PKFLAG", (String)column.get("T_PKFLAG"));
				altMap.put("PDKFLAG", (String)column.get("T_PDKFLAG"));
				altMap.put("NVLFLAG", (String)column.get("T_NVLFLAG"));
				altMap.put("CCOLFLAG", (String)column.get("T_CCOLFLAG"));
				altMap.put("INDXFLAG", (String)column.get("T_INDXFLAG"));
				altMap.put("CODETAB", (String)column.get("T_CODETAB"));
				altMap.put("REMARK", (String)column.get("T_REMARK"));
				altList.add(altMap);
				
				
				//变更登记数据对象
				Map<String,String> altInfoMap = new HashMap<String, String>();
				altInfoMap.put("ALT_ID", altInfoId);
				altInfoMap.put("ALT_SYS_CODE",(String) column.get("DB_CODE"));
				altInfoMap.put("CUR_VER_DATE_NO", curOptId);
				altInfoMap.put("ALT_VER_DATE_NO", tarDate);
				altInfoMap.put("ALT_OPER_DATE", operDate);
				altInfoMap.put("ALT_USER", userId);
				altInfoMap.put("CLASSIFER_TYPE", "COLUMN");
				altInfoMap.put("ALT_TYPE",  (String)column.get("ALTTYPE"));
				altInfoMap.put("ALT_MODE", "U");
				altInfoMap.put("ALT_STS", "3");
				altInfoMap.put("ALT_BATCH", altBatch);
				altInfoList.add(altInfoMap);
			}
			
		}
	}
	
}