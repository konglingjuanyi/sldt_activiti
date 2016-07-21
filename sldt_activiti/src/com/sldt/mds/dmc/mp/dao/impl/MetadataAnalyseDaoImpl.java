package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.sldt.framework.common.PageResults;
import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.MetadataAnalyseDao;
import com.sldt.mds.dmc.mp.util.PropertiesUtil;
import com.sldt.mds.dmc.mp.vo.AnalyseFlow;

@SuppressWarnings("unchecked")
@Repository(value="metaAnalyDao")
public class MetadataAnalyseDaoImpl extends BaseDao implements MetadataAnalyseDao{
	
	private static Log log = LogFactory.getLog(MetadataAnalyseDaoImpl.class);

	@Override
	public void delImpactSide(PageResults page) {
		String altId = page.getParamStr("altIds");
		String sql = "delete T_MP_ALTERTION_IMPACT where alt_id = "+altId+"";
		executeSql(sql, null);
	}

	@Override
	public List<Map<String, Object>> getInstanceId(PageResults page) {
		String classifer = page.getParamStr("classifer");
		String instanceCode = page.getParamStr("instanceCode");
		String parentId = page.getParamStr("parentId");
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		String sql = "select * from "+dbName+".T_MD_INST I WHERE I.PARENT_ID = '"+parentId+"' AND I.CLASS_ID = '"+classifer+"' AND I.INST_CODE = '"+instanceCode+"'";
		return this.queryListForMap(sql);
	}

	@Override
	public List<Map<String, Object>> getSNADBNamespace(PageResults page) {
		String dbCode = page.getParamStr("dbCode");
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		String sql = "select i.INST_ID,s.sna_namespace from t_mp_meta_sys_inventory s,"+dbName+".t_md_inst i where s.db_code = '"+dbCode+"' and i.namespace = s.sna_namespace";
		return this.queryListForMap(sql);
	}

