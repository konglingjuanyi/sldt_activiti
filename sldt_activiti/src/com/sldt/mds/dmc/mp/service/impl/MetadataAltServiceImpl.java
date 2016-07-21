package com.sldt.mds.dmc.mp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.dao.MetadataAltDao;
import com.sldt.mds.dmc.mp.dao.MetadataVerDao;
import com.sldt.mds.dmc.mp.service.MetadataAltService;
import com.sldt.mds.dmc.mp.service.MetadataAnalyseService;
import com.sldt.mds.dmc.mp.util.DateUtil;

@Service(value="metaAltService")
public class MetadataAltServiceImpl implements MetadataAltService{
	
	private static Log log = LogFactory.getLog(MetadataAltServiceImpl.class);

	@Resource(name="metaAltDao")
	private MetadataAltDao metaAltDao;
	
	@Resource(name="metadataVerDao")
	private MetadataVerDao metadataVerDao;
	
	@Resource(name="metaAnalyService")
	private MetadataAnalyseService metaAnalyService;
	
	@Override
	public List<Map<String, Object>> getNewAltMeta(PageResults page) {
		return metaAltDao.getNewAltMeta(page);
	}

	@Override
	public List<Map<String, Object>> getDecAltSumByDbId(PageResults page) {
		return metaAltDao.getDecAltSumByDbId(page);
	}

	@Override
	public List<Map<String, Object>> getAltSumByDbId(PageResults page) {
		return metaAltDao.getAltSumByDbId(page);
	}

	@Override
	public void insUploadInfo(PageResults page) {
         metaAltDao.insUploadInfo(page);		
	}
	
	public Map<String, List<Map<String, String>>> analyseAltMeta(HttpServletRequest request,
			String altUser, String curVerOptId, String tarDate,
			List<Map<String, String>> newMetaList, String operDate,
			String altBatch) {
		Map<String, List<Map<String, String>>> altMetaMap = new HashMap<String, List<Map<String, String>>>();
		// 变更的元数据集合
				List<Map<String, String>> altList = new ArrayList<Map<String, String>>();
				// 变更登记数据集合
				List<Map<String, String>> altInfoList = new ArrayList<Map<String, String>>();
				// 变更登记明细数据集合
				List<Map<String, String>> altDetilInfoList = new ArrayList<Map<String, String>>();
				Map<String, Map<String, String>> classIsNotEqa = new HashMap<String, Map<String, String>>();
				// 修改类型过滤条件
				Map<String, String> isNotEqaU = new HashMap<String, String>();
				initIsNotEqa(classIsNotEqa, isNotEqaU);

				for (Map<String, String> newDBMap : newMetaList) {
					PageResults page = ControllerHelper.buildPage(request);
					page.setParameter("endDate", DateUtil.getYmdDate());
					page.setParameter("optId", curVerOptId);
					String dbId = newDBMap.get("DB_CODE");
					String classIfier = newDBMap.get("CLASSIFER_TYPE");
					page.setParameter("dbId", newDBMap.get("DB_CODE"));
					List<Map<String, Object>> dbList = new ArrayList<Map<String, Object>>();
               if(classIfier.equals("DATABASE")){
            	   dbList = metadataVerDao.getVerDataBaseMeta(page);
               }else if (classIfier.equals("SCHEMA")) {
            	   page.setParameter("schemaId", newDBMap.get("SCHNAME"));
            	   dbList = metadataVerDao.getVerSchemaMeta(page);
               }else if (classIfier.equals("MODULE")){
            	   page.setParameter("schemaId", newDBMap.get("SCHNAME"));
   				   page.setParameter("moduleId", newDBMap.get("MODNAME"));
   				   dbList = metadataVerDao.getVerModuleMeta(page);
               }else if (classIfier.equals("TABLE")){
            	   page.setParameter("schemaId", newDBMap.get("SCHNAME"));
   				   page.setParameter("moduleId", newDBMap.get("MODNAME"));
   				   page.setParameter("tableId", newDBMap.get("TABNAME"));
   				   dbList = metadataVerDao.getVerTableMeta(page);
               }else if (classIfier.equals("COLUMN")){
            	   page.setParameter("schemaId", newDBMap.get("SCHNAME"));
   				   page.setParameter("moduleId", newDBMap.get("MODNAME"));
   				   page.setParameter("tableId", newDBMap.get("TABNAME"));
   				   page.setParameter("columnId", newDBMap.get("COLNAME"));
   				   dbList = metadataVerDao.getVerColumnMeta(page);
               } else if (classIfier.equals("COLCODE")){
            	   page.setParameter("schName", newDBMap.get("SCHNAME"));
   				   page.setParameter("modName", newDBMap.get("MODNAME"));
   				   page.setParameter("tabName", newDBMap.get("TABNAME"));
   				   page.setParameter("colName", newDBMap.get("COLNAME"));
   				   page.setParameter("codeVal", newDBMap.get("CODE_VAL"));
   				   dbList = metadataVerDao.getVerColCodeMeta(page);
               }else if (classIfier.equals("INTFILE")){
            	   page.setParameter("refDbCode", newDBMap.get("REF_DB_CODE"));
   				   page.setParameter("refSchema", newDBMap.get("REF_SCHNAME"));
   				   page.setParameter("refModName", newDBMap.get("REF_MODNAME"));
   				   page.setParameter("refTabName", newDBMap.get("REF_TABNAME"));
   				   page.setParameter("cFuncName", newDBMap.get("C_FUNC_NAME"));
   				   page.setParameter("cType", newDBMap.get("C_TYPE"));
   				   dbList = metadataVerDao.getVerIntFileMeta(page);
               }
       		isMetaAlt(newDBMap, dbList, altList, altInfoList, altDetilInfoList,
					classIsNotEqa.get(classIfier), isNotEqaU, altUser, dbId,
					curVerOptId, tarDate, operDate, altBatch, classIfier);
	   }
				altMetaMap.put("altList", altList);
				altMetaMap.put("altInfoList", altInfoList);
				altMetaMap.put("altDetilInfoList", altDetilInfoList);
				return altMetaMap;
	}
	
