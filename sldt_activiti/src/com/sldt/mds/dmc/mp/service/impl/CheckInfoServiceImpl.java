package com.sldt.mds.dmc.mp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sldt.mds.dmc.mp.dao.CheckInfoDao;
import com.sldt.mds.dmc.mp.service.CheckInfoService;
import com.sldt.mds.dmc.mp.vo.CheckInfo;

@Service(value="checkInfoService")
public class CheckInfoServiceImpl implements CheckInfoService{
	
	@Resource(name="checkInfoDao")
	private CheckInfoDao checkInfoDao;

	@Override
	public List<CheckInfo> getCheckInfo(String whereSql) {
		return checkInfoDao.getCheckInfo(whereSql);
	}

	@Override
	public void save(CheckInfo checkInfo) {
		checkInfoDao.save(checkInfo);
	}

}
