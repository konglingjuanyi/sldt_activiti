package com.sldt.mds.dmc.mp.dao;

import java.util.List;
import java.util.Map;

import com.sldt.framework.common.PageResults;

public interface FlowDao {
	
	public List getMetaSysInventory(String whereSql);
	
	public void updateAlterationAltsts(String altSts, String whereSql);
	public List getUsers(String whereSql);
	public List getUnitUser(String whereSql);
	public List<Map<String, Object>> getAltMetaItem(PageResults page);
	public List getAltertionImpact(PageResults page);
	public List getMetaIntFileDec(PageResults page);
	public List getMetaIntColCodeDec(PageResults page);
}
