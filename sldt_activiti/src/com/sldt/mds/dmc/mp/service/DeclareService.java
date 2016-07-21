package com.sldt.mds.dmc.mp.service;

import java.util.List;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.vo.DeclareVO;

public interface DeclareService {
	
	public List<DeclareVO> getList(PageResults page, String decName, String actSts);

}
