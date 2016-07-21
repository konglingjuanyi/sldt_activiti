package com.sldt.mds.dmc.mp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.dao.DeclareDao;
import com.sldt.mds.dmc.mp.service.DeclareService;
import com.sldt.mds.dmc.mp.vo.DeclareVO;

@Service(value="declareService")
public class DeclareServiceImpl implements DeclareService{
	
	@Resource(name="declareDao")
	private DeclareDao declareDao;

	@Override
	public List<DeclareVO> getList(PageResults page, String decName, String actSts) {
		return declareDao.getList(page, decName, actSts);
	}

}
