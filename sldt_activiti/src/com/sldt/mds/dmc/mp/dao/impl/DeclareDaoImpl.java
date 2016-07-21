package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sldt.framework.common.PageResults;
import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.DeclareDao;
import com.sldt.mds.dmc.mp.vo.DeclareVO;

@SuppressWarnings("unchecked")
@Repository(value="declareDao")
public class DeclareDaoImpl extends BaseDao implements DeclareDao{

	@Override
	public List<DeclareVO> getList(PageResults page, String decName, String actSts) {
		List<DeclareVO> DeclareList = new ArrayList<DeclareVO>();
		String sql = "SELECT T.DEC_ID,"+
				 "T.DEC_NAME,"+
				 "T.DEC_DESC,"+
				 "T.ALT_OPER_DATE,"+
				 "T.ALT_USER,"+
				 "T.ACT_ID,"+
				 "T.ACT_STS,"+
				 "T.LAST_MODIFIER,"+
				 "T.LAST_MODIFY_TIME,"+
				 "'变更申报审核流程' AS TYPE "+
				 "FROM T_MP_DECLARE_INFO T "+	
				 "UNION "+
				 "SELECT S.INIT_DEC_ID DEC_ID,"+	
				 "S.INIT_DEC_NAME DEC_NAME,"+
				 "S.ALT_REASON DEC_DECS,"+
				 "S.ALT_OPER_DATE,"+
				 "S.ALT_USER,"+
				 "S.ACT_ID,"+	
				 "S.ACT_STS,"+
				 "S.LAST_MODIFIER,"+
				 "S.LAST_MODIFY_TIME,"+
				 "'版本初始化申请审核流程' AS TYPE "+
				 "FROM T_MP_INIT_DECLARE_INFO S ";
		sql = "select T.* from (" + sql + ") T where 1=1 ";
		if(StringUtils.isNotBlank(decName)){
			sql += " AND T.DEC_NAME LIKE '%"+decName.trim()+"%' ";
		}
		if(StringUtils.isNotBlank(actSts)){
			sql += " AND T.ACT_STS='"+actSts.trim()+"' ";
		}
		sql += "ORDER BY T.ALT_OPER_DATE DESC ";
		List<Map<String, String>> mapList = this.queryPageMap(sql, null, page);
		if(mapList.size() > 0){
			for(Map<String, String> map : mapList){
				DeclareVO declare = new DeclareVO();
				declare.setType(map.get("TYPE"));
				declare.setActId(map.get("ACT_ID"));
				declare.setActSts(map.get("ACT_STS"));
				declare.setAltOperDate(map.get("ALT_OPER_DATE"));
				declare.setAltUser(map.get("ALT_USER"));
				declare.setDecDesc(map.get("DEC_DESC"));
				declare.setDecId(map.get("DEC_ID"));;
				declare.setDecName(map.get("DEC_NAME"));
				declare.setLastModifier(map.get("LAST_MODIFIER"));
				declare.setLastModifyTime(map.get("LAST_MODIFY_TIME"));
				DeclareList.add(declare);
			}
		}
		return DeclareList;
	}

}
