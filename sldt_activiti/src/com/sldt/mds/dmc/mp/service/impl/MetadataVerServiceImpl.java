package com.sldt.mds.dmc.mp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.dao.MetadataVerDao;
import com.sldt.mds.dmc.mp.service.MetadataVerService;
import com.sldt.mds.dmc.mp.vo.MetadataColumnVO;
import com.sldt.mds.dmc.mp.vo.MetadataDatabaseVO;
import com.sldt.mds.dmc.mp.vo.MetadataModuleVO;
import com.sldt.mds.dmc.mp.vo.MetadataSchemaVO;
import com.sldt.mds.dmc.mp.vo.MetadataTableVO;

@Service(value="metaVerService")
@Transactional(readOnly=true)
public class MetadataVerServiceImpl implements MetadataVerService {
	
	private static final Log log = LogFactory.getLog(MetadataVerServiceImpl.class);
	
	@Resource(name="metadataVerDao")
	private MetadataVerDao metadataVerDao;

	@Override
	public Map<String, Object> getCurrVerMap(PageResults page) {
		Map<String, Object> obj = metadataVerDao.getCurrVerMap(page);
		for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			if(obj.get(key)==null){
				obj.put(key, "");
			}
		}
		return obj;

	}

	@Override
	public List<MetadataDatabaseVO> getMyVersionMeta(PageResults page) {
		List<Map<String,Object>> objList = metadataVerDao.getMyVersionMeta(page);
		List<MetadataDatabaseVO> dbList = new ArrayList<MetadataDatabaseVO>();
		boolean isAdmin = false;
		if(page.getParameter("isAdmin")!=null){
			isAdmin = (Boolean)page.getParameter("isAdmin");
		}
		for (Map<String, Object> objMap : objList) {
			MetadataDatabaseVO dbVO = new MetadataDatabaseVO();
			dbVO.setDbId((String)objMap.get("DB_CODE"));
			dbVO.setDbChName((String)objMap.get("DBCHNAME"));
			dbVO.setUserName((String)objMap.get("NAME"));
			dbVO.setTitle((((String)objMap.get("DBCHNAME")).length()>12) ? ((String)objMap.get("DBCHNAME")).substring(0,12)+"..." : ((String)objMap.get("DBCHNAME")));
			dbVO.setUserId((String)objMap.get("USER_ID"));
			if(page.getParamStr("isUser")!=null&&!"".equals(page.getParamStr("isUser"))){
				if(((String)objMap.get("USER_ID")).equals(page.getParamStr("isUser"))||isAdmin){
					dbVO.setMe(true);
				}else{
					dbVO.setMe(false);
				}
			}
			if("1".equals(((String)objMap.get("ISSNACODE")))){
				dbVO.setIsSnaCode((String)objMap.get("ISSNACODE"));
				dbList.add(dbVO);
				continue;
			}
			dbVO.setDba((String)objMap.get("DBA"));
			dbVO.setDept((String)objMap.get("DEPT"));
			dbVO.setDevloper((String)objMap.get("DEVLOPER"));
			dbVO.setImageUrl(((String)objMap.get("IMGURL")) == null ? "imgs/card/core.png" : ((String)objMap.get("IMGURL")));
			dbVO.setMaintainer((String)objMap.get("MAINTAINER"));
			dbVO.setObjCnt((String)objMap.get("OBJCNT"));
			dbVO.setProMFac((String)objMap.get("PRO_M_FAC"));
			dbVO.setRemark((String)objMap.get("REMARK"));
//			dbVO.setVerName((String)objMap.get("VER_NAME"));
//			dbVO.setVerDesc((String)objMap.get("VER_DESC"));
			dbVO.setTabCnt((String)objMap.get("TABCNT"));
			dbVO.setAltOperDate((String)objMap.get("ALT_OPER_DATE"));
			if(dbVO.getAltOperDate()!=null){
				if(dbVO.getAltOperDate().indexOf("-")==4){
					dbVO.setAltOperDate(dbVO.getAltOperDate().substring(5,10).replaceAll("-", "日")+"月");
				}
			}
			dbVO.setTarget("_blank");
			dbVO.setTotalSize((String)objMap.get("TOTALSIZE"));
			dbVO.setUsedSize((String)objMap.get("USEDSIZE"));
			dbVO.setNamespace((String)objMap.get("SNA_NAMESPACE"));
			dbVO.setfStartdate((String)objMap.get("F_STARTDATE"));
			dbVO.setfEnddate((String)objMap.get("F_ENDDATE"));
			dbVO.setLastVerOptId((String)objMap.get("LASTVER"));
			dbList.add(dbVO);
		}
		return dbList;
	}

	@Override
	public List<Map<String, Object>> getNewVersionMeta(
			Map<String, String> params) {
		return metadataVerDao.getNewVersionMeta(params);
	}

	@Override
	public List<Map<String, Object>> getHisVerList(PageResults page) {
		List<Map<String, Object>> list = metadataVerDao.getHisVerList(page);
		for (Map<String, Object> obj :list){
			for(Iterator iterator = obj.keySet().iterator();iterator.hasNext();){
				String key = (String) iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		} 
		return list;
	}

	@Override
	public Map<String, Object> getInitVer(String dbId) {
		return metadataVerDao.getInitVer(dbId);
	}

	@Override
	public Map<String, Object> getCurDeveVer(PageResults page) {

		return metadataVerDao.getCurDeveVer(page);
	}

	@Override
	public int getVerSchemaMetaCount(PageResults page) {
		return metadataVerDao.getVerSchemaMetaCount(page);
	}

	@Override
	public int getVerModuleMetaCount(PageResults page) {
		return metadataVerDao.getVerModuleMetaCount(page);
	}

	@Override
	public int getVerTableMetaCount(PageResults page) {
		return metadataVerDao.getVerTableMetaCount(page);
	}

	@Override
	public int getVerColumnMetaCount(PageResults page) {
		return metadataVerDao.getVerColumnMetaCount(page);
	}

	@Override
	public List<Map<String, Object>> getForAndDeveVerList(PageResults page) {
		List<Map<String, Object>> list = metadataVerDao.getForAndDeveVerList(page);
		for (Map<String, Object> obj : list) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return list;
	}

	@Override
	public List<MetadataDatabaseVO> getVerDataBaseMeta(PageResults page) {
		List<Map<String,Object>> objList = metadataVerDao.getVerDataBaseMeta(page);
		List<MetadataDatabaseVO> dbList = new ArrayList<MetadataDatabaseVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataDatabaseVO dbVO = new MetadataDatabaseVO();
			dbVO.setDbId((String)objMap.get("DB_CODE"));
			dbVO.setDbChName((String)objMap.get("DBCHNAME"));
			dbVO.setDba((String)objMap.get("DBA"));
			dbVO.setDept((String)objMap.get("DEPT"));
			dbVO.setDevloper((String)objMap.get("DEVLOPER"));
			dbVO.setImageUrl(((String)objMap.get("IMGURL")) == null ? "images/card/core.png" : ((String)objMap.get("IMGURL")));
			dbVO.setMaintainer((String)objMap.get("MAINTAINER"));
			dbVO.setObjCnt((String)objMap.get("OBJCNT"));
			dbVO.setProMFac((String)objMap.get("PRO_M_FAC"));
			dbVO.setRemark((String)objMap.get("REMARK"));
			dbVO.setTabCnt((String)objMap.get("TABCNT"));
			dbVO.setTarget("_blank");
			dbVO.setTotalSize((String)objMap.get("TOTALSIZE"));
			dbVO.setUsedSize((String)objMap.get("USEDSIZE"));
			dbList.add(dbVO);
		}
		return dbList;
	}

	@Override
	public void insMetaTmpByExcData(PageResults page) {
 
       metadataVerDao.insMetaTmpByExcData(page);		
	}

	@Override
	public List<Map<String, Object>> getMetaVerDate(Map<String, String> params) {
		return metadataVerDao.getMetaVerDate(params);
	}

	@Override
	public List<Map<String, Object>> getCurrAltBatch(PageResults page) {
		List<Map<String, Object>> list = metadataVerDao.getCurrAltBatch(page);
		for (Map<String, Object> obj : list) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return list;
	}

	@Override
	public List<MetadataSchemaVO> getProVerSchemaMeta(PageResults page) {
        List<Map<String, Object>> objList = metadataVerDao.getProVerSchemaMeta(page);
        
        List<MetadataSchemaVO> dataList= new ArrayList<MetadataSchemaVO>();
        for (Map<String, Object> objMap : objList) {
			MetadataSchemaVO dataVO = new MetadataSchemaVO();
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setDevloper((String)objMap.get("DEVLOPER"));
			dataVO.setObjCnt((String)objMap.get("OBJCNT"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setSchChName((String)objMap.get("SCHCHNAME"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setTabCnt((String)objMap.get("TABCNT"));
			dataVO.setUsedSize((String)objMap.get("USEDSIZE"));
			dataList.add(dataVO);
		}
		
		return dataList;
	}

	@Override
	public List<MetadataSchemaVO> getVerSchemaMeta(PageResults page) {
        List<Map<String, Object>> objList = metadataVerDao.getVerSchemaMeta(page);
        List<MetadataSchemaVO> dataList = new ArrayList<MetadataSchemaVO>();
        
        for(Map<String, Object> objMap : objList){
        	MetadataSchemaVO dataVO = new MetadataSchemaVO();
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setDevloper((String)objMap.get("DEVLOPER"));
			dataVO.setObjCnt((String)objMap.get("OBJCNT"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setSchChName((String)objMap.get("SCHCHNAME"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setTabCnt((String)objMap.get("TABCNT"));
			dataVO.setUsedSize((String)objMap.get("USEDSIZE"));
			dataList.add(dataVO);
        }
		return dataList;
	}

	@Override
	public List<MetadataModuleVO> getProVerModuleMeta(PageResults page) {
        List<Map<String, Object>> objList = metadataVerDao.getProVerSchemaMeta(page);
        List<MetadataModuleVO> dataList = new ArrayList<MetadataModuleVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataModuleVO dataVO = new MetadataModuleVO();
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setModName((String)objMap.get("MODNAME"));
			dataVO.setModChName((String)objMap.get("MODCHNAME"));
			dataVO.setModPtn((String)objMap.get("MODPTN"));
			dataVO.setSa((String)objMap.get("SA"));
			dataVO.setStatus((String)objMap.get("STATUS"));
			dataVO.setDeptcharger((String)objMap.get("DEPTCHARGER"));
			dataVO.setDept((String)objMap.get("DEPT"));
			dataVO.setDevloper((String)objMap.get("DEVLOPER"));
			dataVO.setObjCnt((String)objMap.get("OBJCNT"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setTabCnt((String)objMap.get("TABCNT"));
			dataVO.setUsedSize((String)objMap.get("USEDSIZE"));
			dataVO.setfStartDate((String)objMap.get("F_STARTDATE"));
			dataVO.setfEndDate((String)objMap.get("F_ENDDATE"));
			dataList.add(dataVO);
		}
		
		return dataList;
	}

	@Override
	public List<MetadataModuleVO> getVerModuleMeta(PageResults page) {
		List<Map<String,Object>> objList = metadataVerDao.getVerModuleMeta(page);
		
		List<MetadataModuleVO> dataList = new ArrayList<MetadataModuleVO>();
		for (Map<String, Object> objMap : objList) {
			MetadataModuleVO dataVO = new MetadataModuleVO();
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setModName((String)objMap.get("MODNAME"));
			dataVO.setModChName((String)objMap.get("MODCHNAME"));
			dataVO.setModPtn((String)objMap.get("MODPTN"));
			dataVO.setSa((String)objMap.get("SA"));
			dataVO.setStatus((String)objMap.get("STATUS"));
			dataVO.setDeptcharger((String)objMap.get("DEPTCHARGER"));
			dataVO.setDept((String)objMap.get("DEPT"));
			dataVO.setDevloper((String)objMap.get("DEVLOPER"));
			dataVO.setObjCnt((String)objMap.get("OBJCNT"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setTabCnt((String)objMap.get("TABCNT"));
			dataVO.setUsedSize((String)objMap.get("USEDSIZE"));
			dataVO.setfStartDate((String)objMap.get("F_STARTDATE"));
			dataVO.setfEndDate((String)objMap.get("F_ENDDATE"));
			dataList.add(dataVO);
		}
		
		return dataList;
	}

	@Override
	public List<MetadataTableVO> getProVerTableMeta(PageResults page) {
		List<Map<String, Object>> objList = metadataVerDao.getProVerTableMeta(page);
		List<MetadataTableVO> dataList = new ArrayList<MetadataTableVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataTableVO dataVO = new MetadataTableVO();
			dataVO.setCrtDate((String)objMap.get("CRTDATE"));
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setFkCols((String)objMap.get("FKCOLS"));
			dataVO.setFkTableName((String)objMap.get("FKTABLENAME"));
			dataVO.setImpFlag((String)objMap.get("IMPFLAG"));
			dataVO.setLcycDesc((String)objMap.get("LCYCDESC"));
			dataVO.setModiyDate((String)objMap.get("MODIYDATE"));
			dataVO.setModName((String)objMap.get("MODNAME"));
			dataVO.setpCnt((String)objMap.get("PCNT"));
			dataVO.setPkCols((String)objMap.get("PKCOLS"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setRowCount((String)objMap.get("ROWCOUNT"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setTabChName((String)objMap.get("TABCHNAME"));
			dataVO.setTabName((String)objMap.get("TABNAME"));
			dataVO.setTabspaceName((String)objMap.get("TABSPACENAME"));
			dataVO.settSize((String)objMap.get("TSIZE"));
			dataVO.setZipDesc((String)objMap.get("ZIPDESC"));
			dataVO.setfStartDate((String)objMap.get("F_STARTDATE"));
			dataVO.setfEndDate((String)objMap.get("F_ENDDATE"));
			
			dataList.add(dataVO);
		}
		
		return dataList;
	}

	@Override
	public List<MetadataTableVO> getVerTableMeta(PageResults page) {
		List<Map<String, Object>> objList = metadataVerDao.getVerTableMeta(page);
		List<MetadataTableVO> dataList = new ArrayList<MetadataTableVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataTableVO dataVO = new MetadataTableVO();
			dataVO.setCrtDate((String)objMap.get("CRTDATE"));
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setFkCols((String)objMap.get("FKCOLS"));
			dataVO.setFkTableName((String)objMap.get("FKTABLENAME"));
			dataVO.setImpFlag((String)objMap.get("IMPFLAG"));
			dataVO.setLcycDesc((String)objMap.get("LCYCDESC"));
			dataVO.setModiyDate((String)objMap.get("MODIYDATE"));
			dataVO.setModName((String)objMap.get("MODNAME"));
			dataVO.setpCnt((String)objMap.get("PCNT"));
			dataVO.setPkCols((String)objMap.get("PKCOLS"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setRowCount((String)objMap.get("ROWCOUNT"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setTabChName((String)objMap.get("TABCHNAME"));
			dataVO.setTabName((String)objMap.get("TABNAME"));
			dataVO.setTabspaceName((String)objMap.get("TABSPACENAME"));
			dataVO.settSize((String)objMap.get("TSIZE"));
			dataVO.setZipDesc((String)objMap.get("ZIPDESC"));
			dataVO.setfStartDate((String)objMap.get("F_STARTDATE"));
			dataVO.setfEndDate((String)objMap.get("F_ENDDATE"));
			
			dataList.add(dataVO);
		}
		
		return dataList;
	}

	@Override
	public List<MetadataColumnVO> getProVerColumnMeta(PageResults page) {
		List<Map<String, Object>> objList = metadataVerDao.getProVerColumnMeta(page);
		List<MetadataColumnVO> dataList = new ArrayList<MetadataColumnVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataColumnVO dataVO = new MetadataColumnVO();
			dataVO.setCcolFlag((String)objMap.get("CCOLFLAG"));
			dataVO.setCodeTab((String)objMap.get("CODETAB"));
			dataVO.setColChName((String)objMap.get("COLCHNAME"));
			dataVO.setColName((String)objMap.get("COLNAME"));
			dataVO.setColSeq((String)objMap.get("COLSEQ"));
			dataVO.setColType((String)objMap.get("COLTYPE"));
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setIndxFlag((String)objMap.get("INDXFLAG"));
			dataVO.setModName((String)objMap.get("MODNAME"));
			dataVO.setNvlFlag((String)objMap.get("NVLFLAG"));
			dataVO.setPdkFlag((String)objMap.get("PDKFLAG"));
			dataVO.setPkFlag((String)objMap.get("PKFLAG"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setTabName((String)objMap.get("TABNAME"));
			dataVO.setfStartDate((String)objMap.get("F_STARTDATE"));
			dataVO.setfEndDate((String)objMap.get("F_ENDDATE"));
			dataList.add(dataVO);
		}
		return dataList;
	}

	@Override
	public List<MetadataColumnVO> getVerColumnMeta(PageResults page) {
		List<Map<String,Object>> objList = metadataVerDao.getVerColumnMeta(page);
		
		List<MetadataColumnVO> dataList = new ArrayList<MetadataColumnVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataColumnVO dataVO = new MetadataColumnVO();
			dataVO.setCcolFlag((String)objMap.get("CCOLFLAG"));
			dataVO.setCodeTab((String)objMap.get("CODETAB"));
			dataVO.setColChName((String)objMap.get("COLCHNAME"));
			dataVO.setColName((String)objMap.get("COLNAME"));
			dataVO.setColSeq((String)objMap.get("COLSEQ"));
			dataVO.setColType((String)objMap.get("COLTYPE"));
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setIndxFlag((String)objMap.get("INDXFLAG"));
			dataVO.setModName((String)objMap.get("MODNAME"));
			dataVO.setNvlFlag((String)objMap.get("NVLFLAG"));
			dataVO.setPdkFlag((String)objMap.get("PDKFLAG"));
			dataVO.setPkFlag((String)objMap.get("PKFLAG"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setTabName((String)objMap.get("TABNAME"));
			dataVO.setDsRef((String)objMap.get("DS_REF"));
			dataVO.setfStartDate((String)objMap.get("F_STARTDATE"));
			dataVO.setfEndDate((String)objMap.get("F_ENDDATE"));
			dataList.add(dataVO);
		}
		return dataList;
	}

	@Override
	public List<Map<String, Object>> getCompAllVersionColumn(PageResults page) {
		page.setParameter("column", page.getParameter("likeName"));
		List<Map<String,Object>> objList = metadataVerDao.getCompAllVersionColumnList(page);
		return objList;
	}

	@Override
	public List<Map<String, Object>> getComptableAutoComplete(PageResults page) {
		page.setParameter("table", page.getParameter("likeName"));
		List<Map<String,Object>> objList = metadataVerDao.getCompAllVersionTableList(page);
		return objList;
	}

	@Override
	public List<Map<String, Object>> getColCodeData(PageResults page) {
		return metadataVerDao.getColCodeData(page);
	}

	@Override
	public List<Map<String, Object>> getProVersionInfo(
			Map<String, String> params) {
		return metadataVerDao.getProVersionInfo(params);
	}

	@Override
	public int getProVerColumnMetaCount(PageResults page) {
		return metadataVerDao.getProVerColumnMetaCount(page);
	}

	@Override
	public int getProVerTableMetaCount(PageResults page) {
		return metadataVerDao.getProVerTableMetaCount(page);
	}

	@Override
	public int getProVerSchemaMetaCount(PageResults page) {
		return metadataVerDao.getProVerSchemaMetaCount(page);
	}

	@Override
	public int getProVerModuleMetaCount(PageResults page) {
		return metadataVerDao.getProVerModuleMetaCount(page);
	}

	@Override
	public List<Map<String, Object>> getFileIntData(PageResults page) {
		return metadataVerDao.getFileIntData(page);
	}
	
	@Override
	public List<Map<String, Object>> getProVersionDbInfo(
			Map<String, String> params) {
		return metadataVerDao.getProVersionDbInfo(params);
	}

	@Override
	public List<MetadataSchemaVO> getVerSchemaMetaSna(String dbId, String optId) {
        List<Map<String,Object>> objList = metadataVerDao.getVerSchemaMetaSna(dbId,optId);
		
		List<MetadataSchemaVO> dataList = new ArrayList<MetadataSchemaVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataSchemaVO dataVO = new MetadataSchemaVO();
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setDevloper((String)objMap.get("DEVLOPER"));
			dataVO.setObjCnt((String)objMap.get("OBJCNT"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setSchChName((String)objMap.get("SCHCHNAME"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setTabCnt((String)objMap.get("TABCNT"));
			dataList.add(dataVO);
		}
		
		return dataList;
	}

	@Override
	public List compSchema(List<MetadataSchemaVO> allVerSchemaList,
			List<MetadataSchemaVO> list1, List<MetadataSchemaVO> list2) {
		List list = new ArrayList();
		MetadataSchemaVO cmp1 = null;
		MetadataSchemaVO cmp2 = null;
		for (MetadataSchemaVO ms:allVerSchemaList) {
			String has = "0";
			String has2 = "0";
			String mod = "0";
			Map map = new HashMap();
			for(MetadataSchemaVO ms1:list1){
				if(ms.getSchname().equals(ms1.getSchname())){
					has = "1";
					cmp1 = ms1;
					break;
				}
			}
			for(MetadataSchemaVO ms2:list2){
				if(ms.getSchname().equals(ms2.getSchname())){
					has2 = "1";
					cmp2 = ms2;
					break;
				}
			}
			if(!has.equals(has2)){//比较不等,改变
				mod = "1";
			}else{
				if(has.equals("1")&&has.equals(has2)){//都存在，判断是否有改变
					if(!cmp1.equals(cmp2))
						mod = "1";
				}
			}
			map.put("has", has2);
			map.put("mod", mod);
			list.add(map);
		}
		
		return list;
	}

	@Override
	public List<MetadataSchemaVO> getCompAllVersionSchemaList(PageResults page) {
		List<Map<String,Object>> objList = metadataVerDao.getCompAllVersionSchemaList(page);
		
		List<MetadataSchemaVO> dataList = new ArrayList<MetadataSchemaVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataSchemaVO dataVO = new MetadataSchemaVO();
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setDevloper((String)objMap.get("DEVLOPER"));
			dataVO.setObjCnt((String)objMap.get("OBJCNT"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setSchChName((String)objMap.get("SCHCHNAME"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setTabCnt((String)objMap.get("TABCNT"));
			dataList.add(dataVO);
		}
		
		return dataList;
	}

	@Override
	public List<MetadataModuleVO> getCompAllVersionModuleList(PageResults page) {
	List<Map<String,Object>> objList = metadataVerDao.getCompAllVersionModuleList(page);
		
		List<MetadataModuleVO> dataList = new ArrayList<MetadataModuleVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataModuleVO dataVO = new MetadataModuleVO();
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setModName((String)objMap.get("MODNAME"));
			dataVO.setModChName((String)objMap.get("MODCHNAME"));
			dataVO.setModPtn((String)objMap.get("MODPTN"));
			dataVO.setSa((String)objMap.get("SA"));
			dataVO.setStatus((String)objMap.get("STATUS"));
			dataVO.setDeptcharger((String)objMap.get("DEPTCHARGER"));
			dataVO.setDept((String)objMap.get("DEPT"));
			dataVO.setDevloper((String)objMap.get("DEVLOPER"));
			dataVO.setObjCnt((String)objMap.get("OBJCNT"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setTabCnt((String)objMap.get("TABCNT"));
			dataVO.setUsedSize((String)objMap.get("USEDSIZE"));
			dataList.add(dataVO);
		}
		
		return dataList;
	}

	@Override
	public List<MetadataModuleVO> getVerModuleMetaSna(String dbId, String optId) {
		List<Map<String,Object>> objList = metadataVerDao.getVerModuleMetaSna(dbId,optId);
		
		List<MetadataModuleVO> dataList = new ArrayList<MetadataModuleVO>();
		
		for (Map<String, Object> objMap : objList) {
			MetadataModuleVO dataVO = new MetadataModuleVO();
			dataVO.setDbId((String)objMap.get("DB_CODE"));
			dataVO.setSchname((String)objMap.get("SCHNAME"));
			dataVO.setModName((String)objMap.get("MODNAME"));
			dataVO.setModChName((String)objMap.get("MODCHNAME"));
			dataVO.setModPtn((String)objMap.get("MODPTN"));
			dataVO.setSa((String)objMap.get("SA"));
			dataVO.setStatus((String)objMap.get("STATUS"));
			dataVO.setDeptcharger((String)objMap.get("DEPTCHARGER"));
			dataVO.setDept((String)objMap.get("DEPT"));
			dataVO.setDevloper((String)objMap.get("DEVLOPER"));
			dataVO.setObjCnt((String)objMap.get("OBJCNT"));
			dataVO.setRemark((String)objMap.get("REMARK"));
			dataVO.setTabCnt((String)objMap.get("TABCNT"));
			dataVO.setUsedSize((String)objMap.get("USEDSIZE"));
			dataList.add(dataVO);
		}
		
		return dataList;
	}

	@Override
	public List compModule(List<MetadataModuleVO> allList,
			List<MetadataModuleVO> list1, List<MetadataModuleVO> list2) {
		List list = new ArrayList();
		MetadataModuleVO cmp1 = null;
		MetadataModuleVO cmp2 = null;
		for (MetadataModuleVO ms:allList) {
			String has = "0";
			String has2 = "0";
			String mod = "0";
			Map map = new HashMap();
			for(MetadataModuleVO ms1:list1){
				if((ms.getModName().equals(ms1.getModName()))&&(ms.getSchname().equals(ms1.getSchname()))){
					has = "1";
					cmp1 = ms1;
					break;
				}
			}
			for(MetadataModuleVO ms2:list2){
				if((ms.getModName().equals(ms2.getModName()))&&(ms.getSchname().equals(ms2.getSchname()))){
					has2 = "1";
					cmp2 = ms2;
					break;
				}
			}
			if(!has.equals(has2)){//比较不等,改变
				mod = "1";
			}else{
				if(has.equals("1")&&has.equals(has2)){//都存在，判断是否有改变
					if(!cmp1.equals(cmp2))
						mod = "1";
				}
			}
			map.put("has", has2);
			map.put("mod", mod);
			list.add(map);
		}
		
		return list;
	}

	@Override
	public List getCompAllVersionTableList(PageResults page) {
		List<Map<String,Object>> objList = metadataVerDao.getCompAllVersionTableList(page);
		
		return objList;
	}

	@Override
	public List<Map<String, Object>> getCompAllVersionColumnList(PageResults page) {
		List<Map<String,Object>> objList = metadataVerDao.getCompAllVersionColumnList(page);
		
		return objList;
	}

	@Override
	public void insVerMetaByDecId(PageResults page) {
		List<Map<String,Object>> list = metadataVerDao.getTarOptDate(page.getParamStr("decId"));
		if(list.size() > 0){
			Map<String,Object> map = list.get(0);
			page.setParameter("tarOptId", map.get("ALT_VER_DATE_NO"));
			page.setParameter("curOptId", map.get("CUR_VER_DATE_NO"));
			//增加冲突处理方法，1更新回变更库状态为4,并且删除版本库中冲突的数据，然后再次更新拉链结束日期为当次投产日期记录的关联日期修改为20991231。
			metadataVerDao.updALtStsCt(page);
			metadataVerDao.delMetaALtCt(page);
			metadataVerDao.rollbakMetaALtCt(page);
			/*****************结束冲突处理******************/
			metadataVerDao.updVerMetaByDecId(page);
			metadataVerDao.insVerMetaByDecId(page);
		}
	}

	@Override
	public void initMetadata(String dbCode) {
		metadataVerDao.initMetadata(dbCode);
	}

}
