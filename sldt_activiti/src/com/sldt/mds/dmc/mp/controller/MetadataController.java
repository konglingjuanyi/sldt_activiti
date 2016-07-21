package com.sldt.mds.dmc.mp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.sldt.mds.dmc.mp.service.MetadataAltService;
import com.sldt.mds.dmc.mp.service.MetadataService;
import com.sldt.mds.dmc.mp.service.MetadataSysMgrService;
import com.sldt.mds.dmc.mp.service.MetadataVerService;
import com.sldt.mds.dmc.mp.util.ActionHelper;
import com.sldt.mds.dmc.mp.util.DateUtil;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;
import com.sldt.mds.dmc.mp.vo.MetaStaUsInfoVo;
import com.sldt.mds.dmc.mp.vo.MetaUserVO;
import com.sldt.mds.dmc.mp.vo.MetadataDatabaseVO;

@Controller
@RequestMapping("/metadata.do")
public class MetadataController {

	private static Log log = LogFactory.getLog(MetadataController.class);
	
	@Resource(name="metaVerService")
	public MetadataVerService metaVerService;
	
	@Resource(name="metaService")
	public MetadataService metaService;
	
	@Resource(name="metaSysMgrService")
	public MetadataSysMgrService metaSysMgrService;
	
	@Resource(name="metaAltService")
	public MetadataAltService metaAltService;
	