	public void initIsNotEqa(Map<String, Map<String, String>> classIsNotEqa,
			Map<String, String> isNotEqaU) {
		// 新增类型过滤条件
		Map<String, String> isNotEqaI = new HashMap<String, String>();
		isNotEqaI.put("MAINTAINER", "");
		isNotEqaI.put("IMGURL", "");
		isNotEqaI.put("TOTALSIZE", "");
		isNotEqaI.put("USEDSIZE", "");
		isNotEqaI.put("TABCNT", "");
		isNotEqaI.put("OBJCNT", "");
		classIsNotEqa.put("DATABASE", isNotEqaI);
		isNotEqaI = new HashMap<String, String>();
		isNotEqaI.put("TABCNT", "");
		isNotEqaI.put("USEDSIZE", "");
		isNotEqaI.put("OBJCNT", "");
		classIsNotEqa.put("SCHEMA", isNotEqaI);
		isNotEqaI = new HashMap<String, String>();
		isNotEqaI.put("DEPT", "");
		isNotEqaI.put("DEPTCHARGER", "");
		isNotEqaI.put("STATUS", "");
		isNotEqaI.put("USEDSIZE", "");
		isNotEqaI.put("TABCNT", "");
		isNotEqaI.put("OBJCNT", "");
		classIsNotEqa.put("MODULE", isNotEqaI);
		isNotEqaI = new HashMap<String, String>();
		isNotEqaI.put("FKTABLENAME", "");
		isNotEqaI.put("ROWCOUNT", "");
		isNotEqaI.put("ZIPDESC", "");
		isNotEqaI.put("PCNT", "");
		isNotEqaI.put("TSIZE", "");
		isNotEqaI.put("CRTDATE", "");
		isNotEqaI.put("MODIYDATE", "");
		isNotEqaI.put("RN", "");
		classIsNotEqa.put("TABLE", isNotEqaI);
		isNotEqaI = new HashMap<String, String>();
		isNotEqaI.put("PDKFLAG", "");
		isNotEqaI.put("RN", "");
		classIsNotEqa.put("COLUMN", isNotEqaI);

		isNotEqaU.put("F_STARTDATE", "");
		isNotEqaU.put("F_ENDDATE", "");
		isNotEqaU.put("COLNAME", "");
		isNotEqaU.put("TABNAME", "");
		isNotEqaU.put("MODNAME", "");
		isNotEqaU.put("SCHNAME", "");
		isNotEqaU.put("DB_CODE", "");

		isNotEqaU.put("CODE_VAL", "");

		isNotEqaU.put("REF_DB_CODE", "");
		isNotEqaU.put("REF_SCHNAME", "");
		isNotEqaU.put("REF_MODNAME", "");
		isNotEqaU.put("REF_TABNAME", "");
		isNotEqaU.put("C_FUNC_NAME", "");
	}
	
