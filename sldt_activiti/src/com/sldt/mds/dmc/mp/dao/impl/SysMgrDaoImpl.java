package com.sldt.mds.dmc.mp.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.sldt.framework.common.PageResults;
import com.sldt.framework.orm.hibernate.dao.BaseDao;
import com.sldt.mds.dmc.mp.dao.SysMgrDao;

@SuppressWarnings("unchecked")
@Repository(value="sysmgrDao")
public class SysMgrDaoImpl extends BaseDao implements SysMgrDao{

	private static Log log = LogFactory.getLog(SysMgrDaoImpl.class);
	
	@Override
	public List<Map<String, Object>> getLinks(PageResults page) {
        String sysClass = page.getParamStr("sysClass");
        String sql = " select i.*,(case when d.db_code is null then '1' else '0' end) isNull from t_mp_meta_sys_inventory i left join t_mp_meta_int_db_info d on i.db_code = d.db_code and d.f_enddate = '20991231' where 1=1 ";
        if(sysClass !=null && !"".equals(sysClass)){
        	sql += " and db_class = '"+sysClass+"' ";
        }
		return queryListForMap(sql);
	}

	@Override
	public List<Map<String, Object>> getSysParams(PageResults page) {
		List<Map<String,Object>> dataMap = new ArrayList<Map<String,Object>>();
		try {
			String parCode = page.getParamStr("parCode");
			String itemCode = page.getParamStr("itemCode");
			String sql = "select * from T_MP_SYS_PARAMS a where 1=1  ";
			
			if(parCode!=null&&!"".equals(parCode)){
				sql += " and a.param_code = '"+parCode+"' ";
			}
			if(itemCode!=null&&!"".equals(itemCode)){
				sql += " and a.item_code = '"+itemCode+"' ";
			}
			
			sql += " order by item_order ";
			
			dataMap = this.queryListForMap(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dataMap;
	}

}
