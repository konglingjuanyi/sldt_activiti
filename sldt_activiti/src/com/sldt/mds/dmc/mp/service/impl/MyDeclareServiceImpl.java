package com.sldt.mds.dmc.mp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sldt.mds.dmc.mp.dao.MyDeclareDao;
import com.sldt.mds.dmc.mp.service.MyDeclareService;
import com.sldt.mds.dmc.mp.vo.MyDeclareVO;

@Service(value="myDeclareService")
public class MyDeclareServiceImpl implements MyDeclareService{
	
	@Resource(name="myDeclareDao")
	private MyDeclareDao myDeclareDao;

	@Override
	public List<MyDeclareVO> getMyDeclare(String whereSql) {
		return myDeclareDao.getMyDeclare(whereSql);
	}

	@Override
	public void update(MyDeclareVO myDeclareVO, String whereSql) {
		myDeclareDao.update(myDeclareVO, whereSql);
	}

	@Override
	public List getDecDetailed(String whereSql) {
		return myDeclareDao.getDecDetailed(whereSql);
	}

	@Override
	public void saveDecDetailed(String decId, String altId) {
		myDeclareDao.saveDecDetailed(decId, altId);
		
	}

}
