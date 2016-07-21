package com.sldt.mds.dmc.mp.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.cas.dmp.ModuleUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sldt.framework.common.ControllerHelper;
import com.sldt.framework.common.PageResults;
import com.sldt.mds.dmc.mp.service.MetadataAltService;
import com.sldt.mds.dmc.mp.service.MetadataService;
import com.sldt.mds.dmc.mp.service.MetadataVerService;
import com.sldt.mds.dmc.mp.thread.MetaAnalyeThread;
import com.sldt.mds.dmc.mp.util.ActionHelper;
import com.sldt.mds.dmc.mp.util.DateUtil;
import com.sldt.mds.dmc.mp.util.ObjectToJSON;
import com.sldt.mds.dmc.mp.vo.MetadataColumnVO;
import com.sldt.mds.dmc.mp.vo.MetadataDatabaseVO;
import com.sldt.mds.dmc.mp.vo.MetadataModuleVO;
import com.sldt.mds.dmc.mp.vo.MetadataSchemaVO;
import com.sldt.mds.dmc.mp.vo.MetadataTableVO;

@Controller
@RequestMapping("/metaExc.do")
public class MetaExcelController {
	
	private static Log log = LogFactory.getLog(MetaExcelController.class);
	
	@Resource(name="metaVerService")
	public MetadataVerService metaVerService;
	
	@Resource(name="metaService")
	public MetadataService metaService;
	
	@Resource(name="metaAltService")
	public MetadataAltService metaAltService;
	
