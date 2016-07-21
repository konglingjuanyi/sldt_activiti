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
import com.sldt.mds.dmc.mp.service.MetadataAnalyseService;
import com.sldt.mds.dmc.mp.util.ActionHelper;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;

@Controller
@RequestMapping("/analyse.do")
public class MetadataAnalyseController {
	
	private static Log log = LogFactory.getLog(MetadataAnalyseController.class);
	
	@Resource(name="metaAnalyService")
	private MetadataAnalyseService metaAnalyService;
	
	/**
	 * 获取当前影响分析值的instanceId
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=getInstanceId")
	@ResponseBody
	public Object getInstanceId(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String,Object>> obj = metaAnalyService.getInstanceId(page);
		listNullToString(obj);
		return ObjectToJSON.retResult(obj,null);
	}

	/**
	 * 将list的null转换为string
	 */
	public void listNullToString(List<Map<String,Object>> list) {
		for (Map<String, Object> obj : list) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
	}
}
