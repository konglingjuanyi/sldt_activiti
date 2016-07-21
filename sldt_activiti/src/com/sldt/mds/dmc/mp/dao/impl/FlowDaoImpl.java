package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.sldt.framework.common.PageResults;
import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.FlowDao;
import com.sldt.mds.dmc.mp.util.ObjectToString;
import com.sldt.mds.dmc.mp.util.PropertiesUtil;

@SuppressWarnings("unchecked")
@Repository(value="flowDao")
public class FlowDaoImpl extends BaseDao implements FlowDao{
	
	private static Log log = LogFactory.getLog(FlowDaoImpl.class);

	@Override
	public List getMetaSysInventory(String whereSql) {
		String sql = "SELECT M.DB_CODE,M.DBCHNAME,M.DB_USER,M.DB_DESC,M.UAT_NAMESPACE,M.SNA_NAMESPACE," +
				"M.CLASS_NAME FROM T_MP_META_SYS_INVENTORY M WHERE 1=1 ";
		if(StringUtils.isNotBlank(whereSql)){
			sql += whereSql;
		}
		return this.queryListForMap(sql);
	}

	@Override
	public void updateAlterationAltsts(String altSts, String whereSql) {
		String sql = "UPDATE T_MP_ALTERATION_REGISTER_INFO A SET A.ALT_STS = '"+altSts+"' WHERE 1=1 ";
		if(StringUtils.isNotBlank(whereSql)){
			sql += whereSql;
		}
		this.executeSql(sql, null);
	}

	@Override
	public List getUsers(String whereSql) {
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		String sql = "SELECT T.USER_ID USERID,T.NAME USERNAME FROM "+dbName+".T_SM_USER T LEFT JOIN T_MP_ACT_ID_MEMBERSHIP A " +
				"ON T.USER_ID = A.USER_ID_ " +
				"WHERE 1=1 ";
		if(StringUtils.isNotBlank(whereSql)){
			sql += whereSql;
		}
		return this.queryListForMap(sql);
	}

	@Override
	public List getUnitUser(String whereSql) {
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		String sql = "SELECT USER_ID, ORG_ID FROM "+dbName+".T_SM_USER_ORG_CFG WHERE 1=1 ";
		if(StringUtils.isNotBlank(whereSql)){
			sql += whereSql;
		}
		return this.queryListForMap(sql);
	}

