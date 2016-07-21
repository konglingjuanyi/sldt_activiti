package com.sldt.mds.dmc.mp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.cas.dmp.ModuleUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.service.MetadataVerService;
import com.sldt.mds.dmc.mp.util.ActionHelper;
import com.sldt.mds.dmc.mp.util.DateUtil;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;
import com.sldt.mds.dmc.mp.vo.MetadataColumnVO;
import com.sldt.mds.dmc.mp.vo.MetadataDatabaseVO;
import com.sldt.mds.dmc.mp.vo.MetadataModuleVO;
import com.sldt.mds.dmc.mp.vo.MetadataSchemaVO;
import com.sldt.mds.dmc.mp.vo.MetadataTableVO;

@Controller
@RequestMapping("/metadataVer.do")
public class MetadataVerController {
	private static Log log = LogFactory.getLog(MetadataVerController.class);
	
	@Resource(name="metaVerService")
	public MetadataVerService metaVerService;
	
	/**
	 * 获取我的版本
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=myVerMetaData")
	@ResponseBody
	public Object myVerMetaData(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
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
			page.setParameter("userId",userId);
			page.setParameter("isAdmin", false);
			list = metaVerService.getMyVersionMeta(page);
			for(MetadataDatabaseVO obj : list) {
				obj.setImageUrl("imgs/card/gf_01.jpg");
				page.setParameter("dbId", obj.getDbId());
				Map<String, Object> currMetaVer = metaVerService.getCurrVerMap(page);
				obj.setCurVerOptId((String)currMetaVer.get("ALT_VER_DATE_NO"));
				obj.setLastVerOptId((String)currMetaVer.get("CUR_VER_DATE_NO"));
				obj.setVerDesc((String)currMetaVer.get("VER_DESC"));
				obj.setVerName((String)currMetaVer.get("VER_NAME"));
			}
		}
		
		return ObjectToJSON.returnResult(list,null);
		
	}
	/**
	 * 获取当前日期的投产时间点
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=newVerMetaData")
	@ResponseBody
	public Object newVerMetaData(HttpServletRequest request){
		Map<String,String> params = new HashMap<String, String>();
		params.put("endDate", DateUtil.getYmdDate());
		List<Map<String, Object>> list = metaVerService.getNewVersionMeta(params);
		PageResults page = ControllerHelper.buildPage(request);
		for (Map<String, Object> obj : list) {
			page.setParameter("dbId", obj.get("DB_CODE"));
			Map<String,Object> altCurrMetaVer = metaVerService.getCurrVerMap(page);
			obj.put("curVerOptId", altCurrMetaVer.get("ALT_VER_DATE_NO"));
			obj.put("VER_DESC", altCurrMetaVer.get("VER_DESC"));
			obj.put("VER_NAME", altCurrMetaVer.get("VER_NAME"));
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
	 * 获取当前日期的投产时间点
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=getMetaVerDate")
	@ResponseBody
	public Object getMetaVerDate(HttpServletRequest request){
		String versionDate = request.getParameter("versionDate");
		Map<String,String> params = new HashMap<String, String>();
		if(versionDate!=null){
			String[] datas = versionDate.split("/");
			params.put("year", datas[1]);
			params.put("month", datas[0]);
		}
		if(request.getParameter("curVerOptId")==null){
			params.put("curVerOptId", DateUtil.getYmdDate());
		}else{
			params.put("curVerOptId", request.getParameter("curVerOptId"));
		}
		List<Map<String, Object>> list = metaVerService.getMetaVerDate(params);
		return ObjectToJSON.returnResult(list,null);
		
	}
	
	@RequestMapping(params="method=verMetaSchemaItem")
	@ResponseBody
	public Object verMetaSchemaItem(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		String optId = request.getParameter("optId");
		page.setParameter("optId", request.getParameter("optId"));
		String isnews = request.getParameter("isnews");
		if("true".equals(isnews)){
			page.setParameter("endDate", "20991230");
		}else{
			page.setParameter("endDate",DateUtil.getYmdDate());
		}
		List<MetadataSchemaVO> list = new ArrayList<MetadataSchemaVO>();
		if(optId.contains("PRO") || optId.contains("UAT")){
			list = metaVerService.getProVerSchemaMeta(page);
		}else{
			list = metaVerService.getVerSchemaMeta(page);
		}
		return ObjectToJSON.returnResult(list,null);
	}
	
	@RequestMapping(params="method=verMetaModuleItem")
	@ResponseBody
	public Object verMetaModuleItem(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("optId", request.getParameter("optId"));
		String isnews = request.getParameter("isnews");
		if("true".equals(isnews)){
			page.setParameter("endDate", "20991230");
		}else{
			page.setParameter("endDate",DateUtil.getYmdDate());
		}
		String optId = request.getParameter("optId");
		List<MetadataModuleVO> list = new ArrayList<MetadataModuleVO>();
		if(optId.contains("PRO") || optId.contains("UAT")){
			list = metaVerService.getProVerModuleMeta(page);
		}else{
			list = metaVerService.getVerModuleMeta(page);
		}
		return ObjectToJSON.returnResult(list,page);
	}
	
	@RequestMapping(params="method=verMetaTableItem")
	@ResponseBody
	public Object verMetaTableItem(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		List<MetadataTableVO> list = new ArrayList<MetadataTableVO>();
		List<MetadataModuleVO> moduleList = new ArrayList<MetadataModuleVO>();
		List<MetadataSchemaVO> schemaList= new ArrayList<MetadataSchemaVO>();
		page.setParameter("optId", request.getParameter("optId"));
		String isnews = request.getParameter("isnews");
		if("true".equals(isnews)){
			page.setParameter("endDate", "20991230");
		}else{
			page.setParameter("endDate",DateUtil.getYmdDate());
		}
		String optId = request.getParameter("optId");
		if(optId.contains("PRO") || optId.contains("UAT")){
			list = metaVerService.getProVerTableMeta(page);
			moduleList = metaVerService.getProVerModuleMeta(page);
			schemaList = metaVerService.getProVerSchemaMeta(page);
		}else{
			int tabCount = metaVerService.getVerTableMetaCount(page);
			page.setTotalRecs(tabCount);
			list = metaVerService.getVerTableMeta(page);
			moduleList = metaVerService.getVerModuleMeta(page);
			schemaList = metaVerService.getVerSchemaMeta(page);
		}
		Map<String,Object> objMap = new HashMap<String, Object>();
		objMap.put("tabData", list);
		objMap.put("moduleData", moduleList);
		objMap.put("schemaData", schemaList);
		return ObjectToJSON.returnResult(objMap,page);
	}
	
	@RequestMapping(params="method=verMetaColumnItem")
	@ResponseBody
	public Object verMetaColumnItem(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("optId", request.getParameter("optId"));
		String isnews = request.getParameter("isnews");
		if("true".equals(isnews)){
			page.setParameter("endDate", "20991230");
		}else{
			page.setParameter("endDate", DateUtil.getYmdDate());
		}
		List<MetadataColumnVO> list = new ArrayList<MetadataColumnVO>();
		List<MetadataModuleVO> moduleList = new ArrayList<MetadataModuleVO>();
		List<MetadataSchemaVO> schemaList = new ArrayList<MetadataSchemaVO>();
		String optId = request.getParameter("optId");
		if(optId.contains("PRO") || optId.contains("UAT")){
			list = metaVerService.getProVerColumnMeta(page);
			moduleList = metaVerService.getProVerModuleMeta(page);
			schemaList = metaVerService.getProVerSchemaMeta(page);
		}else{
			int colCount = metaVerService.getVerColumnMetaCount(page);
			page.setTotalRecs(colCount);
			list = metaVerService.getVerColumnMeta(page);
			moduleList = metaVerService.getVerModuleMeta(page);
			schemaList = metaVerService.getVerSchemaMeta(page);
		}
		
		Map<String,Object> objMap = new HashMap<String, Object>();
		objMap.put("colData", list);
		objMap.put("moduleData", moduleList);
		objMap.put("schemaData", schemaList);
		
		return ObjectToJSON.returnResult(objMap,page);
	}
	
	/**
	 * 根据用户输入模糊匹配值
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=compcolumnAutoComplete")
	@ResponseBody
	public Object compcolumnAutoComplete(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		String endDate = DateUtil.getYmdDate();
		String dbId = page.getParamStr("dbId");
		//比对的分页全部
		List<Map<String,Object>> allList = metaVerService.getCompAllVersionColumn(page);
		listNullToString(allList);
		return ObjectToJSON.returnResult(allList,page);
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
	
	/**
	 * 根据用户输入模糊匹配值
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=comptableAutoComplete")
	@ResponseBody
	public Object comptableAutoComplete(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		String endDate = DateUtil.getYmdDate();
		String dbId = page.getParamStr("dbId");
		//比对的分页全部
		List<Map<String,Object>> allList = metaVerService.getComptableAutoComplete(page);
		listNullToString(allList);
		return ObjectToJSON.returnResult(allList,page);
	}
	
	/**
	 * 获取字段落地代码
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=verColCodeItem")
	@ResponseBody
	public Object verColCodeItem(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate",DateUtil.getYmdDate());
		List<MetadataModuleVO> moduleList = new ArrayList<MetadataModuleVO>();
		List<MetadataSchemaVO> schemaList = new ArrayList<MetadataSchemaVO>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list = metaVerService.getColCodeData(page);
		moduleList = metaVerService.getVerModuleMeta(page);
		schemaList = metaVerService.getVerSchemaMeta(page);
		listNullToString(list);
		Map<String,Object> objMap = new HashMap<String, Object>();
		objMap.put("codeData", list);
		objMap.put("moduleData", moduleList);
		objMap.put("schemaData", schemaList);
		return ObjectToJSON.returnResult(objMap,page);
	}
	
	/**
	 * 获取当前用户所管理的元数据对象
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=verMetaItem")
	@ResponseBody
	public Object verMetaItem(HttpServletRequest request){
		Map<String,String> params = new HashMap<String, String>();
		params.put("dbId", request.getParameter("dbId"));
		params.put("optId", request.getParameter("optId"));
		String optId = request.getParameter("optId");
		String verType = request.getParameter("verType");
		String beforeDate = DateUtil.getBeforeDate();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		boolean isEdit = true;//是否可编辑
		Map map = new HashMap();
		if("history".equals(verType)){
			isEdit = false;//如果是历史版本不可编辑
		}
		if(optId.contains("PRO")||optId.contains("UAT")){
			isEdit = false;//如果是生产或者快照不可编辑
			list = metaVerService.getProVersionInfo(params);
			listNullToString(list);
			for (Map<String, Object> obj : list) {
				obj.put("curVerOptId", "");
			}
		}else{
			if(request.getParameter("dbId")!=null){
				list = metaVerService.getNewVersionMeta(params);
				PageResults page = ControllerHelper.buildPage(request);
				Map<String,Object> altCurrMetaVer = metaVerService.getCurrVerMap(page);
				for (Map<String, Object> obj : list) {
					obj.put("curVerOptId", altCurrMetaVer.get("ALT_VER_DATE_NO"));
					obj.put("VER_DESC", altCurrMetaVer.get("VER_DESC"));
					obj.put("VER_NAME", altCurrMetaVer.get("VER_NAME"));
					isEdit = true;
				/*	if(userVO.isAdmin()){
						isEdit = true;
					}else{
						if(obj.get("DB_USER")!=null){
							if(userVO.getUsercode().toUpperCase().equals(((String)obj.get("DB_USER")).toUpperCase())){
								isEdit = true;
							}else {
								isEdit = false;
							}
						}else{
							isEdit = false;
						}
					}*/
					for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
						String key = (String)iterator.next();
						if(obj.get(key)==null){
							obj.put(key, "");
						}
					}
				}
			}
		}
		map.put("list", list);
		map.put("isEdit", isEdit);
		return ObjectToJSON.returnResult(map,null);
	}
	
	@RequestMapping(params="method=curSysVerCount")
	@ResponseBody
	public Object curSysVerCount(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		String optId = request.getParameter("optId");
		page.setParameter("optId", request.getParameter("optId"));
		page.setParameter("endDate",DateUtil.getYmdDate());
		List<Map<String,Object>> list  = new ArrayList<Map<String,Object>>();
		int colCount = 0;
		int tabCount = 0;
		int schCount = 0;
		int modCount = 0;
		if(optId.contains("PRO")||optId.contains("UAT")){
			colCount = metaVerService.getProVerColumnMetaCount(page);
			tabCount = metaVerService.getProVerTableMetaCount(page);
			schCount = metaVerService.getProVerSchemaMetaCount(page);
			modCount = metaVerService.getProVerModuleMetaCount(page);
		}else {
			colCount = metaVerService.getVerColumnMetaCount(page);
			tabCount = metaVerService.getVerTableMetaCount(page);
			schCount = metaVerService.getVerSchemaMetaCount(page);
			modCount = metaVerService.getVerModuleMetaCount(page);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("COLCOUNT", colCount);
		map.put("TABCOUNT", tabCount);
		map.put("SCHCOUNT", schCount);
		map.put("MODCOUNT", modCount);
		list.add(map);
		return ObjectToJSON.returnResult(list,page);
	}
	
	/**
	 * 获取字段落地代码
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=fileIntList")
	@ResponseBody
	public Object fileIntList(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		List<Map<String,Object>> list = metaVerService.getFileIntData(page);
		for (Map<String, Object> obj : list) {
			String type = (String)obj.get("C_TYPE");
			if("1".equals(type)){
				obj.put("C_TYPE", "程序依赖");
			}else if("0".equals(type)){
				obj.put("C_TYPE", "数据表依赖");
			}
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		JSONObject reusltObj=new JSONObject();
		page.refreshPageNum();
		reusltObj.put("page", page.getStartIndex()); 
	    reusltObj.put("total", page.getTotalPages());
		reusltObj.put("rows", list); 
	    reusltObj.put("records", page.getTotalRecs());
		return reusltObj;
	}
	
	/**
	 * 版本比对【数据库】
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=metadataVerCompDataBase")
	@ResponseBody
	public Object metadataVerCompDataBase(HttpServletRequest request){
		Map<String,String> params = new HashMap<String, String>();
		params.put("dbId", request.getParameter("dbId"));
		String col_1 = request.getParameter("Col_1");
		String col_2 = request.getParameter("Col_2");
		String col_3 = request.getParameter("Col_3");
		String col_4 = request.getParameter("Col_4");
		String col_5 = request.getParameter("Col_5");
		//第一列	
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
		//2列
		List<Map<String,Object>> list2 = new ArrayList<Map<String,Object>>();
		//3列
		List<Map<String,Object>> list3 = new ArrayList<Map<String,Object>>();
		//第四列
		List<Map<String,Object>> list4 = new ArrayList<Map<String,Object>>();
		//第五列
		List<Map<String,Object>> list5 = new ArrayList<Map<String,Object>>();
		Map map = new HashMap();
		if(request.getParameter("dbId")!=null){
			if(StringUtils.isNotBlank(col_1)){
				params.put("optId", col_1);//第一列
				if(col_1.contains("PRO")||col_1.contains("UAT")){
					list1 = metaVerService.getProVersionDbInfo(params);
				}else{
					list1 = metaVerService.getNewVersionMeta(params);
				}
				listNullToString(list1);
			}
			if(StringUtils.isNotBlank(col_2)){
				params.put("optId", col_2);//第一列
				if(col_2.contains("PRO")||col_2.contains("UAT")){
					list2 = metaVerService.getProVersionDbInfo(params);
				}else{
					list2 = metaVerService.getNewVersionMeta(params);
				}
				listNullToString(list2);
			}
			if(StringUtils.isNotBlank(col_3)){
				params.put("optId", col_3);//第一列
				if(col_3.contains("PRO")||col_3.contains("UAT")){
					list3 = metaVerService.getProVersionDbInfo(params);
				}else{
					list3 = metaVerService.getNewVersionMeta(params);
				}
				listNullToString(list3);
			}
			if(StringUtils.isNotBlank(col_4)){
				params.put("optId", col_4);//第一列
				if(col_4.contains("PRO")||col_4.contains("UAT")){
					list4 = metaVerService.getProVersionDbInfo(params);
				}else{
					list4 = metaVerService.getNewVersionMeta(params);
				}
				listNullToString(list4);
			}
			if(StringUtils.isNotBlank(col_5)){
				params.put("optId", col_5);//第一列
				if(col_5.contains("PRO")||col_5.contains("UAT")){
					list5 = metaVerService.getProVersionDbInfo(params);
				}else{
					list5 = metaVerService.getNewVersionMeta(params);
				}
				listNullToString(list5);
			}
			
			/*list2 = metadataVerService.getProVersionDbInfo(params);
			listNullToString(list2);*/
			
			map.put("col1", list1);
			map.put("col2", list2);
			map.put("col3", list3);
			map.put("col4", list4);
			map.put("col5", list5);
		}
		return ObjectToJSON.returnResult(map,null);
	}
	/**
	 * 版本比对【模式列表】
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=initCompShema")
	@ResponseBody
	public Object initCompShema(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		String dbId = page.getParamStr("dbId");
		//比对的全部
		List<MetadataSchemaVO> allVerSchemaList = metaVerService.getCompAllVersionSchemaList(page);
		
		List<MetadataSchemaVO> list = new ArrayList<MetadataSchemaVO>();
		List<MetadataSchemaVO> clist = null;
		
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List list3 = new ArrayList();
		List list4 = new ArrayList();
		List list5 = new ArrayList();
		
		String col_1 = request.getParameter("Col_1");
		String col_2 = request.getParameter("Col_2");
		String col_3 = request.getParameter("Col_3");
		String col_4 = request.getParameter("Col_4");
		String col_5 = request.getParameter("Col_5");
		
		if(StringUtils.isNotBlank(col_1)){
			page.setParameter("optId", col_1);
			if(col_1.contains("PRO")||col_1.contains("UAT")){
				list = metaVerService.getVerSchemaMetaSna(dbId,col_1);
				list1  = metaVerService.compSchema(allVerSchemaList,list,list);
				
			}else{
				list = metaVerService.getVerSchemaMeta(page);
				list1  = metaVerService.compSchema(allVerSchemaList,list,list);
			}
		}
		if(StringUtils.isNotBlank(col_2)){
			clist = new ArrayList<MetadataSchemaVO>();
			page.setParameter("optId", col_2);
			if(col_2.contains("PRO")||col_2.contains("UAT")){
				clist = metaVerService.getVerSchemaMetaSna(dbId,col_2);
				list2  = metaVerService.compSchema(allVerSchemaList,list,clist);
			}else{
				clist = metaVerService.getVerSchemaMeta(page);
				list2  = metaVerService.compSchema(allVerSchemaList,list,clist);
			}
		}
		if(StringUtils.isNotBlank(col_3)){
			clist = new ArrayList<MetadataSchemaVO>();
			page.setParameter("optId", col_3);
			if(col_3.contains("PRO")||col_3.contains("UAT")){
				clist = metaVerService.getVerSchemaMetaSna(dbId,col_3);
				list3  = metaVerService.compSchema(allVerSchemaList,list,clist);
			}else{
				clist = metaVerService.getVerSchemaMeta(page);
				list3  = metaVerService.compSchema(allVerSchemaList,list,clist);
			}
		}
		if(StringUtils.isNotBlank(col_4)){
			clist = new ArrayList<MetadataSchemaVO>();
			page.setParameter("optId", col_4);
			if(col_4.contains("PRO")||col_4.contains("UAT")){
				clist = metaVerService.getVerSchemaMetaSna(dbId,col_4);
				list4  = metaVerService.compSchema(allVerSchemaList,list,clist);
			}else{
				clist = metaVerService.getVerSchemaMeta(page);
				list4  = metaVerService.compSchema(allVerSchemaList,list,clist);
			}
		}
		if(StringUtils.isNotBlank(col_5)){
			clist = new ArrayList<MetadataSchemaVO>();
			page.setParameter("optId", col_5);
			if(col_5.contains("PRO")||col_5.contains("UAT")){
				clist = metaVerService.getVerSchemaMetaSna(dbId,col_5);
				list5  = metaVerService.compSchema(allVerSchemaList,list,clist);
			}else{
				clist = metaVerService.getVerSchemaMeta(page);
				list5  = metaVerService.compSchema(allVerSchemaList,list,clist);
			}
		}
		Map map = new HashMap();
		map.put("sch1", list1);
		map.put("sch2", list2);
		map.put("sch3", list3);
		map.put("sch4", list4);
		map.put("sch5", list5);
		map.put("sch", allVerSchemaList);
		return ObjectToJSON.returnResult(map,null);
	}
	
	/**
	 * 版本比对【模块列表】
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=initCompModule")
	@ResponseBody
	public Object initCompModule(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		String dbId = page.getParamStr("dbId");
		//比对的全部
		List<MetadataModuleVO> allList = metaVerService.getCompAllVersionModuleList(page);
		List<MetadataModuleVO> list = new ArrayList<MetadataModuleVO>();
		List<MetadataModuleVO> clist = null;
		
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List list3 = new ArrayList();
		List list4 = new ArrayList();
		List list5 = new ArrayList();
		
		String col_1 = request.getParameter("Col_1");
		String col_2 = request.getParameter("Col_2");
		String col_3 = request.getParameter("Col_3");
		String col_4 = request.getParameter("Col_4");
		String col_5 = request.getParameter("Col_5");
		
		if(StringUtils.isNotBlank(col_1)){
			page.setParameter("optId", col_1);
			if(col_1.contains("PRO")||col_1.contains("UAT")){
				list = metaVerService.getVerModuleMetaSna(dbId,col_1);
				list1  = metaVerService.compModule(allList,list,list);
			}else{
				list = metaVerService.getVerModuleMeta(page);
				list1  = metaVerService.compModule(allList,list,list);
			}
		}
		if(StringUtils.isNotBlank(col_2)){
			page.setParameter("optId", col_2);
			if(col_2.contains("PRO")||col_2.contains("UAT")){
				clist = metaVerService.getVerModuleMetaSna(dbId,col_2);
				list2  = metaVerService.compModule(allList,list,clist);
			}else{
				clist = metaVerService.getVerModuleMeta(page);
				list2  = metaVerService.compModule(allList,list,clist);
			}
		}
		if(StringUtils.isNotBlank(col_3)){
			page.setParameter("optId", col_3);
			if(col_3.contains("PRO")||col_3.contains("UAT")){
				clist = metaVerService.getVerModuleMetaSna(dbId,col_3);
				list3  = metaVerService.compModule(allList,list,clist);
			}else{
				clist = metaVerService.getVerModuleMeta(page);
				list3  = metaVerService.compModule(allList,list,clist);
			}
		}
		if(StringUtils.isNotBlank(col_4)){
			page.setParameter("optId", col_4);
			if(col_4.contains("PRO")||col_4.contains("UAT")){
				clist = metaVerService.getVerModuleMetaSna(dbId,col_4);
				list4  = metaVerService.compModule(allList,list,clist);
			}else{
				clist = metaVerService.getVerModuleMeta(page);
				list4  = metaVerService.compModule(allList,list,clist);
			}
		}
		if(StringUtils.isNotBlank(col_5)){
			page.setParameter("optId", col_5);
			if(col_5.contains("PRO")||col_5.contains("UAT")){
				clist = metaVerService.getVerModuleMetaSna(dbId,col_5);
				list5  = metaVerService.compModule(allList,list,clist);
			}else{
				clist = metaVerService.getVerModuleMeta(page);
				list5  = metaVerService.compModule(allList,list,clist);
			}
		}
		Map map = new HashMap();
		map.put("col1", list1);
		map.put("col2", list2);
		map.put("col3", list3);
		map.put("col4", list4);
		map.put("col5", list5);
		map.put("col", allList);
		return ObjectToJSON.returnResult(map,null);
	}
	
	/**
	 * 版本比对【表列表】
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=initCompTable")
	@ResponseBody
	public Object initCompTable(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate",DateUtil.getYmdDate());
		String endDate = DateUtil.getYmdDate();
		String dbId = page.getParamStr("dbId");
		//比对的分页全部
		List<Map<String, Object>> allList = metaVerService.getCompAllVersionTableList(page);
		Map map = outPutVersionComp(allList,page);
		return ObjectToJSON.returnResult(map,page);
	}
	
	/**
	 * 版本比对信息
	 * @param allList
	 */
	private Map outPutVersionComp(List<Map<String, Object>> allList,PageResults page) {
		//1列
		List list1 = new ArrayList();
		//2列
		List list2 = new ArrayList();
		//3列
		List list3 = new ArrayList();
		//4列
		List list4 = new ArrayList();
		//5列
		List list5 = new ArrayList();
		
		String c1 = page.getParamStr("Col_1");
		String c2 = page.getParamStr("Col_2");
		String c3 = page.getParamStr("Col_3");
		String c4 = page.getParamStr("Col_4");
		String c5 = page.getParamStr("Col_5");
		
		String has1 = "";
		String has = "";
		String mod = "0";
		Map map = null;
		for(Map<String, Object> mt:allList){
			if(c2.equals("")&&c3.equals("")&&c4.equals("")&&c5.equals("")){//只有快照
				map = new HashMap();
				map.put("has","1");
				map.put("mod", "0");
				list1.add(map);
			}else{
				if(StringUtils.isNotBlank(c1)){
				    map = new HashMap();
					if(mt.get("HASC1")==null){
						has1 = "0";
					}else{
						has1 = mt.get("HASC1")+"";
					}
					map.put("has",has1);
					map.put("mod", "0");
					list1.add(map);
				}
				
				if(StringUtils.isNotBlank(c2)){
					 map = new HashMap();
					String has2 = "";
					String mod2 = "0";
					if(mt.get("HASC2")==null){
						has2 = "0";
					}else{
						has2 = mt.get("HASC2")+"";
					}
					if(mt.get("C2MOD")==null&&!"0".equals(has1)){
						mod2 = "1";
					}else if(mt.get("C2MOD")==null&&"0".equals(has1)){//第一列和该列都不存在，没有改变
						mod2 = "0";
					}else{
						mod2 = mt.get("C2MOD")+"";
					}
					map.put("has", has2);
					map.put("mod", mod2);
					list2.add(map);
				}
				if(StringUtils.isNotBlank(c3)){
					map = new HashMap();
					String has3 = "";
					String mod3 = "0";
					if(mt.get("HASC3")==null){
						has3 = "0";
					}else{
						has3 = mt.get("HASC3")+"";
					}
					if(mt.get("C3MOD")==null&&!"0".equals(has1)){
						mod3 = "1";
					}else if(mt.get("C3MOD")==null&&"0".equals(has1)){//第一列和该列都不存在，没有改变
						mod3 = "0";
					}else{
						mod3 = mt.get("C3MOD")+"";
					}
					map.put("has", has3);
					map.put("mod", mod3);
					list3.add(map);
				}
				if(StringUtils.isNotBlank(c4)){
					 map = new HashMap();
					if(mt.get("HASC4")==null){
						has = "0";
					}else{
						has = mt.get("HASC4")+"";
					}
					if(mt.get("C4MOD")==null&&!"0".equals(has1)){
						mod = "1";
					}else if(mt.get("C4MOD")==null&&"0".equals(has1)){
						mod = "0";
					}else{
						mod = mt.get("C4MOD")+"";
					}
					map.put("has", has);
					map.put("mod", mod);
					list4.add(map);
				}
				if(StringUtils.isNotBlank(c5)){
					map = new HashMap();
					if(mt.get("HASC5")==null){
						has = "0";
					}else{
						has = mt.get("HASC5")+"";
					}
					if(mt.get("C5MOD")==null&&!"0".equals(has1)){
						mod = "1";
					}else if(mt.get("C5MOD")==null&&"0".equals(has1)){
						mod = "0";
					}else{
						mod = mt.get("C5MOD")+"";
					}
					map.put("has", has);
					map.put("mod", mod);
					list5.add(map);
				
				}
		}
		}
		listNullToString(allList);
		map = new HashMap();
		map.put("col1", list1);
		map.put("col2", list2);
		map.put("col3", list3);
		map.put("col4", list4);
		map.put("col5", list5);
		map.put("col", allList);
		return map;
	}
	/**
	 * 版本比对【字段列表】
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=initCompColumn")
	@ResponseBody
	public Object initCompColumn(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		String endDate = DateUtil.getYmdDate();
		String dbId = page.getParamStr("dbId");
		//比对的分页全部
		List<Map<String,Object>> allList = metaVerService.getCompAllVersionColumnList(page);
		Map map = outPutVersionComp(allList,page);
		return ObjectToJSON.returnResult(map,page);
	}
	
}
