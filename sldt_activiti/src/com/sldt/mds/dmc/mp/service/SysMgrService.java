package com.sldt.mds.dmc.mp.service;

import java.util.List;
import java.util.Map;

import com.sldt.framework.common.PageResults;

public interface SysMgrService {

	public List<Map<String, Object>> getLinks(PageResults page);
	/**
	 * 根据参数获取系统参数表中的数据对象
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getSysParams(PageResults page);
}