	@Override
	public List<Map<String, Object>> getAltMetaItem(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			if(page == null){
				return dataMap;
			}
			
			String altIds = page.getParamStr("altIds");
			
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
			//如何取代oracle中的伪列排序
			sql = "select r.*,P.ITEM_VALUE AS ALTTYPENM,P1.ITEM_VALUE AS ALTMODE,P2.ITEM_VALUE AS CLASSIFER from ("+sql+") R " 
					+ " LEFT JOIN T_MP_SYS_PARAMS P ON R.ALT_TYPE = P.ITEM_CODE AND P.PARAM_CODE='ALTTYPE' "
					+ " LEFT JOIN T_MP_SYS_PARAMS P1 ON R.ALT_MODE = P1.ITEM_CODE AND P1.PARAM_CODE='ALT_MODE'"
					+ " LEFT JOIN T_MP_SYS_PARAMS P2 ON R.CLASSIFER_TYPE = P2.ITEM_CODE AND P2.PARAM_CODE='StatisticMetadata'" 
					+ " WHERE 1=1 ";
			if(altIds!=null&&!"".equals(altIds)){
				sql += " AND R.ALT_ID IN ("+ObjectToString.loadSql(altIds)+") ";
			}
			
			dataMap = this.queryPageMap(sql, null, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	@Override
	public List getAltertionImpact(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		if(page == null){
			return dataMap;
		}
		String dbName = (String)PropertiesUtil.props.getProperty("dbName");
		String altIds = page.getParamStr("altIds");
		String whereSql = "";
		if(StringUtils.isNotBlank(altIds)){
			whereSql = ObjectToString.loadSql(altIds);
		}
		String sql = " ";
		
		sql+="SELECT A.ALT_IMP_SYS,                                                                                                                                                                  ";
		sql+="       A.ALT_IMP_SYS_NM,                                                                                                                                                               ";
		sql+="       sum(A.ALT_IMP_TAB_COUNT) ALT_IMP_TAB_COUNT,                                                                                                                                     ";
		sql+="       A.ALT_IMP_USER,                                                                                                                                                                 ";
		sql+="       A.ALT_IMP_USER_DEP,                                                                                                                                                             ";
		sql+="       (case                                                                                                                                                                           ";
		sql+="         when A.ALT_ANA_BASIS_TYPE = '1' then                                                                                                                                          ";
		sql+="          'ETL映射'                                                                                                                                                                    ";
		sql+="         when A.ALT_ANA_BASIS_TYPE = '2' then                                                                                                                                          ";
		sql+="          '表级使用关系'                                                                                                                                                               ";
		sql+="       end) ALT_ANA_BASIS_TYPE                                                                                                                                                         ";
		sql+="  FROM T_MP_ALTERTION_IMPACT A                                                                                                                                                        ";
		sql+=" WHERE 1 = 1                                                                                                                                                                           ";
		sql+="   AND A.ALT_ID IN ('-1',"+whereSql+")                                                                                                                              ";
		sql+=" group by A.ALT_IMP_SYS,                                                                                                                                                               ";
		sql+="          A.ALT_IMP_SYS_NM,                                                                                                                                                            ";
		sql+="          A.ALT_IMP_SYS_NM,                                                                                                                                                            ";
		sql+="          A.ALT_IMP_USER,                                                                                                                                                              ";
		sql+="          A.ALT_IMP_USER_DEP,                                                                                                                                                          ";
		sql+="          A.ALT_ANA_BASIS_TYPE                                                                                                                                                         ";
		sql+="  union all                                                                                                                                                                            ";
		sql+="select ALT_IMP_SYS,ALT_IMP_SYS_NM,count(ref_tabname)ALT_IMP_TAB_COUNT ,ALT_IMP_USER,ALT_IMP_USER_DEP,ALT_ANA_BASIS_TYPE  from (                                                        ";
		sql+=" select distinct f.db_code as ALT_IMP_SYS ,s1.dbchname as ALT_IMP_SYS_NM,f.ref_tabname,s1.db_user as ALT_IMP_USER ,u.org_name as ALT_IMP_USER_DEP,'表级使用关系' as ALT_ANA_BASIS_TYPE   ";
		sql+="   from T_MP_ALTERATION_REGISTER_INFO r                                                                                                                                               ";
		sql+="   left join T_MP_meta_sys_inventory s                                                                                                                                                ";
		sql+="     on r.alt_sys_code = s.db_code                                                                                                                                                     ";
		sql+="   left join T_MP_META_INT_COL_INFO_DEC C                                                                                                                                             ";
		sql+="     on r.alt_id = c.alt_id                                                                                                                                                            ";
		sql+="     ,T_MP_META_INT_FILE F left join t_mp_meta_sys_inventory s1 on f.db_code = s1.db_code                                                                                                                                                    ";
		sql+="   left join "+dbName+".t_sm_user_org_cfg n on s1.db_user = n.user_id                                                                                                                               ";
		sql+="   left join "+dbName+".t_sm_org_info u on u.org_id = n.org_id                                                                                                                                     ";
		sql+="  where r.alt_id in ('-1',"+whereSql+")                                                                                                                               ";
		sql+="      AND c.db_code = F.REF_DB_CODE AND C.SCHNAME = F.REF_SCHNAME AND C.MODNAME = F.REF_MODNAME AND C.TABNAME = F.REF_TABNAME                                                          ";
		sql+=") a                                                                                                                                                                                     ";
		sql+="group by ALT_IMP_SYS,ALT_IMP_SYS_NM ,ALT_IMP_USER,ALT_IMP_USER_DEP,ALT_ANA_BASIS_TYPE                                                                                                  ";
		sql+="union all                                                                                                                                                                              ";
		sql+="select ALT_IMP_SYS,ALT_IMP_SYS_NM,count(ref_tabname)ALT_IMP_TAB_COUNT ,ALT_IMP_USER,ALT_IMP_USER_DEP,ALT_ANA_BASIS_TYPE  from (                                                        ";
		sql+=" select distinct f.db_code as ALT_IMP_SYS ,s1.dbchname as ALT_IMP_SYS_NM,f.ref_tabname,s1.db_user as ALT_IMP_USER ,u.org_name as ALT_IMP_USER_DEP,'表级使用关系' as ALT_ANA_BASIS_TYPE   ";
		sql+="   from T_MP_ALTERATION_REGISTER_INFO r                                                                                                                                               ";
		sql+="   left join t_mp_meta_sys_inventory s                                                                                                                                                ";
		sql+="     on r.alt_sys_code = s.db_code                                                                                                                                                     ";
		sql+="   left join T_MP_META_INT_TAB_INFO_DEC C                                                                                                                                             ";
		sql+="     on r.alt_id = c.alt_id                                                                                                                                                            ";
		sql+="     ,T_MP_META_INT_FILE F left join t_mp_meta_sys_inventory s1 on f.db_code = s1.db_code                                                                                                                                                           ";
		sql+="   left join "+dbName+".t_sm_user_org_cfg n on s1.db_user = n.user_id                                                                                                                               ";
		sql+="   left join "+dbName+".t_sm_org_info u on u.org_id = n.org_id                                                                                                                                     ";
		sql+="  where r.alt_id in ('-1',"+whereSql+")                                                                                                                               ";
		sql+="      AND c.db_code = F.REF_DB_CODE AND C.SCHNAME = F.REF_SCHNAME AND C.MODNAME = F.REF_MODNAME AND C.TABNAME = F.REF_TABNAME                                                          ";
		sql+=") b                                                                                                                                                                                     ";
		sql+="group by ALT_IMP_SYS,ALT_IMP_SYS_NM ,ALT_IMP_USER,ALT_IMP_USER_DEP,ALT_ANA_BASIS_TYPE                                                                                                  ";
		
		
		sql = " SELECT T.* FROM ( "+sql+") T ";
		dataMap = this.queryPageMap(sql, null, page);
		return dataMap;
	}

	@Override
	public List getMetaIntFileDec(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		if(page == null){
			return dataMap;
		}
		String altIds = page.getParamStr("altIds");
		String sql = "SELECT M.F_C_ID,M.ALT_ID,M.DB_CODE,M.REF_DB_CODE,M.REF_SCHNAME,M.REF_MODNAME,M.REF_TABNAME," +
				"M.C_FUNC_NAME,M.C_DESC,M.C_TYPE FROM T_MP_META_INT_FILE_DEC M WHERE 1=1 ";
		if(StringUtils.isNotBlank(altIds)){
			sql += "AND M.ALT_ID IN("+ObjectToString.loadSql(altIds)+") ";
		}
		sql = " SELECT T.*,ROWNUM AS XL FROM ( "+sql+") T ";
		dataMap = this.queryPageMap(sql, null, page);
		return dataMap;
	}

	@Override
	public List getMetaIntColCodeDec(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		if(page == null){
			return dataMap;
		}
		String altIds = page.getParamStr("altIds");
		String sql = "SELECT M.C_C_ID,M.ALT_ID,M.DB_CODE,M.SCHNAME,M.MODNAME,M.TABNAME,M.COLNAME,M.CODE_VAL,M.CODE_CH_NAME," +
				"M.CODE_DESC,M.CODE_STS,M.CODE_START_DATE,M.CODE_END_DATE,M.DS_REF FROM T_MP_META_INT_COL_CODE_DEC M WHERE 1=1 ";
		if(StringUtils.isNotBlank(altIds)){
			sql += "AND M.ALT_ID IN("+ObjectToString.loadSql(altIds)+") ";
		}
		sql = " SELECT T.*,ROWNUM AS XL FROM ( "+sql+") T ";
		dataMap = this.queryPageMap(sql, null, page);
		return dataMap;
	}

}