	public void isMetaAlt(Map<String, String> newDBMap,
			List<Map<String, Object>> dbList,
			List<Map<String, String>> altList,
			List<Map<String, String>> altInfoList,
			List<Map<String, String>> altDetilInfoList,
			Map<String, String> isNotEqaI, Map<String, String> isNotEqaU,
			String altUser, String dbId, String curVerOptId, String tarDate,
			String operDate, String altBatch, String classIfier) {
		boolean metaIsAlt = false;
		String altId = UUID.randomUUID().randomUUID().toString()
				.replaceAll("-", "");
		String altMetaId = UUID.randomUUID().randomUUID().toString()
				.replaceAll("-", "");
		// 变更类型：I/U/D
		String altType = "";
		if (dbList.size() <= 0) {
			altType = "I";
			metaIsAlt = true;
		} else {
			altType = "U";
			for (Map<String, Object> dbMap : dbList) {
				for (Iterator<String> iterator = dbMap.keySet().iterator(); iterator
						.hasNext();) {
					String key = iterator.next();
					// 不比对属性
					if (isNotEqaU.containsKey(key)
							|| isNotEqaI.containsKey(key)) {
						continue;
					}
					String dValue =  getObjectValue(dbMap.get(key));
//					String dValue =  (String)dbMap.get(key);
					String nValue = newDBMap.get(key);
					if (isALt(dValue, nValue)) {
						// 增加变更明细数据对象到集合中
						altDetilInfoList.add(getAltDetailMap(altMetaId, altId,
								key, classIfier, dValue, nValue));
						metaIsAlt = true;
					}
				}
			}
		}
		// 元数据属性发生了变更
		if (metaIsAlt) {
			altInfoList.add(getAltInfoMap(altId, dbId, curVerOptId, tarDate,
					altUser, altType, classIfier, altBatch, operDate));
			if (classIfier.equals("DATABASE")) {
				newDBMap.put("DB_C_ID", altMetaId);
			} else if (classIfier.equals("SCHEMA")) {
				newDBMap.put("S_C_ID", altMetaId);
			} else if (classIfier.equals("MODULE")) {
				newDBMap.put("M_C_ID", altMetaId);
			} else if (classIfier.equals("TABLE")) {
				newDBMap.put("T_C_ID", altMetaId);
			} else if (classIfier.equals("COLUMN")) {
				newDBMap.put("C_C_ID", altMetaId);
			} else if (classIfier.equals("COLCODE")) {
				newDBMap.put("C_C_ID", altMetaId);
			} else if (classIfier.equals("INTFILE")) {
				newDBMap.put("F_C_ID", altMetaId);
			}
			newDBMap.put("ALT_ID", altId);
			altList.add(newDBMap);
		}
	}

	public String getObjectValue(Object param) {
		String val = "";
		if (param instanceof Integer) {
			int value = ((Integer) param).intValue();
			val = ""+value;
		} else if (param instanceof String) {
			val = (String) param;
		} else if (param instanceof Double) {
			double d = ((Double) param).doubleValue();
			val = "" + d;
		} else if (param instanceof Float) {
			float f = ((Float) param).floatValue();
			val = ""+f;
		} else if (param instanceof Long) {
			long l = ((Long) param).longValue();
			val = ""+l;
		} else if (param instanceof Boolean) {
			boolean b = ((Boolean) param).booleanValue();
			val = ""+b;
		} else if (param instanceof Date) {
			Date d = (Date) param;
		}
		return val;
	}
	
	public boolean isALt(String str, String str1) {
		if ((str == null && str1 == null)
				|| (str == null && "null".equals(str1))) {
			return false;
		}
		if (str == null && str1 != null) {
			return true;
		}
		if (str.equals(str1)) {
			return false;
		} else {
			return true;
		}

	}
	
	public Map<String, String> getAltDetailMap(String altMetaId, String altId,
			String altColName, String classIfier, String sVlue, String tValue) {
		Map<String, String> altDetailMap = new HashMap<String, String>();
		altDetailMap.put("ACT_META_ID", altMetaId);
		altDetailMap.put("ALT_ID", altId);
		altDetailMap.put("ALT_COL_NAME", altColName);
		altDetailMap.put("CLASSIFER_TYPE", classIfier);
		altDetailMap.put("S_VALUE", sVlue);
		altDetailMap.put("T_VALUE", tValue);
		return altDetailMap;
	}
	
