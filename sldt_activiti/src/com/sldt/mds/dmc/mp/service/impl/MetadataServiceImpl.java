package com.sldt.mds.dmc.mp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.dao.MetadataDao;
import com.sldt.mds.dmc.mp.service.MetadataService;
import com.sldt.mds.dmc.mp.vo.MetadataDatabaseVO;

@Service(value="metaService")
@Transactional(readOnly=true)
public class MetadataServiceImpl implements MetadataService{
	
	private static Log log = LogFactory.getLog(MetadataServiceImpl.class);
	
	@Resource(name="metadataDao")
	private MetadataDao metadataDao;
	
	@Override
	public List<MetadataDatabaseVO> getMetadataDatabase(PageResults page) {
		List<Map<String, Object>> objList = metadataDao.getMetadataDatabase(page);
		List<MetadataDatabaseVO> dbList = new ArrayList<MetadataDatabaseVO>();
		
		for(Map<String, Object> objMap : objList){
			MetadataDatabaseVO dbVO = new MetadataDatabaseVO();
			dbVO.setDbId((String)objMap.get("DB_CODE"));
			dbVO.setDbChName((String)objMap.get("DBCHNAME"));
			dbVO.setDba((String)objMap.get("DBA"));
			dbVO.setDept((String)objMap.get("DEPT"));
			dbVO.setDevloper((String)objMap.get("DEVLOPER"));
			dbVO.setImageUrl((String)objMap.get("IMGURL"));
			dbVO.setMaintainer((String)objMap.get("MAINTAINER"));
			dbVO.setObjCnt((String)objMap.get("OBJCNT"));
			dbVO.setProMFac((String)objMap.get("PRO_M_FAC"));
			dbVO.setRemark((String)objMap.get("REMARK"));
			dbVO.setTabCnt((String)objMap.get("TABCNT"));
			dbVO.setTarget((String)objMap.get("_Target"));
			dbVO.setTotalSize((String)objMap.get("TOTALSIZE"));
			dbVO.setUsedSize((String)objMap.get("USEDSIZE"));
			dbList.add(dbVO);
		}
		return dbList;
	}

	@Override
	public List<Map<String, Object>> getProMetaByDbId(PageResults page) {
		
		return metadataDao.getProMetaByDbId(page);
	}

	@Override
	public List<Map<String, Object>> getUatMetaByDbId(PageResults page) {
		
		return metadataDao.getUatMetaByDbId(page);
	}

	@Override
	public List<Map<String, Object>> getColumnAutoComplete(PageResults page) {
		List<Map<String,Object>> objList = metadataDao.getColumnAutoComplete(page);
		return objList;
	}

	@Override
	public List<Map<String, Object>> getTableAutoComplete(PageResults page) {
		List<Map<String, Object>> objList = metadataDao.getTableAutoComplete(page);
		return objList;
	}

}
