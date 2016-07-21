package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.MyInitDeclareDao;
import com.sldt.mds.dmc.mp.util.ObjectToString;
import com.sldt.mds.dmc.mp.util.PropertiesUtil;
import com.sldt.mds.dmc.mp.vo.MyInitDeclareVO;

@SuppressWarnings("unchecked")
@Repository(value="myInitDeclareDao")
public class MyInitDeclareDaoImpl extends BaseDao implements MyInitDeclareDao{
	
	private static Log log = LogFactory.getLog(MyInitDeclareDaoImpl.class);
	
	@Override
	public void saveInitDeclare(MyInitDeclareVO myInitDeclareVO) {
		if(myInitDeclareVO != null){
			this.save(myInitDeclareVO);
		}
	}

	@Override
	public void updateInitDeclare(MyInitDeclareVO myInitDeclareVO) {
		if(myInitDeclareVO != null){
			this.update(myInitDeclareVO);
		}
	}

	@Override
	public List<MyInitDeclareVO> getMyInitDeclare(String whereSql) {
		List<MyInitDeclareVO> result = new ArrayList<MyInitDeclareVO>();
		String dbName = PropertiesUtil.props.getProperty("dbName");
		String sql = "SELECT INIT_DEC_ID, DB_CODE, ACT_ID, ALT_USER, ALT_OPER_DATE, DBCHNAME, DB_USER, DB_DESC, ACT_STS, ALT_REASON, " +
				"INSTANCE_ID, INIT_DEC_NAME, LAST_MODIFIER, LAST_MODIFY_TIME, " +
			"NAME ALTUSERNAME FROM T_MP_INIT_DECLARE_INFO LEFT JOIN "+dbName+".T_SM_USER " +
			"ON ALT_USER = USER_ID WHERE 1=1 ";
		if(StringUtils.isNotBlank(whereSql)){
			sql += whereSql;
		}
		List<Map<String, String>> list = this.queryListForMap(sql);
		if(list.size() > 0){
			for(Map<String, String> map : list){
				MyInitDeclareVO myInitDeclareVO = new MyInitDeclareVO();
				myInitDeclareVO.setActId(ObjectToString.retResult(map.get("ACT_ID")));
				myInitDeclareVO.setAltOperDate(ObjectToString.retResult(map.get("ALT_OPER_DATE")));
				myInitDeclareVO.setAltUser(ObjectToString.retResult(map.get("ALT_USER")));
				myInitDeclareVO.setInitDecId(ObjectToString.retResult(map.get("INIT_DEC_ID")));
				myInitDeclareVO.setDbCode(ObjectToString.retResult(map.get("DB_CODE")));
				myInitDeclareVO.setDbChName(ObjectToString.retResult(map.get("DBCHNAME")));
				myInitDeclareVO.setDbUser(ObjectToString.retResult(map.get("DB_USER")));
				myInitDeclareVO.setDbDesc(ObjectToString.retResult(map.get("DB_DESC")));
				myInitDeclareVO.setActSts(ObjectToString.retResult(map.get("ACT_STS")));
				myInitDeclareVO.setAltReason(ObjectToString.retResult(map.get("ALT_REASON")));
				myInitDeclareVO.setInstanceId(ObjectToString.retResult(map.get("INSTANCE_ID")));
				myInitDeclareVO.setInitDecName(ObjectToString.retResult(map.get("INIT_DEC_NAME")));
				myInitDeclareVO.setLastModifier(ObjectToString.retResult(map.get("LAST_MODIFIER")));
				myInitDeclareVO.setLastModifyTime(ObjectToString.retResult(map.get("LAST_MODIFY_TIME")));
				myInitDeclareVO.setAltUserName(ObjectToString.retResult(map.get("ALTUSERNAME")));
				result.add(myInitDeclareVO);
			}
		}
		return result;
	}

}
