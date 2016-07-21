package com.sldt.mds.dmc.mp.vo;


public class MetadataModuleVO {
	
	private String dbId;//数据库ID
	private String schname;//模式名
	private String modName;//模块名
	private String modChName;//模块中文名称
	private String dept;//所属机构
	private String deptcharger;//分管室经理
	private String devloper;//开发负责联系人
	private String sa;//SA
	private String modPtn;//模块识别表达式
	private String status;//使用状态
	private String remark;//模式描述
	private String usedSize;//已用大小
	private String tabCnt;//总表数
	private String objCnt;//总对象数
	private String fStartDate;//总对象数
	private String fEndDate;//总对象数
	
	public String getfStartDate() {
		return fStartDate;
	}
	public void setfStartDate(String fStartDate) {
		this.fStartDate = fStartDate;
	}
	public String getfEndDate() {
		return fEndDate;
	}
	public void setfEndDate(String fEndDate) {
		this.fEndDate = fEndDate;
	}
	public String getDbId() {
		return dbId;
	}
	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	public String getSchname() {
		return schname;
	}
	public void setSchname(String schname) {
		this.schname = schname;
	}
	public String getModName() {
		return modName;
	}
	public void setModName(String modName) {
		this.modName = modName;
	}
	public String getModChName() {
		return modChName;
	}
	public void setModChName(String modChName) {
		this.modChName = modChName;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDeptcharger() {
		return deptcharger;
	}
	public void setDeptcharger(String deptcharger) {
		this.deptcharger = deptcharger;
	}
	public String getDevloper() {
		return devloper;
	}
	public void setDevloper(String devloper) {
		this.devloper = devloper;
	}
	public String getSa() {
		return sa;
	}
	public void setSa(String sa) {
		this.sa = sa;
	}
	public String getModPtn() {
		return modPtn;
	}
	public void setModPtn(String modPtn) {
		this.modPtn = modPtn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUsedSize() {
		return usedSize;
	}
	public void setUsedSize(String usedSize) {
		this.usedSize = usedSize;
	}
	public String getTabCnt() {
		return tabCnt;
	}
	public void setTabCnt(String tabCnt) {
		this.tabCnt = tabCnt;
	}
	public String getObjCnt() {
		return objCnt;
	}
	public void setObjCnt(String objCnt) {
		this.objCnt = objCnt;
	}
	
	/**
	 * 下面连个方法重新，用于比较两个对象是否相等，勿删，慎改
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dept == null) ? 0 : dept.hashCode());
		result = prime * result
				+ ((deptcharger == null) ? 0 : deptcharger.hashCode());
		result = prime * result
				+ ((devloper == null) ? 0 : devloper.hashCode());
		result = prime * result
				+ ((modChName == null) ? 0 : modChName.hashCode());
		result = prime * result + ((modName == null) ? 0 : modName.hashCode());
		result = prime * result + ((modPtn == null) ? 0 : modPtn.hashCode());
		result = prime * result + ((objCnt == null) ? 0 : objCnt.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((sa == null) ? 0 : sa.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tabCnt == null) ? 0 : tabCnt.hashCode());
		result = prime * result
				+ ((usedSize == null) ? 0 : usedSize.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetadataModuleVO other = (MetadataModuleVO) obj;
		if (dept == null) {
			if (other.dept != null)
				return false;
		} else if (!dept.equals(other.dept))
			return false;
		if (deptcharger == null) {
			if (other.deptcharger != null)
				return false;
		} else if (!deptcharger.equals(other.deptcharger))
			return false;
		if (devloper == null) {
			if (other.devloper != null)
				return false;
		} else if (!devloper.equals(other.devloper))
			return false;
		if (modChName == null) {
			if (other.modChName != null)
				return false;
		} else if (!modChName.equals(other.modChName))
			return false;
		if (modName == null) {
			if (other.modName != null)
				return false;
		} else if (!modName.equals(other.modName))
			return false;
		if (modPtn == null) {
			if (other.modPtn != null)
				return false;
		} else if (!modPtn.equals(other.modPtn))
			return false;
		if (objCnt == null) {
			if (other.objCnt != null)
				return false;
		} else if (!objCnt.equals(other.objCnt))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (sa == null) {
			if (other.sa != null)
				return false;
		} else if (!sa.equals(other.sa))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tabCnt == null) {
			if (other.tabCnt != null)
				return false;
		} else if (!tabCnt.equals(other.tabCnt))
			return false;
		if (usedSize == null) {
			if (other.usedSize != null)
				return false;
		} else if (!usedSize.equals(other.usedSize))
			return false;
		return true;
	}
	
	
	
	
}