	/**
	 * 获取当前用户所管理的元数据对象
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=cardMeta")
	@ResponseBody
	public Object cardMeta(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("isAdmin", false);
		page.setParameter("endDate", DateUtil.getYmdDate());
		page.setParameter("isCard", true);
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		List<MetadataDatabaseVO> list= null;
		if(StringUtils.isNotEmpty(userId)){
			list = metaVerService.getMyVersionMeta(page);
			for(MetadataDatabaseVO metaDatabaseVO:list){
				page.setParameter("dbId", metaDatabaseVO.getDbId());
				//获取模式总数
				int schemaCount = metaVerService.getVerSchemaMetaCount(page);
				//获取模板总数
				int moduleCount = metaVerService.getVerModuleMetaCount(page);
				//获取表级总数
				int tableCount = metaVerService.getVerTableMetaCount(page);
				//获取字段级总数
				int columnCount = metaVerService.getVerColumnMetaCount(page);
				
				Map<String, Object> altMap = new HashMap<String, Object>();
				altMap.put("schemaCount", schemaCount);
				altMap.put("moduleCount", moduleCount);
				altMap.put("tableCount", tableCount);
				altMap.put("columnCount", columnCount);
				//获取最新的变更信息
				List<Map<String, Object>> altMaps = metaAltService.getNewAltMeta(page);
				for (Map<String, Object> alt : altMaps) {
					String classIfer = (String)alt.get("CLASSIFER_TYPE");
					//变更是否存在该变更模型
					if(altMap.containsKey(classIfer)){
						String altTypeStr = (String)alt.get("ALT_TYPE");
						Map<String,Object> altType = (Map<String,Object>)altMap.get(classIfer);
						//判断是否存在该变更类型，新增、修改、删除
						if(altType.containsKey(altTypeStr)){
							//如果存在不处理，是数据存在问题？
						}else{
							altType.put((String)alt.get("ALT_TYPE"), alt.get("ALTCOUNT"));
						}
					}else{
						Map<String,Object> altType = new HashMap<String, Object>();
						altType.put((String)alt.get("ALT_TYPE"), alt.get("ALTCOUNT"));
						altMap.put(classIfer, altType);
					}
				}
				metaDatabaseVO.setAltMap(altMap);
			}
		}
		
		return list;
			
		}
		
	/**
	 * 当前用户的系统的全部版本
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=getMySysAllVer")
	@ResponseBody
	public Object getMySysAllVer(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		page.setParameter("allMyVer", "true");
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		List<MetadataDatabaseVO> list = null;
		List allMySysList = null;
		if(StringUtils.isNotEmpty(userId)){
			list = metaVerService.getMyVersionMeta(page);
		    allMySysList = new ArrayList();
			for (int k = 0 ; k < list.size(); k++) {
				MetadataDatabaseVO meta = list.get(k);
				String namespace = meta.getNamespace();
				if(allMySysList.size()>4){
					break;
				}
				if(namespace == null && !"".equals(namespace)){
					continue;
				}
				page.setParameter("dbId", meta.getDbId());
				page.setParameter("sysName", meta.getDbChName());
				Map verMap = getVersion(page);
				if(namespace.lastIndexOf("/")!=-1){
					namespace = namespace.substring(namespace.lastIndexOf("/")+1);
				}
				verMap.put("instance_id", namespace);
				verMap.put("sysBelongCurUser", true);
				allMySysList.add(verMap);
			}
		}
		 
		return ObjectToJSON.retResult(allMySysList,null);
		
	}
	private Map getVersion(PageResults page){
		page.setPageSize(500);
		String proVerId = "";//生产版本id
		String uatVerId = "";//uat版本id
		String proName = "";
		String uatName = "";
		String devName = "";
		//开发版本Id
		String devVer = "";
		String dbId = page.getParamStr("dbId");
		page.setParameter("VER_DATE","");
		List<Map<String, Object>> curlist = new ArrayList<Map<String,Object>>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("dbId", dbId);
		String currVerDate = "";
		if(dbId!=null){
			Map<String, Object> altCurrMetaVer = metaVerService.getCurrVerMap(page);
			currVerDate = (String) altCurrMetaVer.get("ALT_VER_DATE_NO");
			if(currVerDate==null){
				currVerDate = "";
			}
			params.put("optId",currVerDate);
			if(altCurrMetaVer.size()>0){
				curlist.add(altCurrMetaVer);
			}
		}
		page.setParameter("currVerDate", DateUtil.getYmdDate());
		//获取生产版本信息
		List<Map<String,Object>> proList = metaService.getProMetaByDbId(page);
		//获取UAT版本信息
		List<Map<String, Object>> uatList = metaService.getUatMetaByDbId(page);
		
		Map<String, Object> fcMap = metaVerService.getCurDeveVer(page);
		
		Map<String, Object> curmap = (Map<String, Object>) fcMap.get("curVer");
		//获取远期版本的信息
		List<Map<String, Object>> altList = (List<Map<String, Object>>) fcMap.get("forvList");
		
		List<Map<String, Object>> farVerList = new ArrayList<Map<String,Object>>();
		//当前生产版本
		List<Map<String, Object>> curList = new ArrayList<Map<String,Object>>();
		//获取历史版本信息
		List<Map<String, Object>> pastList = metaVerService.getHisVerList(page);
	   if(curmap!=null){
		   if(!curmap.isEmpty()){
			   curList.add(curmap);
		   }
	   }
	   if(!"19700101".equals((String) curmap.get("ALT_VER_DATE_NO"))){
		 //没有变更，取初始化版本
		   Map<String, Object> map = metaVerService.getInitVer(dbId);
		   if(map!=null&&!map.isEmpty()){
				map.put("ALT_SYS_CODE", dbId);
				pastList.add(map);
			}
	   }
	   if(curList.size()>0){
		   devVer = (String) curList.get(0).get("ALT_VER_DATE_NO");
		   devName = (String) curList.get(0).get("VER_NAME");
		   if(devVer==null){
				devVer = "";
			}
			if(devName==null){
				devName = "";
			}
	   }
	   if(altList.size()>0){
		   for(int i = 0;i < altList.size();i++){
			   page.setParameter("VER_DATE", altList.get(i).get("ALT_VER_DATE_NO"));
			   Map map = new HashMap();
			   map.put("ALT_VER_DATE_NO", altList.get(i).get("ALT_VER_DATE_NO"));
			   map.put("ALT_SYS_CODE", altList.get(i).get("ALT_SYS_CODE"));
			   map.put("VER_NAME", altList.get(i).get("VER_NAME"));
			   map.put("dbName", page.getParameter("sysName"));
			   map.put("isSubmit",metaVerService.getCurrVerMap(page).size()>0);
			   boolean flag = metaVerService.getCurrVerMap(page).size()>0;
			   if(flag){
				   //版本是否已经生效
				   farVerList.add(map);
			   }
		   }
	   }
	   Map map = new HashMap();
	   //系统信息
	   Map sysmap = new HashMap();
	   sysmap.put("sysName", page.getParameter("sysName")==null?"":page.getParamStr("sysName"));
	   sysmap.put("dbId", dbId);
	   
	   listNullToString(pastList);
	   listNullToString(altList);
	   listNullToString(curlist);
	   listNullToString(proList);
	   listNullToString(uatList);
	   listNullToString(curList);
	   
	   if(proList.size()>0){
		   proVerId = dbId +"_PRO";
		   proName = (String) proList.get(0).get("VER_NAME");
	   }
	   if(uatList.size()>0){
			uatVerId = dbId+"_UAT";
			uatName = (String)uatList.get(0).get("VER_NAME");
		}
	   if(curlist.size()==0){//没有生产版本信息
			currVerDate = "";
		}else{
			currVerDate = (String)curlist.get(0).get("CUR_VER_DATE_NO");
		}
	   map.put("proVer", proList);//生产
		map.put("uatList", uatList);//uat
		map.put("proVerId", proVerId);//生产Id
		map.put("uatVerId", uatVerId);//uatId
		map.put("proName", proName);//生产name
		map.put("uatName", uatName);//uatname
		map.put("devName", devName);//devName
		map.put("devVerId", devVer);//开发版本id
		map.put("devVerList", curList);//开发
		map.put("farVerList", farVerList);//远期
		map.put("pastList", pastList);//历史
		map.put("sys", sysmap);//系统名称和Id
		map.put("curlist", curlist);//当前
		map.put("currVerDate", currVerDate);//当前版本时间
		return ObjectToJSON.retResult(map,null);
		
	}
	/**
	 * 把list的null转换为String
	 * @param list
	 */
	public void listNullToString(List<Map<String, Object>> list){
		for (Map<String, Object> obj:list){
			for(Iterator iterator = obj.keySet().iterator(); iterator.hasNext();){
				String key = (String) iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
	}
	
	/**
	 * 获取全部系统,且讲当前用户所管理的元数据对象放在前面
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=getAllMetadataDatabase")
	@ResponseBody
	public Object getAllMetadataDatabase(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		page.setParameter("isAdmin", false);
		page.setPageSize(500);
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
		List<MetadataDatabaseVO> alist = null;
		if(StringUtils.isNotEmpty(userId)){
			//当前用户管辖的系统
			List<MetadataDatabaseVO> ulist = metaVerService.getMyVersionMeta(page);
			//全部的系统
		    alist = metaService.getMetadataDatabase(page);
			//经过排序的全部系统
			List<MetadataDatabaseVO> rlist = new ArrayList<MetadataDatabaseVO>();
		}
		
		return ObjectToJSON.retResult(alist,null);
		
	}
	
	/**
	 * 获取首页中元数据用户信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(params="method=getMetaUser")
	@ResponseBody
	public void getMetaUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String metaPeMaId = request.getParameter("metaPeMaId");
		String metaMaId = request.getParameter("metaMaId");
		String metaRelaId = request.getParameter("metaRelaId");
		
		//查询元数据统计信息
		MetaStaUsInfoVo peMaStaUsInfo = metaSysMgrService.getMetaStaUsInfoByRoleId(metaPeMaId, "peMa");
		MetaStaUsInfoVo maStaUsInfo = metaSysMgrService.getMetaStaUsInfoByRoleId(metaMaId, "ma");
		MetaStaUsInfoVo relaStaUsInfo = metaSysMgrService.getMetaStaUsInfoByRoleId(metaRelaId, "rela");
		
		//查询元数据用户
		List<MetaUserVO> peMaUsers = metaSysMgrService.getMetaUserlistByRoleId(metaPeMaId, "peMa");
		List<MetaUserVO> maUsers = metaSysMgrService.getMetaUserlistByRoleId(metaMaId, "ma");
		List<MetaUserVO> relaUsers = metaSysMgrService.getMetaUserlistByRoleId(metaRelaId, "rela");
		//设置返回信息
		JSONObject resultObj = new JSONObject();
		resultObj.put("code", "1");
		resultObj.put("msg", "查询成功!");
		
		resultObj.put("peMaStaUsInfo", peMaStaUsInfo);
		resultObj.put("maStaUsInfo", maStaUsInfo);
		resultObj.put("relaStaUsInfo", relaStaUsInfo);
		
		resultObj.put("peMaUsers", peMaUsers);
		resultObj.put("maUsers", maUsers);
		resultObj.put("relaUsers", relaUsers);
	    
		response.setCharacterEncoding("utf-8");
	    response.getWriter().write(resultObj.toString());
	}
	
	/**
	 * 根据用户输入模糊匹配字段值
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=columnAutoComplete")
	@ResponseBody
	public Object columnAutoComplete(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		List<Map<String,Object>> list = metaService.getColumnAutoComplete(page);
		for (Map<String, Object> obj : list) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.retResult(list,null);
	}
	
	@RequestMapping(params="method=tableAutoComplete")
	@ResponseBody
	public Object tableAutoComplete(HttpServletRequest request){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate", DateUtil.getYmdDate());
		List<Map<String,Object>> list = metaService.getTableAutoComplete(page);
		for (Map<String, Object> obj : list) {
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
		return ObjectToJSON.retResult(list,null);
	}
	
	/**
	 * 根据系统获取版本信息（当前，远期，历史..）
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=getSysMetadataVersion")
	@ResponseBody
	public Object getSysMetadataVersion(HttpServletRequest request){
		String userId = "";
		try {
			//userId = ModuleUtil.getCurrUserId();
			userId = "admin";
		} catch (Exception e) {
			log.error("获取当前登录用户出错："+e.getMessage());
			e.printStackTrace();
		}
			PageResults page = ControllerHelper.buildPage(request);
			page.setParameter("endDate", DateUtil.getYmdDate());
			page.setParameter("userId", userId);
			page.setParameter("isAdmin", true);
			String dbId = page.getParamStr("dbId");	
			//当前用户管辖的系统
			List<MetadataDatabaseVO> ulist = metaVerService.getMyVersionMeta(page);
			boolean sysBelongCurUser = false;//当前系统是否属于当前用户管辖
			for(MetadataDatabaseVO mm:ulist){
				if(dbId.equals(mm.getDbId())){
					sysBelongCurUser = true;
					break;
				}
			}
			Map verMap = getVersion(page);
			verMap.put("sysBelongCurUser", sysBelongCurUser);
		return ObjectToJSON.retResult(verMap,null);
	}
}
