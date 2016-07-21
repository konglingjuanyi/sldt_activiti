package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.CheckInfoDao;
import com.sldt.mds.dmc.mp.util.PropertiesUtil;
import com.sldt.mds.dmc.mp.vo.CheckInfo;

@SuppressWarnings("unchecked")
@Repository(value="checkInfoDao")
public class CheckInfoDaoImpl extends BaseDao implements CheckInfoDao{

	@Override
	public List<CheckInfo> getCheckInfo(String whereSql) {
		String dbName = PropertiesUtil.props.getProperty("dbName");
		String sql = "SELECT C.ACT_ID, C.AUDITOR,(select r1.role_name from "+dbName+".T_SM_USER u left join "+dbName+".T_SM_USER_ROLE_CFG r on r.user_id = u.user_id left join "+dbName+".T_SM_ROLE r1 on r1.role_id = r.role_id where r1.role_id is not null limit 1 ) ROLENAME,(select u.name from "+dbName+".T_SM_USER u where u.user_id = c.auditor limit 1 ) USERNAME, C.AUDIT_TIME, C.AUDIT_RESULT, C.AUDIT_OPINION, C.DEPARTMENT, C.NODE FROM T_MP_CHECK_INFO C WHERE 1=1 ";
		if(StringUtils.isNotBlank(whereSql)){
			sql += whereSql;
		}
		List<Map<String, String>> mapList = this.queryListForMap(sql);
		List<CheckInfo> checkInfos = new ArrayList<CheckInfo>();
		if(mapList.size() > 0){
			for(Map<String, String> map : mapList){
				CheckInfo checkInfo = new CheckInfo();
				checkInfo.setAuditor(map.get("AUDITOR"));
				checkInfo.setAuditOpinion(map.get("AUDIT_OPINION"));
				checkInfo.setAuditResult(map.get("AUDIT_RESULT"));
				checkInfo.setAuditTime(map.get("AUDIT_TIME"));
				checkInfo.setDepartment(map.get("DEPARTMENT"));
				checkInfo.setActId(map.get("ACT_ID"));
				checkInfo.setNode(map.get("NODE"));
				checkInfo.setUserName(map.get("USERNAME"));
				checkInfo.setRoleName(map.get("ROLENAME"));
				checkInfos.add(checkInfo);
			}
		}
		return checkInfos;
	}

	@Override
	public void save(CheckInfo checkInfo) {
		if(checkInfo != null){
			String sql = "INSERT INTO T_MP_CHECK_INFO (ACT_ID,AUDITOR,AUDIT_TIME,AUDIT_RESULT,AUDIT_OPINION," +
				"DEPARTMENT,NODE) VALUES (?,?,?,?,?,?,?)";
			Object[] params = {checkInfo.getActId(), checkInfo.getAuditor(), checkInfo.getAuditTime(), checkInfo.getAuditResult(), 
					checkInfo.getAuditOpinion(), checkInfo.getDepartment(), checkInfo.getNode()};
			this.updateSQL(sql, params);
		}
	}

}
