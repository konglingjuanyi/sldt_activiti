package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.MyDeclareDao;
import com.sldt.mds.dmc.mp.util.PropertiesUtil;
import com.sldt.mds.dmc.mp.vo.DeclareDetailVO;
import com.sldt.mds.dmc.mp.vo.MyDeclareVO;

@SuppressWarnings("unchecked")
@Repository(value="myDeclareDao")
public class MyDeclareDaoImpl extends BaseDao implements MyDeclareDao{

	@Override
	public List<MyDeclareVO> getMyDeclare(String whereSql) {
		String dbName = PropertiesUtil.props.getProperty("dbName");
		String sql = "SELECT DEC_ID, DEC_NAME, DEC_DESC, ALT_OPER_DATE, ALT_USER, ACT_ID, ACT_STS, LAST_MODIFIER, " +
				"LAST_MODIFY_TIME, DEC_TYPE, NAME ALTUSERNAME FROM T_MP_DECLARE_INFO LEFT JOIN "+dbName+".T_SM_USER ON ALT_USER = USER_ID WHERE 1=1 ";
		if(StringUtils.isNotBlank(whereSql)){
			sql += whereSql;
		}
		
		List<Map<String, String>> list = this.queryListForMap(sql);
		List<MyDeclareVO> result = new ArrayList<MyDeclareVO>();
		if(list.size() > 0){
			for(Map<String, String> map : list){
				MyDeclareVO myDeclareVO = new MyDeclareVO();
				myDeclareVO.setDecId(map.get("DEC_ID"));
				myDeclareVO.setDecName(map.get("DEC_NAME"));
				myDeclareVO.setDecDesc(map.get("DEC_DESC"));
				myDeclareVO.setAltOperDate(map.get("ALT_OPER_DATE"));
				myDeclareVO.setAltUser(map.get("ALT_USER"));
				myDeclareVO.setActId(map.get("ACT_ID"));
				myDeclareVO.setActSts(map.get("ACT_STS"));
				myDeclareVO.setLastModifier(map.get("LAST_MODIFIER"));
				myDeclareVO.setLastModifyTime(map.get("LAST_MODIFY_TIME"));
				myDeclareVO.setDecType(map.get("DEC_TYPE"));
				myDeclareVO.setAltUserName(map.get("ALTUSERNAME"));
				result.add(myDeclareVO);
			}
		}
		return result;
	}

	@Override
	public void update(MyDeclareVO myDeclareVO, String whereSql) {
		String sql = "UPDATE T_MP_DECLARE_INFO SET DEC_ID=?, DEC_NAME=?, DEC_DESC=?, ALT_OPER_DATE=?, ALT_USER=?, " +
				"ACT_ID=?, ACT_STS=?, LAST_MODIFIER=?, LAST_MODIFY_TIME=?, DEC_TYPE=? WHERE 1=1 ";
		if(StringUtils.isNotBlank(whereSql)){
			sql += whereSql;
		}
		Object[] params = {myDeclareVO.getDecId(), myDeclareVO.getDecName(), myDeclareVO.getDecDesc(), myDeclareVO.getAltOperDate(), 
				myDeclareVO.getAltUser(), myDeclareVO.getActId(), myDeclareVO.getActSts(), myDeclareVO.getLastModifier(), myDeclareVO.getLastModifyTime(), 
				myDeclareVO.getDecType()};
		this.updateSQL(sql, params);
	}

	@Override
	public List getDecDetailed(String whereSql) {
		String sql = "SELECT DECLARE_ID DECID, ALT_ID ALTID FROM T_MP_DECLARE_DETAIL WHERE 1=1 ";
		if(StringUtils.isNotBlank(whereSql)){
			sql += whereSql;
		}
		return this.queryListForMap(sql);
	}

	@Override
	public void saveDeclare(MyDeclareVO myDeclareVO) {
		if(myDeclareVO != null){
			this.save(myDeclareVO);
		}
	}

	@Override
	public void saveDecDetailed(String decId, String altId) {
		if(decId == null || altId == null){
			return;
		}
		DeclareDetailVO declareDetail = new DeclareDetailVO();
		declareDetail.setDeclareId(decId);
		declareDetail.setAltId(altId);
		this.save(declareDetail);
	}

}
