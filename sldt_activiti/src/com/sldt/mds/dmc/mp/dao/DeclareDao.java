package com.sldt.mds.dmc.mp.dao;

import java.util.List;

import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.vo.DeclareVO;

public interface DeclareDao {
	
	public List<DeclareVO> getList(PageResults page, String decName, String actSts);

}
