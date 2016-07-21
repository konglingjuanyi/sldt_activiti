package com.sldt.mds.dmc.mp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sldt.mds.dmc.mp.dao.MyInitDeclareDao;
import com.sldt.mds.dmc.mp.service.MyInitDeclareService;
import com.sldt.mds.dmc.mp.vo.MyInitDeclareVO;

@Service(value="myInitDeclareService")
public class MyInitDeclareServiceImpl implements MyInitDeclareService{
	
	@Resource(name="myInitDeclareDao")
	private MyInitDeclareDao myInitDeclareDao;

	@Override
	public List<MyInitDeclareVO> getMyInitDeclare(String whereSql) {
		return myInitDeclareDao.getMyInitDeclare(whereSql);
	}

	@Override
	public void update(MyInitDeclareVO myInitDeclareVO) {
		myInitDeclareDao.updateInitDeclare(myInitDeclareVO);
	}

}