	@RequestMapping(params="method=impMetaExcel")
	@ResponseBody
    public Object handleFileUpload(HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest multRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multRequest.getFile("fileUpload");
		String msg = "";
		try {
			if(!file.isEmpty()){
				String tarOptId = request.getParameter("optSelect");
				String uploadType = request.getParameter("uploadType");
				String curOptId = request.getParameter("currOptId");
				String userId = "";
				//userId = ModuleUtil.getCurrUserId();
				userId = "admin";
				Map<String, String> dbMap = new HashMap<String, String>();
				//变更的元数据集合
				List<Map<String, String>> altList = new ArrayList<Map<String,String>>();
				getTargetList(file, altList, dbMap);
				MetaAnalyeThread analye = new MetaAnalyeThread(altList, dbMap, uploadType, userId, file.getOriginalFilename(), curOptId, tarOptId);
				analye.start();
				msg = "文件上传成功，后台正在处理中，请稍后查看！";
			}else {
	        	
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,String> obj = new HashMap<String, String>();
	       obj.put("success", "true");
	       obj.put("msg", msg);
	       return null;
    }
	
	public void getTargetList(MultipartFile multipartFile,List<Map<String, String>> altList,Map<String, String> dbMap){
		String fileType = "";
		try {
			String fileName = multipartFile.getOriginalFilename();
			fileType = fileName.substring(fileName.lastIndexOf(".") + 1,
					 fileName.lastIndexOf(".") + 4);
		} catch (Exception e) {
			log.error(e);
			fileType = "";
		}
		if(!fileType.toLowerCase().equals("xls")){
		   this.log.debug("导入的文件格式不正确，应该不是excel文件")	;
		}
		HSSFWorkbook wb = null;
		try {
			String id = UUID.randomUUID().randomUUID().toString().replaceAll("-", "");
			wb = new HSSFWorkbook(multipartFile.getInputStream());
			for(int j = 0 ;j < wb.getNumberOfFonts() ; j++){
				HSSFSheet sheet = wb.getSheetAt(j);
				log.debug("sheet name = "+wb.getSheetName(j));
				if(j==0){
					setDBAltMeta(altList, sheet, dbMap);
				}else if (j==1){
					setSchemaAltMeta(altList, sheet);
				}else if (j==2){
					setModuleAltMeta(altList, sheet);
				}else if (j==3){
					setTableAltMeta(altList, sheet);
				}else if (j==4){
					setColumnAltMeta(altList, sheet);
				}else if (j==5){
					setColCode(altList, sheet);
				}else if (j==6){
					setIntFile(altList, sheet);
				}
			}
		} catch (Exception e) {
			log.error(e);
            e.printStackTrace();
		}
	}
	
	/**
	 * 创建database变更元数据对象
	 * @param altList
	 * @param sheet
	 * @param dbMap
	 */
	public void setDBAltMeta(List<Map<String, String>> altList,HSSFSheet sheet,Map<String, String> dbMap){
		for(int i = 1; i<=sheet.getLastRowNum();i++){
			HSSFRow row = sheet.getRow(i);
			//记录变更数据对象
			Map<String, String> altMap = new HashMap<String, String>();
			dbMap.put(clearStr(row.getCell(0)), clearStr(row.getCell(1)));
			altMap.put("CLASSIFER_TYPE", "DATABASE");
			altMap.put("DB_CODE", clearStr(row.getCell(0)));
			altMap.put("DBCHNAME", clearStr(row.getCell(1)));
			altMap.put("DEPT", clearStr(row.getCell(2)));
			altMap.put("DEVLOPER", clearStr(row.getCell(3)));
			altMap.put("PRO_M_FAC", clearStr(row.getCell(4)));
			altMap.put("DBA", clearStr(row.getCell(5)));
			altMap.put("REMARK", clearStr(row.getCell(6)));
			
			altMap.put("MAINTAINER", "");
			altMap.put("IMGURL", "");
			altMap.put("TOTALSIZE", "");
			altMap.put("USEDSIZE", "");
			altMap.put("TABCNT", "");
			altMap.put("OBJCNT", "");
			
			if((!"".equals((String)altMap.get("DB_CODE")))&&altMap.get("DB_CODE")!=null){
				altList.add(altMap);
			}
		}
	}
	
	public String clearStr(HSSFCell cell){
		String str = "";
		if(cell==null){
			return str;
		}
		if (cell.getCellType()==0) {
			str += cell.getNumericCellValue();
			if(str.indexOf(".")!=-1){
				str = str.substring(0,str.indexOf("."));
			}
		} else if (cell.getCellType()==1) {
			str = cell.getStringCellValue();
		} else if (cell.getCellType()==2) {
			//cell.setCellValue((RichTextString) value);
		} else if (cell.getCellType()==3) {
			//cell.setCellValue((Double) value);
		} else if (cell.getCellType()==4) {
			//cell.setCellValue((Date) value);
		} else if (cell.getCellType()==5) {
			//cell.setCellValue((Calendar) value);
		} 
		return str.replaceAll("'", "''");
	}
	
	/**
	 * 创建Schema变更元数据对象
	 * @param altList
	 * @param sheet
	 */
	public void setSchemaAltMeta(List<Map<String, String>> altList,HSSFSheet sheet){
		for (int i = 1;i<=sheet.getLastRowNum();i++) {
			HSSFRow row = sheet.getRow(i);
			//记录变更数据对象
			Map<String,String> altMap = new HashMap<String, String>();
			altMap.put("CLASSIFER_TYPE", "SCHEMA");
			altMap.put("DB_CODE", clearStr(row.getCell(0)));
			altMap.put("SCHNAME", clearStr(row.getCell(1)));
			altMap.put("SCHCHNAME", clearStr(row.getCell(2)));
			altMap.put("DEVLOPER", clearStr(row.getCell(3)));
			altMap.put("REMARK", clearStr(row.getCell(4)));
			
			altMap.put("TABCNT", "");
			altMap.put("USEDSIZE", "");
			altMap.put("OBJCNT", "");
			
			if((!"".equals((String)altMap.get("DB_CODE")))&&altMap.get("DB_CODE")!=null){
				altList.add(altMap);
			}
		}
	}
	
	/**
	 * 创建module变更元数据对象
	 * @param altList
	 * @param sheet
	 */
	public void setModuleAltMeta(List<Map<String, String>> altList,HSSFSheet sheet){
		for (int i = 1;i<=sheet.getLastRowNum();i++) {
			HSSFRow row = sheet.getRow(i);
			//记录变更数据对象
			Map<String,String> altMap = new HashMap<String, String>();
			altMap.put("CLASSIFER_TYPE", "MODULE");
			altMap.put("DB_CODE", clearStr(row.getCell(0)));
			altMap.put("SCHNAME", clearStr(row.getCell(1)));
			altMap.put("MODNAME", clearStr(row.getCell(2)));
			altMap.put("MODCHNAME", clearStr(row.getCell(3)));
			altMap.put("DEVLOPER", clearStr(row.getCell(4)));
			altMap.put("SA", clearStr(row.getCell(5)));
			altMap.put("MODPTN", clearStr(row.getCell(6)));
			altMap.put("REMARK", clearStr(row.getCell(7)));
			
			altMap.put("DEPT", "");
			altMap.put("DEPTCHARGER", "");
			altMap.put("STATUS", "");
			altMap.put("USEDSIZE", "");
			altMap.put("TABCNT", "");
			altMap.put("OBJCNT", "");
			if((!"".equals((String)altMap.get("DB_CODE")))&&altMap.get("DB_CODE")!=null){
				altList.add(altMap);
			}
		}
	}
	
	/**
	 *创建Table变更元数据对象 
	 * @param altList
	 * @param sheet
	 */
	public void setTableAltMeta(List<Map<String, String>> altList,HSSFSheet sheet){
		for (int i = 1;i<=sheet.getLastRowNum();i++) {
			HSSFRow row = sheet.getRow(i);
			//记录变更数据对象
			Map<String,String> altMap = new HashMap<String, String>();
			//变更主键ID
			altMap.put("CLASSIFER_TYPE", "TABLE");
			altMap.put("DB_CODE", clearStr(row.getCell(0)));
			altMap.put("SCHNAME", clearStr(row.getCell(1)));
			altMap.put("MODNAME", clearStr(row.getCell(2)));
			altMap.put("TABNAME", clearStr(row.getCell(3)));
			altMap.put("TABCHNAME", clearStr(row.getCell(4)));
			altMap.put("TABSPACENAME", clearStr(row.getCell(5)));
			altMap.put("PKCOLS", clearStr(row.getCell(6)));
			altMap.put("FKCOLS", clearStr(row.getCell(7)));
			altMap.put("IMPFLAG", clearStr(row.getCell(8)));
			altMap.put("LCYCDESC", clearStr(row.getCell(9)));
			altMap.put("REMARK", clearStr(row.getCell(10)));
			
			altMap.put("FKTABLENAME", "");
			altMap.put("ROWCOUNT", "");
			altMap.put("ZIPDESC", "");
			altMap.put("PCNT", "");
			altMap.put("TSIZE", "");
			altMap.put("CRTDATE", "");
			altMap.put("MODIYDATE", "");
			
			if((!"".equals((String)altMap.get("DB_CODE")))&&altMap.get("DB_CODE")!=null){
				altList.add(altMap);
			}
		}
	}
	
	/**
	 * 创建Column变更元数据对象
	 * @param altList
	 * @param sheet
	 */
	public void setColumnAltMeta(List<Map<String, String>> altList, HSSFSheet sheet){
		try {
			for (int i = 1;i<=sheet.getLastRowNum();i++) {
				HSSFRow row = sheet.getRow(i);
				//记录变更数据对象
				Map<String,String> altMap = new HashMap<String, String>();
				altMap.put("CLASSIFER_TYPE", "COLUMN");
				altMap.put("DB_CODE", clearStr(row.getCell(0)));
				altMap.put("SCHNAME", clearStr(row.getCell(1)));
				altMap.put("MODNAME", clearStr(row.getCell(2)));
				altMap.put("TABNAME", clearStr(row.getCell(3)));
				altMap.put("COLNAME",clearStr( row.getCell(4)));
				altMap.put("COLCHNAME", clearStr(row.getCell(5)));
				altMap.put("COLTYPE", clearStr(row.getCell(6)));
				altMap.put("COLSEQ", clearStr(row.getCell(7)));
				altMap.put("PKFLAG", clearStr(row.getCell(8)));
				altMap.put("NVLFLAG", clearStr(row.getCell(9)));
				altMap.put("CCOLFLAG", clearStr(row.getCell(10)));
				altMap.put("INDXFLAG", clearStr(row.getCell(11)));
				altMap.put("CODETAB", clearStr(row.getCell(12)));
				altMap.put("REMARK", clearStr(row.getCell(13)));
				
				altMap.put("PDKFLAG", "");
				
				if((!"".equals((String)altMap.get("DB_CODE")))&&altMap.get("DB_CODE")!=null){
					altList.add(altMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建字段落地代码元数据对象
	 * @param altList
	 * @param sheet
	 */
	public void setColCode(List<Map<String,String>> altList,HSSFSheet sheet){
		try {
			for (int i = 1;i<=sheet.getLastRowNum();i++) {
				HSSFRow row = sheet.getRow(i);
				//记录变更数据对象
				Map<String,String> altMap = new HashMap<String, String>();
				altMap.put("CLASSIFER_TYPE", "COLCODE");
				altMap.put("DB_CODE", clearStr(row.getCell(0)));
				altMap.put("SCHNAME", clearStr(row.getCell(1)));
				altMap.put("MODNAME", clearStr(row.getCell(2)));
				altMap.put("TABNAME", clearStr(row.getCell(3)));
				altMap.put("COLNAME",clearStr( row.getCell(4)));
				altMap.put("CODE_VAL", clearStr(row.getCell(5)));
				altMap.put("CODE_CH_NAME", clearStr(row.getCell(6)));
				altMap.put("CODE_DESC", clearStr(row.getCell(7)));
				altMap.put("CODE_STS", clearStr(row.getCell(8)));
				altMap.put("CODE_START_DATE",  clearStr(row.getCell(9)));
				altMap.put("CODE_END_DATE", clearStr(row.getCell(10)));
				altMap.put("DS_REF", "");
				if((!"".equals((String)altMap.get("DB_CODE")))&&altMap.get("DB_CODE")!=null){
					altList.add(altMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建接口文件/程序元数据对象
	 * @param altList
	 * @param sheet
	 */
	public void setIntFile(List<Map<String,String>> altList,HSSFSheet sheet){
		try {
			for (int i = 1;i<=sheet.getLastRowNum();i++) {
				HSSFRow row = sheet.getRow(i);
				//记录变更数据对象
				Map<String,String> altMap = new HashMap<String, String>();
				altMap.put("CLASSIFER_TYPE", "INTFILE");
				altMap.put("DB_CODE", clearStr(row.getCell(0)));
				altMap.put("REF_DB_CODE", clearStr(row.getCell(1)));
				altMap.put("REF_SCHNAME", clearStr(row.getCell(2)));
				altMap.put("REF_MODNAME", clearStr(row.getCell(3)));
				altMap.put("REF_TABNAME", clearStr(row.getCell(4)));
				altMap.put("C_FUNC_NAME",clearStr( row.getCell(5)));
				altMap.put("C_DESC", clearStr(row.getCell(6)));
				altMap.put("C_TYPE", clearStr(row.getCell(7)));
				if((!"".equals((String)altMap.get("DB_CODE")))&&altMap.get("DB_CODE")!=null){
					altList.add(altMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(params="method=delUploadInfo")
	@ResponseBody
	public Object delUploadInfo(HttpServletRequest request,HttpServletResponse response){
		PageResults page = ControllerHelper.buildPage(request);
		metaAltService.delUploadInfo(page);
		Map<String,String> map = new HashMap<String, String>();
		map.put("success", "true");
		map.put("msg", "删除元数据上传记录成功！");
		return ObjectToJSON.retResult(map, null);
	}
	
	@RequestMapping(params="method=getUploadList")
	@ResponseBody
    public Object getUploadList(HttpServletRequest request,HttpServletResponse response){
		PageResults page = ControllerHelper.buildPage(request);
		List<Map<String,Object>> list = metaAltService.getUploadList(page);
		for (Map<String, Object> obj : list) {
			obj.put("UPLOAD_METHOD", "<a style=\"cursor: pointer;\" onclick=\"delUploadInfo('"+obj.get("UPLOAD_ID")+"','"+obj.get("UPLOAD_BATCH_ID")+"')\">删除</a>");
			if(obj.get("UPLOAD_STS").equals("0")){
				obj.put("UPLOAD_STS", "上传成功");
			}else if(obj.get("UPLOAD_STS").equals("1")){
				obj.put("UPLOAD_STS", "变更分析中");
			}else if(obj.get("UPLOAD_STS").equals("2")){
				obj.put("UPLOAD_STS", "分析成功");
			}
			if(obj.get("UPLOAD_M").equals("0")){
				obj.put("UPLOAD_M","增量");
			}else if(obj.get("UPLOAD_M").equals("1")){
				obj.put("UPLOAD_M","全量");
			}else if(obj.get("UPLOAD_M").equals("2")){
				obj.put("UPLOAD_M","删除");
			}
			
			for (Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(obj.get(key)==null){
					obj.put(key, "");
				}
			}
		}
//		listNullToString(list);
		JSONObject reusltObj=new JSONObject();
		page.refreshPageNum();
		reusltObj.put("page", page.getStartIndex()); 
	    reusltObj.put("total", page.getTotalPages());
		reusltObj.put("rows", list); 
	    reusltObj.put("records", page.getTotalRecs());
	   
		return reusltObj;
	}
	
	/**
	 * 导出excel文件
	 * @param request
	 * @return
	 */
	@RequestMapping(params="method=expMetadata")
	@ResponseBody
	public void expMetadata(HttpServletRequest request,HttpServletResponse response){
		PageResults page = ControllerHelper.buildPage(request);
		page.setParameter("endDate",DateUtil.getYmdDate());
		log.info("导出元数据excel开始！");
		page.setPageSize(60000);
		List<MetadataDatabaseVO> dbList = metaVerService.getVerDataBaseMeta(page);
		List<MetadataSchemaVO> schList = metaVerService.getVerSchemaMeta(page);
		List<MetadataModuleVO> modList = metaVerService.getVerModuleMeta(page);
		List<MetadataTableVO> tabList = metaVerService.getVerTableMeta(page);
		List<MetadataColumnVO> colList = metaVerService.getVerColumnMeta(page);
		log.info("查询导出元数据excel结束！");
		
		log.info("开始生成元数据excel对象！");
		HSSFWorkbook wbook = new HSSFWorkbook();
		createSysSheet(wbook, dbList);
		createSchemaSheet(wbook, schList);
		createModuleSheet(wbook, modList);
		createTableSheet(wbook, tabList);
		createColumnSheet(wbook, colList);
		log.info("结束生成元数据excel对象！");
		response.setContentType("application/x-download");
		response.setHeader("Content-disposition", "attachment;filename=MetaData.xls");  
		OutputStream ouputStream = null;
		try {
		  	ouputStream = response.getOutputStream();  
	        wbook.write(ouputStream);  
	        ouputStream.flush();  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(ouputStream!=null)
					ouputStream.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	public void createSysSheet(HSSFWorkbook wbook,List<MetadataDatabaseVO> sysList){
		HSSFSheet sheet = wbook.createSheet("系统元数据");
		// 设置系统级Title
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString("系统标识"));
		cell = row.createCell(1);
		cell.setCellValue(new HSSFRichTextString("系统中文名称"));
		cell = row.createCell(2);
		cell.setCellValue(new HSSFRichTextString("所属开发科室"));
		cell = row.createCell(3);
		cell.setCellValue(new HSSFRichTextString("开发负责人"));
		cell = row.createCell(4);
		cell.setCellValue(new HSSFRichTextString("开发商"));
		cell = row.createCell(5);
		cell.setCellValue(new HSSFRichTextString("DBA"));
		cell = row.createCell(6);
		cell.setCellValue(new HSSFRichTextString("功能简介"));
		
		for (int i = 0 ; i < sysList.size(); i ++) {
			MetadataDatabaseVO sysMd = sysList.get(i);
			HSSFRow row1 = sheet.createRow(i+1);
			cell = row1.createCell(0);
			cell.setCellValue(new HSSFRichTextString(sysMd.getDbId()));
			cell = row1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(sysMd.getDbChName()));
			cell = row1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(sysMd.getDept()));
			cell = row1.createCell(3);
			cell.setCellValue(new HSSFRichTextString(sysMd.getDevloper()));
			cell = row1.createCell(4);
			cell.setCellValue(new HSSFRichTextString(sysMd.getProMFac()));
			cell = row1.createCell(5);
			cell.setCellValue(new HSSFRichTextString(sysMd.getDba()));
			cell = row1.createCell(6);
			cell.setCellValue(new HSSFRichTextString(sysMd.getRemark()));
		}
	}
	public void createSchemaSheet(HSSFWorkbook wbook,List<MetadataSchemaVO> schemaList){
		HSSFSheet sheet = wbook.createSheet("模式元数据");
		// 设置系统级Title
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString("系统标识"));
		cell = row.createCell(1);
		cell.setCellValue(new HSSFRichTextString("模式名"));
		cell = row.createCell(2);
		cell.setCellValue(new HSSFRichTextString("模式中文名称"));
		cell = row.createCell(3);
		cell.setCellValue(new HSSFRichTextString("总表数"));
		cell = row.createCell(4);
		cell.setCellValue(new HSSFRichTextString("模式描述"));
		cell = row.createCell(5);
		
		for (int i = 0 ; i < schemaList.size(); i ++) {
			MetadataSchemaVO schema = schemaList.get(i);
			HSSFRow row1 = sheet.createRow(i+1);
			cell = row1.createCell(0);
			cell.setCellValue(new HSSFRichTextString(schema.getDbId()));
			cell = row1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(schema.getSchname()));
			cell = row1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(schema.getSchChName()));
			cell = row1.createCell(3);
			cell.setCellValue(new HSSFRichTextString(schema.getTabCnt()));
			cell = row1.createCell(4);
			cell.setCellValue(new HSSFRichTextString(schema.getRemark()));
		}
		
	}
	public void createModuleSheet(HSSFWorkbook wbook,List<MetadataModuleVO> moduleList){
		HSSFSheet sheet = wbook.createSheet("模块元数据");
		// 设置系统级Title
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString("系统标识"));
		cell = row.createCell(1);
		cell.setCellValue(new HSSFRichTextString("模式名"));
		cell = row.createCell(2);
		cell.setCellValue(new HSSFRichTextString("模块名"));
		cell = row.createCell(3);
		cell.setCellValue(new HSSFRichTextString("模块中文名称"));
		cell = row.createCell(4);
		cell.setCellValue(new HSSFRichTextString("开发负责联系人"));
		cell = row.createCell(5);
		cell.setCellValue(new HSSFRichTextString("SA"));
		cell = row.createCell(6);
		cell.setCellValue(new HSSFRichTextString("模块识别表达式"));
		cell = row.createCell(7);
		cell.setCellValue(new HSSFRichTextString("描述"));
		cell = row.createCell(8);
		cell.setCellValue(new HSSFRichTextString("总表数"));
		
		for (int i = 0 ; i < moduleList.size(); i ++) {
			MetadataModuleVO module = moduleList.get(i);
			HSSFRow row1 = sheet.createRow(i+1);
			cell = row1.createCell(0);
			cell.setCellValue(new HSSFRichTextString(module.getDbId()));
			cell = row1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(module.getSchname()));
			cell = row1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(module.getModName()));
			cell = row1.createCell(3);
			cell.setCellValue(new HSSFRichTextString(module.getModChName()));
			cell = row1.createCell(4);
			cell.setCellValue(new HSSFRichTextString(module.getDevloper()));
			cell = row1.createCell(5);
			cell.setCellValue(new HSSFRichTextString(module.getSa()));
			cell = row1.createCell(6);
			cell.setCellValue(new HSSFRichTextString(module.getModPtn()));
			cell = row1.createCell(7);
			cell.setCellValue(new HSSFRichTextString(module.getRemark()));
			cell = row1.createCell(8);
			cell.setCellValue(new HSSFRichTextString(module.getTabCnt()));
		}
	}
	public void createTableSheet(HSSFWorkbook wbook,List<MetadataTableVO> tableList){
		HSSFSheet sheet = wbook.createSheet("表级元数据");
		// 设置系统级Title
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString("系统标识"));
		cell = row.createCell(1);
		cell.setCellValue(new HSSFRichTextString("模式名"));
		cell = row.createCell(2);
		cell.setCellValue(new HSSFRichTextString("模块名"));
		cell = row.createCell(3);
		cell.setCellValue(new HSSFRichTextString("表英文名"));
		cell = row.createCell(4);
		cell.setCellValue(new HSSFRichTextString("表中文名"));
		cell = row.createCell(5);
		cell.setCellValue(new HSSFRichTextString("所属表空间"));
		cell = row.createCell(6);
		cell.setCellValue(new HSSFRichTextString("主键字段"));
		cell = row.createCell(7);
		cell.setCellValue(new HSSFRichTextString("外键字段"));
		cell = row.createCell(8);
		cell.setCellValue(new HSSFRichTextString("是否关键表"));
		cell = row.createCell(9);
		cell.setCellValue(new HSSFRichTextString("生命周期说明"));
		cell = row.createCell(10);
		cell.setCellValue(new HSSFRichTextString("描述"));
		
		for (int i = 0 ; i < tableList.size(); i ++) {
			MetadataTableVO table = tableList.get(i);
			HSSFRow row1 = sheet.createRow(i+1);
			cell = row1.createCell(0);
			cell.setCellValue(new HSSFRichTextString(table.getDbId()));
			cell = row1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(table.getSchname()));
			cell = row1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(table.getModName()));
			cell = row1.createCell(3);
			cell.setCellValue(new HSSFRichTextString(table.getTabName()));
			cell = row1.createCell(4);
			cell.setCellValue(new HSSFRichTextString(table.getTabChName()));
			cell = row1.createCell(5);
			cell.setCellValue(new HSSFRichTextString(table.getTabspaceName()));
			cell = row1.createCell(6);
			cell.setCellValue(new HSSFRichTextString(table.getPkCols()));
			cell = row1.createCell(7);
			cell.setCellValue(new HSSFRichTextString(table.getFkCols()));
			cell = row1.createCell(8);
			cell.setCellValue(new HSSFRichTextString(table.getImpFlag()));
			cell = row1.createCell(9);
			cell.setCellValue(new HSSFRichTextString(table.getLcycDesc()));
			cell = row1.createCell(10);
			cell.setCellValue(new HSSFRichTextString(table.getRemark()));
		}
	}
	
	public void createColumnSheet(HSSFWorkbook wbook,List<MetadataColumnVO> columnList){
		HSSFSheet sheet = wbook.createSheet("字段级元数据");
		// 设置系统级Title
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString("系统标识"));
		cell = row.createCell(1);
		cell.setCellValue(new HSSFRichTextString("模式名"));
		cell = row.createCell(2);
		cell.setCellValue(new HSSFRichTextString("模块名"));
		cell = row.createCell(3);
		cell.setCellValue(new HSSFRichTextString("表英文名"));
		cell = row.createCell(4);
		cell.setCellValue(new HSSFRichTextString("字段英文名"));
		cell = row.createCell(5);
		cell.setCellValue(new HSSFRichTextString("字段中文名"));
		cell = row.createCell(6);
		cell.setCellValue(new HSSFRichTextString("字段类型"));
		cell = row.createCell(7);
		cell.setCellValue(new HSSFRichTextString("字段序号"));
		cell = row.createCell(8);
		cell.setCellValue(new HSSFRichTextString("主键"));
		cell = row.createCell(9);
		cell.setCellValue(new HSSFRichTextString("是否允许空值"));
		cell = row.createCell(10);
		cell.setCellValue(new HSSFRichTextString("是否代码字段"));
		cell = row.createCell(11);
		cell.setCellValue(new HSSFRichTextString("是否有索引"));
		cell = row.createCell(12);
		cell.setCellValue(new HSSFRichTextString("引用代码表"));
		cell = row.createCell(13);
		cell.setCellValue(new HSSFRichTextString("描述"));
		
		for (int i = 0 ; i < columnList.size(); i ++) {
			MetadataColumnVO column = columnList.get(i);
			HSSFRow row1 = sheet.createRow(i+1);
			cell = row1.createCell(0);
			cell.setCellValue(new HSSFRichTextString(column.getDbId()));
			cell = row1.createCell(1);
			cell.setCellValue(new HSSFRichTextString(column.getSchname()));
			cell = row1.createCell(2);
			cell.setCellValue(new HSSFRichTextString(column.getModName()));
			cell = row1.createCell(3);
			cell.setCellValue(new HSSFRichTextString(column.getTabName()));
			cell = row1.createCell(4);
			cell.setCellValue(new HSSFRichTextString(column.getColName()));
			cell = row1.createCell(5);
			cell.setCellValue(new HSSFRichTextString(column.getColChName()));
			cell = row1.createCell(6);
			cell.setCellValue(new HSSFRichTextString(column.getColType()));
			cell = row1.createCell(7);
			cell.setCellValue(new HSSFRichTextString(column.getColSeq()));
			cell = row1.createCell(8);
			cell.setCellValue(new HSSFRichTextString(column.getPkFlag()));
			cell = row1.createCell(9);
			cell.setCellValue(new HSSFRichTextString(column.getNvlFlag()));
			cell = row1.createCell(10);
			cell.setCellValue(new HSSFRichTextString(column.getCcolFlag()));
			cell = row1.createCell(11);
			cell.setCellValue(new HSSFRichTextString(column.getIndxFlag()));
			cell = row1.createCell(12);
			cell.setCellValue(new HSSFRichTextString(column.getCodeTab()));
			cell = row1.createCell(13);
			cell.setCellValue(new HSSFRichTextString(column.getRemark()));
		}
	}
	
	
}
