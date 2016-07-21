package com.sldt.mds.dmc.mp.service;

import java.util.List;

import com.sldt.mds.dmc.mp.vo.CheckInfo;

public interface CheckInfoService {
	
	/**
	 * 保存审核信息
	 * @param checkInfo
	 */
	public void save(CheckInfo checkInfo);
	
	/**
	 * 获取审核信息
	 * @param whereSql
	 * @return
	 */
	public List<CheckInfo> getCheckInfo(String whereSql);

}
