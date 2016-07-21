package com.sldt.mds.dmc.mp.vo;


public class MetadataSchemaVO {
	
	private String dbId;//数据库ID
	private String schname;//模式名
	private String schChName;//模式中文名称
	private String devloper;//开发负责联系人
	private String remark;//描述
	private String usedSize;//已用大小
	private String tabCnt;//总表数
	private String objCnt;//总对象数
	
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
	public String getSchChName() {
		return schChName;
	}
	public void setSchChName(String schChName) {
		this.schChName = schChName;
	}
	public String getDevloper() {
		return devloper;
	}
	public void setDevloper(String devloper) {
		this.devloper = devloper;
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
	 * 
	 * 重写下面两个方法，用于判断两个对象是否相等，提高效率,不能删除，慎重修改
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((devloper == null) ? 0 : devloper.hashCode());
		result = prime * result + ((objCnt == null) ? 0 : objCnt.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result
				+ ((schChName == null) ? 0 : schChName.hashCode());
		result = prime * result + ((schname == null) ? 0 : schname.hashCode());
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
		MetadataSchemaVO other = (MetadataSchemaVO) obj;
		if (devloper == null) {
			if (other.devloper != null)
				return false;
		} else if (!devloper.equals(other.devloper))
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
		if (schChName == null) {
			if (other.schChName != null)
				return false;
		} else if (!schChName.equals(other.schChName))
			return false;
		if (schname == null) {
			if (other.schname != null)
				return false;
		} else if (!schname.equals(other.schname))
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