	@Override
	public List<Map<String, Object>> getImpactSide(PageResults page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getImpactSys(PageResults page) {
		String namespace = page.getParamStr("namespace");
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		String sql = "select * from "+dbName+".t_md_inst i left join t_mp_meta_sys_inventory s on i.namespace = s.sna_namespace left join (select un.unitname,u.userid from "+dbName+".tb_unit_user u , "+dbName+".tb_unit un where u.unitid = un.unitid) u on u.userid = s.db_user where i.namespace like '"+namespace+"%' and i.class_id = 'PASystem' ";
		return this.queryListForMap(sql);
	}

	@Override
	public List<Map<String, Object>> getImpactTable(PageResults page) {
		String ids = page.getParamStr("ids");
		String namespace = page.getParamStr("namespace");
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		String sql = "select * from "+dbName+".t_md_inst i where i.namespace like '"+namespace+"%' and i.class_id = 'PATable'and i.inst_id in ('-1'"+ids+")";
		return this.queryListForMap(sql);
	}

	@Override
	public List<AnalyseFlow> getAnalyseFlow(PageResults page) {
		List<AnalyseFlow> flowList = new ArrayList<AnalyseFlow>();
		String sessionId = page.getParamStr("sessionId");
		String instanceId = page.getParamStr("instanceId");
		long createtime = System.currentTimeMillis();
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		//执行影响分析存储过程
		executeSql("call "+dbName+".P_IMPACT_ANALYSE('"+instanceId+"','"+sessionId+"','',"+createtime+")");
		String sql = "";
		sql+="select distinct UPSTREAM_ID,                                                ";
		sql+="                DOWNSTREAM_ID,                                              ";
		sql+="                (CASE                                                       ";
		sql+="                  WHEN UPPER(U.CLASSIFIER_ID) like 'PAETL%' OR              ";
		sql+="                       UPPER(U.CLASSIFIER_ID) like '%MAPPING' THEN          ";
		sql+="                   '1'                                                      ";
		sql+="                  ELSE                                                      ";
		sql+="                   '0'                                                      ";
		sql+="                END) UPMODELTYPE,                                           ";
		sql+="                (CASE                                                       ";
		sql+="                  WHEN UPPER(D.CLASSIFIER_ID) like 'PAETL%' OR              ";
		sql+="                       UPPER(D.CLASSIFIER_ID) like '%MAPPING' THEN          ";
		sql+="                   '1'                                                      ";
		sql+="                  ELSE                                                      ";
		sql+="                   '0'                                                      ";
		sql+="                END) DOWNMODELTYPE,                                         ";
		sql+="                u.inst_code UPSTREAMCODE,                               ";
		sql+="                u.class_id UpstreamClassifier,                         ";
		sql+="                d.inst_code DOWNUPSTREAMCODE,                           ";
		sql+="                d.class_id DownUpstreamClassifier,u.namespace upnamespace, d.namespace downupnamespace                      ";
		sql+="  from "+dbName+".T_ANALYSE_IL_RESULT r, META"+dbName+"D_INST U, "+dbName+".T_MD_INST D              ";
		sql+=" where session_id = '"+sessionId+"'                  ";
		sql+="   and U.INST_ID = R.UPSTREAM_ID                                        ";
		sql+="   AND R.DOWNSTREAM_ID = D.INST_ID                                      ";
		
		
		List<Map<String,Object>> list = this.queryListForMap(sql);
		
		for (Map<String, Object> map : list) {
			AnalyseFlow f = new AnalyseFlow();
			f.setUpstreamId((String)map.get("UPSTREAM_ID"));
			f.setDownstreamId((String)map.get("DOWNSTREAM_ID"));
			f.setUpstreamModelType(String.valueOf(map.get("UPMODELTYPE")));
			f.setDownUpstreamModelType(String.valueOf(map.get("DOWNMODELTYPE")));
			f.setUpstreamClassifier(String.valueOf(map.get("UPSTREAMCLASSIFIER")));
			f.setDownUpstreamClassifier(String.valueOf(map.get("DOWNUPSTREAMCLASSIFIER")));
			f.setUpstreamCode(String.valueOf(map.get("UPSTREAMCODE")));
			f.setDownUpstreamCode(String.valueOf(map.get("DOWNUPSTREAMCODE")));
			f.setUpnamespace(String.valueOf(map.get("UPNAMESPACE")));
			f.setDownupnamespace(String.valueOf(map.get("DOWNUPNAMESPACE")));
			flowList.add(f);
		}
		return flowList;
	}

	@Override
	public void insImpactSide(PageResults page) {
		List<Map<String,Object>> impacts = (List<Map<String,Object>>) page.getParameter("impacts");
		String altId = page.getParamStr("altIds");
		for (Map<String, Object> map : impacts) {
			String sql = "INSERT INTO T_MP_ALTERTION_IMPACT (ALT_ID,ALT_IMP_SYS,ALT_IMP_SYS_NM,ALT_IMP_TAB_COUNT,ALT_IMP_USER,ALT_IMP_USER_DEP,ALT_ANA_BASIS_TYPE)";
			sql += " VALUES( ";
			sql += " "+altId+", ";
			sql += " '"+map.get("INSTANCE_CODE")+"', ";
			sql += " '"+map.get("INSTANCE_NAME")+"', ";
			if(map.get("IMPACT_TABLE")!=null){
				sql += " '"+((List<Map<String,Object>>)map.get("IMPACT_TABLE")).size()+"', ";
			}else{
				sql += " '0', ";
			}
			sql += " '"+(map.get("USERID")==null ? "" : map.get("USERID"))+"', ";
			sql += " '"+(map.get("UNITNAME") == null ? "" : map.get("UNITNAME"))+"', ";
			sql += " '1' ";
			sql += " ) ";
			executeSql(sql, null);
		}
	}

}
