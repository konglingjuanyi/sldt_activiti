package com.sldt.mds.dmc.mp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.sldt.mds.dmc.mp.dao.MetadataSysMgrDao;
import com.sldt.mds.dmc.mp.service.MetadataSysMgrService;
import com.sldt.mds.dmc.mp.vo.MetaStaUsInfoVo;
import com.sldt.mds.dmc.mp.vo.MetaUserVO;

@Service(value="metaSysMgrService")
public class MetadataSysMgrServiceImpl implements MetadataSysMgrService{

	private static Log log = LogFactory.getLog(MetadataSysMgrServiceImpl.class);
	
	@Resource(name="metaSysMgrDao")
	private MetadataSysMgrDao metaSysMgrDao;
	
	@Override
	public List<MetaUserVO> getMetaUserlistByRoleId(String roleId,
			String dealType) {
		return metaSysMgrDao.getMetaUserlistByRoleId(roleId, dealType);
	}

	@Override
	public MetaStaUsInfoVo getMetaStaUsInfoByRoleId(String roleId,
			String dealType) {
		return metaSysMgrDao.getMetaStaUsInfoByRoleId(roleId, dealType);
	}

}
