package com.sldt.mds.dmc.mp.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.dao.SysMgrDao;
import com.sldt.mds.dmc.mp.service.SysMgrService;

@Service(value="sysmgrService")
public class SysMgrServiceImpl implements SysMgrService{
	
	private static Log log = LogFactory.getLog(SysMgrServiceImpl.class);
	
	@Resource(name="sysmgrDao")
	private SysMgrDao sysmgrDao;

	@Override
	public List<Map<String, Object>> getLinks(PageResults page) {
		return sysmgrDao.getLinks(page);
	}

	@Override
	public List<Map<String, Object>> getSysParams(PageResults page) {
		List<Map<String, Object>> list = sysmgrDao.getSysParams(page);
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

}
