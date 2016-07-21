package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.sldt.framework.common.PageResults;
import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.MetadataAltDao;
import com.sldt.mds.dmc.mp.util.DateUtil;

@SuppressWarnings("unchecked")
@Repository(value="metaAltDao")
public class MetadataAltDaoImpl extends BaseDao implements MetadataAltDao{
	
	private static Log log = LogFactory.getLog(MetadataAltDaoImpl.class);

	@Override
	public List<Map<String, Object>> getNewAltMeta(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		if(page == null){
			return dataMap;
		}
		
		String endDate = page.getParamStr("endDate");
		String dbId = page.getParamStr("dbId");
		String sql = "select d.DB_CODE,d.DBCHNAME,P.ITEM_VALUE className,i.ALT_TYPE, i.CLASSIFER_TYPE,P1.ITEM_VALUE altType,COUNT(P.ITEM_VALUE) altcount,max(i.ALT_VER_DATE_NO) ALT_VER_DATE_NO from T_MP_ALTERATION_REGISTER_INFO I LEFT JOIN t_mp_meta_int_db_info d ON  I.ALT_SYS_CODE = d.DB_CODE LEFT JOIN T_MP_SYS_PARAMS P1 ON P1.ITEM_CODE = I.Alt_Type  AND p1.param_code = 'ALTTYPE' LEFT JOIN T_MP_SYS_PARAMS P ON P.ITEM_CODE = I.CLASSIFER_TYPE AND P.PARAM_CODE = 'StatisticMetadata' WHERE d.F_ENDDATE > '"+endDate+"' and i.ALT_MODE <> 'S' ";
		if(dbId!=null&&!"".equals(dbId)){
			sql+= " and d.db_code = '"+dbId+"' ";
		}
			
		sql += " GROUP BY d.db_code,d.dbchname,P.ITEM_VALUE , i.alt_type,i.classifer_type,P1.ITEM_VALUE order by  max(i.alt_oper_date) desc";
		dataMap = this.queryListForMap(sql);
		
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getDecAltSumByDbId(PageResults page) {

		List<Map<String, Object>> dataMap = new ArrayList<Map<String,Object>>();
		
		if(page == null){
			return dataMap;
		}
		String db_code = page.getParamStr("dbId");
		String sql = "select R.ALT_SYS_CODE,R.ALT_STS,COUNT(R.ALT_STS) ALTSUM from T_MP_ALTERATION_REGISTER_INFO R WHERE 1=1 ";
		if(db_code!=null&&!"".equals(db_code)){
			sql += " AND R.ALT_SYS_CODE = '"+db_code+"' ";
		}
		sql += "  GROUP BY R.ALT_SYS_CODE,R.ALT_STS  ";
		dataMap = this.queryListForMap(sql);
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getAltSumByDbId(PageResults page) {
        List<Map<String, Object>> dataMap = new ArrayList<Map<String,Object>>();
        
        if(page == null) {
        	return dataMap;
        }
        String db_code = page.getParamStr("dbId");
        
        String sql = "select O.OPT_YAR,O.OPT_MONTH,O.OPT_DAY,R.ALT_SYS_CODE,R.ALT_VER_DATE_NO,V.VER_DESC,S.ITEM_VALUE,COUNT(1) ALTSUM FROM T_MP_ALTERATION_REGISTER_INFO R,T_MP_SYS_PARAMS S,T_MP_VERSION_DEFINE V,T_MP_OPT_CALENDAR O WHERE V.OPT_ID = O.OPT_ID AND R.ALT_VER_DATE_NO = V.OPT_ID AND R.CLASSIFER_TYPE = S.ITEM_CODE AND S.PARAM_CODE = 'StatisticMetadata' ";
        
        if(db_code != null && !"".equals(db_code)){
        	sql +=" AND R.ALT_SYS_CODE = '"+ db_code+"' ";
        }
        sql += " GROUP BY R.ALT_VER_DATE_NO,S.ITEM_VALUE,V.VER_DESC,R.ALT_SYS_CODE,O.OPT_YAR,O.OPT_MONTH,O.OPT_DAY order by R.ALT_VER_DATE_NO desc ";
        dataMap = this.queryListForMap(sql);
		return dataMap;
	}

	@Override
	public void insUploadInfo(PageResults page) {
		String sql = "INSERT INTO T_MP_META_IMP_INFO (UPLOAD_ID,UPLOAD_USER,UPLOAD_TIME,UPLOAD_FIME_NAME,UPLOAD_BATCH_ID,UPLOAD_ALT_DESC,UPLOAD_STS,UPLOAD_M)";
      	Map<String, String> uploadInfo = new HashMap<String, String>();
      	uploadInfo = (Map<String, String>) page.getParameter("uploadInfo");
      	sql += " VALUES(";
		sql += "'"+uploadInfo.get("UPLOAD_ID")+"',";
		sql += "'"+uploadInfo.get("UPLOAD_USER")+"',";
		sql += "'"+uploadInfo.get("UPLOAD_TIME")+"',";
		sql += "'"+uploadInfo.get("UPLOAD_FIME_NAME")+"',";
		sql += "'"+uploadInfo.get("UPLOAD_BATCH_ID")+"',";
		sql += "'"+uploadInfo.get("UPLOAD_ALT_DESC")+"',";
		sql += "'"+uploadInfo.get("UPLOAD_STS")+"',";
		sql += "'"+uploadInfo.get("UPLOAD_M")+"'";
		sql += " )";
		executeSql(sql, null);
	}

	@Override
	public void insAltMetaItem(PageResults page) {
		//变更的元数据集合
		List<Map<String,String>> altList = (List<Map<String,String>>)page.getParameter("altMetaList");
		//变更登记数据集合
		List<Map<String,String>> altInfoList = (List<Map<String,String>>)page.getParameter("altInfoList");
		//变更登记明细数据集合
		List<Map<String,String>> altDetilInfoList = (List<Map<String,String>>)page.getParameter("altDetilInfoList");
		String sql = "";
		for (Map<String, String> altInfoMap : altInfoList) {
			sql = "insert into T_MP_ALTERATION_REGISTER_INFO(ALT_ID,ALT_SYS_CODE,CUR_VER_DATE_NO,ALT_VER_DATE_NO,ALT_OPER_DATE,ALT_USER,LAST_MODIFY,LAST_MODIFY_TIME,CLASSIFER_TYPE,ALT_TYPE,ALT_MODE,ALT_STS,ALT_BATCH) ";
			sql += "VALUES(";
			sql += "'"+altInfoMap.get("ALT_ID")+"',";
			sql += "'"+altInfoMap.get("ALT_SYS_CODE")+"',";
			sql += "'"+altInfoMap.get("CUR_VER_DATE_NO")+"',";
			sql += "'"+altInfoMap.get("ALT_VER_DATE_NO")+"',";
			sql += "'"+altInfoMap.get("ALT_OPER_DATE")+"',";
			sql += "'"+altInfoMap.get("ALT_USER")+"',";
			sql += "'',";
			sql += "'',";
			sql += "'"+altInfoMap.get("CLASSIFER_TYPE")+"',";
			sql += "'"+altInfoMap.get("ALT_TYPE")+"',";
			sql += "'"+altInfoMap.get("ALT_MODE")+"',";
			sql += "'"+altInfoMap.get("ALT_STS")+"',";
			sql += "'"+altInfoMap.get("ALT_BATCH")+"'";
			sql += ")";
			executeSql(sql, null);
		}
				
		for (Map<String, String> altMetaMap : altList) {
			String classiferType = altMetaMap.get("CLASSIFER_TYPE");
			if(classiferType.equals("DATABASE")){
				sql = "INSERT INTO T_MP_META_INT_DB_INFO_DEC (DB_C_ID,ALT_ID,DB_CODE,DBCHNAME,DEPT,DEVLOPER,MAINTAINER,DBA,REMARK,IMGURL,PRO_M_FAC,TOTALSIZE,USEDSIZE,TABCNT,OBJCNT)";
				sql += " VALUES(";
				sql += "'"+altMetaMap.get("DB_C_ID")+"',";
				sql += "'"+altMetaMap.get("ALT_ID")+"',";
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
				executeSql(sql, null);
			}else if(classiferType.equals("SCHEMA")){
				sql = "INSERT INTO T_MP_META_INT_SCH_INFO_DEC (S_C_ID,ALT_ID,DB_CODE,SCHNAME,SCHCHNAME,DEVLOPER,REMARK,USEDSIZE,TABCNT,OBJCNT)";
				sql += " VALUES(";
				sql += "'"+altMetaMap.get("S_C_ID")+"',";
				sql += "'"+altMetaMap.get("ALT_ID")+"',";
				sql += "'"+altMetaMap.get("DB_CODE")+"',";
				sql += "'"+altMetaMap.get("SCHNAME")+"',";
				sql += "'"+altMetaMap.get("SCHCHNAME")+"',";
				sql += "'"+altMetaMap.get("DEVLOPER")+"',";
				sql += "'"+altMetaMap.get("REMARK")+"',";
				sql += "'"+altMetaMap.get("USEDSIZE")+"',";
				sql += "'"+altMetaMap.get("TABCNT")+"',";
				sql += "'"+altMetaMap.get("OBJCNT")+"'";
				sql += ")";
				executeSql(sql, null);
			}else if(classiferType.equals("MODULE")){
				sql = "INSERT INTO T_MP_META_INT_BLK_INFO_DEC (M_C_ID,ALT_ID,DB_CODE,SCHNAME,MODNAME,MODCHNAME,DEPT,DEPTCHARGER,DEVLOPER,SA,MODPTN,STATUS,REMARK,USEDSIZE,TABCNT,OBJCNT)";
				sql += " VALUES(";
				sql += "'"+altMetaMap.get("M_C_ID")+"',";
				sql += "'"+altMetaMap.get("ALT_ID")+"',";
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
				executeSql(sql, null);
			}else if(classiferType.equals("TABLE")){
				sql = "INSERT INTO T_MP_META_INT_TAB_INFO_DEC (T_C_ID,ALT_ID,DB_CODE,SCHNAME,MODNAME,TABNAME,TABCHNAME,TABSPACENAME,PKCOLS,FKCOLS,FKTABLENAME,ROWCOUNT,IMPFLAG,REMARK,ZIPDESC,LCYCDESC,PCNT,TSIZE,CRTDATE,MODIYDATE)";
				sql+= " VALUES(";
				sql+= "'"+altMetaMap.get("T_C_ID")+"',";
				sql+= "'"+altMetaMap.get("ALT_ID")+"',";
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
				executeSql(sql, null);
			}else if(classiferType.equals("COLUMN")){
				sql = "INSERT INTO T_MP_META_INT_COL_INFO_DEC (C_C_ID,ALT_ID,DB_CODE,SCHNAME,MODNAME,TABNAME,COLNAME,COLCHNAME,COLTYPE,COLSEQ,PKFLAG,PDKFLAG,NVLFLAG,CCOLFLAG,INDXFLAG,CODETAB,REMARK)";
				sql += " VALUES(";
				sql += "'"+altMetaMap.get("C_C_ID")+"',";
				sql += "'"+altMetaMap.get("ALT_ID")+"',";
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
			    executeSql(sql, null);
			}else if(classiferType.equals("COLCODE")){
				sql = "INSERT INTO T_MP_META_INT_COL_CODE_DEC (C_C_ID,ALT_ID,DB_CODE,SCHNAME,MODNAME,TABNAME,COLNAME,CODE_VAL,CODE_CH_NAME,CODE_DESC,CODE_STS,CODE_START_DATE,CODE_END_DATE,DS_REF)";
				sql += " VALUES(";
				sql += "'"+altMetaMap.get("C_C_ID")+"',";
				sql += "'"+altMetaMap.get("ALT_ID")+"',";
				sql += "'"+altMetaMap.get("DB_CODE")+"',";
				sql += "'"+altMetaMap.get("SCHNAME")+"',";
				sql += "'"+altMetaMap.get("MODNAME")+"',";
				sql += "'"+altMetaMap.get("TABNAME")+"',";
				sql += "'"+altMetaMap.get("COLNAME")+"',";
				sql += "'"+altMetaMap.get("CODE_VAL")+"',";
				sql += "'"+altMetaMap.get("CODE_CH_NAME")+"',";
				sql += "'"+altMetaMap.get("CODE_DESC")+"',";
				sql += "'"+altMetaMap.get("CODE_STS")+"',";
				sql += "'"+altMetaMap.get("CODE_START_DATE")+"',";
				sql += "'"+altMetaMap.get("CODE_END_DATE")+"',";
				sql += "'"+altMetaMap.get("DS_REF")+"'";
				sql += ")";
				executeSql(sql, null);
			}else if(classiferType.equals("INTFILE")){
				sql = "INSERT INTO T_MP_META_INT_FILE_DEC (F_C_ID,ALT_ID,DB_CODE,REF_DB_CODE,REF_SCHNAME,REF_MODNAME,REF_TABNAME,C_FUNC_NAME,C_DESC,C_TYPE)";
				sql += " VALUES(";
				sql += "'"+altMetaMap.get("F_C_ID")+"',";
				sql += "'"+altMetaMap.get("ALT_ID")+"',";
				sql += "'"+altMetaMap.get("DB_CODE")+"',";
				sql += "'"+altMetaMap.get("REF_DB_CODE")+"',";
				sql += "'"+altMetaMap.get("REF_SCHNAME")+"',";
				sql += "'"+altMetaMap.get("REF_MODNAME")+"',";
				sql += "'"+altMetaMap.get("REF_TABNAME")+"',";
				sql += "'"+altMetaMap.get("C_FUNC_NAME")+"',";
				sql += "'"+altMetaMap.get("C_DESC")+"',";
				sql += "'"+altMetaMap.get("C_TYPE")+"'";
				sql += ")";
				executeSql(sql, null);
			}
		}
				
		for (Map<String, String> altInfoDetailMap : altDetilInfoList) {
			sql = "INSERT INTO T_MP_ALTERTION_MODIFY_INFO (ACT_META_ID,ALT_ID,CLASSIFER_TYPE,ALT_COL_NAME,S_VALUE,T_VALUE)";
			sql+= " VALUES(";
			sql+= "'"+altInfoDetailMap.get("ACT_META_ID")+"',";
			sql+= "'"+altInfoDetailMap.get("ALT_ID")+"',";
			sql+= "'"+altInfoDetailMap.get("CLASSIFER_TYPE")+"',";
			sql+= "'"+altInfoDetailMap.get("ALT_COL_NAME")+"',";
			sql+= "'"+altInfoDetailMap.get("S_VALUE")+"',";
			sql+= "'"+altInfoDetailMap.get("T_VALUE")+"'";
			sql+= ")";
			executeSql(sql, null);
		}	    
		
	}

	@Override
	public void updUploadInfo(PageResults page) {
		String sql = "UPDATE T_MP_META_IMP_INFO ";
		Map<String,String> uploadInfo = new HashMap<String, String>();
		String uploadId = page.getParamStr("uploadId");
		uploadInfo = (Map<String,String>)page.getParameter("uploadInfo");
		sql += " set ";
		sql += " UPLOAD_ALT_DESC = '"+uploadInfo.get("UPLOAD_ALT_DESC")+"',";
		sql += " UPLOAD_STS = '"+uploadInfo.get("UPLOAD_STS")+"'";
		
		sql += " where upload_id = '"+uploadId+"'";
		
	   executeSql(sql, null);
		
	}

	@Override
	public Map<String, Object> analyseAltMetaFull(String tmpId, String dbId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String, Object>> analyseAltModuleMetaFull(String tmpId,String dbId) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		
		String sql = "SELECT (case when t1.DB_CODE is not null then t1.DB_CODE when t2.DB_CODE is not null then t2.DB_CODE END ) DB_CODE, 						  "
				+"        (case when t1.SCHNAME is not null then t1.SCHNAME when t2.SCHNAME is not null then t2.SCHNAME END ) SCHNAME, 							 "
				+"       (case when t1.MODNAME is not null then t1.MODNAME when t2.MODNAME is not null then t2.MODNAME END ) MODNAME,  							  "
				+"       (case when t1.modchname = t2.modchname or (t1.modchname is null and t2.modchname is null) then 0 else 1 end) mod_modchname,             "
				+"       T1.modchname AS S_modchname,                                                                                                             "
				+"       T2.modchname AS T_modchname,                                                                                                             "
				+"       (case when t1.dept = t2.dept or (t1.dept is null and t2.dept is null) then 0 else 1 end) mod_dept,                                        "
				+"       T1.dept AS S_dept,                                                                                                                       "
				+"       T2.dept AS T_dept,                                                                                                                       "
				+"       (case when t1.deptcharger = t2.deptcharger or (t1.deptcharger is null and t2.deptcharger is null) then 0 else 1 end) mod_deptcharger,     "
				+"       T1.deptcharger AS deptcharger,                                                                                                           "
				+"       T2.deptcharger AS deptcharger,                                                                                                           "
				+"       (case when t1.devloper = t2.devloper or (t1.devloper is null and t2.devloper is null) then 0 else 1 end) mod_devloper,                    "
				+"       T1.devloper AS S_devloper,                                                                                                               "
				+"       T2.devloper AS T_devloper,                                                                                                               "
				+"       (case when t1.sa = t2.sa or (t1.sa is null and t2.sa is null) then 0 else 1 end) mod_sa,                                                  "
				+"       T1.sa AS S_sa,                                                                                                                           "
				+"       T2.sa AS T_sa,                                                                                                                           "
				+"       (case when t1.modptn = t2.modptn or (t1.modptn is null and t2.modptn is null) then 0 else 1 end) mod_modptn,                              "
				+"       T1.modptn AS S_modptn,                                                                                                                   "
				+"       T2.modptn AS T_modptn,                                                                                                                   "
				+"       (case when t1.status = t2.status or (t1.status is null and t2.status is null) then 0 else 1 end) mod_status,                              "
				+"       T1.status AS S_status,                                                                                                                   "
				+"       T2.status AS T_status,                                                                                                                   "
				+"       (case when t1.remark = t2.remark or (t1.remark is null and t2.remark is null) then 0 else 1 end) mod_remark,                              "
				+"       T1.remark AS S_remark,                                                                                                                   "
				+"       T2.remark AS T_remark,                                                                                                                   "
				+"       (case when t1.usedsize = t2.usedsize or (t1.usedsize is null and t2.usedsize is null) then 0 else 1 end) mod_usedsize,                    "
				+"       T1.usedsize AS S_usedsize,                                                                                                               "
				+"       T2.usedsize AS T_usedsize,                                                                                                               "
				+"       (case when t1.tabcnt = t2.tabcnt or (t1.tabcnt is null and t2.tabcnt is null) then 0 else 1 end) mod_tabcnt,                              "
				+"       T1.tabcnt AS S_tabcnt,                                                                                                                   "
				+"       T2.tabcnt AS T_tabcnt,                                                                                                                   "
				+"       (case when t1.objcnt = t2.objcnt or (t1.objcnt is null and t2.objcnt is null) then 0 else 1 end) mod_objcnt,                              "
				+"       T1.objcnt AS S_objcnt,                                                                                                                   "
				+"       T2.objcnt AS T_objcnt,                                                                                                                   "
				+"      (case when t1.MODNAME is null then 'I' when t2.MODNAME is null then 'D' ELSE 'U' END ) altType                                                     "
				+"  FROM (SELECT *                                                                                                                                "
				+"          FROM t_mp_meta_int_blk_info I                                                                                                        "
				+"         WHERE I.DB_CODE = '"+dbId+"'                                                                  "
				+"           AND I.F_ENDDATE > '"+DateUtil.getYmdDate()+"') T1                                                                                                     "
				+"  FULL JOIN (SELECT *                                                                                                                           "
				+"               FROM t_mp_meta_int_blk_info_tmp I                                                                                               "
				+"              WHERE I.DB_CODE = '"+dbId+"'  and i.id = '"+tmpId+"'                                                         "
				+"              ) T2                                                                                                                              "
				+"                ON T1.DB_CODE = T2.DB_CODE                                                                                                      "
				+"                AND T1.SCHNAME = T2.SCHNAME                                                                                                     "
				+"                AND T1.MODNAME = T2.MODNAME                                                                                                     ";
		dataMap = this.queryListForMap(sql);
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> analyseAltTableMetaFull(String tmpId,
			String dbId) {
List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		
		String sql = "select  (case when t1.DB_CODE is not null then t1.DB_CODE when t2.DB_CODE is not null then t2.DB_CODE END ) DB_CODE,                  "
				+"        (case when t1.SCHNAME is not null then t1.SCHNAME when t2.SCHNAME is not null then t2.SCHNAME END ) SCHNAME,                      "
				+"       (case when t1.MODNAME is not null then t1.MODNAME when t2.MODNAME is not null then t2.MODNAME END ) MODNAME,                             "
				+"       (case when t1.tabname is not null then t1.tabname when t2.tabname is not null then t2.tabname END ) tabname,                                "
				+"       (case when t1.tabchname = t2.tabchname OR (t1.tabchname is null and t2.tabchname is null) THEN 0  else 1 end) mod_tabchname,                 "
				+"       t1.tabchname as S_tabchname,                                                                                                                "
				+"       T2.tabchname AS T_tabchname,                                                                                                                "
				+"       (case when t1.tabspacename = t2.tabspacename OR (t1.tabspacename is null and t2.tabspacename is null) THEN 0  else 1 end) mod_tabspacename,  "
				+"       T1.tabspacename AS S_tabspacename,                                                                                                          "
				+"       T2.tabspacename AS T_tabspacename,                                                                                                          "
				+"       (case when t1.pkcols = t2.pkcols OR (t1.pkcols is null and t2.pkcols is null) THEN 0  else 1 end) mod_pkcols,                                "
				+"       T1.pkcols AS S_pkcols,                                                                                                                      "
				+"       T2.pkcols AS T_pkcols,                                                                                                                      "
				+"       (case when t1.fkcols = t2.fkcols OR (t1.fkcols is null and t2.fkcols is null) THEN 0  else 1 end) mod_fkcols,                                "
				+"       T1.fkcols AS S_fkcols,                                                                                                                      "
				+"       T2.fkcols AS T_fkcols,                                                                                                                      "
				+"       (case when t1.fktablename = t2.fktablename OR (t1.fktablename is null and t2.fktablename is null) THEN 0  else 1 end) mod_fktablename,       "
				+"       T1.fktablename AS S_fktablename,                                                                                                            "
				+"       T2.fktablename AS T_fktablename,                                                                                                            "
				+"       (case when t1.rowcount = t2.rowcount OR (t1.rowcount is null and t2.rowcount is null) THEN 0  else 1 end) mod_rowcount,                      "
				+"       T1.rowcount AS S_rowcount,                                                                                                                  "
				+"       T2.rowcount AS T_rowcount,                                                                                                                  "
				+"       (case when t1.impflag = t2.impflag OR (t1.impflag is null and t2.impflag is null) THEN 0  else 1 end) mod_impflag,                           "
				+"       T1.impflag AS S_impflag,                                                                                                                    "
				+"       T2.impflag AS T_impflag,                                                                                                                    "
				+"       (case when t1.remark = t2.remark OR (t1.remark is null and t2.remark is null) THEN 0  else 1 end) mod_remark,                                "
				+"       T1.remark AS S_remark,                                                                                                                      "
				+"       T2.remark AS T_remark,                                                                                                                      "
				+"       (case when t1.zipdesc = t2.zipdesc OR (t1.zipdesc is null and t2.zipdesc is null) THEN 0  else 1 end) zipdesc,                              "
				+"       T1.zipdesc AS S_zipdesc,                                                                                                                    "
				+"       T2.zipdesc AS T_zipdesc,                                                                                                                    "
				+"       (case when t1.LCYCDESC = t2.LCYCDESC OR (t1.LCYCDESC is null and t2.LCYCDESC is null) THEN 0  else 1 end) mod_LCYCDESC,                      "
				+"       T1.LCYCDESC AS S_LCYCDESC,                                                                                                                  "
				+"       T2.LCYCDESC AS T_LCYCDESC,                                                                                                                  "
				+"       (case when t1.PCNT = t2.PCNT or (t1.PCNT is null and t2.PCNT is null) THEN 0  else 1 end) mod_PCNT,                                          "
				+"       T1.PCNT AS S_PCNT,                                                                                                                          "
				+"       T2.PCNT AS T_PCNT,                                                                                                                          "
				+"       (case when t1.TSIZE = t2.TSIZE or (t1.TSIZE is null and t2.TSIZE is null) THEN 0  else 1 end) mod_TSIZE,                                     "
				+"       T1.TSIZE AS S_TSIZE,                                                                                                                        "
				+"       T2.TSIZE AS T_TSIZE,                                                                                                                        "
				+"       (case when t1.CRTDATE = t2.CRTDATE OR (t1.CRTDATE is null and t2.CRTDATE is null) THEN 0  else 1 end) mod_CRTDATE,                           "
				+"       T1.CRTDATE AS S_CRTDATE,                                                                                                                    "
				+"       T2.CRTDATE AS T_CRTDATE,                                                                                                                    "
				+"       (case when t1.MODIYDATE = t2.MODIYDATE OR (t1.MODIYDATE is null and t2.MODIYDATE is null) THEN 0  else 1 end) mod_MODIYDATE,                 "
				+"       T1.MODIYDATE AS S_MODIYDATE,                                                                                                                "
				+"       T2.MODIYDATE AS T_MODIYDATE,                                                                                                                "
				+"       (case when t1.tabname is null then 'I' when t2.tabname is null then 'D' ELSE 'U' END ) altType                                                      "
				+"  from (select * from T_MP_META_INT_TAB_INFO a                                                                                                    "
				+"         where a.db_code = '"+dbId+"'                                                                                                                  "
				+"           and a.f_enddate > '"+DateUtil.getYmdDate()+"') t1 full join                                                                                              "
				+"       (select * from T_MP_META_INT_TAB_INFO_TMP c where c.db_code = '"+dbId+"' and c.id = '"+tmpId+"'  ) t2                                                                                                               "
				+"         on  t1.tabName = t2.tabname                                                                                                               "
				+"           and t1.db_code = t2.db_code                                                                                                             "
				+"           and t1.schname = t2.schname                                                                                                             "
				+"           and t1.modname = t2.modname                                                                                                             ";

		dataMap = this.queryListForMap(sql);
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> analyseAltColumnMetaFull(String tmpId,
			String dbId) {
        List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		
		String sql = "select (case when t1.DB_CODE is not null then t1.DB_CODE when t2.DB_CODE is not null then t2.DB_CODE END ) DB_CODE,                       "
				+"        (case when t1.SCHNAME is not null then t1.SCHNAME when t2.SCHNAME is not null then t2.SCHNAME END ) SCHNAME,                      "
				+"       (case when t1.MODNAME is not null then t1.MODNAME when t2.MODNAME is not null then t2.MODNAME END ) MODNAME,                             "
				+"       (case when t1.tabname is not null then t1.tabname when t2.tabname is not null then t2.tabname END ) tabname,                                "
				+"       (case when t1.colname is not null then t1.colname when t2.colname is not null then t2.colname END ) colname,                      "
				+"       (case when t1.COLCHNAME = t2.COLCHNAME or (t1.COLCHNAME is null and t2.COLCHNAME is null) then 0 else 1 end) mod_COLCHNAME,        "
				+"       t1.COLCHNAME as S_COLCHNAME,                                                                                                      "
				+"       T2.COLCHNAME AS T_COLCHNAME,                                                                                                      "
				+"       (case when t1.COLTYPE = t2.COLTYPE or (t1.COLTYPE is null and t2.COLTYPE is null) then 0 else 1 end) mod_COLTYPE,                  "
				+"       T1.COLTYPE AS S_COLTYPE,                                                                                                          "
				+"       T2.COLTYPE AS T_COLTYPE,                                                                                                          "
				+"       (case when t1.COLSEQ = t2.COLSEQ OR (t1.COLSEQ is null and t2.COLSEQ is null) THEN 0  else 1 end) mod_COLSEQ,                      "
				+"       T1.COLSEQ AS S_COLSEQ,                                                                                                            "
				+"       T2.COLSEQ AS T_COLSEQ,                                                                                                            "
				+"       (case when t1.PKFLAG = t2.PKFLAG OR (t1.PKFLAG is null and t2.PKFLAG is null) THEN 0  else 1 end) mod_PKFLAG,                      "
				+"       T1.PKFLAG AS S_PKFLAG,                                                                                                            "
				+"       T2.PKFLAG AS T_PKFLAG,                                                                                                            "
				+"       (case when t1.NVLFLAG = t2.NVLFLAG OR (t1.NVLFLAG is null and t2.NVLFLAG is null) THEN 0  else 1 end) mod_NVLFLAG,                 "
				+"       T1.NVLFLAG AS S_NVLFLAG,                                                                                                          "
				+"       T2.NVLFLAG AS T_NVLFLAG,                                                                                                          "
				+"       (case when t1.CCOLFLAG = t2.CCOLFLAG OR (t1.CCOLFLAG is null and t2.CCOLFLAG is null) THEN 0  else 1 end) mod_CCOLFLAG,            "
				+"       T1.CCOLFLAG AS S_CCOLFLAG,                                                                                                        "
				+"       T2.CCOLFLAG AS T_CCOLFLAG,                                                                                                        "
				+"       (case when t1.INDXFLAG = t2.INDXFLAG OR (t1.INDXFLAG is null and t2.INDXFLAG is null) THEN 0  else 1 end) mod_INDXFLAG,            "
				+"       T1.INDXFLAG AS S_INDXFLAG,                                                                                                        "
				+"       T2.INDXFLAG AS T_INDXFLAG,                                                                                                        "
				+"       (case when t1.CODETAB = t2.CODETAB OR (t1.CODETAB is null and t2.CODETAB is null) THEN 0  else 1 end) mod_CODETAB,                 "
				+"       T1.CODETAB AS S_CODETAB,                                                                                                          "
				+"       T2.CODETAB AS T_CODETAB,                                                                                                          "
				+"       (case when t1.REMARK = t2.REMARK OR (t1.REMARK is null and t2.REMARK is null) THEN 0  else 1 end) mod_REMARK,                      "
				+"       T1.REMARK AS S_REMARK,                                                                                                            "
				+"       T2.REMARK AS T_REMARK,                                                                                                            "
				+"       (case when t1.colname is null then 'I' when t2.colname is null then 'D' ELSE 'U' END ) altType                                             "
				+"  from (select *                                                                                                                         "
				+"          from T_MP_META_INT_COL_INFO b                                                                                                 "
				+"         where b.f_enddate >= '"+DateUtil.getYmdDate()+"'                                                                                                 "
				+"           and b.db_code = '"+dbId+"') t1 full join                                                                                          "
				+"       (select *                                                                                                                         "
				+"          from T_MP_META_INT_COL_INFO_TMP b                                                                                             "
				+"         where  b.db_code = '"+dbId+"' and b.id = '"+tmpId+"') t2                                                                                                    "
				+"           on t1.db_code = t2.db_code                                                                                                    "
				+"           and t1.schname = t2.schname                                                                                                   "
				+"           and t1.modname = t2.modname                                                                                                   "
				+"           and t1.tabname = t2.tabname                                                                                                   "
				+"           and t1.colname = t2. colname                                                                                                  ";
		dataMap = this.queryListForMap(sql);
		return dataMap;
	}

	@Override
	public void delMetaInfoTmp(String tmpId) {
		String sql = "delete t_mp_meta_int_col_info_tmp where id = '"+tmpId+"'";
		executeHql(sql, null);
		sql = "delete t_mp_meta_int_blk_info_tmp where id = '"+tmpId+"'";
		executeHql(sql, null);
		sql = "delete t_mp_meta_int_db_info_tmp where id = '"+tmpId+"'";
		executeHql(sql, null);
		sql = "delete t_mp_meta_int_sch_info_tmp where id = '"+tmpId+"'";
		executeHql(sql, null);
		sql = "delete t_mp_meta_int_tab_info_tmp where id = '"+tmpId+"'";
		executeHql(sql, null);
		
	}

	@Override
	public void delUploadInfo(PageResults page) {
		String uploadId = page.getParamStr("uploadId");
		String batchId = page.getParamStr("batchId");
		String sql = "delete T_MP_META_IMP_INFO where upload_id = '"+uploadId+"'";
		executeSql(sql, null);
		
		sql = "DELETE T_MP_META_INT_COL_INFO_DEC D WHERE D.ALT_ID in (select alt_id from T_MP_ALTERATION_REGISTER_INFO I WHERE I.ALT_BATCH = '"+batchId+"') ";
		executeSql(sql, null);
		
		sql = "DELETE T_MP_META_INT_TAB_INFO_DEC  D WHERE D.ALT_ID in (select alt_id from T_MP_ALTERATION_REGISTER_INFO I WHERE I.ALT_BATCH = '"+batchId+"') ";
		executeSql(sql, null);
		
		sql = "DELETE T_MP_META_INT_BLK_INFO_DEC  D WHERE D.ALT_ID in (select alt_id from T_MP_ALTERATION_REGISTER_INFO I WHERE I.ALT_BATCH = '"+batchId+"') ";
		executeSql(sql, null);
		
		sql = "DELETE T_MP_META_INT_SCH_INFO_DEC  D WHERE D.ALT_ID in (select alt_id from T_MP_ALTERATION_REGISTER_INFO I WHERE I.ALT_BATCH = '"+batchId+"') ";
		executeSql(sql, null);
		
		sql = "DELETE T_MP_META_INT_DB_INFO_DEC  D WHERE D.ALT_ID in (select alt_id from T_MP_ALTERATION_REGISTER_INFO I WHERE I.ALT_BATCH = '"+batchId+"') ";
		executeSql(sql, null);
		
		sql = "DELETE T_MP_ALTERTION_MODIFY_INFO I WHERE  I.ALT_ID IN (select alt_id from T_MP_ALTERATION_REGISTER_INFO I WHERE I.ALT_BATCH = '"+batchId+"')";
		executeSql(sql, null);
		
		sql = "DELETE t_mp_meta_int_col_code_dec I WHERE  I.ALT_ID IN (select alt_id from T_MP_ALTERATION_REGISTER_INFO I WHERE I.ALT_BATCH = '"+batchId+"')";
		executeSql(sql, null);

		sql = "DELETE T_MP_META_INT_FILE_DEC I WHERE  I.ALT_ID IN (select alt_id from T_MP_ALTERATION_REGISTER_INFO I WHERE I.ALT_BATCH = '"+batchId+"')";
		executeSql(sql, null);
		
		sql = "DELETE T_MP_ALTERATION_REGISTER_INFO I WHERE  I.ALT_BATCH = '"+batchId+"'";
		executeSql(sql, null);
		
	}

	@Override
	public List<Map<String, Object>> getUploadList(PageResults page) {
     List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		
		if(page == null){
			return dataMap;
		}
		boolean isAdmin = false;
		if(page.getParameter("isAdmin")!=null){
			isAdmin = (Boolean)page.getParameter("isAdmin");
		}
		String batchId = page.getParamStr("batchId");
		
		String userId = page.getParamStr("userId");
		
		String uploadM = page.getParamStr("upload_m");
		
		String sql = " select * from T_MP_META_IMP_INFO I WHERE 1 = 1 ";
		
		if(batchId!=null&&!"".equals(batchId)){
			sql += " AND i.UPLOAD_BATCH_ID  like '%"+batchId+"%' ";
		}
		
		if(userId!=null&&!"".equals(userId)&&!isAdmin){
			sql += " AND i.UPLOAD_USER  = '"+userId+"' ";
		}
		
		if(uploadM!=null&&!"".equals(uploadM)){
			sql += " AND i.UPLOAD_M  = '"+uploadM+"' ";
		}
		sql += " order by UPLOAD_TIME desc ";
		dataMap = this.queryListForMap(sql);
		/*sql = "select count(1) from ("+sql+") a";
		page.setTotalRecs(this.countSQL(sql, null));*/
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getAltMetaItem(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			if(page == null){
				return dataMap;
			}
			
			String lastAltOptId = page.getParamStr("lastAltOptId");
			String classId = page.getParamStr("classId");
			String altType = page.getParamStr("altType");
			String altMode = page.getParamStr("altMode");
			String altBatch = page.getParamStr("altBatch");
			String altObj = page.getParamStr("altObj");
			String dbId = page.getParamStr("dbId");
			String tarVerDate = page.getParamStr("optId");
			String altSts = page.getParamStr("altSts");
			
			String sql = "select R.*,D.DB_CODE AS ALTOBJ ,'/'||D.DB_CODE AS METACONTEXT "
					+" from T_MP_ALTERATION_REGISTER_INFO R "
					+" , T_MP_META_INT_DB_INFO_DEC D "
					+" WHERE R.ALT_ID = D.ALT_ID "
					+" UNION  "
					+" select R.*,S.SCHNAME AS ALTOBJ ,'/'||S.DB_CODE||'/'||S.SCHNAME AS METACONTEXT "
					+" from T_MP_ALTERATION_REGISTER_INFO R "
					+" , T_MP_META_INT_SCH_INFO_DEC S "
					+" WHERE R.ALT_ID = S.ALT_ID "
					+" UNION      "
					+" select R.*,B.MODNAME AS ALTOBJ ,'/'||B.DB_CODE||'/'||B.SCHNAME||'/'||B.MODNAME AS METACONTEXT "
					+" from T_MP_ALTERATION_REGISTER_INFO R "
					+" , T_MP_META_INT_BLK_INFO_DEC B "
					+" WHERE R.ALT_ID = B.ALT_ID "
					+" UNION	      "
					+" select R.*,T.TABNAME AS ALTOBJ ,'/'||T.DB_CODE||'/'||T.SCHNAME||'/'||T.MODNAME||'/'||T.TABNAME AS METACONTEXT "
					+" from T_MP_ALTERATION_REGISTER_INFO R "
					+" , T_MP_META_INT_TAB_INFO_DEC T "
					+" WHERE R.ALT_ID = T.ALT_ID "
					+" UNION      "
					+" select R.*,                                                                   "
					+"      C.COLNAME AS ALTOBJ,                                                     "
					+"      '/' || C.DB_CODE || '/' || C.SCHNAME || '/' ||                           "
					+"      C.MODNAME || '/' || C.TABNAME || '/' || C.COLNAME AS METACONTEXT         "
					+" from T_MP_ALTERATION_REGISTER_INFO R,                                        "
					+"      T_MP_META_INT_COL_CODE_DEC    C                                         "
					+" WHERE R.ALT_ID = C.ALT_ID                                                     "
					+" UNION      "
					+"select R.*,                              "
					+"   'INTFILE' AS ALTOBJ,                  "
					+"   '/' || C.DB_CODE  AS METACONTEXT      "
					+"from T_MP_ALTERATION_REGISTER_INFO R,   "
					+"   T_MP_META_INT_FILE_DEC    C          "
					+"WHERE R.ALT_ID = C.ALT_ID                "
					+" UNION      "
					+" select R.*,C.COLNAME AS ALTOBJ ,'/'||C.DB_CODE||'/'||C.SCHNAME||'/'||C.MODNAME||'/'||C.TABNAME||'/'||C.COLNAME AS METACONTEXT "
					+" from T_MP_ALTERATION_REGISTER_INFO R "
					+" , T_MP_META_INT_COL_INFO_DEC C "
					+" WHERE R.ALT_ID = C.ALT_ID ";
			
			sql = "select r.*,P.ITEM_VALUE AS ALTTYPENM,P1.ITEM_VALUE AS ALTMODE,P2.ITEM_VALUE AS CLASSIFER from ("+sql+") R " 
					+ " LEFT JOIN T_MP_SYS_PARAMS P ON R.ALT_TYPE = P.ITEM_CODE AND P.PARAM_CODE='ALTTYPE' "
					+ " LEFT JOIN T_MP_SYS_PARAMS P1 ON R.ALT_MODE = P1.ITEM_CODE AND P1.PARAM_CODE='ALT_MODE'"
					+ " LEFT JOIN T_MP_SYS_PARAMS P2 ON R.CLASSIFER_TYPE = P2.ITEM_CODE AND P2.PARAM_CODE='StatisticMetadata'" 
					+ " WHERE 1=1 ";
			if(dbId!=null&&!"".equals(dbId)){
				sql += " AND R.ALT_SYS_CODE = '"+dbId+"' ";
			}
			if(tarVerDate!=null&&!"".equals(tarVerDate)){
				sql += " AND R.ALT_VER_DATE_NO = '"+tarVerDate+"' ";
			}
			
			if(lastAltOptId!=null&&!"".equals(lastAltOptId)){
				sql += " AND R.CUR_VER_DATE_NO = '"+lastAltOptId+"' ";
			}
			if(classId!=null&&!"".equals(classId)){
				sql += " AND R.CLASSIFER_TYPE = '"+classId+"' ";
			}
			if(altType!=null&&!"".equals(altType)){
				sql += " AND R.ALT_TYPE = '"+altType+"' ";
			}
			if(altMode!=null&&!"".equals(altMode)){
				sql += " AND R.ALT_MODE = '"+altMode+"' ";
			}
			if(altBatch!=null&&!"".equals(altBatch)){
				sql += " AND R.ALT_BATCH = '"+altBatch+"' ";
			}
			if(altObj!=null&&!"".equals(altObj)){
				sql += " AND R.ALTOBJ like '%"+altObj+"%' ";
			}
			if(altSts!=null&&!"".equals(altSts)){
				sql += " AND R.ALT_STS = '"+altSts+"' ";
			}
			sql += " order by alt_oper_date desc ";
			dataMap = this.queryPageMap(sql, null, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public void delAltMetaDetail(PageResults page) {
       String altIds = page.getParamStr("altIds");
		
		String sql = "DELETE T_MP_META_INT_COL_INFO_DEC ";
		
		String where = " WHERE ALT_ID IN ('-1',"+altIds+") ";

		executeSql(sql+where, null);
		
		sql = "DELETE T_MP_META_INT_TAB_INFO_DEC ";
		executeSql(sql+where, null);
		
		sql = "DELETE T_MP_META_INT_BLK_INFO_DEC ";
		executeSql(sql+where, null);
		
		sql = "DELETE T_MP_META_INT_SCH_INFO_DEC ";
		executeSql(sql+where, null);
		
		sql = "DELETE T_MP_META_INT_DB_INFO_DEC ";
		executeSql(sql+where, null);
		
		sql = "DELETE T_MP_META_INT_FILE_DEC ";
		executeSql(sql+where, null);
		
		sql = "DELETE T_MP_META_INT_COL_CODE_DEC ";
		executeSql(sql+where, null);
		
		sql = "DELETE T_MP_ALTERTION_MODIFY_INFO ";
		executeSql(sql+where, null);
		
	}

	@Override
	public void updAltMetaSts(PageResults page) {
        String altIds = page.getParamStr("altIds");
		
		String altSts = page.getParamStr("altSts");
		
		String sql = " UPDATE T_MP_ALTERATION_REGISTER_INFO SET ALT_STS = '"+altSts+"' ";
		
		sql += " WHERE ALT_ID IN ('-1',"+altIds+") ";
		
		executeSql(sql, null);
		
	}

	@Override
	public List<Map<String, Object>> showAltMetaDetail(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		
		if(page == null){
			return dataMap;
		}
		
		String altId = page.getParamStr("altId");
		
		String parCode = page.getParamStr("parCode");
		
		String sql = " select M.*,P.ITEM_VALUE as colChName from T_MP_ALTERTION_MODIFY_INFO M LEFT JOIN T_MP_SYS_PARAMS P ON P.PARAM_CODE = '"+parCode+"' and P.ITEM_CODE = M.ALT_COL_NAME WHERE 1=1 ";
		
		if(altId!=null&&!"".equals(altId)){
			sql += " AND M.ALT_ID  = '"+altId+"' ";
		}
		dataMap = this.queryListForMap(sql);
		
		return dataMap;
	}

}
