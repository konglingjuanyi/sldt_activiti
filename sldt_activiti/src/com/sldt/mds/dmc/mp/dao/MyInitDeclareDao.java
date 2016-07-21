package com.sldt.mds.dmc.mp.dao;

import java.util.List;

import com.sldt.mds.dmc.mp.vo.MyInitDeclareVO;

public interface MyInitDeclareDao {
	
	public void saveInitDeclare(MyInitDeclareVO myInitDeclareVO);
	public void updateInitDeclare(MyInitDeclareVO myInitDeclareVO);
	public List<MyInitDeclareVO> getMyInitDeclare(String whereSql);

}
