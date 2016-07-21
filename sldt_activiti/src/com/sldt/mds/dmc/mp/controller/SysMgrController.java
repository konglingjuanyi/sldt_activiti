package com.sldt.mds.dmc.mp.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.service.SysMgrService;
import com.sldt.mds.dmc.mp.util.ActionHelper;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;

@Controller
@RequestMapping("/sysMgr.do")
public class SysMgrController {
	
	private static Log log = LogFactory.getLog(SysMgrController.class);
	
	@Resource(name="sysmgrService")
	public SysMgrService sysmgrService;

	/**
	 * 友情接连
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=links")
	@ResponseBody
	public Object links(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String, Object>> list = sysmgrService.getLinks(page);
		for (Map<String, Object> obj:list){
			for(Iterator iterator = obj.keySet().iterator(); iterator.hasNext();){
				String key = (String) iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.retResult(list, null);
		
	}
}
