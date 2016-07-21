package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.sldt.framework.common.PageResults;
import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.MetadataDao;
import com.sldt.mds.dmc.mp.util.PropertiesUtil;

@SuppressWarnings("unchecked")
@Repository(value="metadataDao")
public class MetadataDaoImpl extends BaseDao implements MetadataDao{
	
	private static final Log log = LogFactory.getLog(MetadataDaoImpl.class);

	@Override
	public List<Map<String, Object>> getMetadataDatabase(PageResults page) {
        List<Map<String, Object>> dataMap = new ArrayList<Map<String,Object>>();
        if(page == null){
        	return dataMap;
        }
        String db_code = page.getParamStr("db_code");
        String endDate = page.getParamStr("endDate");
        
        String sql = "select * from t_mp_meta_int_db_info d where d.f_enddate>'"+endDate+"' and d.f_startdate <= '"+endDate+"'";
        
        if(db_code!=null){
        	sql += "and d.db_code = '"+db_code+"'";
        }
        dataMap = this.queryListForMap(sql);
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getProMetaByDbId(PageResults page) {
		List<Map<String, Object>> dataMap = new ArrayList<Map<String,Object>>();
		String dbId = page.getParamStr("dbId");
		String sql = "select * from t_mp_product_ver d where 1=1 and d.ENT_MODE='S'";
		if(dbId != null && !"".equals(dbId)){
			sql += "and db_code = '"+dbId+"'";
		}
		dataMap = this.queryPageMap(sql, null, page);
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getUatMetaByDbId(PageResults page) {
		List<Map<String, Object>> dataMap = new ArrayList<Map<String,Object>>();
		String dbId = page.getParamStr("dbId");
		String sql = "select * from T_MP_PRODUCT_VER d where 1=1 and d.ENT_MODE='U'";
		if(dbId!= null && !"".equals(dbId)){
			dbId = getUATDbCode(dbId);
			sql +=" and db_code = '"+dbId+"'";
		}
		dataMap = this.queryPageMap(sql, null, page);
		return dataMap;
	}
	public String getUATDbCode(String snaDbId){
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		String sql = "select * from "+dbName+".t_md_inst a where a.namespace in (select t.uat_namespace from t_mp_meta_sys_inventory t where t.db_code='"+snaDbId+"')";
		List<Map<String, Object>> list = this.queryListForMap(sql);
		String uatDbCode = "";
		if(list.size()>0){
			uatDbCode = (String) list.get(0).get("INST_CODE");
		}
			
		return uatDbCode;
		
	}

	@Override
	public List<Map<String, Object>> getColumnAutoComplete(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		
		String likeName = page.getParamStr("likeName");
		if(page == null||likeName==null||"".equals(likeName)){
			return dataMap;
		}
		
		String endDate = page.getParamStr("endDate");
		
		if(likeName!=null&&!"".equals(likeName)){
			likeName = likeName.toUpperCase();
		}
		String sql = "select *  from T_MP_META_INT_COL_INFO d where d.f_enddate>'"+endDate+"' and (upper(colname) like '%"+likeName+"%' or upper(colchname) like '%"+likeName+"%')";
		dataMap = this.queryPageMap(sql, null, page);
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getTableAutoComplete(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		
		String likeName = page.getParamStr("likeName");
		if(page == null||likeName==null||"".equals(likeName)){
			return dataMap;
		}
		
		String endDate = page.getParamStr("endDate");
		if(likeName!=null&&!"".equals(likeName)){
			likeName = likeName.toUpperCase();
		}
		String sql = "select *  from T_MP_META_INT_TAB_INFO d where d.f_enddate>'"+endDate+"' and (upper(tabname) like '%"+likeName+"%' or upper(tabchname) like '%"+likeName+"%')";
		dataMap = this.queryPageMap(sql, null, page);
		return dataMap;
	}

}
