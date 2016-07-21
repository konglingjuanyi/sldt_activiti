package com.sldt.mds.dmc.mp.service;

import java.util.List;

import com.sldt.mds.dmc.mp.vo.MetaStaUsInfoVo;
import com.sldt.mds.dmc.mp.vo.MetaUserVO;

public interface MetadataSysMgrService {
	
	public List<MetaUserVO> getMetaUserlistByRoleId(String roleId,String dealType);
	
	public MetaStaUsInfoVo getMetaStaUsInfoByRoleId(String roleId,String dealType);

}
