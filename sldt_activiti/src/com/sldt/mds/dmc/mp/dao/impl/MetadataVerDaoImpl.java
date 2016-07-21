package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.sldt.framework.common.PageResults;
import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.MetadataVerDao;
import com.sldt.mds.dmc.mp.util.DateUtil;
import com.sldt.mds.dmc.mp.util.PropertiesUtil;

@SuppressWarnings("unchecked")
@Repository(value="metadataVerDao")
public class MetadataVerDaoImpl extends BaseDao implements MetadataVerDao{

	private static final Log log = LogFactory.getLog(MetadataVerDaoImpl.class);
	
	@Override
	public List<Map<String, Object>> getMyVersionMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			if(page == null){
				return dataMap;
			}
			String sql = "";
			boolean isAdmin = false;
			if(page.getParameter("isAdmin")!=null){
				isAdmin = (Boolean)page.getParameter("isAdmin");
			}
			boolean isCard = false;
			if(page.getParameter("isCard")!=null){
				isCard = (Boolean)page.getParameter("isCard");
			}
			//用户ID
			String userId = page.getParamStr("userId");
			String endDate = page.getParamStr("endDate");
			String allMyVer = page.getParamStr("allMyVer");
			String dbName = (String)PropertiesUtil.props.getProperty("dbName");
			if(allMyVer!=null&&allMyVer.equals("true")){
				sql = "select I.DB_CODE,I.DBCHNAME,I.SNA_NAMESPACE from T_MP_META_SYS_INVENTORY I left join t_mp_meta_int_db_info d on i.db_code = d.db_code and d.f_startdate>'"+endDate+"' where 1=1  ";
				if(userId !=null&&!"".equals(userId)&&!isAdmin){
					sql += " and I.DB_USER = '"+userId+"' ";
				}
			}else{
				if(isCard){
					sql = "select (case when d.DB_CODE is null or d.DB_CODE='' then i.DB_CODE else d.DB_CODE end) DB_CODE,(case when d.DBCHNAME is null or d.DBCHNAME='' then i.DBCHNAME else d.DBCHNAME end) DBCHNAME,(case when d.DB_CODE is null or d.DB_CODE='' then '1' else '0' end) isSnaCode,d.DEPT,d.DEVLOPER,d.MAINTAINER,d.DBA,d.REMARK,d.TOTALSIZE,d.USEDSIZE,d.TABCNT,d.OBJCNT,d.F_STARTDATE,d.F_ENDDATE,d.PRO_M_FAC,d.IMGURL,u.NAME,u.USER_ID,  (select t1.ALT_OPER_DATE from (select i.ALT_OPER_DATE,i.DB_CODE from t_mp_init_declare_info i where i.ACT_STS = '2' order by i.ALT_OPER_DATE desc) t1 LIMIT 1 ) ALT_OPER_DATE from T_MP_META_SYS_INVENTORY I left join t_mp_meta_int_db_info d on i.DB_CODE =d.DB_CODE and d.F_ENDDATE ='20991231' , "+dbName+".t_sm_user u   where  i.DB_USER = u.USER_ID ";
				}else{
					String isnew = page.getParamStr("isnew");
					sql = "select d.*,u.name NAME,u.user_id USER_ID,  (select t1.alt_oper_date from (select i.alt_oper_date,i.db_code from t_mp_init_declare_info i where i.act_sts = '2' order by i.alt_oper_date desc)as t1 LIMIT 1 ) alt_oper_date from T_MP_META_SYS_INVENTORY I , t_mp_meta_int_db_info d,"+dbName+".t_sm_user u where   I.DB_CODE = D.DB_CODE and i.db_user = u.user_id ";
					
					if(isnew!=null&&!"".equals(isnew)){
						sql += " and d.f_enddate = '20991231' ";
					}else{
						sql += " and d.f_enddate >'"+endDate+"' ";
					}
				}
				if(userId !=null&&!"".equals(userId)&&!isAdmin){
					sql += " and I.DB_USER='"+userId+"' ";
				}
				sql+=" order by alt_oper_date desc ";
			}
			dataMap = this.queryListForMap(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public Map<String, Object> getCurrVerMap(PageResults page) {
		Map<String,Object> dataMap = new HashMap<String, Object>();
		try {
			if(page==null)
				return dataMap;
			String dbId = page.getParamStr("dbId");
			String verDate = page.getParamStr("VER_DATE");
			if(dbId==null)
				return dataMap;
			String sql = "select * from T_MP_ALTERATION_REGISTER_INFO R,T_MP_VERSION_DEFINE V WHERE R.ALT_STS = '0' AND R.ALT_SYS_CODE = '"+dbId+"' AND V.OPT_ID = R.ALT_VER_DATE_NO ";
			if(StringUtils.isNotBlank(verDate)){
				sql += " and  R.ALT_VER_DATE_NO = '"+verDate+"'";
			}
			sql += " ORDER BY R.ALT_VER_DATE_NO DESC";
			
			sql = "select * from ("+sql+")as T 	LIMIT 1";
			
			List<Map<String,Object>> list = this.queryListForMap(sql);
			if(list.size()>0){
				dataMap = list.get(0);
			}else{
				if(StringUtils.isBlank(verDate)){
					sql = "select * from T_MP_META_INT_DB_INFO A WHERE A.DB_CODE = '"+dbId+"'";
					list = queryListForMap(sql);
					if(list.size()>0){
						sql = "select '' CUR_VER_DATE_NO,a.opt_id ALT_VER_DATE_NO,v.* from t_mp_opt_calendar a,T_MP_VERSION_DEFINE V where a.opt_id = '19700101' and v.opt_id = a.opt_id";
						list = queryListForMap(sql);
						if(list.size()>0){
							dataMap = list.get(0);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;

	}

	@Override
	public List<Map<String, Object>> getNewVersionMeta(
			Map<String, String> params) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			String dbId = params.get("dbId");
			String optId = params.get("optId");
			String endDate = params.get("endDate");
			String sql = " select d.*,s.db_user from t_mp_meta_int_db_info d left join t_mp_meta_sys_inventory s on d.db_code = s.db_code where 1=1  ";
			if(StringUtils.isNotBlank(dbId))
				sql += " and d.db_code = '"+dbId+"' ";
			if(StringUtils.isNotBlank(optId)){
				sql+= " and d.f_startdate <= '"+optId+"' and d.f_enddate > '"+optId+"' ";
			}
			else if(endDate!=null&&!"".equals(endDate)){
				sql += " and d.f_enddate >'"+endDate+"' ";
			}
			dataMap = this.queryListForMap(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getHisVerList(PageResults page) {
		List<Map<String, Object>> dataMap = new ArrayList<Map<String,Object>>();
		if(page == null){
			return dataMap;
		}
		String db_code = page.getParamStr("dbId");
		String currVerDate = page.getParamStr("currVerDate");
		String sql = "select R.ALT_SYS_CODE,R.ALT_VER_DATE_NO,v.ver_name FROM T_MP_ALTERATION_REGISTER_INFO R ,T_MP_VERSION_DEFINE V WHERE 1=1 AND V.OPT_ID = R.ALT_VER_DATE_NO ";
		if(db_code!=null && !"".equals(db_code)){
			sql += " AND R.ALT_SYS_CODE = '"+db_code+"'";
		}
		
		if(currVerDate!=null && !"".equals(currVerDate)){
			sql += " AND R.ALT_VER_DATE_NO < '"+currVerDate+"' ";
		}
		sql +=" GROUP BY R.ALT_SYS_CODE,R.ALT_VER_DATE_NO,v.ver_name order by R.ALT_VER_DATE_NO";
		dataMap = this.queryListForMap(sql);
		return dataMap;
	}

	@Override
	public Map<String, Object> getInitVer(String dbId) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String sql = "select * from T_MP_META_INT_DB_INFO A WHERE A.DB_CODE = '"+dbId+"'";
        List<Map<String, Object>> list = this.queryListForMap(sql);
        if(list.size()>0){
        	sql = "select '' CUR_VER_DATE_NO,a.opt_id ALT_VER_DATE_NO,v.* from t_mp_opt_calendar a,T_MP_VERSION_DEFINE V where a.opt_id = '19700101' and v.opt_id = a.opt_id";
        	list = this.queryListForMap(sql);
        	if(list.size()>0){
				dataMap = list.get(0);
			}
        }
		return dataMap;
	}
	
    //获取当前开发版本
	@Override
	public Map<String, Object> getCurDeveVer(PageResults page) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> forMap = new HashMap<String, Object>();
        Map<String, Object> curMap = new HashMap<String, Object>();
        List<Map<String, Object>> forvList2 = new ArrayList<Map<String,Object>>();
        
        String currVerDate = (String) curMap.get("ALT_VER_DATE_NO");
        page.setParameter("currVerDate", currVerDate);
        List<Map<String, Object>> forvList = getForAndDeveVerList(page);
        if(forvList.size()>0){
        	forMap = forvList.get(0);
        }
        if(currVerDate==null){
        	currVerDate = "";
        	returnMap.put("curVer", dataMap);//当前开发
        	returnMap.put("forvList", forvList2);//远期
        	return returnMap;
        }
        if("19700101".equals(currVerDate)){
        	dataMap = curMap;
        }else{
        	//当前的时间点-3天小于等于当前投产版本，则当前开发版本等于当前版本
        	if(Integer.parseInt(DateUtil.getBeforeDate())<=Integer.parseInt(currVerDate)){
        		dataMap = curMap;
        		forvList2 = forvList;
        	}else{
        		dataMap = forMap;
        		for(int i = 1; i<forvList.size();i++){
        			forvList2.add(forvList.get(i));
        		}
        	}
        }
        returnMap.put("curVer", dataMap);//当前开发
        returnMap.put("forvList", forvList2);//远期
        
		return returnMap;
	}
	
	public List<Map<String, Object>> getForAndDeveVerList(PageResults page){
		List<Map<String, Object>> dataMap = new ArrayList<Map<String,Object>>();
		if(page == null){
			return dataMap;
		}
		String db_code = page.getParamStr("dbId");
		String currVerDate = page.getParamStr("currVerDate");
		String sql = "SELECT R.ALT_SYS_CODE,R.ALT_VER_DATE_NO,v.ver_name FROM T_MP_ALTERATION_REGISTER_INFO R,T_MP_VERSION_DEFINE V WHERE 1=1 AND V.OPT_ID = R.ALT_VER_DATE_NO";
		
		if(db_code!=null && !"".equals(db_code)){
			sql += " AND R.ALT_SYS_CODE = '"+db_code+"' ";
			
		}
		if(currVerDate!=null && !"".equals(currVerDate)){
			sql += " AND R.ALT_VER_DATE_NO > '"+currVerDate+"' ";
		}
		
		sql += " GROUP BY R.ALT_SYS_CODE,R.ALT_VER_DATE_NO,v.ver_name order by R.ALT_VER_DATE_NO ";
		dataMap = this.queryListForMap(sql);
		
		return dataMap;
		
	}

	@Override
	public int getVerSchemaMetaCount(PageResults page) {
		int count = 0;
		try {
			if(page==null)
				return 0;
				String dbId = page.getParamStr("dbId");
				String endDate = page.getParamStr("endDate");
				String optId = page.getParamStr("optId");
				String sql = "select * from t_mp_meta_int_sch_info b where b.db_code = '"+dbId+"' ";
				sql+= getVerDateWhere(endDate, optId);
				
				sql = "select count(1) sum from ("+sql+") a";
				count = this.countSQL(sql, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public String getVerDateWhere(String endDate,String optId){
		String sql = "";
		if(StringUtils.isNotBlank(optId) && optId!=null && !"null".equals(optId) &&!"".equals(optId) &&!"19700101".equals(optId)){
			sql +=" and b.f_startdate <= '"+optId+"' and b.f_enddate > '"+optId+"' ";
		}else{
			sql +=" and b.f_enddate > '"+endDate+"' ";
		}
		return sql;
	}

	@Override
	public int getVerModuleMetaCount(PageResults page) {
        int count = 0;
        try {
			if(page==null)
				return 0;
			String dbId = page.getParamStr("dbId");
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String sql = "select * from t_mp_meta_int_blk_info b where b.db_code = '"+dbId+"' ";
			sql+= getVerDateWhere(endDate, optId);
			sql = "select count(1) sum from ("+sql+") a"; 
			count = countSQL(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int getVerTableMetaCount(PageResults page) {
		int count = 0;
		try {
			if(page==null)
				return 0;
			String dbId = page.getParamStr("dbId");
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String schemaId = page.getParamStr("schemaId");
			String moduleId = page.getParamStr("moduleId");
			String searchText = page.getParamStr("searchText");
			String sql = "select * from t_mp_meta_int_tab_info b where b.db_code = '"+dbId+"' ";
			if(StringUtils.isNotBlank(searchText)){
				sql += " and (tabname like '%"+searchText+"%' or tabchname like '%"+searchText+"%') ";
			}
			if(StringUtils.isNotBlank(schemaId)){
				sql += " and schname = '"+schemaId+"'";
			}
			if(StringUtils.isNotBlank(moduleId)){
				sql += "  and modname = '"+moduleId+"' ";
			}
			
			sql+= getVerDateWhere(endDate, optId);
			sql = "select count(1) sum from ("+sql+") a"; 
			count = countSQL(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int getVerColumnMetaCount(PageResults page) {
		int count = 0;
		try {
			if(page==null)
				return 0;
			
			String dbId = page.getParamStr("dbId");
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String schemaId = page.getParamStr("schemaId");
			String moduleId = page.getParamStr("moduleId");
			String tableId = page.getParamStr("tableId");
			String searchText = page.getParamStr("searchText");
			String tabSearchText = page.getParamStr("tabSearcharText");
			String sql = " select * from t_mp_meta_int_col_info b where b.db_code = '"+dbId+"' ";
			if(StringUtils.isNotBlank(searchText)){
				sql += " and (colName like '%"+searchText+"%' or colchname like '%"+searchText+"%') ";
			}
				
			if(StringUtils.isNotBlank(schemaId)){
				sql += " and schname = '"+schemaId+"' ";
			}
			
			if(StringUtils.isNotBlank(moduleId)){
				sql += " and modname = '"+moduleId+"' ";
			}
			
			if(StringUtils.isNotBlank(tableId)){
				sql += " and tabname='"+tableId+"' ";
			}
			
			if(StringUtils.isNotBlank(tabSearchText)){
				sql += " and tabname like '"+tabSearchText+"%' ";
			}
			
			sql+= getVerDateWhere(endDate, optId);
			
			sql = "select count(1) sum from ("+sql+") a"; 
			count = countSQL(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<Map<String, Object>> getVerDataBaseMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			if(page==null)
				return dataMap;
			String dbId = page.getParamStr("dbId");
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String sql = "select * from T_MP_META_INT_DB_INFO b where 1=1  ";
			if(dbId!=null){
				sql += "  and b.db_code = '"+dbId+"' ";
			}
			sql+= getVerDateWhere(endDate, optId);
			dataMap = this.queryListForMap(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getVerSchemaMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			String dbId = page.getParamStr("dbId");
			if(dbId==null||"".equals(dbId)){
				return dataMap;
			}
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String schemaId = page.getParamStr("schemaId");
			String sql = "select * from t_mp_meta_int_sch_info b where upper(b.db_code) = '"+dbId.toUpperCase()+"'";
			if(schemaId!=null&&!schemaId.equals("")){
				sql += " and upper(SCHNAME) = '"+schemaId.toUpperCase()+"'";
			}
			sql+= getVerDateWhere(endDate, optId);
			dataMap = this.queryListForMap(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getVerModuleMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			String dbId = page.getParamStr("dbId");
			if(dbId==null||"".equals(dbId)){
				return dataMap;
			}
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String schemaId = page.getParamStr("schemaId");
			String moduleId = page.getParamStr("moduleId");
			String sql = "select * from t_mp_meta_int_blk_info b where upper(b.db_code) = '"+dbId.toUpperCase()+"'";
			if(schemaId!=null&&!schemaId.equals("")){
				sql += " and upper(SCHNAME) = '"+schemaId.toUpperCase()+"'";
			}
			if(moduleId!=null&&!moduleId.equals("")){
				sql += " and upper(MODNAME) = '"+moduleId.toUpperCase()+"'";
			}
			sql+= getVerDateWhere(endDate, optId);
			dataMap = this.queryListForMap(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getVerTableMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			String dbId = page.getParamStr("dbId");
			if(dbId==null||"".equals(dbId)){
				return dataMap;
			}
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String schemaId = page.getParamStr("schemaId");
			String moduleId = page.getParamStr("moduleId");
			String searchText = page.getParamStr("searchText");
			String tableId = page.getParamStr("tableId");
			String sql = " select * from t_mp_meta_int_tab_info b where upper(b.db_code) = '"+dbId.toUpperCase()+"' ";
			
			if(StringUtils.isNotBlank(searchText)){
				sql += " and (tabname like '%"+searchText+"%' or tabchname like '%"+searchText+"%') ";
			}
			if(schemaId!=null&&!schemaId.equals("")){
				sql += " and upper(SCHNAME) = '"+schemaId.toUpperCase()+"'";
			}
			if(moduleId!=null&&!moduleId.equals("")){
				sql += " and upper(MODNAME) = '"+moduleId.toUpperCase()+"'";
			}
			if(tableId!=null&&!tableId.equals("")){
				sql += " and upper(TABNAME) = '"+tableId.toUpperCase()+"'";
			}
			
			sql+= getVerDateWhere(endDate, optId);
			
			dataMap = this.queryPageMap(sql, null, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getVerColumnMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			String dbId = page.getParamStr("dbId");
			if(dbId==null||"".equals(dbId)){
				return dataMap;
			}
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String schemaId = page.getParamStr("schemaId");
			String moduleId = page.getParamStr("moduleId");
			String tableId = page.getParamStr("tableId");
			String searchText = page.getParamStr("searchText");
			String tabSearchText = page.getParamStr("tabSearcharText");
			String columnId = page.getParamStr("columnId");
			
			String sql = " select * from t_mp_meta_int_col_info b where b.db_code = '"+dbId+"'";
			
			if(StringUtils.isNotBlank(searchText)){
				sql += " and (colName like '%"+searchText+"%' or colchname like '%"+searchText+"%') ";
			}
				
			if(schemaId!=null&&!schemaId.equals("")){
				sql += " and upper(SCHNAME) = '"+schemaId.toUpperCase()+"'";
			}
			if(moduleId!=null&&!moduleId.equals("")){
				sql += " and upper(MODNAME) = '"+moduleId.toUpperCase()+"'";
			}
			if(tableId!=null&&!tableId.equals("")){
				sql += " and upper(TABNAME) = '"+tableId.toUpperCase()+"'";
			}
			if(columnId!=null&&!columnId.equals("")){
				sql += " and upper(COLNAME) = '"+columnId.toUpperCase()+"'";
			}
			
			if(StringUtils.isNotBlank(tabSearchText)){
				sql += " and tabname like '"+tabSearchText+"%' ";
			}
			sql+= getVerDateWhere(endDate, optId);
			
			sql = " select * from ("+sql+") a1 order by schname, modname, tabname,  CAST(COLSEQ AS signed) ";
			dataMap = this.queryPageMap(sql, null, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getVerColCodeMeta(PageResults page) {
		String endDate = page.getParamStr("endDate");
		String optId = page.getParamStr("optId");
		String dbCode = page.getParamStr("dbCode");
		String schName = page.getParamStr("schName");
		String modName = page.getParamStr("modName");
		String tabName = page.getParamStr("tabName");
		String colName = page.getParamStr("colName");
		String codeVal = page.getParamStr("codeVal");
		String sql = "SELECT * FROM T_MP_META_INT_COL_CODE b WHERE 1=1 ";
		
		if(dbCode!=null&&!"".equals(dbCode)){
			sql+=" AND UPPER(DB_CODE) = '"+dbCode.toUpperCase()+"' ";
		}
		if(schName!=null&&!"".equals(schName)){
			sql+=" AND UPPER(SCHNAME) = '"+schName.toUpperCase()+"' ";
		}
		if(modName!=null&&!"".equals(modName)){
			sql+=" AND UPPER(MODNAME) = '"+modName.toUpperCase()+"' ";
		}
		if(tabName!=null&&!"".equals(tabName)){
			sql+=" AND UPPER(TABNAME) = '"+tabName.toUpperCase()+"' ";
		}
		if(colName!=null&&!"".equals(colName)){
			sql+=" AND UPPER(COLNAME) = '"+colName.toUpperCase()+"' ";
		}
		if(codeVal!=null&&!"".equals(codeVal)){
			sql+=" AND UPPER(CODE_VAL) = '"+codeVal.toUpperCase()+"' ";
		}
		
		sql += getVerDateWhere(endDate, optId);
		
		return this.queryListForMap(sql);
	}

	@Override
	public List<Map<String, Object>> getVerIntFileMeta(PageResults page) {
		String endDate = page.getParamStr("endDate");
		String optId = page.getParamStr("optId");
		String dbId = page.getParamStr("dbId");
		String refDbCode = page.getParamStr("refDbCode");
		String refSchema = page.getParamStr("refSchema");
		String refModName = page.getParamStr("refModName");
		String refTabName = page.getParamStr("refTabName");
		String cFuncName = page.getParamStr("cFuncName");
		String cType = page.getParamStr("cType");
		
		String sql = " SELECT * FROM T_MP_META_INT_FILE b WHERE 1=1 ";
		
		if(dbId!=null&&!"".equals(dbId)){
			sql+=" AND UPPER(DB_CODE) = '"+dbId.toUpperCase()+"' ";
		}
		if(refDbCode!=null&&!"".equals(refDbCode)){
			sql+=" AND UPPER(REF_DB_CODE) = '"+refDbCode.toUpperCase()+"' ";
		}
		if(refSchema!=null&&!"".equals(refSchema)){
			sql+=" AND UPPER(REF_SCHNAME) = '"+refSchema.toUpperCase()+"' ";
		}
		if(refModName!=null&&!"".equals(refModName)){
			sql+=" AND UPPER(REF_MODNAME) = '"+refModName.toUpperCase()+"' ";
		}
		if(refTabName!=null&&!"".equals(refTabName)){
			sql+=" AND UPPER(REF_TABNAME) = '"+refTabName.toUpperCase()+"' ";
		}
		if(cFuncName!=null&&!"".equals(cFuncName)){
			sql+=" AND UPPER(C_FUNC_NAME) = '"+cFuncName.toUpperCase()+"' ";
		}
		if(cType!=null&&!"".equals(cType)){
			sql+=" AND UPPER(C_TYPE) = '"+cType.toUpperCase()+"' ";
		}
		sql += getVerDateWhere(endDate, optId);
		
		return this.queryListForMap(sql);
	}

	@Override
	public void insMetaTmpByExcData(PageResults page) {
		//变更的元数据集合
		List<Map<String,String>> altList = (List<Map<String,String>>)page.getParameter("altMeta");
		String sql = "";
		for (Map<String, String> altMetaMap : altList) {
			String classiferType = altMetaMap.get("CLASSIFER_TYPE");
			if(classiferType.equals("DATABASE")){
				sql = "INSERT INTO T_MP_META_INT_DB_INFO_TMP (ID,DB_CODE,DBCHNAME,DEPT,DEVLOPER,MAINTAINER,DBA,REMARK,IMGURL,PRO_M_FAC,TOTALSIZE,USEDSIZE,TABCNT,OBJCNT)";
					sql += " VALUES(";
					sql += "'"+altMetaMap.get("ID")+"',";
					sql += "'"+altMetaMap.get("DB_CODE")+"',";
					sql += "'"+altMetaMap.get("DBCHNAME")+"',";
					sql += "'"+altMetaMap.get("DEPT")+"',";
					sql += "'"+altMetaMap.get("DEVLOPER")+"',";
					sql += "'"+altMetaMap.get("MAINTAINER")+"',";
					sql += "'"+altMetaMap.get("DBA")+"',";
					sql += "'"+altMetaMap.get("REMARK")+"',";
					sql += "'"+altMetaMap.get("IMGURL")+"',";
					sql += "'"+altMetaMap.get("PRO_M_FAC")+"',";
					sql += "'"+altMetaMap.get("TOTALSIZE")+"',";
					sql += "'"+altMetaMap.get("USEDSIZE")+"',";
					sql += "'"+altMetaMap.get("TABCNT")+"',";
					sql += "'"+altMetaMap.get("OBJCNT")+"'";
					sql += ")";
				}else if(classiferType.equals("SCHEMA")){
					sql = "INSERT INTO T_MP_META_INT_SCH_INFO_TMP (ID,DB_CODE,SCHNAME,SCHCHNAME,DEVLOPER,REMARK,USEDSIZE,TABCNT,OBJCNT)";
					sql += " VALUES(";
					sql += "'"+altMetaMap.get("ID")+"',";
					sql += "'"+altMetaMap.get("DB_CODE")+"',";
					sql += "'"+altMetaMap.get("SCHNAME")+"',";
					sql += "'"+altMetaMap.get("SCHCHNAME")+"',";
					sql += "'"+altMetaMap.get("DEVLOPER")+"',";
					sql += "'"+altMetaMap.get("REMARK")+"',";
					sql += "'"+altMetaMap.get("USEDSIZE")+"',";
					sql += "'"+altMetaMap.get("TABCNT")+"',";
					sql += "'"+altMetaMap.get("OBJCNT")+"'";
					sql += ")";
				}else if(classiferType.equals("MODULE")){
					sql = "INSERT INTO T_MP_META_INT_BLK_INFO_TMP (ID,DB_CODE,SCHNAME,MODNAME,MODCHNAME,DEPT,DEPTCHARGER,DEVLOPER,SA,MODPTN,STATUS,REMARK,USEDSIZE,TABCNT,OBJCNT)";
					sql += " VALUES(";
					sql += "'"+altMetaMap.get("ID")+"',";
					sql += "'"+altMetaMap.get("DB_CODE")+"',";
					sql += "'"+altMetaMap.get("SCHNAME")+"',";
					sql += "'"+altMetaMap.get("MODNAME")+"',";
					sql += "'"+altMetaMap.get("MODCHNAME")+"',";
					sql += "'"+altMetaMap.get("DEPT")+"',";
					sql += "'"+altMetaMap.get("DEPTCHARGER")+"',";
					sql += "'"+altMetaMap.get("DEVLOPER")+"',";
					sql += "'"+altMetaMap.get("SA")+"',";
					sql += "'"+altMetaMap.get("MODPTN")+"',";
					sql += "'"+altMetaMap.get("STATUS")+"',";
					sql += "'"+altMetaMap.get("REMARK")+"',";
					sql += "'"+altMetaMap.get("USEDSIZE")+"',";
					sql += "'"+altMetaMap.get("TABCNT")+"',";
					sql += "'"+altMetaMap.get("OBJCNT")+"'";
					sql += ")";
				}else if(classiferType.equals("TABLE")){
					sql = "INSERT INTO T_MP_META_INT_TAB_INFO_TMP (ID,DB_CODE,SCHNAME,MODNAME,TABNAME,TABCHNAME,TABSPACENAME,PKCOLS,FKCOLS,FKTABLENAME,ROWCOUNT,IMPFLAG,REMARK,ZIPDESC,LCYCDESC,PCNT,TSIZE,CRTDATE,MODIYDATE)";
					sql+= " VALUES(";
					sql+= "'"+altMetaMap.get("ID")+"',";
					sql+= "'"+altMetaMap.get("DB_CODE")+"',";
					sql+= "'"+altMetaMap.get("SCHNAME")+"',";
					sql+= "'"+altMetaMap.get("MODNAME")+"',";
					sql+= "'"+altMetaMap.get("TABNAME")+"',";
					sql+= "'"+altMetaMap.get("TABCHNAME")+"',";
					sql+= "'"+altMetaMap.get("TABSPACENAME")+"',";
					sql+= "'"+altMetaMap.get("PKCOLS")+"',";
					sql+= "'"+altMetaMap.get("FKCOLS")+"',";
					sql+= "'"+altMetaMap.get("FKTABLENAME")+"',";
					sql+= "'"+altMetaMap.get("ROWCOUNT")+"',";
					sql+= "'"+altMetaMap.get("IMPFLAG")+"',";
					sql+= "'"+altMetaMap.get("REMARK")+"',";
					sql+= "'"+altMetaMap.get("ZIPDESC")+"',";
					sql+= "'"+altMetaMap.get("LCYCDESC")+"',";
					sql+= "'"+altMetaMap.get("PCNT")+"',";
					sql+= "'"+altMetaMap.get("TSIZE")+"',";
					sql+= "'"+altMetaMap.get("CRTDATE")+"',";
					sql+= "'"+altMetaMap.get("MODIYDATE")+"'";
					sql+= ")";
				}else if(classiferType.equals("COLUMN")){
					sql = "INSERT INTO T_MP_META_INT_COL_INFO_TMP (ID,DB_CODE,SCHNAME,MODNAME,TABNAME,COLNAME,COLCHNAME,COLTYPE,COLSEQ,PKFLAG,PDKFLAG,NVLFLAG,CCOLFLAG,INDXFLAG,CODETAB,REMARK)";
					sql += " VALUES(";
					sql += "'"+altMetaMap.get("ID")+"',";
					sql += "'"+altMetaMap.get("DB_CODE")+"',";
					sql += "'"+altMetaMap.get("SCHNAME")+"',";
					sql += "'"+altMetaMap.get("MODNAME")+"',";
					sql += "'"+altMetaMap.get("TABNAME")+"',";
					sql += "'"+altMetaMap.get("COLNAME")+"',";
					sql += "'"+altMetaMap.get("COLCHNAME")+"',";
					sql += "'"+altMetaMap.get("COLTYPE")+"',";
					sql += "'"+altMetaMap.get("COLSEQ")+"',";
					sql += "'"+altMetaMap.get("PKFLAG")+"',";
					sql += "'"+altMetaMap.get("PDKFLAG")+"',";
					sql += "'"+altMetaMap.get("NVLFLAG")+"',";
					sql += "'"+altMetaMap.get("CCOLFLAG")+"',";
					sql += "'"+altMetaMap.get("INDXFLAG")+"',";
					sql += "'"+altMetaMap.get("CODETAB")+"',";
					sql += "'"+altMetaMap.get("REMARK")+"'";
					sql += ")";
				}
				 executeSql(sql, null);
				}
	}

	@Override
	public List<Map<String, Object>> getMetaVerDate(Map<String, String> params) {
		String sql = "select * from T_MP_OPT_CALENDAR where 1=1";
		String month = params.get("month");
		String year = params.get("year");
		String optId = params.get("optId");
		String currVerOptId = params.get("curVerOptId");
		if(StringUtils.isNotBlank(year)){
			sql +=" and OPT_YAR = '"+year+"'";
		}
		if(StringUtils.isNotBlank(month)){
			sql +=" and OPT_MONTH = '"+month+"'";
		}
		if(StringUtils.isNotBlank(optId)){
			sql += " and opt_id = '"+optId+"' ";
		}
		if(StringUtils.isNotBlank(currVerOptId)){
			sql +=" and opt_id > '"+currVerOptId+"'";
		}
		sql += " order by opt_id ";
		return this.queryListForMap(sql);
	}

	@Override
	public List<Map<String, Object>> getCurrAltBatch(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			if(page==null)
				return dataMap;
			
			String db_code = page.getParamStr("dbId");
			
			String currVerDate = page.getParamStr("optId");
			
			String sql = "select distinct R.ALT_BATCH  from T_MP_ALTERATION_REGISTER_INFO R WHERE 1=1 ";
			if(db_code!=null&&!"".equals(db_code)){
				sql += " AND R.ALT_SYS_CODE = '"+db_code+"' ";
			}
			
			if(currVerDate!=null&&!"".equals(currVerDate)){
				sql += " AND R.ALT_VER_DATE_NO = '"+currVerDate+"' ";
			}
			sql += " ORDER BY R.ALT_BATCH ";
			dataMap = this.queryListForMap(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getProVerSchemaMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			String dbId = page.getParamStr("dbId");
			if(dbId==null||"".equals(dbId)){
				return dataMap;
			}
			String optId = page.getParamStr("optId");
			String schemaId = page.getParamStr("schemaId");
			String table = "T_MP_META_INT_SCH_INFO_SNA";
			if(optId.contains("UAT")){
				table = "T_MP_META_INT_SCH_INFO_UAT";
				dbId = getUATDbCode(dbId);
			}
			String sql = "select * from "+table+" b where upper(b.db_code) = '"+dbId.toUpperCase()+"'";
			if(schemaId!=null&&!schemaId.equals("")){
				sql += " and upper(SCHNAME) = '"+schemaId.toUpperCase()+"'";
			}
			dataMap = this.queryListForMap(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}
	
	public String getUATDbCode(String snaDbId) {
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		String sql = "select * from "+dbName+".t_md_inst a where a.namespace in (select t.uat_namespace from t_mp_meta_sys_inventory t where t.db_code='"+snaDbId+"')";
		List<Map<String, Object>> list = this.queryListForMap(sql);
		String uatDbCode = "";
		if(list.size()>0){
			uatDbCode = (String)list.get(0).get("INST_CODE");
		}
		return uatDbCode;
	}

	@Override
	public List<Map<String, Object>> getProVerTableMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		int count = 0;
		try {
			String dbId = page.getParamStr("dbId");
			if(dbId==null||"".equals(dbId)){
				return dataMap;
			}
			String optId = page.getParamStr("optId");
			String schemaId = page.getParamStr("schemaId");
			String moduleId = page.getParamStr("moduleId");
			String searchText = page.getParamStr("searchText");
			String tableId = page.getParamStr("tableId");
			String table = "T_MP_META_INT_TAB_INFO_SNA";
			if(optId.contains("UAT")){
				table = "T_MP_META_INT_TAB_INFO_UAT";
				dbId = getUATDbCode(dbId);
			}
			String sql = " select * from "+table+" b where upper(b.db_code) = '"+dbId.toUpperCase()+"' ";
			
			if(StringUtils.isNotBlank(searchText)){
				sql += " and (tabname like '%"+searchText+"%' or tabchname like '%"+searchText+"%') ";
			}
			if(schemaId!=null&&!schemaId.equals("")){
				sql += " and upper(SCHNAME) = '"+schemaId.toUpperCase()+"'";
			}
			if(moduleId!=null&&!moduleId.equals("")){
				sql += " and upper(MODNAME) = '"+moduleId.toUpperCase()+"'";
			}
			if(tableId!=null&&!tableId.equals("")){
				sql += " and upper(TABNAME) = '"+tableId.toUpperCase()+"'";
			}
			dataMap = this.queryPageMap(sql, null, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getProVerColumnMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		int count = 0;
		try {
			String dbId = page.getParamStr("dbId");
			if(dbId==null||"".equals(dbId)){
				return dataMap;
			}
			String optId = page.getParamStr("optId");
			String schemaId = page.getParamStr("schemaId");
			String moduleId = page.getParamStr("moduleId");
			String tableId = page.getParamStr("tableId");
			String searchText = page.getParamStr("searchText");
			String tabSearchText = page.getParamStr("tabSearcharText");
			String columnId = page.getParamStr("columnId");
			String table = "T_MP_META_INT_COL_INFO_SNA";
			if(optId.contains("UAT")){
				table = "T_MP_META_INT_COL_INFO_UAT";
				dbId = getUATDbCode(dbId);
			}
			String sql = " select * from "+table+" b where b.db_code = '"+dbId+"'";
			
			if(StringUtils.isNotBlank(searchText)){
				sql += " and (colName like '%"+searchText+"%' or colchname like '%"+searchText+"%') ";
			}
				
			if(schemaId!=null&&!schemaId.equals("")){
				sql += " and upper(SCHNAME) = '"+schemaId.toUpperCase()+"'";
			}
			if(moduleId!=null&&!moduleId.equals("")){
				sql += " and upper(MODNAME) = '"+moduleId.toUpperCase()+"'";
			}
			if(tableId!=null&&!tableId.equals("")){
				sql += " and upper(TABNAME) = '"+tableId.toUpperCase()+"'";
			}
			if(columnId!=null&&!columnId.equals("")){
				sql += " and upper(COLNAME) = '"+columnId.toUpperCase()+"'";
			}
			
			if(StringUtils.isNotBlank(tabSearchText)){
				sql += " and tabname like '"+tabSearchText+"%' ";
			}
			
			sql = " select * from ("+sql+") a1 order by schname, modname, tabname, CAST(COLSEQ AS signed) ";
			dataMap = this.queryPageMap(sql, null, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getCompAllVersionColumnList(PageResults page) {
		//有问题sql full join 
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		String dbId = page.getParamStr("dbId");
		String endDate = page.getParamStr("endDate");
		String schemaId = page.getParamStr("schemaId");
		String moduleId = page.getParamStr("moduleId");
		String searchText = page.getParamStr("column");
		String tabSearchText = page.getParamStr("table");
		String noSameColumn = page.getParamStr("noSameCol");

		String col1 = page.getParamStr("Col_1");
		String col2 = page.getParamStr("Col_2");
		String col3 = page.getParamStr("Col_3");
		String col4 = page.getParamStr("Col_4");
		String col5 = page.getParamStr("Col_5");
		if("true".equals(noSameColumn)&&col1.equals("")){
			return dataMap;
		}
		/**********************************************************column下面的代码比较乱，后期优化************************************************************/
		String compColSql = "";
		String sql = "";
		String sql1 = getCompColumnsql(dbId,col1,endDate,"UNION");
		String sql2 = getCompColumnsql(dbId,col2,endDate,"UNION");//"select c.db_code,c.schname,c.MODNAME,c.TABNAME,c.COLNAME from T_MP_META_INT_COL_INFO_SNA c where c.DB_CODE = '"+dbId+"'";
		String sql3 = getCompColumnsql(dbId,col3,endDate,"UNION");
		String sql4 = getCompColumnsql(dbId,col4,endDate,"UNION");
		String sql5 = getCompColumnsql(dbId,col5,endDate,"UNION");
		sql += sql1;
		if(!sql2.equals("")){
			sql += " UNION "+sql2;
		}
		if(!sql3.equals("")){
			sql += " UNION "+sql3;
		}
		if(!sql4.equals("")){
			sql += " UNION "+sql4;
		}
		if(!sql5.equals("")){
			sql += " UNION "+sql5;
		}
		//都移除，只剩快照
		if(col2.equals("")&&col3.equals("")&&col4.equals("")&&col5.equals("")){
			compColSql = sql;
		}else{	
			if(!col1.equals("")){//第一列存在，需要比较
				boolean has1 = false;
				String has1Sql = compVerColumnHasCaseSql("b","hasc1")+",";
				String selectSql = "select t.DB_CODE, t.schname, t.MODNAME, t.TABNAME,t.colname,hasc1";
		 compColSql = /*"select t.DB_CODE, t.schname, t.MODNAME, t.TABNAME,t.colname,hasc1,hasc2,hasc3,c2mod,c3mod " +*/
		 		" from " +
				"("+sql+") T " ;
				
		 if(!col2.equals("")){
			 has1 = true;
			 selectSql += ",hasc2,c2mod ";
		   compColSql+=" left join (select "+comoVerColumnBaseCaseSql("c2","tabname")
					+comoVerColumnBaseCaseSql("c2","schname")
					+comoVerColumnBaseCaseSql("c2","modname")
					+comoVerColumnBaseCaseSql("c2","db_code")
					+comoVerColumnBaseCaseSql("c2","colname")
					+has1Sql
					+compVerColumnHasCaseSql("c2","hasc2")+","
					+compVerColcaseSql("c2","c2mod")+" from (" +
					"select k.* from"+
					"("+getCompColumnsql(dbId,col1,endDate,"all")+") k left join " +
					"("+getCompColumnsql(dbId,col2,endDate,"all")+") c2 on"
					+whereColSql("k","c2")+
					" union "+
					"select k.* from"+
					"("+getCompColumnsql(dbId,col1,endDate,"all")+") k right join " +
					"("+getCompColumnsql(dbId,col2,endDate,"all")+") c2 on"
				    +whereColSql("k","c2")+
				    ") as bc2 ) t2 on " +
				    whereColSql("t","t2");
			    }
		 if(!col3.equals("")){
			 selectSql += ",hasc3,c3mod ";
			 if(has1){
				 has1Sql = "";
			 }else {
				 has1 = true;
			}
		   compColSql+=" left join (select "+comoVerColumnBaseCaseSql("c1","tabname")
					+comoVerColumnBaseCaseSql("c1","schname")
					+comoVerColumnBaseCaseSql("c1","modname")
					+comoVerColumnBaseCaseSql("c1","db_code")
					+comoVerColumnBaseCaseSql("c1","colname")
					+has1Sql
					+compVerColumnHasCaseSql("c1","hasc3")+","
					+compVerColcaseSql("c1","c3mod")+" from (" +
					"select k.* from"+
					"("+getCompColumnsql(dbId,col1,endDate,"all")+") k left join " +
					"("+getCompColumnsql(dbId,col3,endDate,"all")+") c1 on"
					+whereColSql("k","c1")+
					" union "+
					"select k.* from"+
					"("+getCompColumnsql(dbId,col1,endDate,"all")+") k right join " +
					"("+getCompColumnsql(dbId,col3,endDate,"all")+") c1 on"
				    +whereColSql("k","c1")+
				    ") as bc3 ) t3 on " +
				    whereColSql("t","t3");
			    }
		 if(!col4.equals("")){
			 selectSql += ",hasc4,c4mod ";
			 if(has1){
				 has1Sql = "";
			 }else {
				 has1 = true;
			}
			 compColSql+=" left join (select "+comoVerColumnBaseCaseSql("c4","tabname")
					 +comoVerColumnBaseCaseSql("c4","schname")
					 +comoVerColumnBaseCaseSql("c4","modname")
					 +comoVerColumnBaseCaseSql("c4","db_code")
					 +comoVerColumnBaseCaseSql("c4","colname")
					 +has1Sql
					 +compVerColumnHasCaseSql("c4","hasc4")+","
					 +compVerColcaseSql("c4","c4mod")+" from (" +
					 "select k.* from"+
					 "("+getCompColumnsql(dbId,col1,endDate,"all")+") k left join " +
					 "("+getCompColumnsql(dbId,col4,endDate,"all")+") c4 on"
					 +whereColSql("k","c4")+
					 " union "+
					 "select k.* from"+
					 "("+getCompColumnsql(dbId,col1,endDate,"all")+") k right join " +
					 "("+getCompColumnsql(dbId,col4,endDate,"all")+") c4 on"
					 +whereColSql("k","c4")+
					 ") as bc4 ) t4 on " +
					 whereColSql("t","t4");
		 }
		 if(!col5.equals("")){
			 selectSql += ",hasc5,c5mod ";
			 if(has1){
				 has1Sql = "";
			 }else {
				 has1 = true;
			}
			 compColSql+=" left join (select "+comoVerColumnBaseCaseSql("c5","tabname")
					 +comoVerColumnBaseCaseSql("c5","schname")
					 +comoVerColumnBaseCaseSql("c5","modname")
					 +comoVerColumnBaseCaseSql("c5","db_code")
					 +comoVerColumnBaseCaseSql("c5","colname")
					 +has1Sql
					 +compVerColumnHasCaseSql("c5","hasc5")+","
					 +compVerColcaseSql("c5","c5mod")+" from (" +
					 "select k.* from"+
					 "("+getCompColumnsql(dbId,col1,endDate,"all")+") k left join " +
					 "("+getCompColumnsql(dbId,col5,endDate,"all")+") c5 on "
					 +whereColSql("k","c5")+
					 " union "+
					 "select k.* from"+
					 "("+getCompColumnsql(dbId,col1,endDate,"all")+") k right join " +
					 "("+getCompColumnsql(dbId,col5,endDate,"all")+") c5 on "
					 +whereColSql("k","c5")+
					 ") as bc5) t5 on " +
					 whereColSql("t","t5");
		 };
		 compColSql= selectSql+compColSql;
		}
		}			
		compColSql = "select * from ("+compColSql+") T where 1=1 ";
		if(StringUtils.isNotBlank(searchText)){
			compColSql += " and T.colName like '%"+searchText+"%' ";
		}
			
		if(StringUtils.isNotBlank(schemaId)){
			compColSql += " and T.schname = '"+schemaId+"' ";
		}
		
		if(StringUtils.isNotBlank(moduleId)){
			compColSql += " and T.modname = '"+moduleId+"' ";
		}
		
		if(StringUtils.isNotBlank(tabSearchText)){
			compColSql += " and T.tabname like '"+tabSearchText+"%' ";
		}
		
		if("true".equals(noSameColumn)&&!col1.equals("")){
			if(!col2.equals("")){
				compColSql += " and (c2mod=1 or (c2mod is null and hasc1=1) ";
			}
			if(!col3.equals("")){
				compColSql +="or c3mod=1 or (c3mod is null and hasc1=1)";
			}
			if(!col4.equals("")){
				compColSql +="or c4mod=1 or (c4mod is null and hasc1=1)";
			}
			if(!col5.equals("")){
				compColSql +="or c5mod=1 or (c5mod is null and hasc1=1)";
			}
			compColSql += ")";
		}
		try {
			dataMap = this.queryPageMap(compColSql, null, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}
	
	private String getCompColumnsql(String dbId, String optId, String endDate,String type) {
		if(StringUtils.isBlank(optId)){
			return "";
		}
		String sql = "";
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		if(optId.contains("PRO")||optId.contains("UAT")){
			String table = "T_MP_META_INT_COL_INFO_SNA";
			if(optId.contains("UAT")){
				//table = "T_MP_META_INT_COL_INFO_UAT";
				dbId = getUATDbCode(dbId);
			}
			if(type.equals("UNION")){
				 sql = "select c.db_code,c.schname,c.MODNAME,c.TABNAME,c.COLNAME from "+table+" c where c.DB_CODE = '"+dbId+"'";
				 if(optId.contains("UAT")){
					 sql = "SELECT t1.db_code,u.schname,u.modname,u.tabname,u.COLNAME"+
							 	" FROM T_MP_META_INT_COL_INFO_UAT u LEFT JOIN (select *"+
							 	" from "+dbName+".t_md_inst s, T_MP_META_SYS_INVENTORY I"+
							 	" where s.namespace = i.uat_namespace"+
							 	" and s.inst_code = '"+dbId+"'"+
							 	" and s.class_id = 'PASystem') t1 on u.db_code = t1.inst_code where t1.db_code is not null";
				 }
			}else{
				 sql = "select * from "+table+" c where c.DB_CODE = '"+dbId+"'";
				 if(optId.contains("UAT")){
						sql = "SELECT t1.db_code,u.modname,u.schname,u.tabname,u.COLNAME,u.COLCHNAME,u.COLTYPE,u.COLSEQ,u.PKFLAG,u.NVLFLAG,u.CCOLFLAG,u.INDXFLAG,u.CODETAB,u.REMARK"+
	                         " FROM T_MP_META_INT_COL_INFO_UAT u LEFT JOIN (select *"+
	                         " from "+dbName+".t_md_inst s, T_MP_META_SYS_INVENTORY I"+
	                         " where s.namespace = i.uat_namespace"+
	                         " and s.inst_code = '"+dbId+"'"+
	                         " and s.class_id = 'PASystem') t1 on u.db_code = t1.inst_code where t1.db_code is not null";
					}
			}
		}else{
			if(type.equals("UNION")){
				sql = "select b.db_code,b.MODNAME,b.SCHNAME,b.TABNAME ,b.COLNAME from T_MP_META_INT_COL_INFO b where b.db_code = '"+dbId+"'";
			}else{
				sql = "select * from T_MP_META_INT_COL_INFO b where b.db_code = '"+dbId+"'";
			}
			sql+= getVerDateWhere(endDate, optId);
		}
		return sql;
	
	}
	private String compVerColumnHasCaseSql(String tableName, String hasName) {
		return "(case when "+tableName+".colname is null THEN 0  else 1 end)  "+hasName;
	}
	private String comoVerColumnBaseCaseSql(String tabName, String showName) {
		return "(case when b."+showName+" is not null then b."+showName+" when "+tabName+"."+showName+" is not null then "+tabName+"."+showName+" END ) "+showName+",";
	}
	private String whereColSql(String t1,String t2){
		return  " "+t1+".db_code = "+t2+".db_code"
				+" and "+t1+".schname = "+t2+".schname"
				+" and "+t1+".modname = "+t2+".modname"
				+" and "+t1+".tabname = "+t2+".tabname"
				+" and "+t1+".COLNAME = "+t2+".COLNAME";
	}
	private String compVerColcaseSql(String cTableName,String cNo){
		String sql = " (case when (b.COLNAME = "+cTableName+".COLNAME or (b.COLNAME is null and "+cTableName+".COLNAME is null))"+
				" and( b.COLCHNAME = "+cTableName+".COLCHNAME or (b.COLCHNAME is null and "+cTableName+".COLCHNAME is null))"+
				" and(b.COLTYPE = "+cTableName+".COLTYPE or (b.COLTYPE is null and "+cTableName+".COLTYPE is null))"+
				" and(b.COLSEQ = "+cTableName+".COLSEQ or (b.COLSEQ is null and "+cTableName+".COLSEQ is null))"+
				" and(b.PKFLAG = "+cTableName+".PKFLAG or (b.PKFLAG is null and "+cTableName+".PKFLAG is null))"+
				" and(b.NVLFLAG = "+cTableName+".NVLFLAG or (b.NVLFLAG is null and "+cTableName+".NVLFLAG is null))"+
				" and(b.CCOLFLAG = "+cTableName+".CCOLFLAG or (b.CCOLFLAG is null and "+cTableName+".CCOLFLAG is null))"+
				" and(b.INDXFLAG = "+cTableName+".INDXFLAG or (b.INDXFLAG is null and "+cTableName+".INDXFLAG is null))"+
				" and(b.CODETAB = "+cTableName+".CODETAB or (b.CODETAB is null and "+cTableName+".CODETAB is null))"+
				" and(b.REMARK = "+cTableName+".REMARK or (b.REMARK is null and "+cTableName+".REMARK is null))"+
				" then 0 "+
				" else 1 "+
				" end)  "+cNo;
		return sql;
	}

	@Override
	public List<Map<String, Object>> getCompAllVersionTableList(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		String dbId = page.getParamStr("dbId");
		String schemaId = page.getParamStr("schemaId");
		String moduleId = page.getParamStr("moduleId");
		String searchText = page.getParamStr("table");
		String col1 = page.getParamStr("Col_1");
		String col2 = page.getParamStr("Col_2");
		String col3 = page.getParamStr("Col_3");
		String col4 = page.getParamStr("Col_4");
		String col5 = page.getParamStr("Col_5");
		
		String noSameTable = page.getParamStr("noSameTable");
		
		String endDate = page.getParamStr("endDate");
		if("true".equals(noSameTable)&&col1.equals("")){
			return dataMap;
		}
		/**********************************************************table下面的代码比较乱，时间紧，只实现功能，后期优化************************************************************/
		String compSql = "";
		String sql = "";
		String sql1 = getCompTableSql(dbId,col1,endDate,"UNION");
		String sql2 = getCompTableSql(dbId,col2,endDate,"UNION");//"select c.DB_CODE,c.schname,c.MODNAME,c.TABNAME from t_mp_META_INT_TAB_INFO_SNA c where c.DB_CODE = '"+dbId+"'";
		String sql3 = getCompTableSql(dbId,col3,endDate,"UNION");
		String sql4 = getCompTableSql(dbId,col4,endDate,"UNION");
		String sql5 = getCompTableSql(dbId,col5,endDate,"UNION");
		sql += sql1;
		if(!sql2.equals("")){
			sql += " UNION "+sql2;
		}
		if(!sql3.equals("")){
			sql += " UNION "+sql3;
		}
		if(!sql4.equals("")){
			sql += " UNION "+sql4;
		}
		if(!sql5.equals("")){
			sql += " UNION "+sql5;
		}
		//都移除
		if(col2.equals("")&&col3.equals("")&&col4.equals("")&&col5.equals("")){
			compSql = sql;
		}else
		{
			if(!col1.equals("")){//第一列存在，需要比较
				boolean has1 = false;//判断has1是否已经添加进去
				String has1Sql = compVerTableHasCaseSql("b","hasc1")+",";
				String selectSql = "select t.DB_CODE, t.schname, t.MODNAME, t.TABNAME,hasc1";
				compSql = /*"select t.DB_CODE, t.schname, t.MODNAME, t.TABNAME,hasc1,hasc2,hasc3,c2mod,c3mod " +*/
						" from " +
						"("+sql+") T" ;
				if(!col2.equals("")){//加上第2列
					has1 = true;
					selectSql += ",hasc2,c2mod";
					compSql +=     " left join (select "
							+comoVerTableBaseCaseSql("c2","tabname")
							+comoVerTableBaseCaseSql("c2","tabchname")
							+comoVerTableBaseCaseSql("c2","schname")
							+comoVerTableBaseCaseSql("c2","modname")
							+comoVerTableBaseCaseSql("c2","db_code")
							+has1Sql
							+compVerTableHasCaseSql("c2","hasc2")+","
							+compVerTabcaseSql("c2","c2mod")+" from (" +
							"("+getCompTableSql(dbId,col1,endDate,"all")+") b left join " +
							"("+getCompTableSql(dbId,col2,endDate,"all")+") c2 on " +
							"union"+
							"("+getCompTableSql(dbId,col1,endDate,"all")+") b right join " +
							"("+getCompTableSql(dbId,col2,endDate,"all")+") c2 on " +
							whereTabSql("b","c2")+")"+") t2 on "
						    +whereTabSql("t","t2");
				}	
				if(!col3.equals("")){//加上第三列
					selectSql += ",hasc3,c3mod";
					if(has1){
						has1Sql = "";
					}
					else {
						 has1 = true;
					}
					compSql +=     " left join (select "+comoVerTableBaseCaseSql("c3","tabname")
							+comoVerTableBaseCaseSql("c3","tabchname")
							+comoVerTableBaseCaseSql("c3","schname")
							+comoVerTableBaseCaseSql("c3","modname")
							+comoVerTableBaseCaseSql("c3","db_code")
							+has1Sql
							+compVerTableHasCaseSql("c3","hasc3")+","
							+compVerTabcaseSql("c3","c3mod")+" from (" +
							"("+getCompTableSql(dbId,col1,endDate,"all")+") b left join " +
							"("+getCompTableSql(dbId,col3,endDate,"all")+") c3 on " +
							"union"+
							"("+getCompTableSql(dbId,col1,endDate,"all")+") b right join " +
							"("+getCompTableSql(dbId,col3,endDate,"all")+") c3 on " +
							whereTabSql("b","c3")+")"+") t3 on "
						    +whereTabSql("t","t3");
				}
				if(!col4.equals("")){//加上第四列
					selectSql += ",hasc4,c4mod";
					if(has1){
						has1Sql = "";
					}
					else {
						 has1 = true;
					}
					compSql +=     " left join (select "+comoVerTableBaseCaseSql("c4","tabname")
							+comoVerTableBaseCaseSql("c4","tabchname")
							+comoVerTableBaseCaseSql("c4","schname")
							+comoVerTableBaseCaseSql("c4","modname")
							+comoVerTableBaseCaseSql("c4","db_code")
							+has1Sql
							+compVerTableHasCaseSql("c4","hasc4")+","
							+compVerTabcaseSql("c4","c4mod")+" from (" +
							"("+getCompTableSql(dbId,col1,endDate,"all")+") b left join " +
							"("+getCompTableSql(dbId,col4,endDate,"all")+") c4 on " +
							"union"+
							"("+getCompTableSql(dbId,col1,endDate,"all")+") b right join " +
							"("+getCompTableSql(dbId,col4,endDate,"all")+") c4 on " +
							whereTabSql("b","c4")+")"+") t4 on "
						    +whereTabSql("t","t4");
				}
				if(!col5.equals("")){//加上第五列
					selectSql += ",hasc5,c5mod";
					if(has1){
						has1Sql = "";
					}
					else {
						 has1 = true;
					}
					compSql +=     " left join (select "+comoVerTableBaseCaseSql("c5","tabname")
							+comoVerTableBaseCaseSql("c5","tabchname")
							+comoVerTableBaseCaseSql("c5","schname")
							+comoVerTableBaseCaseSql("c5","modname")
							+comoVerTableBaseCaseSql("c5","db_code")
							+has1Sql
							+compVerTableHasCaseSql("c5","hasc5")+","
							+compVerTabcaseSql("c5","c5mod")+" from (" +
							"("+getCompTableSql(dbId,col1,endDate,"all")+") b left join " +
							"("+getCompTableSql(dbId,col5,endDate,"all")+") c5 on " +
							"union"+
							"("+getCompTableSql(dbId,col1,endDate,"all")+") b right join " +
							"("+getCompTableSql(dbId,col5,endDate,"all")+") c5 on " +
							whereTabSql("b","c5")+")"+") t5 on "
						    +whereTabSql("t","t5");
				}
				compSql = selectSql+compSql;
			}
		}
		 
		compSql = "select * from ("+compSql+") T where 1=1 ";
		if(StringUtils.isNotBlank(searchText)){
			compSql += " and T.tabname like '%"+searchText+"%' ";
		}
		if(StringUtils.isNotBlank(schemaId)){
			compSql += " and T.schname = '"+schemaId+"'";
		}
		if(StringUtils.isNotBlank(moduleId)){
			compSql += "  and T.modname = '"+moduleId+"' ";
		}
		if("true".equals(noSameTable)&&!col1.equals("")){//筛选过滤相同条目
			//有改变或者（该记录不存在但是第一列存在也算是改变）
			if(!col2.equals("")){
				compSql += " and (c2mod=1 or (c2mod is null and hasc1=1) ";
			}
			if(!col3.equals("")){
				compSql +="or c3mod=1 or (c3mod is null and hasc1=1)";
			}
			if(!col4.equals("")){
				compSql +="or c4mod=1 or (c4mod is null and hasc1=1)";
			}
			if(!col5.equals("")){
				compSql +="or c5mod=1 or (c5mod is null and hasc1=1)";
			}
			compSql += ")";
		}
		try {
			dataMap = this.queryPageMap(compSql, null, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dataMap;
	}
	
	private String getCompTableSql(String dbId,String optId,String endDate,String type){
		if(StringUtils.isBlank(optId)){
			return "";
		}
		String sql = "";
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		if(optId.contains("PRO")||optId.contains("UAT")){
			String table = "T_MP_META_INT_TAB_INFO_SNA";
			if(optId.contains("UAT")){
				dbId = getUATDbCode(dbId);
			}
			if(type.equals("UNION")){
				 sql = "select c.DB_CODE,c.schname,c.MODNAME,c.TABNAME from "+table+" c where c.DB_CODE = '"+dbId+"'";
				 if(optId.contains("UAT")){
					 sql = "SELECT t1.db_code,u.schname,u.modname,u.tabname"+
							 	" FROM T_MP_META_INT_TAB_INFO_UAT u LEFT JOIN (select *"+
							 	" from "+dbName+".t_md_inst s, T_MP_META_SYS_INVENTORY I"+
							 	" where s.namespace = i.uat_namespace"+
							 	" and s.inst_code = '"+dbId+"'"+
							 	" and s.class_id = 'PASystem') t1 on u.db_code = t1.inst_code where t1.db_code is not null";
				 }
			}else{
				sql = "select * from "+table+" c where c.DB_CODE = '"+dbId+"'";
				if(optId.contains("UAT")){
					sql = "SELECT t1.db_code,u.fkcols,u.modname,u.schname,u.tabname,u.MODIYDATE,u.CRTDATE,u.TSIZE,u.PCNT,u.LCYCDESC,u.zipdesc,u.remark,u.impflag,u.rowcount,u.fktablename,u.pkcols,u.tabspacename,u.tabchname"+
                         " FROM T_MP_META_INT_TAB_INFO_UAT u LEFT JOIN (select *"+
                         " from "+dbName+".t_md_inst s, T_MP_META_SYS_INVENTORY I"+
                         " where s.namespace = i.uat_namespace"+
                         " and s.inst_code = '"+dbId+"'"+
                         " and s.class_id = 'PASystem') t1 on u.db_code = t1.inst_code where t1.db_code is not null";
				}
			}
		}else {
			if(type.equals("UNION")){
				sql = "select b.DB_CODE,b.MODNAME,b.SCHNAME,b.TABNAME from t_mp_meta_int_tab_info b where b.db_code = '"+dbId+"'";
			}else{
				sql = "select * from t_mp_meta_int_tab_info b where b.db_code = '"+dbId+"'";
			}
			sql+= getVerDateWhere(endDate, optId);
		}
		return sql;
	}
	
	private String compVerTableHasCaseSql(String tableName,String hasName){
		return "(case when "+tableName+".tabname is null THEN 0  else 1 end)  "+hasName;
	}
	private String comoVerTableBaseCaseSql(String tabName,String showName){
		return "(case when b."+showName+" is not null then b."+showName+" when "+tabName+"."+showName+" is not null then "+tabName+"."+showName+" END ) "+showName+",";
	}
	private String whereTabSql(String t1,String t2){
	    return  " "+t1+".db_code = "+t2+".db_code"
		    +" and "+t1+".schname = "+t2+".schname"
		    +" and "+t1+".modname = "+t2+".modname"
		    +" and "+t1+".tabname = "+t2+".tabname";
}
	//表版本比较用到
		private String compVerTabcaseSql(String cTableName,String cNo){
			String sql = " (case when (b.tabname = "+cTableName+".tabname or (b.tabname is null and "+cTableName+".tabname is null))"+
					" and( b.tabchname = "+cTableName+".tabchname or (b.tabchname is null and "+cTableName+".tabchname is null))"+
					" and(b.tabspacename = "+cTableName+".tabspacename or (b.tabspacename is null and "+cTableName+".tabspacename is null))"+
					" and(b.pkcols = "+cTableName+".pkcols or (b.pkcols is null and "+cTableName+".pkcols is null))"+
					" and(b.fkcols = "+cTableName+".fkcols or (b.fkcols is null and "+cTableName+".fkcols is null))"+
					" and(b.fktablename = "+cTableName+".fktablename or (b.fktablename is null and "+cTableName+".fktablename is null))"+
					" and(b.rowcount = "+cTableName+".rowcount or (b.rowcount is null and "+cTableName+".rowcount is null))"+
					" and(b.impflag = "+cTableName+".impflag or (b.impflag is null and "+cTableName+".impflag is null))"+
					" and(b.remark = "+cTableName+".remark or (b.remark is null and "+cTableName+".remark is null))"+
					" and(b.zipdesc = "+cTableName+".zipdesc or (b.zipdesc is null and "+cTableName+".zipdesc is null))"+
					" and(b.LCYCDESC = "+cTableName+".LCYCDESC or (b.LCYCDESC is null and "+cTableName+".LCYCDESC is null))"+
					" and(b.PCNT = "+cTableName+".PCNT or (b.PCNT is null and "+cTableName+".PCNT is null))"+
					" and(b.TSIZE = "+cTableName+".TSIZE or (b.TSIZE is null and "+cTableName+".TSIZE is null))"+
					" and(b.CRTDATE = "+cTableName+".CRTDATE or (b.CRTDATE is null and "+cTableName+".CRTDATE is null))"+
					" and(b.MODIYDATE = "+cTableName+".MODIYDATE or (b.MODIYDATE is null and "+cTableName+".MODIYDATE is null))"+
					" then 0 "+
					" else 1 "+
					" end)  "+cNo;
			return sql;
		}

		@Override
		public List<Map<String, Object>> getColCodeData(PageResults page) {
			String dbId = page.getParamStr("dbId");
			String schema = page.getParamStr("schema");
			String module = page.getParamStr("module");
			String tabName = page.getParamStr("tableName");
			String colName = page.getParamStr("colName");
			String codeVal = page.getParamStr("codeVal");
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String sql = "SELECT * FROM T_MP_META_INT_COL_CODE b WHERE 1=1 ";
			
			if(dbId!=null&&!"".equals(dbId)){
				sql += " AND DB_CODE = '"+dbId+"'";
			}
			if(schema!=null&&!"".equals(schema)){
				sql += " AND SCHNAME = '"+schema+"'";
			}
			if(module!=null&&!"".equals(module)){
				sql += " AND MODNAME = '"+module+"'";
			}
			if(tabName!=null&&!"".equals(tabName)){
				sql += " AND TABNAME = '"+tabName+"'";
			}
			if(colName!=null&&!"".equals(colName)){
				sql += " AND COLNAME = '"+colName+"'";
			}
			if(codeVal!=null&&!"".equals(codeVal)){
				sql += " AND CODE_VAL like '%"+codeVal+"%'";
			}
			sql+= getVerDateWhere(endDate, optId);
			sql += " order by DB_CODE,SCHNAME,MODNAME,TABNAME,COLNAME,CODE_VAL ";
			//sql = "select count(1) from ("+sql+") a1";
			List<Map<String,Object>> list = this.queryPageMap(sql, null, page);
			//page.setTotalRecs(countSQL(sql, null));
			return list;
		}

		@Override
		public List<Map<String, Object>> getProVersionInfo(
				Map<String, String> params) {
			String dbCode = params.get("dbId");
			String optId = params.get("optId");
			String table = "T_MP_META_INT_DB_INFO_SNA";
			String sql2 = "  and v.ent_mode='S'";
			if(optId.contains("UAT")){
				sql2 = "  and v.ent_mode='U'";
				table = "T_MP_META_INT_DB_INFO_UAT";
				dbCode = getUATDbCode(dbCode);
			}
			String sql = "select v.*,d.*,'' as curVerOptId from  T_MP_PRODUCT_VER v,"+table+" d" +
					" where v.db_code = d.db_code and v.db_code='"+dbCode+"' "+sql2;
			return this.queryListForMap(sql);
		}

		@Override
		public int getProVerColumnMetaCount(PageResults page) {
			int count = 0;
			try {
				if(page==null)
					return 0;
				
				String dbId = page.getParamStr("dbId");
				String optId = page.getParamStr("optId");
				String schemaId = page.getParamStr("schemaId");
				String moduleId = page.getParamStr("moduleId");
				String tableId = page.getParamStr("tableId");
				String searchText = page.getParamStr("searchText");
				String tabSearchText = page.getParamStr("tabSearcharText");
				String tableName = "T_MP_META_INT_COL_INFO_SNA";
				if(optId.contains("UAT")){
					tableName = "T_MP_META_INT_COL_INFO_UAT";
					dbId = getUATDbCode(dbId);
				}
				String sql = " select * from "+tableName+" b where b.db_code = '"+dbId+"' ";
				if(StringUtils.isNotBlank(searchText)){
					sql += " and (colName like '%"+searchText+"%' or colchname like '%"+searchText+"%') ";
				}
					
				if(StringUtils.isNotBlank(schemaId)){
					sql += " and schname = '"+schemaId+"' ";
				}
				
				if(StringUtils.isNotBlank(moduleId)){
					sql += " and modname = '"+moduleId+"' ";
				}
				
				if(StringUtils.isNotBlank(tableId)){
					sql += " and tabname='"+tableId+"' ";
				}
				
				if(StringUtils.isNotBlank(tabSearchText)){
					sql += " and tabname like '"+tabSearchText+"%' ";
				}
				
				sql = "select count(1) sum from ("+sql+")"; 
				
				count = countSQL(sql, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return count;
		}

		@Override
		public int getProVerTableMetaCount(PageResults page) {
			int count = 0;
			try {
				if(page==null)
					return 0;
				String dbId = page.getParamStr("dbId");
				String optId = page.getParamStr("optId");
				String schemaId = page.getParamStr("schemaId");
				String moduleId = page.getParamStr("moduleId");
				String searchText = page.getParamStr("searchText");
				String tableName = "T_MP_META_INT_TAB_INFO_SNA";
				if(optId.contains("UAT")){
					tableName = "T_MP_META_INT_TAB_INFO_UAT";
					dbId = getUATDbCode(dbId);
				}
				String sql = "select * from "+tableName+" b where b.db_code = '"+dbId+"' ";
				if(StringUtils.isNotBlank(searchText)){
					sql += " and (tabname like '%"+searchText+"%' or tabchname like '%"+searchText+"%') ";
				}
				if(StringUtils.isNotBlank(schemaId)){
					sql += " and schname = '"+schemaId+"'";
				}
				if(StringUtils.isNotBlank(moduleId)){
					sql += "  and modname = '"+moduleId+"' ";
				}
				
				sql = "select count(1) sum from ("+sql+")"; 
				count = countSQL(sql, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return count;
		}

		@Override
		public int getProVerSchemaMetaCount(PageResults page) {
			int count = 0;
			try {
				if(page==null)
					return 0;
				String dbId = page.getParamStr("dbId");
				String optId = page.getParamStr("optId");
				String tableName = "T_MP_META_INT_SCH_INFO_SNA";
				if(optId.contains("UAT")){
					tableName = "T_MP_META_INT_SCH_INFO_UAT";
					dbId = getUATDbCode(dbId);
				}
				String sql = " select * from "+tableName+" b where b.db_code = '"+dbId+"' ";
				sql = "select count(1) sum from ("+sql+")"; 
				count = countSQL(sql, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return count;
		}

		@Override
		public int getProVerModuleMetaCount(PageResults page) {
			int count = 0;
			try {
				if(page==null)
					return 0;
				String dbId = page.getParamStr("dbId");
				String optId = page.getParamStr("optId");
				String tableName = "T_MP_META_INT_BLK_INFO_SNA";
				if(optId.contains("UAT")){
					tableName = "T_MP_META_INT_BLK_INFO_UAT";
					dbId = getUATDbCode(dbId);
				}
				String sql = "select * from "+tableName+" b where b.db_code = '"+dbId+"' ";
				sql = "select count(1) sum from ("+sql+")"; 
				count = countSQL(sql, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return count;
		}

		@Override
		public List<Map<String, Object>> getFileIntData(PageResults page) {
			String dbId = page.getParamStr("dbId");
			String endDate = page.getParamStr("endDate");
			String optId = page.getParamStr("optId");
			String refDbId = page.getParamStr("refDbId");
			String refSchema = page.getParamStr("refSchema");
			String refModule = page.getParamStr("refModule");
			String refTable = page.getParamStr("refTable");
			String refFuncName = page.getParamStr("refFuncName");
			String refType = page.getParamStr("cType");
			String type = page.getParamStr("refType");
			String sql = " select * from T_MP_META_INT_FILE b where 1=1 ";
			if("1".equals(type)){
				if(dbId!=null&&!"".equals(dbId)){
					sql += " AND DB_CODE = '"+dbId+"'";
				}
				
				if(refDbId!=null&&!"".equals(refDbId)){
					sql += " AND REF_DB_CODE = '"+refDbId+"' ";
				}
			}else{
				if(dbId!=null&&!"".equals(dbId)){
					sql += " AND REF_DB_CODE = '"+dbId+"'";
				}
				
				if(refDbId!=null&&!"".equals(refDbId)){
					sql += " AND DB_CODE = '"+refDbId+"' ";
				}
			}
			
			if(refSchema!=null&&!"".equals(refSchema)){
				sql += " AND REF_SCHNAME = '"+refSchema+"' ";
			}
			
			if(refModule!=null&&!"".equals(refModule)){
				sql += " AND REF_MODNAME = '"+refModule+"' ";
			}
			
			if(refTable!=null&&!"".equals(refTable)){
				sql += " AND REF_TABNAME = '"+refTable+"' ";
			}
			
			if(refFuncName!=null&&!"".equals(refFuncName)){
				sql += " AND C_FUNC_NAME = '"+refFuncName+"' ";
			}
			
			if(refType!=null&&!"".equals(refType)){
				sql += " AND C_TYPE = '"+refType+"' ";
			}
			sql+= getVerDateWhere(endDate, optId);
			sql = "select * from ("+sql+") a";
			List<Map<String,Object>> list = this.queryPageMap(sql, null, page);
			page.setTotalRecs(countSQL(sql, null));
			return list;
		}
		
		@Override
		public List<Map<String, Object>> getProVersionDbInfo(
				Map<String, String> params) {
			String dbId = params.get("dbId");
			String optId = params.get("optId");
			String tableName = "T_MP_META_INT_DB_INFO_SNA";
			if(optId.contains("UAT")){
				tableName = "T_MP_META_INT_DB_INFO_UAT";
				dbId = getUATDbCode(dbId);
			}
			String sql = "select * from "+tableName+" d where 1=1";
			if(dbId!=null&&!"".equals(dbId)){
				sql+=" and DB_CODE = '"+dbId+"' ";
			}
			return this.queryListForMap(sql);
		}

		@Override
		public List<Map<String, Object>> getVerSchemaMetaSna(String dbId,
				String optId) {
			List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
			String table = "T_MP_META_INT_SCH_INFO_SNA";
			if(optId.contains("UAT")){
				table = "T_MP_META_INT_SCH_INFO_UAT";
				dbId = getUATDbCode(dbId);
			}
			String sql2 = "select c.db_code,c.schname ,c.tabcnt,c.devloper,c.schchname,c.remark,c.usedsize,c.objcnt from "+table+" c where c.DB_CODE = '"+dbId+"'";
			try {
				dataMap = this.queryListForMap(sql2);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return dataMap;
		}

		@Override
		public List<Map<String, Object>> getCompAllVersionSchemaList(PageResults page) {
			List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
			String dbId = page.getParamStr("dbId");
			String col1 = page.getParamStr("Col_1");
			String col2 = page.getParamStr("Col_2");
			String col3 = page.getParamStr("Col_3");
			String col4 = page.getParamStr("Col_4");
			String col5 = page.getParamStr("Col_5");
			
			String endDate = page.getParamStr("endDate");
			String sql = "";
			String sql1 = getCompSchemaSql(dbId,col1,endDate);
	/*		String sql2 = "select c.schname from T_MP_META_INT_SCH_INFO_SNA c where c.DB_CODE = '"+dbId+"'";
	*/		String sql2 = getCompSchemaSql(dbId,col2,endDate);
			String sql3 = getCompSchemaSql(dbId,col3,endDate);
			String sql4 = getCompSchemaSql(dbId,col4,endDate);
			String sql5 = getCompSchemaSql(dbId,col5,endDate);
			sql += sql1;
			if(!sql2.equals("")){
				sql += " UNION "+sql2;
			}
			if(!sql3.equals("")){
				sql += " UNION "+sql3;
			}
			if(!sql4.equals("")){
				sql += " UNION "+sql4;
			}
			if(!sql5.equals("")){
				sql += " UNION "+sql5;
			}
			try {
				dataMap = this.queryListForMap(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dataMap;
		}
		
		private String getCompSchemaSql(String dbId,String optId,String endDate){
			if(StringUtils.isBlank(optId)){
				return "";
			}
			String sql = "";
			if(optId.contains("PRO")||optId.contains("UAT")){
				String table = "T_MP_META_INT_SCH_INFO_SNA";
				if(optId.contains("UAT")){
					table = "T_MP_META_INT_SCH_INFO_UAT";
					dbId = getUATDbCode(dbId);
				}
				sql = "select b.schname from "+table+" b where b.db_code = '"+dbId+"'";
			}else {
				sql = "select b.schname from t_mp_meta_int_sch_info b where b.db_code = '"+dbId+"'";
				sql+= getVerDateWhere(endDate, optId);
			}
			return sql;
		}

		@Override
		public List<Map<String, Object>> getCompAllVersionModuleList(PageResults page) {
			List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
			String dbId = page.getParamStr("dbId");
			String col1 = page.getParamStr("Col_1");
			String col2 = page.getParamStr("Col_2");
			String col3 = page.getParamStr("Col_3");
			String col4 = page.getParamStr("Col_4");
			String col5 = page.getParamStr("Col_5");
			
			String endDate = page.getParamStr("endDate");
			String sql = "";
			String sql1 = getCompModuleSql(dbId,col1,endDate);
			String sql2 = getCompModuleSql(dbId,col2,endDate);//"select c.MODNAME,c.SCHNAME from T_MP_META_INT_BLK_INFO_SNA c where c.DB_CODE = '"+dbId+"'";
			String sql3 = getCompModuleSql(dbId,col3,endDate);
			String sql4 = getCompModuleSql(dbId,col4,endDate);
			String sql5 = getCompModuleSql(dbId,col5,endDate);
			sql += sql1;
			if(!sql2.equals("")){
				sql += " UNION "+sql2;
			}
			if(!sql3.equals("")){
				sql += " UNION "+sql3;
			}
			if(!sql4.equals("")){
				sql += " UNION "+sql4;
			}
			if(!sql5.equals("")){
				sql += " UNION "+sql5;
			}
			try {
				dataMap = this.queryListForMap(sql);
			} catch (Exception e) {
				System.out.println("查询模块出错！");
				e.printStackTrace();
			}
			return dataMap;
		}
		
		private String getCompModuleSql(String dbId,String optId,String endDate){
			if(StringUtils.isBlank(optId)){
				return "";
			}
			String sql = "";
			String dbName = (String)PropertiesUtil.props.getProperty("dbName");
			if(optId.contains("PRO")||optId.contains("UAT")){
				String table = "T_MP_META_INT_BLK_INFO_SNA";
				if(optId.contains("UAT")){
					table = "T_MP_META_INT_BLK_INFO_UAT";
					dbId = getUATDbCode(dbId);
					sql = "SELECT t1.db_code,u.modname, u.schname"+
							" FROM T_MP_META_INT_BLK_INFO_UAT u"+
	                        " LEFT JOIN (select *"+
	                            "  from "+dbName+".t_md_inst s,"+
	                               "    T_MP_META_SYS_INVENTORY I "+
	                             " where s.namespace = i.uat_namespace"+
	                              " and s.inst_code = '"+dbId+"'"+
	                              " and s.class_id = 'PASystem') t1"+
	                   " on u.db_code = t1.inst_code"+
	                " where t1.db_code is not null";
				}else {
					sql = "select b.db_code,b.MODNAME,b.SCHNAME  from "+table+" b where b.db_code = '"+dbId+"'";
				}

			}else {
			    sql = "select b.db_code,b.MODNAME,b.SCHNAME  from T_MP_META_INT_BLK_INFO b where b.db_code = '"+dbId+"'";
				sql+= getVerDateWhere(endDate, optId);
			}
			return sql;
		}

		@Override
		public List<Map<String, Object>> getVerModuleMetaSna(String dbId,
				String optId) {
			List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
			
			String table = "T_MP_META_INT_BLK_INFO_SNA";
			if(optId.contains("UAT")){
				table = "T_MP_META_INT_BLK_INFO_UAT";
				dbId = getUATDbCode(dbId);
			}
			String sql = "select * from "+table+" s " +
						 "where s.DB_CODE='"+dbId+"'";
			try {
				dataMap = this.queryListForMap(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dataMap;
		}

		@Override
		public List<Map<String, Object>> getTarOptDate(String decId) {
			String sql = "";
			sql = sql + "select DISTINCT R.ALT_VER_DATE_NO, r.cur_ver_date_no ";
			sql = sql + "from T_MP_ALTERATION_REGISTER_INFO R, ";
			sql = sql + "T_MP_DECLARE_DETAIL DE ";
			sql = sql + "WHERE R.ALT_STS = '1' ";
			sql = sql + "AND DE.DECLARE_ID = '"+decId+"' ";
			sql = sql + "AND R.ALT_ID = DE.ALT_ID ";
			return this.queryListForMap(sql);
		}

		@Override
		public void updVerMetaByDecId(PageResults page) {
			String decId = page.getParamStr("decId");
			String tarOptId = page.getParamStr("tarOptId");
			String curOptId = page.getParamStr("curOptId");
			String sql = "";
			sql=sql+"UPDATE T_MP_META_INT_DB_INFO                                     ";
			sql=sql+"   SET F_ENDDATE = '"+tarOptId+"'                                             ";
			sql=sql+" WHERE (DB_CODE) IN                                               ";
			sql=sql+"       (select d.DB_CODE                                          ";
			sql=sql+"          from T_MP_ALTERATION_REGISTER_INFO R,                   ";
			sql=sql+"               T_MP_META_INT_DB_INFO_DEC     D,                   ";
			sql=sql+"               T_MP_DECLARE_DETAIL           DE                   ";
			sql=sql+"         WHERE R.ALT_ID = D.ALT_ID                                ";
			sql=sql+"           AND R.ALT_STS = '1'                                    ";
			sql=sql+"           AND DE.DECLARE_ID = '"+decId+"' ";
			sql=sql+"           AND R.ALT_ID = DE.ALT_ID)         AND f_startdate <= '"+curOptId+"' and f_enddate > '"+curOptId+"'                    ";
			this.executeSql(sql, null);
			
			/******************************************模式级****************************/
			sql="";
			sql=sql+" UPDATE T_MP_META_INT_SCH_INFO                                    ";
			sql=sql+"    SET F_ENDDATE = '"+tarOptId+"'                                             ";
			sql=sql+"  WHERE (DB_CODE, SCHNAME) IN                                      ";
			sql=sql+"        (select s.DB_CODE, s.SCHNAME                               ";
			sql=sql+"           from T_MP_ALTERATION_REGISTER_INFO R,                   ";
			sql=sql+"                T_MP_META_INT_SCH_INFO_DEC    S,                   ";
			sql=sql+"                T_MP_DECLARE_DETAIL           DE                   ";
			sql=sql+"          WHERE R.ALT_ID = S.ALT_ID                                ";
			sql=sql+"            AND R.ALT_STS = '1'                                    ";
			sql=sql+"            AND DE.DECLARE_ID = '"+decId+"' ";
			sql=sql+"            AND R.ALT_ID = DE.ALT_ID)       AND f_startdate <= '"+curOptId+"' and f_enddate > '"+curOptId+"'                          ";
			this.executeSql(sql, null);
			
			/********************************模块级*********************************/
			sql = "";
			sql=sql+"UPDATE T_MP_META_INT_BLK_INFO SET F_ENDDATE = '"+tarOptId+"' WHERE (DB_CODE,SCHNAME,MODNAME) IN ( ";
			sql=sql+"select                                                                                 ";
			sql=sql+"       B.DB_CODE,                                                                      ";
			sql=sql+"       B.SCHNAME,                                                                      ";
			sql=sql+"       B.MODNAME                                                                       ";
			sql=sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                                                ";
			sql=sql+"       T_MP_META_INT_BLK_INFO_DEC    B,                                                ";
			sql=sql+"       T_MP_DECLARE_DETAIL           DE                                                ";
			sql=sql+" WHERE R.ALT_ID = B.ALT_ID                                                             ";
			sql=sql+"   AND R.ALT_STS = '1'                                                                 ";
			sql=sql+"   AND DE.DECLARE_ID = '"+decId+"'                      ";
			sql=sql+"   AND R.ALT_ID = DE.ALT_ID                  AND f_startdate <= '"+curOptId+"' and f_enddate > '"+curOptId+"'                                            ";
			sql=sql+"   )                                                                                   ";
			this.executeSql(sql, null);
			
			/**************************************************表级*************************/
			sql = "";
			sql=sql+"UPDATE T_MP_META_INT_TAB_INFO SET F_ENDDATE = '"+tarOptId+"' WHERE (DB_CODE,SCHNAME,MODNAME,TABNAME) IN (  ";
			sql=sql+"select                                                                                          ";
			sql=sql+"       T.DB_CODE,                                                                               ";
			sql=sql+"       T.SCHNAME,                                                                               ";
			sql=sql+"       T.MODNAME,                                                                               ";
			sql=sql+"       T.TABNAME                                                                                ";
			sql=sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                                                         ";
			sql=sql+"       T_MP_META_INT_TAB_INFO_DEC    T,                                                         ";
			sql=sql+"       T_MP_DECLARE_DETAIL           DE                                                         ";
			sql=sql+" WHERE R.ALT_ID = T.ALT_ID                                                                      ";
			sql=sql+"   AND R.ALT_STS = '1'                                                                          ";
			sql=sql+"   AND DE.DECLARE_ID = '"+decId+"'                                      ";
			sql=sql+"   AND R.ALT_ID = DE.ALT_ID            AND f_startdate <= '"+curOptId+"' and f_enddate > '"+curOptId+"'                                                           ";
			sql=sql+")                                                                                  ";
			this.executeSql(sql, null);
			
			/*********************************字段级**************************************/
			sql = "";
			sql=sql+"UPDATE T_MP_META_INT_COL_INFO SET F_ENDDATE = '"+tarOptId+"' WHERE (DB_CODE,SCHNAME,MODNAME,TABNAME,COLNAME) IN (select ";
			sql=sql+"       C.DB_CODE,                                                                                            ";
			sql=sql+"       C.SCHNAME,                                                                                            ";
			sql=sql+"       C.MODNAME,                                                                                            ";
			sql=sql+"       C.TABNAME,                                                                                            ";
			sql=sql+"       C.COLNAME                                                                                             ";
			sql=sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                                                                      ";
			sql=sql+"       T_MP_META_INT_COL_INFO_DEC    C,                                                                      ";
			sql=sql+"       T_MP_DECLARE_DETAIL           DE                                                                      ";
			sql=sql+" WHERE R.ALT_ID = C.ALT_ID                                                                                   ";
			sql=sql+"   AND R.ALT_STS = '1'                                                                                       ";
			sql=sql+"   AND DE.DECLARE_ID = '"+decId+"'                                                   ";
			sql=sql+"   AND R.ALT_ID = DE.ALT_ID   AND f_startdate <= '"+curOptId+"' and f_enddate > '"+curOptId+"'                                                                           ";
			sql=sql+")                                                                                                            ";
			this.executeSql(sql, null);
			
			/*********************************字段落地代码**************************************/
			sql = "";
			sql=sql+"UPDATE T_MP_META_INT_COL_CODE SET F_ENDDATE = '"+tarOptId+"' WHERE (DB_CODE,SCHNAME,MODNAME,TABNAME,COLNAME,CODE_VAL) IN (select ";
			sql=sql+"       C.DB_CODE,                                                                                            ";
			sql=sql+"       C.SCHNAME,                                                                                            ";
			sql=sql+"       C.MODNAME,                                                                                            ";
			sql=sql+"       C.TABNAME,                                                                                            ";
			sql=sql+"       C.COLNAME, C.CODE_VAL                                                                                             ";
			sql=sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                                                                      ";
			sql=sql+"       T_MP_META_INT_COL_CODE_DEC    C,                                                                      ";
			sql=sql+"       T_MP_DECLARE_DETAIL           DE                                                                      ";
			sql=sql+" WHERE R.ALT_ID = C.ALT_ID                                                                                   ";
			sql=sql+"   AND R.ALT_STS = '1'                                                                                       ";
			sql=sql+"   AND DE.DECLARE_ID = '"+decId+"'                                                   ";
			sql=sql+"   AND R.ALT_ID = DE.ALT_ID AND f_startdate <= '"+curOptId+"' and f_enddate > '"+curOptId+"'                                                                           ";
			sql=sql+")                                                                                                            ";
			this.executeSql(sql, null);
			
			/*********************************文件接口**************************************/
			sql = "";
			sql=sql+"UPDATE T_MP_META_INT_FILE SET F_ENDDATE = '"+tarOptId+"' WHERE (DB_CODE,C_FUNC_NAME,REF_DB_CODE,REF_SCHNAME,REF_MODNAME,REF_TABNAME) IN (select ";
			sql=sql+"       F.DB_CODE,                                                                                            ";
			sql=sql+"       F.C_FUNC_NAME,F.REF_DB_CODE,F.REF_SCHNAME,F.REF_MODNAME,F.REF_TABNAME                                                                                           ";
			sql=sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                                                                      ";
			sql=sql+"       T_MP_META_INT_FILE_DEC    F,                                                                          ";
			sql=sql+"       T_MP_DECLARE_DETAIL           DE                                                                      ";
			sql=sql+" WHERE R.ALT_ID = F.ALT_ID   AND F.C_TYPE = '1'                                                                                   ";
			sql=sql+"   AND R.ALT_STS = '1'                                                                                       ";
			sql=sql+"   AND DE.DECLARE_ID = '"+decId+"'                                                   ";
			sql=sql+"   AND R.ALT_ID = DE.ALT_ID          AND f_startdate <= '"+curOptId+"' and f_enddate > '"+curOptId+"'                                                                           ";
			sql=sql+")                                                                                                            ";
			this.executeSql(sql, null);
		}

		@Override
		public void insVerMetaByDecId(PageResults page) {
			String decId = page.getParamStr("decId");
			String sql = "";
			sql = sql+"INSERT INTO T_MP_META_INT_DB_INFO (DB_CODE,DBCHNAME,DEPT,DEVLOPER,MAINTAINER,DBA,REMARK,IMGURL,PRO_M_FAC,TOTALSIZE,USEDSIZE,TABCNT,OBJCNT,F_STARTDATE,F_ENDDATE)";
			sql = sql+" select d.DB_CODE,                                             ";
			sql = sql+"       d.DBCHNAME,                                            ";
			sql = sql+"       d.DEPT,                                                ";
			sql = sql+"       d.DEVLOPER,                                            ";
			sql = sql+"       d.MAINTAINER,                                          ";
			sql = sql+"       d.DBA,                                                 ";
			sql = sql+"       d.REMARK,                                              ";
			sql = sql+"       d.IMGURL,                                              ";
			sql = sql+"       d.PRO_M_FAC,                                           ";
			sql = sql+"       d.TOTALSIZE,                                           ";
			sql = sql+"       d.USEDSIZE,                                            ";
			sql = sql+"       d.TABCNT,                                              ";
			sql = sql+"       d.OBJCNT,                                              ";
			sql = sql+"       R.ALT_VER_DATE_NO,                                     ";
			sql = sql+"       '20991231'                                             ";
			sql = sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                       ";
			sql = sql+"       T_MP_META_INT_DB_INFO_DEC     D,                       ";
			sql = sql+"       T_MP_DECLARE_DETAIL           DE                       ";
			sql = sql+" WHERE R.ALT_ID = D.ALT_ID                                    ";
			sql = sql+"   AND R.ALT_STS = '1'                                        ";
			sql = sql+"   AND DE.DECLARE_ID = '"+decId+"'     ";
			sql = sql+"   AND R.ALT_ID = DE.ALT_ID                                   ";
			this.executeSql(sql);
			
			/******************************************模式级****************************/
			sql="";
			sql = sql+"INSERT INTO T_MP_META_INT_SCH_INFO (DB_CODE,SCHNAME,SCHCHNAME,DEVLOPER,REMARK,USEDSIZE,TABCNT,OBJCNT,F_STARTDATE,F_ENDDATE)";
			sql = sql+" select s.DB_CODE,                                            ";
			sql = sql+"       s.SCHNAME,                                            ";
			sql = sql+"       s.SCHCHNAME,                                          ";
			sql = sql+"       s.DEVLOPER,                                           ";
			sql = sql+"       s.REMARK,                                             ";
			sql = sql+"       s.USEDSIZE,                                           ";
			sql = sql+"       s.TABCNT,                                             ";
			sql = sql+"       s.OBJCNT,                                             ";
			sql = sql+"       R.ALT_VER_DATE_NO,                                    ";
			sql = sql+"       '20991231'                                            ";
			sql = sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                      ";
			sql = sql+"       T_MP_META_INT_SCH_INFO_DEC    S,                      ";
			sql = sql+"       T_MP_DECLARE_DETAIL           DE                      ";
			sql = sql+" WHERE R.ALT_ID = S.ALT_ID                                   ";
			sql = sql+"   AND R.ALT_STS = '1'                                       ";
			sql = sql+"   AND DE.DECLARE_ID = '"+decId+"'    ";
			sql = sql+"   AND R.ALT_ID = DE.ALT_ID                                  ";
			this.executeSql(sql);
			
			/********************************模块级*********************************/
			sql = "";
			sql = sql+"INSERT INTO T_MP_META_INT_BLK_INFO (DB_CODE,SCHNAME,MODNAME,MODCHNAME,DEPT,DEPTCHARGER,DEVLOPER,SA,MODPTN,STATUS,REMARK,USEDSIZE,TABCNT,OBJCNT,F_STARTDATE,F_ENDDATE)";
			sql = sql+" select B.DB_CODE,                                         ";
			sql = sql+"       B.SCHNAME,                                         ";
			sql = sql+"       B.MODNAME,                                         ";
			sql = sql+"       B.MODCHNAME,                                       ";
			sql = sql+"       B.DEPT,                                            ";
			sql = sql+"       B.DEPTCHARGER,                                     ";
			sql = sql+"       B.DEVLOPER,                                        ";
			sql = sql+"       B.SA,                                              ";
			sql = sql+"       B.MODPTN,                                          ";
			sql = sql+"       B.STATUS,                                          ";
			sql = sql+"       B.REMARK,                                          ";
			sql = sql+"       B.USEDSIZE,                                        ";
			sql = sql+"       B.TABCNT,                                          ";
			sql = sql+"       B.OBJCNT,                                          ";
			sql = sql+"       R.ALT_VER_DATE_NO,                                 ";
			sql = sql+"       '20991231'                                         ";
			sql = sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                   ";
			sql = sql+"       T_MP_META_INT_BLK_INFO_DEC    B,                   ";
			sql = sql+"       T_MP_DECLARE_DETAIL           DE                   ";
			sql = sql+" WHERE R.ALT_ID = B.ALT_ID                                ";
			sql = sql+"   AND R.ALT_STS = '1'                                    ";
			sql = sql+"   AND DE.DECLARE_ID = '"+decId+"' ";
			sql = sql+"   AND R.ALT_ID = DE.ALT_ID                               ";
			this.executeSql(sql);
			
			/**************************************************表级*************************/
			sql = "";
			sql = sql+"INSERT INTO T_MP_META_INT_TAB_INFO (DB_CODE,SCHNAME,MODNAME,TABNAME,TABCHNAME,TABSPACENAME,PKCOLS,FKCOLS,FKTABLENAME,ROWCOUNT,IMPFLAG,REMARK,ZIPDESC,LCYCDESC,PCNT,TSIZE,CRTDATE,MODIYDATE,F_STARTDATE,F_ENDDATE)";
			sql=sql+" select                                                ";
			sql=sql+"       T.DB_CODE,                                              ";
			sql=sql+"       T.SCHNAME,                                              ";
			sql=sql+"       T.MODNAME,                                              ";
			sql=sql+"       T.TABNAME,                                              ";
			sql=sql+"       T.TABCHNAME,                                            ";
			sql=sql+"       T.TABSPACENAME,                                         ";
			sql=sql+"       T.PKCOLS,                                               ";
			sql=sql+"       T.FKCOLS,                                               ";
			sql=sql+"       T.FKTABLENAME,                                          ";
			sql=sql+"       T.ROWCOUNT,                                             ";
			sql=sql+"       T.IMPFLAG,                                              ";
			sql=sql+"       T.REMARK,                                               ";
			sql=sql+"       T.ZIPDESC,                                              ";
			sql=sql+"       T.LCYCDESC,                                             ";
			sql=sql+"       T.PCNT,                                                 ";
			sql=sql+"       T.TSIZE,                                                ";
			sql=sql+"       T.CRTDATE,                                              ";
			sql=sql+"       T.MODIYDATE,                                            ";
			sql=sql+"       R.ALT_VER_DATE_NO,                                      ";
			sql=sql+"       '20991231'                                              ";
			sql=sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                        ";
			sql=sql+"       T_MP_META_INT_TAB_INFO_DEC    T,                        ";
			sql=sql+"       T_MP_DECLARE_DETAIL           DE                        ";
			sql=sql+" WHERE R.ALT_ID = T.ALT_ID                                     ";
			sql=sql+"   AND R.ALT_STS = '1'                                         ";
			sql=sql+"   AND DE.DECLARE_ID = '"+decId+"'      ";
			sql=sql+"   AND R.ALT_ID = DE.ALT_ID                                    ";
			this.executeSql(sql);
			
			/*********************************字段级**************************************/
			sql = "";
			sql = sql+"INSERT INTO T_MP_META_INT_COL_INFO (DB_CODE,SCHNAME,MODNAME,TABNAME,COLNAME,COLCHNAME,COLTYPE,COLSEQ,PKFLAG,PDKFLAG,NVLFLAG,CCOLFLAG,INDXFLAG,CODETAB,REMARK,F_STARTDATE,F_ENDDATE)";
			sql = sql+" select C.DB_CODE,                                            ";
			sql = sql+"       C.SCHNAME,                                            ";
			sql = sql+"       C.MODNAME,                                            ";
			sql = sql+"       C.TABNAME,                                            ";
			sql = sql+"       C.COLNAME,                                            ";
			sql = sql+"       C.COLCHNAME,                                          ";
			sql = sql+"       C.COLTYPE,                                            ";
			sql = sql+"       C.COLSEQ,                                             ";
			sql = sql+"       C.PKFLAG,                                             ";
			sql = sql+"       C.PDKFLAG,                                            ";
			sql = sql+"       C.NVLFLAG,                                            ";
			sql = sql+"       C.CCOLFLAG,                                           ";
			sql = sql+"       C.INDXFLAG,                                           ";
			sql = sql+"       C.CODETAB,                                            ";
			sql = sql+"       C.REMARK,                                             ";
			sql = sql+"       R.ALT_VER_DATE_NO,                                    ";
			sql = sql+"       '20991231'                                            ";
			sql = sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                      ";
			sql = sql+"       T_MP_META_INT_COL_INFO_DEC    C,                      ";
			sql = sql+"       T_MP_DECLARE_DETAIL           DE                      ";
			sql = sql+" WHERE R.ALT_ID = C.ALT_ID                                   ";
			sql = sql+"   AND R.ALT_STS = '1'                                       ";
			sql = sql+"   AND DE.DECLARE_ID = '"+decId+"'    ";
			sql = sql+"   AND R.ALT_ID = DE.ALT_ID                                  ";
			// TODO Auto-generated method stub
			
			/*********************************字段落地代码**************************************/
			sql = "";
			sql = sql+"INSERT INTO T_MP_META_INT_COL_CODE (DB_CODE,SCHNAME,MODNAME,TABNAME,COLNAME,CODE_VAL,CODE_CH_NAME,CODE_DESC,CODE_STS,CODE_START_DATE,CODE_END_DATE,DS_REF,F_STARTDATE,F_ENDDATE)";
			sql = sql+" select C.DB_CODE,                                            ";
			sql = sql+"       C.SCHNAME,                                             ";
			sql = sql+"       C.MODNAME,                                             ";
			sql = sql+"       C.TABNAME,                                             ";
			sql = sql+"       C.COLNAME,                                             ";
			sql = sql+"       C.CODE_VAL,                                            ";
			sql = sql+"       C.CODE_CH_NAME,                                        ";
			sql = sql+"       C.CODE_DESC,                                           ";
			sql = sql+"       C.CODE_STS,                                            ";
			sql = sql+"       C.CODE_START_DATE,                                     ";
			sql = sql+"       C.CODE_END_DATE,                                       ";
			sql = sql+"       C.DS_REF,                                              ";
			sql = sql+"       R.ALT_VER_DATE_NO,                                     ";
			sql = sql+"       '20991231'                                             ";
			sql = sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                       ";
			sql = sql+"       T_MP_META_INT_COL_CODE_DEC    C,                       ";
			sql = sql+"       T_MP_DECLARE_DETAIL           DE                       ";
			sql = sql+" WHERE R.ALT_ID = C.ALT_ID                                    ";
			sql = sql+"   AND R.ALT_STS = '1'                                        ";
			sql = sql+"   AND DE.DECLARE_ID = '"+decId+"'    ";
			sql = sql+"   AND R.ALT_ID = DE.ALT_ID                                  ";
			// TODO Auto-generated method stub
			
			/*********************************文件接口**************************************/
			sql = "";
			sql = sql+"INSERT INTO T_MP_META_INT_FILE (DB_CODE,REF_DB_CODE,REF_SCHNAME,REF_MODNAME,REF_TABNAME,C_FUNC_NAME,C_DESC,C_TYPE,F_STARTDATE,F_ENDDATE)";
			sql = sql+" select C.DB_CODE,                                            ";
			sql = sql+"       C.REF_DB_CODE,                                         ";
			sql = sql+"       C.REF_SCHNAME,                                         ";
			sql = sql+"       C.REF_MODNAME,                                         ";
			sql = sql+"       C.REF_TABNAME,                                         ";
			sql = sql+"       C.C_FUNC_NAME,                                         ";
			sql = sql+"       C.C_DESC,                                            	 ";
			sql = sql+"       C.C_TYPE,                                              ";
			sql = sql+"       R.ALT_VER_DATE_NO,                                     ";
			sql = sql+"       '20991231'                                             ";
			sql = sql+"  from T_MP_ALTERATION_REGISTER_INFO R,                       ";
			sql = sql+"       T_MP_META_INT_FILE_DEC    C,                           ";
			sql = sql+"       T_MP_DECLARE_DETAIL           DE                       ";
			sql = sql+" WHERE R.ALT_ID = C.ALT_ID                                    ";
			sql = sql+"   AND R.ALT_STS = '1'                                        ";
			sql = sql+"   AND DE.DECLARE_ID = '"+decId+"'    ";
			sql = sql+"   AND R.ALT_ID = DE.ALT_ID                                   ";
			this.executeSql(sql);
		}

		@Override
		public void updALtStsCt(PageResults page) {
			String decId = page.getParamStr("decId");
			String tarOptId = page.getParamStr("tarOptId");
			String sql = "";
			
			/*******************数据库*******************/
			sql="";
			sql+="UPDATE T_MP_ALTERATION_REGISTER_INFO SET ALT_STS = '4' WHERE alt_id IN       ";
			sql+=" (SELECT alt_id FROM  T_MP_META_INT_DB_INFO t, (SELECT S.DB_CODE,r.alt_id    ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                              ";
			sql+="               T_MP_META_INT_DB_INFO_DEC    S,                               ";
			sql+="               T_MP_DECLARE_DETAIL           DE                              ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                           ";
			sql+="           AND R.ALT_STS = '1'                                               ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'            ";
			sql+="           AND R.ALT_ID = DE.ALT_ID) t1                                      ";
			sql+=" WHERE  t.db_code = t1.db_code                                               ";
			sql+="   AND F_STARTDATE >= '"+tarOptId+"'                                         ";
			sql+="   )                                                                         ";
			this.executeSql(sql);
			
			/*********************模式******************/
			sql="";
			sql+="UPDATE T_MP_ALTERATION_REGISTER_INFO SET ALT_STS = '4' WHERE alt_id IN                  ";
			sql+="(SELECT alt_id FROM  T_MP_META_INT_SCH_INFO t, (SELECT S.DB_CODE, S.SCHNAME,r.alt_id    ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                         ";
			sql+="               T_MP_META_INT_SCH_INFO_DEC    S,                                         ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                         ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                      ";
			sql+="           AND R.ALT_STS = '1'                                                          ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                       ";
			sql+="           AND R.ALT_ID = DE.ALT_ID) t1                                                 ";
			sql+=" WHERE  t.db_code = t1.db_code AND t.schname = t1.schname                               ";
			sql+="   AND F_STARTDATE >= '"+tarOptId+"'                                                        ";
			sql+="   )                                                                                    ";
			this.executeSql(sql);
			
			/******************模块冲突********************/
			sql="";
			sql+="UPDATE T_MP_ALTERATION_REGISTER_INFO SET ALT_STS = '4' WHERE alt_id IN                           ";
			sql+=" (SELECT alt_id FROM  T_MP_META_INT_BLK_INFO t, (SELECT S.DB_CODE, S.SCHNAME,s.modname,r.alt_id  ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                  ";
			sql+="               T_MP_META_INT_BLK_INFO_DEC    S,                                                  ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                  ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                               ";
			sql+="           AND R.ALT_STS = '1'                                                                   ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                ";
			sql+="           AND R.ALT_ID = DE.ALT_ID) t1                                                          ";
			sql+=" WHERE  t.db_code = t1.db_code AND t.schname = t1.schname AND t.modname = t1.modname             ";
			sql+="   AND F_STARTDATE >= '"+tarOptId+"'                                                                 ";
			sql+="   )                                                                                             ";
			this.executeSql(sql);
			
			/******************表级处理***********************/
			sql="";
			sql+="UPDATE T_MP_ALTERATION_REGISTER_INFO SET ALT_STS = '4' WHERE alt_id IN                                          ";
			sql+=" (SELECT alt_id FROM  T_MP_META_INT_TAB_INFO t, (SELECT S.DB_CODE, S.SCHNAME,s.modname,s.tabname,r.alt_id       ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                 ";
			sql+="               T_MP_META_INT_TAB_INFO_DEC    S,                                                                 ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                 ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                              ";
			sql+="           AND R.ALT_STS = '1'                                                                                  ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                               ";
			sql+="           AND R.ALT_ID = DE.ALT_ID) t1                                                                         ";
			sql+=" WHERE  t.db_code = t1.db_code AND t.schname = t1.schname AND t.modname = t1.modname AND t.tabname = t1.tabname ";
			sql+="   AND F_STARTDATE >= '"+tarOptId+"'                                                                                ";
			sql+="   )                                                                                                            ";
			this.executeSql(sql);
			
			/**************************字段********************************/
			sql = "";
			sql+="UPDATE T_MP_ALTERATION_REGISTER_INFO SET ALT_STS = '4' WHERE alt_id IN                                                                             ";
			sql+=" (SELECT alt_id FROM  T_MP_META_INT_COL_INFO t, (SELECT S.DB_CODE, S.SCHNAME,s.modname,s.tabname,s.colname,r.alt_id                                ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                                                    ";
			sql+="               T_MP_META_INT_COL_INFO_DEC    S,                                                                                                    ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                                                    ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                                                                 ";
			sql+="           AND R.ALT_STS = '1'                                                                                                                     ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                                                                  ";
			sql+="           AND R.ALT_ID = DE.ALT_ID) t1                                                                                                            ";
			sql+=" WHERE  t.db_code = t1.db_code AND t.schname = t1.schname AND t.modname = t1.modname AND t.tabname = t1.tabname AND t.colname = t1.colname         ";
			sql+="   AND F_STARTDATE >= '"+tarOptId+"'                                                                                                                   ";
			sql+="   )                                                                                                                                               ";
			this.executeSql(sql);
			
			/******************************字段落地*******************************/
			sql = "";
			sql+="UPDATE T_MP_ALTERATION_REGISTER_INFO SET ALT_STS = '4' WHERE alt_id IN                                                           ";
			sql+=" (SELECT alt_id FROM  T_MP_META_INT_COL_CODE t, (SELECT S.DB_CODE, S.SCHNAME,s.modname,s.tabname,s.colname,s.code_val,r.alt_id   ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                                  ";
			sql+="               T_MP_META_INT_COL_CODE_DEC    S,                                                                                  ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                                  ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                                               ";
			sql+="           AND R.ALT_STS = '1'                                                                                                   ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                                                ";
			sql+="           AND R.ALT_ID = DE.ALT_ID) t1                                                                                          ";
			sql+=" WHERE  t.db_code = t1.db_code AND t.schname = t1.schname AND t.modname = t1.modname                                             ";
			sql+=" AND t.tabname = t1.tabname AND t.colname = t1.colname AND t.code_val = t1.code_val                                              ";
			sql+="   AND F_STARTDATE >= '"+tarOptId+"'                                                                                                 ";
			sql+="   )                                                                                                                             ";
			this.executeSql(sql);
			
			/*********************************文件接口********************************/
			sql = "";
			sql+="UPDATE T_MP_ALTERATION_REGISTER_INFO SET ALT_STS = '4' WHERE alt_id IN                                                        ";
			sql+=" (SELECT alt_id FROM  T_MP_META_INT_FILE t, (SELECT s.db_code, S.Ref_Db_Code,s.ref_schname,s.ref_modname,                     ";
			sql+="         s.ref_tabname,s.c_func_name,r.alt_id                                                                                 ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                               ";
			sql+="               T_MP_META_INT_FILE_DEC    S,                                                                                   ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                               ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                                            ";
			sql+="           AND R.ALT_STS = '1'                                                                                                ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                                             ";
			sql+="           AND R.ALT_ID = DE.ALT_ID) t1                                                                                       ";
			sql+="   WHERE t.db_code = t1.db_code AND t.Ref_Db_Code = t1.ref_db_code AND t.ref_schname = t1.ref_schname                         ";
			sql+="               AND t.ref_modname = t1.ref_modname AND t.ref_tabname = t1.ref_tabname AND t.c_func_name = t1.c_func_name       ";
			sql+="   AND F_STARTDATE >= '"+tarOptId+"'                                                                                              ";
			sql+="   )                                                                                                                          ";
			this.executeSql(sql);
		}

		@Override
		public void delMetaALtCt(PageResults page) {
			String decId = page.getParamStr("decId");
			String tarOptId = page.getParamStr("tarOptId");
			String start = DateUtil.getYmdDate();
			String sql = "";
			
			/*******************数据库*******************/
			sql="";
			sql+=" delete  T_MP_META_INT_DB_INFO t where (t.DB_CODE) in (SELECT S.DB_CODE      ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                              ";
			sql+="               T_MP_META_INT_DB_INFO_DEC    S,                               ";
			sql+="               T_MP_DECLARE_DETAIL           DE                              ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                           ";
			sql+="           AND R.ALT_STS = '1'                                               ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'            ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                       ";
			sql+="   AND F_STARTDATE >= '"+start+"' and F_STARTDATE <= '"+tarOptId+"'                                             ";
			this.executeSql(sql);
			
			/*********************模式******************/
			sql="";
			sql+="delete T_MP_META_INT_SCH_INFO t where (t.DB_CODE, t.SCHNAME) in (SELECT S.DB_CODE, S.SCHNAME   ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                         ";
			sql+="               T_MP_META_INT_SCH_INFO_DEC    S,                                         ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                         ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                      ";
			sql+="           AND R.ALT_STS = '1'                                                          ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                       ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                  ";
			sql+="   AND F_STARTDATE >= '"+start+"' and F_STARTDATE <= '"+tarOptId+"'                                                     ";
			this.executeSql(sql);
			
			/******************模块冲突********************/
			sql="";
			sql+="delete  T_MP_META_INT_BLK_INFO t where (t.DB_CODE, t.SCHNAME,t.modname) in (SELECT S.DB_CODE, S.SCHNAME,s.modname ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                  ";
			sql+="               T_MP_META_INT_BLK_INFO_DEC    S,                                                  ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                  ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                               ";
			sql+="           AND R.ALT_STS = '1'                                                                   ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                           ";
			sql+="   AND F_STARTDATE >= '"+start+"' and F_STARTDATE <= '"+tarOptId+"'                                                     ";
			this.executeSql(sql);
			
			/******************表级处理***********************/
			sql="";
			sql+="delete T_MP_META_INT_TAB_INFO t where (t.DB_CODE, t.SCHNAME,t.modname,t.tabname) ";
			sql+= "in (SELECT S.DB_CODE, S.SCHNAME,s.modname,s.tabname     ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                 ";
			sql+="               T_MP_META_INT_TAB_INFO_DEC    S,                                                                 ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                 ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                              ";
			sql+="           AND R.ALT_STS = '1'                                                                                  ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                               ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                                         ";
	  	    sql+="   AND F_STARTDATE >= '"+start+"' and F_STARTDATE <= '"+tarOptId+"'                                                                               ";
	  	    this.executeSql(sql);
	  	    
			/**************************字段********************************/
			sql = "";
			sql+="delete T_MP_META_INT_COL_INFO t where  (t.DB_CODE, t.SCHNAME,t.modname,t.tabname,t.colname)  ";
			sql+= "in (SELECT S.DB_CODE, S.SCHNAME,s.modname,s.tabname,s.colname                               ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                                                    ";
			sql+="               T_MP_META_INT_COL_INFO_DEC    S,                                                                                                    ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                                                    ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                                                                 ";
			sql+="           AND R.ALT_STS = '1'                                                                                                                     ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                                                                  ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                                                                            ";
			sql+="   AND F_STARTDATE >= '"+start+"' and F_STARTDATE <= '"+tarOptId+"'                                                                                                                 ";
			this.executeSql(sql);
			
			/******************************字段落地*******************************/
			sql = "";
			sql+=" delete T_MP_META_INT_COL_CODE t where (t.db_code ,t.schname ,t.modname,t.tabname,t.colname ,t.code_val)";
			sql += "in (SELECT S.DB_CODE, S.SCHNAME,s.modname,s.tabname,s.colname,s.code_val  ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                                  ";
			sql+="               T_MP_META_INT_COL_CODE_DEC    S,                                                                                  ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                                  ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                                               ";
			sql+="           AND R.ALT_STS = '1'                                                                                                   ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                                                ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                                                          ";
			sql+="   AND F_STARTDATE >= '"+start+"' and F_STARTDATE <= '"+tarOptId+"'                                                                                              ";
			this.executeSql(sql);
			
			/*********************************文件接口********************************/
			sql = "";
			sql+="DELETE T_MP_META_INT_FILE t WHERE (t.db_code, t.Ref_Db_Code,t.ref_schname,t.ref_modname,                  ";
			sql+="         t.ref_tabname,t.c_func_name) IN (SELECT s.db_code, S.Ref_Db_Code,s.ref_schname,s.ref_modname,    ";
			sql+="         s.ref_tabname,s.c_func_name                                                                      ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                           ";
			sql+="               T_MP_META_INT_FILE_DEC    S,                                                               ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                           ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                        ";
			sql+="           AND R.ALT_STS = '1'                                                                            ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                         ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                                      ";
			sql+="   AND F_STARTDATE >= '"+start+"' and F_STARTDATE <= '"+tarOptId+"'                                                              ";		
			this.executeSql(sql);
		}

		@Override
		public void rollbakMetaALtCt(PageResults page) {
			String decId = page.getParamStr("decId");
			String tarOptId = page.getParamStr("tarOptId");
			String start = DateUtil.getYmdDate();
			String sql = "";
			
			/*******************数据库*******************/
			sql="";
			sql+=" update T_MP_META_INT_DB_INFO t set f_enddate='20991231' where (t.DB_CODE) in (SELECT S.DB_CODE   ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                              ";
			sql+="               T_MP_META_INT_DB_INFO_DEC    S,                               ";
			sql+="               T_MP_DECLARE_DETAIL           DE                              ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                           ";
			sql+="           AND R.ALT_STS = '1'                                               ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'            ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                       ";
			sql+="   AND F_ENDDATE>='"+start+"' and F_ENDDATE <= '"+tarOptId+"'                                             ";
			this.executeSql(sql);
			
			/*********************模式******************/
			sql="";
			sql+="update T_MP_META_INT_SCH_INFO t set t.f_enddate='20991231' where (t.DB_CODE, t.SCHNAME) in (SELECT S.DB_CODE, S.SCHNAME   ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                         ";
			sql+="               T_MP_META_INT_SCH_INFO_DEC    S,                                         ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                         ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                      ";
			sql+="           AND R.ALT_STS = '1'                                                          ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                       ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                  ";
			sql+="   AND F_ENDDATE>='"+start+"' and F_ENDDATE <= '"+tarOptId+"'                                                        ";
			this.executeSql(sql);
			
			/******************模块冲突********************/
			sql="";
			sql+="update  T_MP_META_INT_BLK_INFO t set t.f_enddate='20991231' where (t.DB_CODE, t.SCHNAME,t.modname) in (SELECT S.DB_CODE, S.SCHNAME,s.modname ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                  ";
			sql+="               T_MP_META_INT_BLK_INFO_DEC    S,                                                  ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                  ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                               ";
			sql+="           AND R.ALT_STS = '1'                                                                   ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                           ";
			sql+="  AND F_ENDDATE>='"+start+"' and F_ENDDATE <= '"+tarOptId+"'                                                              ";
			this.executeSql(sql);
			
			/******************表级处理***********************/
			sql="";
			sql+="UPDATE T_MP_META_INT_TAB_INFO t SET F_ENDDATE='20991231' where (t.DB_CODE, t.SCHNAME,t.modname,t.tabname) ";
			sql+= "in (SELECT S.DB_CODE, S.SCHNAME,s.modname,s.tabname     ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                 ";
			sql+="               T_MP_META_INT_TAB_INFO_DEC    S,                                                                 ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                 ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                              ";
			sql+="           AND R.ALT_STS = '1'                                                                                  ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                               ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                                         ";
			sql+="   AND F_ENDDATE>='"+start+"' and F_ENDDATE <= '"+tarOptId+"'                                                                                 ";
			this.executeSql(sql);
			
			/**************************字段********************************/
			sql = "";
			sql+="UPDATE T_MP_META_INT_COL_INFO t SET F_ENDDATE='20991231' where  (t.DB_CODE, t.SCHNAME,t.modname,t.tabname,t.colname) ";
			sql+= "in (SELECT S.DB_CODE, S.SCHNAME,s.modname,s.tabname,s.colname                               ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                                                    ";
			sql+="               T_MP_META_INT_COL_INFO_DEC    S,                                                                                                    ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                                                    ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                                                                 ";
			sql+="           AND R.ALT_STS = '1'                                                                                                                     ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                                                                  ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                                                                            ";
			sql+="  AND F_ENDDATE>='"+start+"' and F_ENDDATE <= '"+tarOptId+"'                                                                                                                 ";
			this.executeSql(sql);
			
			/******************************字段落地*******************************/
			sql = "";
			sql+=" UPDATE T_MP_META_INT_COL_CODE t SET F_ENDDATE='20991231' where (t.db_code ,t.schname ,t.modname,t.tabname,t.colname ,t.code_val)";
			sql += "in (SELECT S.DB_CODE, S.SCHNAME,s.modname,s.tabname,s.colname,s.code_val  ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                                                  ";
			sql+="               T_MP_META_INT_COL_CODE_DEC    S,                                                                                  ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                                                  ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                                               ";
			sql+="           AND R.ALT_STS = '1'                                                                                                   ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                                                ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                                                          ";
			sql+="   AND F_ENDDATE>='"+start+"' and F_ENDDATE <= '"+tarOptId+"'                                                                                                  ";
			this.executeSql(sql);
			
			/*********************************文件接口********************************/
			sql = "";
			sql+="UPDATE T_MP_META_INT_FILE t SET F_ENDDATE='20991231' WHERE (t.db_code, t.Ref_Db_Code,t.ref_schname,t.ref_modname,                 ";
			sql+="         t.ref_tabname,t.c_func_name) IN (SELECT s.db_code, S.Ref_Db_Code,s.ref_schname,s.ref_modname,    ";
			sql+="         s.ref_tabname,s.c_func_name                                                                      ";
			sql+="          FROM T_MP_ALTERATION_REGISTER_INFO R,                                                           ";
			sql+="               T_MP_META_INT_FILE_DEC    S,                                                               ";
			sql+="               T_MP_DECLARE_DETAIL           DE                                                           ";
			sql+="         WHERE R.ALT_ID = S.ALT_ID                                                                        ";
			sql+="           AND R.ALT_STS = '1'                                                                            ";
			sql+="           AND DE.DECLARE_ID = '"+decId+"'                                         ";
			sql+="           AND R.ALT_ID = DE.ALT_ID)                                                                      ";
			sql+="   AND F_ENDDATE>='"+start+"' and F_ENDDATE <= '"+tarOptId+"'                                                              ";		
			this.executeSql(sql);
		}

		@Override
		public void initMetadata(String dbCode) {
			// TODO Auto-generated method stub
			
		}
		
}
