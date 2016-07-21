package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.MetadataSysMgrDao;
import com.sldt.mds.dmc.mp.util.ObjectToString;
import com.sldt.mds.dmc.mp.util.PropertiesUtil;
import com.sldt.mds.dmc.mp.vo.MetaStaUsInfoVo;
import com.sldt.mds.dmc.mp.vo.MetaUserVO;

@SuppressWarnings("unchecked")
@Repository(value="metaSysMgrDao")
public class MetadataSysMgrDaoImpl extends BaseDao implements MetadataSysMgrDao{

	private static final Log log = LogFactory.getLog(MetadataSysMgrDaoImpl.class);
	
	/**
	 * 根据角色id 查找用户
	 */
	@Override
	public List<MetaUserVO> getMetaUserlistByRoleId(String roleId,
			String dealType) {
		List<MetaUserVO> metaUsers = new ArrayList<MetaUserVO>();
		if(roleId!= null && !"".equals(roleId)){
			String dbName = (String) PropertiesUtil.props.getProperty("dbName");
			String sql = "select * from "+dbName+".t_sm_user u where u.user_id in (select r.user_id from "+dbName+".t_sm_user_role_cfg r where r.role_id='"+roleId.trim()+"')";
			List<Map<String, Object>> mapList = this.queryListForMap(sql);
			if(mapList.size()>0){
				for(Map<String, Object> map:mapList){
					MetaUserVO metaUser = new MetaUserVO();
					metaUser.setImgUrl("");
					metaUser.setUserDesc(ObjectToString.retResult(map.get("USERNAME")));
					String userId = ObjectToString.retResult(map.get("USERID"));
					metaUser.setUserId(userId);
					int dealTimes = getDealTimes(userId, dealType);
					metaUser.setDealTimes(dealTimes);
					metaUser.setUserName(ObjectToString.retResult(map.get("USERNAME")));
					metaUser.setImgUrl(ObjectToString.retResult(map.get("IMG_URL")));
					metaUsers.add(metaUser);
				}
			}
		}
		return metaUsers;
	}
	
	private int getDealTimes(String userId,String dealType){
		int dealTimes = 0;
		String countSql = "";
		if("peMa".equals(dealType)){
			countSql = "select count(*) from T_MP_DECLARE_INFO l where l.ALT_USER='"+userId+"'";
		}else if("ma".equals(dealType)){
			countSql = "select count(*) from t_mp_check_info l where l.auditor='"+userId+"'";
		}else if("rela".equals(dealType)){
			countSql = "select count(*) from t_mp_check_info l where l.auditor='"+userId+"'";
		}
		if(countSql!=null && !"".equals(countSql)){
			dealTimes = this.countSQL(countSql, null);
		}
		return dealTimes;
	}

	/**
	 * 获取元数据 用户统计信息
	 */
	@Override
	public MetaStaUsInfoVo getMetaStaUsInfoByRoleId(String roleId,
			String dealType) {
		MetaStaUsInfoVo msuiv = new MetaStaUsInfoVo();
		String dbName = (String) PropertiesUtil.props.getProperty("dbName");
		if(roleId!=null && !"".equals(roleId)){
			String countSql = "select count(*) from "+dbName+".t_sm_user u where u.user_id in (select r.user_id from "+dbName+".t_sm_user_role_cfg r where r.role_id = '"+roleId.trim()+"')";
		    //总用户数
			msuiv.setTotalUsers(this.countSQL(countSql, null));
		}
		String countSql = "";
		String fstUserSql  = "";
		if("peMa".equals(dealType)){
			countSql = "select count(*) from T_MP_DECLARE_INFO l where l.ALT_USER in (select r.user_id from "+dbName+".t_sm_user_role_cfg r where r.role_id='"+roleId.trim()+"')";
			
			fstUserSql = "select * from "+
			"(select u.user_id,u.name,u.img_url from "+dbName+".t_sm_user u where u.user_id in (select r.user_id from "+dbName+".t_sm_user_role_cfg r where r.role_id='"+roleId.trim()+"')"+
			") a left join +" +
			"(select t.alt_user, count(*) AS DETIMES from T_MP_DECLARE_INFO t group by t.alt_user"+
			") b  on a.userid = b.alt_user order by DETIMES asc";	
		}else if("ma".equals(dealType)){
			countSql = "select count(*) from t_mp_check_info l where l.auditor in (select r.user_id from "+dbName+".t_sm_user_role_cfg r where r.role_id='"+roleId.trim()+"')";
			
			fstUserSql = "select * from "+
			"(select u.user_id,u.name,u.img_url from "+dbName+".t_sm_user u where u.user_id in (select r.user_id from "+dbName+".t_sm_user_role_cfg r where r.role_id='"+roleId.trim()+"')"+
			") a left join "+
			"(select t.auditor, count(*) AS DETIMES from t_mp_check_info t group by t.auditor"+
			") b  on a.userid = b.auditor order by DETIMES asc";
			
		}else if("rela".equals(dealType)){
			countSql = "select count(*) from t_mp_check_info l where l.auditor  in (select r.user_id from "+dbName+".t_sm_user_role_cfg r where r.role_id='"+roleId.trim()+"')";
		   
			fstUserSql = "select * from "+
					"(select u.user_id,u.name,u.img_url from "+dbName+".t_sm_user u where u.user_id in (select r.user_id from "+dbName+".t_sm_user_role_cfg r where r.role_id='"+roleId.trim()+"')"+
					") a left join "+
					"(select t.auditor, count(*) AS DETIMES from t_mp_check_info t group by t.auditor"+
					") b  on a.userid = b.auditor order by DETIMES asc";
		}
		//总处理次数
		msuiv.setTotalDealTimes(this.countSQL(countSql, null));
		List<Map<String, Object>> mapList = this.queryListForMap(countSql);
        if(mapList.size()>0){
        	Map<String, Object> map = mapList.get(0);
        	msuiv.setFstUserId(ObjectToString.retResult(map.get("USERID")));
        	msuiv.setFstName(ObjectToString.retResult(map.get("USERNAME")));
        	msuiv.setFsImgUrl(ObjectToString.retResult(map.get("IMG_URL")));
        	if(map.get("DETIMES")==null){
				msuiv.setFstDealTimes(0);
			}else{
				msuiv.setFstDealTimes(Integer.parseInt(map.get("DETIMES").toString()));
			}
        }
		return msuiv;
	}

}