	public Map<String, String> getAltInfoMap(String altId, String dbId,
			String curVerOptId, String tarDate, String altUser, String altType,
			String classIfier, String altBatch, String operDate) {
		Map<String, String> altInfoMap = new HashMap<String, String>();
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

	@Override
	public void insAltMetaItem(PageResults page) {
		metaAltDao.insAltMetaItem(page);
		
	}

	@Override
	public void updUploadInfo(PageResults page) {
		metaAltDao.updUploadInfo(page);
		
	}

	@Override
	public Map<String, Object> analyseAltMetaFull(String tmpId, String dbId) {
		Map<String, Object> obj = new HashMap<String, Object>();
		Map<String, Object> dbObjMap = new HashMap<String, Object>();
		Map<String, Object> schObjMap = new HashMap<String, Object>();
		Map<String, Object> modObjMap = new HashMap<String, Object>();
		Map<String, Object> tabObjMap = new HashMap<String, Object>();
		Map<String, Object> colObjMap = new HashMap<String, Object>();
		List<Map<String, Object>> modList = metaAltDao.analyseAltModuleMetaFull(tmpId, dbId);
		modObjMap.put("msg", getMsg("模块", modList));
		modObjMap.put("obj", modList);
		List<Map<String, Object>> tabList = metaAltDao.analyseAltTableMetaFull(tmpId, dbId);
		tabObjMap.put("msg", getMsg("表级", tabList));
		tabObjMap.put("obj", tabList);
		List<Map<String, Object>> colList = metaAltDao.analyseAltColumnMetaFull(tmpId, dbId);
		colObjMap.put("msg", getMsg("字段级", colList));
		colObjMap.put("obj", colList);
		// 清空数据
		metaAltDao.delMetaInfoTmp(tmpId);

		// obj.put("dbMap", dbObjMap);
		// obj.put("schMap", schObjMap);
		obj.put("modMap", modObjMap);
		obj.put("tabMap", tabObjMap);
		obj.put("colMap", colObjMap);

		return obj;
	}
	public String getMsg(String msgType, List<Map<String, Object>> list) {
		int addCount = 0;
		int updCount = 0;
		int delCount = 0;
		String altType = "";
		for (Map<String, Object> schMap : list) {
			altType = (String) schMap.get("ALTTYPE");
			// 删除
			if (altType != null && altType.equals("D")) {
				delCount++;
				// 新增
			} else if (altType != null && altType.equals("I")) {
				addCount++;
			} else {
				for (Iterator iterator = schMap.keySet().iterator(); iterator
						.hasNext();) {
					String key = (String) iterator.next();
					if (key.startsWith("MOD_")) {
						if (schMap.get(key).toString().equals("1")) {
							updCount++;
							break;
						}
					}

				}
			}
		}
		String msg = "";
		if (addCount > 0) {
			msg = msgType + "新增" + addCount + "个，";
		}
		if (updCount > 0) {
			msg = msgType + "修改" + updCount + "个，";
		}
		if (delCount > 0) {
			msg = msgType + "删除" + delCount + "个，";
		}
		return msg;
	}

	@Override
	public void delUploadInfo(PageResults page) {
		metaAltDao.delUploadInfo(page);
		
	}

	@Override
	public List<Map<String, Object>> getUploadList(PageResults page) {
		return metaAltDao.getUploadList(page);
	}

	@Override
	public List<Map<String, Object>> getAltMetaItem(PageResults page) {
		return metaAltDao.getAltMetaItem(page);
	}

	@Override
	public void delAltMetaDetail(PageResults page) {

		metaAltDao.delAltMetaDetail(page);
	}

	@Override
	public void updAltMetaSts(PageResults page) {
		/**
		 * 获取当前元数据的真实instanceId
		 */
		List<Map<String, Object>> instId = metaAnalyService.getInstanceId(page);
		if (instId != null && instId.size() > 0) {
			String instanceId = (String) instId.get(0).get("INSTANCE_ID");
			page.setParameter("instanceId", instanceId);
			List<Map<String, Object>> impacts = metaAnalyService.getImpactSide(page);
			page.setParameter("impacts", impacts);
			metaAnalyService.insImpactSide(page);
		}
		metaAltDao.updAltMetaSts(page);
		
	}

	@Override
	public List<Map<String, Object>> showAltMetaDetail(PageResults page) {
		// TODO Auto-generated method stub
		return metaAltDao.showAltMetaDetail(page);
	}
}
