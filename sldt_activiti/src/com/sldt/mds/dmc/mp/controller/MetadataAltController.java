package com.sldt.mds.dmc.mp.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.cas.dmp.ModuleUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.service.MetadataAltService;
import com.sldt.mds.dmc.mp.service.MetadataVerService;
import com.sldt.mds.dmc.mp.service.SysMgrService;
import com.sldt.mds.dmc.mp.util.ActionHelper;
import com.sldt.mds.dmc.mp.util.DateUtil;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;
import com.sldt.mds.dmc.mp.vo.MetadataDatabaseVO;

@Controller
@RequestMapping("/metadataAlt.do")
public class MetadataAltController {

	@Resource(name="metaVerService")
	public MetadataVerService metaVerService;
	
	@Resource(name="metaAltService")
	public MetadataAltService metaAltService;
	
	@Resource(name="sysmgrService")
	public SysMgrService sysmgrService;
	
	private static Log log = LogFactory.getLog(MetadataAltController.class);
	
	/**
	 * 获取当前用户所有系统的变更申报及开发版本、远期版本数据
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=altMetaItem")
	@ResponseBody
	public Object altMetaItem(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("isAdmin", false);
		page.setParameter("endDate", DateUtil.getYmdDate());
		
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		List<MetadataDatabaseVO> list = null;
		if(StringUtils.isNotEmpty(userId)){
			list = metaVerService.getMyVersionMeta(page);
			for(MetadataDatabaseVO obj : list){
				page.setParameter("dbId", obj.getDbId());
				Map<String, Object> altCurrMetaVer = metaVerService.getCurrVerMap(page);
				page.setParameter("currVerDate", altCurrMetaVer.get("ALT_VER_DATE_NO"));
				
				List<Map<String, Object>> altMetaList = metaVerService.getForAndDeveVerList(page);
				List<Map<String, Object>> altSumList = metaAltService.getDecAltSumByDbId(page);
				
				obj.setCurVerOptId((String)altCurrMetaVer.get("ALT_VER_DATE_NO"));
				//变更统计
				for (int i = 0; i < altSumList.size(); i++) {
					Map<String,Object> altMap = altSumList.get(i);
					String altSts = (String)altMap.get("ALT_STS");
					//变更已生效
					if("0".equals(altSts)){
						obj.setValid(((BigInteger)altMap.get("ALTSUM")).toString());
					}else if("1".equals(altSts)){//申报中
						obj.setDec(((BigInteger)altMap.get("ALTSUM")).toString());
					}else if("2".equals(altSts)){//未申报
						obj.setNotDec(((BigInteger)altMap.get("ALTSUM")).toString());
					}else if("3".equals(altSts)){//未确认
						obj.setUnc(((BigInteger)altMap.get("ALTSUM")).toString());
					}
				}
				//变更日历
				for (int i = 0 ; i < altMetaList.size(); i ++) {
					Map<String,Object> altMap = altMetaList.get(i);
					
					if(i==3){
						break;
					}
					
					if(i==0){
						obj.setDevelopAltHtml("<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+obj.getDbId()+"','"+obj.getDbChName()+"','"+altMap.get("ALT_VER_DATE_NO")+"','')\">"+altMap.get("ALT_VER_DATE_NO")+"</a>");
						obj.setDevelopAlt((String)altMap.get("ALT_VER_DATE_NO"));
					}else{
						if(obj.getForwardAlt()!=null){
							obj.setForwardAlt(obj.getForwardAlt()+"&nbsp;&nbsp;<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+obj.getDbId()+"','"+obj.getDbChName()+"','"+altMap.get("ALT_VER_DATE_NO")+"','')\">"+altMap.get("ALT_VER_DATE_NO")+"</a>");
						}else{
							obj.setForwardAlt("&nbsp;<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+obj.getDbId()+"','"+obj.getDbChName()+"','"+altMap.get("ALT_VER_DATE_NO")+"','')\">"+altMap.get("ALT_VER_DATE_NO")+"</a>");
						}
					}
				}
			}
		}
		
		return ObjectToJSON.retResult(list,null);
		
	}
	
	/**
	 * 获取系统的变更历史数据，包含变更的明细统计
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=altSysMetaHis")
	@ResponseBody
	public Object altSysMetaHis(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String, Object>> altMeta = metaAltService.getAltSumByDbId(page);
		List<Map<String, Object>> yarList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> obj :altMeta){
			for(Iterator iterator = obj.keySet().iterator(); iterator.hasNext();){
				String key = (String) iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
			boolean yarFlag = false;
			String optYar = (String) obj.get("OPT_YAR");
			if(yarList.size()>0){
				//从年份的list集合中获取月份数据对象
				for(Map<String, Object> yarMap:yarList){
					String _optYar = (String) yarMap.get("OPT_YAR");
					if(optYar.equals(_optYar)){
						String altVerDate = (String)obj.get("ALT_VER_DATE_NO");
						List<Map<String,Object>> monthList = (List<Map<String,Object>>)yarMap.get("LIST");
						boolean flag = false;
						for (Map<String, Object> map : monthList) {
							String _altVerDate = (String)map.get("ALT_VER_DATE_NO");
							//如果当前的年份中存在一样的月份，则添加变更描述
							if(altVerDate.equals(_altVerDate)){
								flag = true;
								map.put("ALT_DESC", map.get("ALT_DESC")+"<br/>"+"元数据类型【"+obj.get("ITEM_VALUE")+"】产生"+obj.get("ALTSUM")+"个变更。");
								break;
							}
						}
						//如果不存在一样的月份，则创建一个新的月份
						if(!flag){
							Map<String,Object> monthMap = new HashMap<String, Object>();
							monthMap.put("MMDD", obj.get("OPT_MONTH")+"."+obj.get("OPT_DAY"));
							monthMap.put("ALT_VER_DATE_NO", obj.get("ALT_VER_DATE_NO"));
							monthMap.put("VER_DESC", obj.get("VER_DESC"));
							monthMap.put("OPT_ID", obj.get("ALT_VER_DATE_NO"));
							monthMap.put("ALT_DESC", "元数据类型【"+obj.get("ITEM_VALUE")+"】产生"+obj.get("ALTSUM")+"个变更。");
							monthList.add(monthMap);
						}
						yarFlag = true;
						break;
					}
				}
			}
			if(!yarFlag){
				Map<String,Object> yarMap = new HashMap<String, Object>();
				yarMap.put("OPT_YAR", obj.get("OPT_YAR"));
				List<Map<String,Object>> monthList = new ArrayList<Map<String,Object>>();
				Map<String,Object> monthMap = new HashMap<String, Object>();
				monthMap.put("MMDD", obj.get("OPT_MONTH")+"."+obj.get("OPT_DAY"));
				monthMap.put("ALT_VER_DATE_NO", obj.get("ALT_VER_DATE_NO"));
				monthMap.put("VER_DESC", obj.get("VER_DESC"));
				monthMap.put("OPT_ID", obj.get("ALT_VER_DATE_NO"));
				monthMap.put("ALT_DESC", "元数据类型【"+obj.get("ITEM_VALUE")+"】产生"+obj.get("ALTSUM")+"个变更。");
				monthList.add(monthMap);
				yarMap.put("LIST", monthList);
				yarList.add(yarMap);
		} 
     }
		return ObjectToJSON.retResult(yarList, null);
		
	}
	/**
	 * 获取当前用户所管理的元数据对象
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=altMetaDeFindWhere")
	@ResponseBody
	public Object altMetaDeFindWhere(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		//获取当前最新版本信息
		Map<String, Object> altCurrMetaVer = metaVerService.getCurrVerMap(page);
		page.setParameter("currVerDate", altCurrMetaVer.get("ALT_VER_DATE_NO"));
		//获取开发中及远期版本
		List<Map<String, Object>> altMetaList = metaVerService.getForAndDeveVerList(page);
		//获取历史
		List<Map<String, Object>> hisMetaList = metaVerService.getHisVerList(page);
		//获取目标版本批次数据
		List<Map<String, Object>> altBatchList = metaVerService.getCurrAltBatch(page);
		
		//获取系统参数表中的模型分类数据
		page.setParameter("parCode", "StatisticMetadata");
		List<Map<String, Object>> classiferList = sysmgrService.getSysParams(page);
		
		//获取参数表中的变更分类数据
		page.setParameter("parCode", "ALTTYPE");
		List<Map<String,Object>> altTypeList = sysmgrService.getSysParams(page);
		
		//获取参数表中的变更方式数据
		page.setParameter("parCode", "ALT_MODE");
		List<Map<String,Object>> altModeList = sysmgrService.getSysParams(page);
		
		Map<String,Object> obj = new HashMap<String, Object>();
		//当前版本
		obj.put("curVerOptId",  altCurrMetaVer.get("ALT_VER_DATE_NO"));
		obj.put("lastAltVer", hisMetaList);
		obj.put("altBatch", altBatchList);
		obj.put("classifer", classiferList);
		obj.put("altType", altTypeList);
		obj.put("altMode", altModeList);
		
		//变更日历
		for (int i = 0 ; i < altMetaList.size(); i ++) {
			Map<String,Object> altMap = altMetaList.get(i);
			if(i==3){
				break;
			}
			if(i==0){
				obj.put("developAltHtml", "<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+page.getParamStr("dbId")+"','"+page.getParamStr("dbName")+"','"+altMap.get("ALT_VER_DATE_NO")+"','')\">"+altMap.get("ALT_VER_DATE_NO")+"</a>");
				obj.put("developAlt", altMap.get("ALT_VER_DATE_NO"));
			}else{
				if(obj.get("forwardAlt")!=null){
					obj.put("forwardAlt", obj.get("forwardAlt")+"&nbsp;&nbsp;<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+page.getParamStr("dbId")+"','"+page.getParamStr("dbName")+"','"+altMap.get("ALT_VER_DATE_NO")+"','')\">"+altMap.get("ALT_VER_DATE_NO")+"</a>");
				}else{
					obj.put("forwardAlt", "&nbsp;<a style=\"cursor: pointer;\" onclick=\"reqAltMetaDet('"+page.getParamStr("dbId")+"','"+page.getParamStr("dbName")+"','"+altMap.get("ALT_VER_DATE_NO")+"','')\">"+altMap.get("ALT_VER_DATE_NO")+"</a>");
				}
			}
		}
		
		result.add(obj);
		
		return ObjectToJSON.retResult(result,null);
	}
	/**
	 * 获取元数据变更明细数据
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=altMetaDetailItem")
	@ResponseBody
	public Object altMetaDetailItem(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String, Object>> list = metaAltService.getAltMetaItem(page);
		for (Map<String, Object> obj : list) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.returnResult(list,page);		
	}
	
	/**
	 * 删除变更元数据
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=delAltMetaDetail")
	@ResponseBody
	public Object delAltMetaDetail(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		metaAltService.delAltMetaDetail(page);
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("msg", "删除变更元数据成功！");
		return ObjectToJSON.retResult(obj,null);
	}
	
	/**
	 * 确认变更元数据
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=updAltMetaDetail")
	@ResponseBody
	public Object updAltMetaDetail(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("sessionId", UUID.randomUUID().randomUUID().toString().replaceAll("-", ""));
		metaAltService.updAltMetaSts(page);
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("msg", "确认元数据变更成功！");
		return ObjectToJSON.retResult(obj,null);
	}
	
	/**
	 * 获取元数据变更明细数据
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=showAltMetaDetail")
	@ResponseBody
	public Object showAltMetaDetail(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		//获取参数表中的变更方式数据
		page.setParameter("parCode", "ALT_CLASS_TABLE_NAME");
		List<Map<String,Object>> altClassTableNames = sysmgrService.getSysParams(page);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(altClassTableNames.size()>0){
			String altClassTableName = (String)altClassTableNames.get(0).get("ITEM_VALUE");
			page.setParameter("parCode", altClassTableName);
			list = metaAltService.showAltMetaDetail(page);
			for (Map<String, Object> obj : list) {
				for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
					String key = (String)iterator.next();
					if(obj.get(key)==null){
						obj.put(key, "");
					}
				}
			}
		}
		return ObjectToJSON.retResult(list,null);
	}
}
