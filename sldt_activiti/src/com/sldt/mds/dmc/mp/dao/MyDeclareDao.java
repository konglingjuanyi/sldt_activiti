package com.sldt.mds.dmc.mp.dao;

import java.util.List;

import com.sldt.mds.dmc.mp.vo.MyDeclareVO;

public interface MyDeclareDao {
	
	public List<MyDeclareVO> getMyDeclare(String whereSql);
	
	public void update(MyDeclareVO myDeclareVO, String whereSql);
	
	public List getDecDetailed(String whereSql);
	
	public void saveDeclare(MyDeclareVO myDeclareVO);
	
	public void saveDecDetailed(String decId, String altId);
}
