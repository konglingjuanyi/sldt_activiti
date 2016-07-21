package com.sldt.mds.dmc.mp.dao;

import java.util.List;

import com.sldt.mds.dmc.mp.vo.CheckInfo;

public interface CheckInfoDao {
	
	public void save(CheckInfo checkInfo);
	
	public List<CheckInfo> getCheckInfo(String whereSql);

}
