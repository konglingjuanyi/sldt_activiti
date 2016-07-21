package com.sldt.mds.dmc.mp.service;

import java.util.List;

import com.sldt.mds.dmc.mp.vo.MyInitDeclareVO;

public interface MyInitDeclareService {
	
	/**
	 * 获取初始化申请信息
	 * @param whereSql
	 * @return
	 */
	public List<MyInitDeclareVO> getMyInitDeclare(String whereSql);
	/**
	 * 更新初始化申请信息
	 * @param myInitDeclareVO
	 * @param whereSql
	 */
	public void update(MyInitDeclareVO myInitDeclareVO);

}
