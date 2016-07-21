package com.sldt.mds.dmc.mp.vo;


public class MetadataTableVO {
	
	private String dbId;//数据库ID
	private String schname;//模式名
	private String modName;//模块名
	private String tabName;//表英文名
	private String tabChName;//表中文名
	private String tabspaceName;//所属表空间
	private String pkCols;//主键字段
	private String fkCols;//外键字段
	private String fkTableName;//外键关联表
	private String rowCount;//记录数
	private String impFlag;//是否关键表
	private String remark;//描述
	private String zipDesc;//压缩描述
	private String lcycDesc;//生命周期说明
	private String pCnt;//分区数
	private String tSize;//表大小
	private String crtDate;//创建日期
	private String modiyDate;//最后更新日期
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
	
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public String getTabChName() {
		return tabChName;
	}
	public void setTabChName(String tabChName) {
		this.tabChName = tabChName;
	}
	public String getTabspaceName() {
		return tabspaceName;
	}
	public void setTabspaceName(String tabspaceName) {
		this.tabspaceName = tabspaceName;
	}
	public String getPkCols() {
		return pkCols;
	}
	public void setPkCols(String pkCols) {
		this.pkCols = pkCols;
	}
	public String getFkCols() {
		return fkCols;
	}
	public void setFkCols(String fkCols) {
		this.fkCols = fkCols;
	}
	public String getFkTableName() {
		return fkTableName;
	}
	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}
	public String getRowCount() {
		return rowCount;
	}
	public void setRowCount(String rowCount) {
		this.rowCount = rowCount;
	}
	public String getImpFlag() {
		return impFlag;
	}
	public void setImpFlag(String impFlag) {
		this.impFlag = impFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getZipDesc() {
		return zipDesc;
	}
	public void setZipDesc(String zipDesc) {
		this.zipDesc = zipDesc;
	}
	public String getLcycDesc() {
		return lcycDesc;
	}
	public void setLcycDesc(String lcycDesc) {
		this.lcycDesc = lcycDesc;
	}
	public String getpCnt() {
		return pCnt;
	}
	public void setpCnt(String pCnt) {
		this.pCnt = pCnt;
	}
	public String gettSize() {
		return tSize;
	}
	public void settSize(String tSize) {
		this.tSize = tSize;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public String getModiyDate() {
		return modiyDate;
	}
	public void setModiyDate(String modiyDate) {
		this.modiyDate = modiyDate;
	}
	
	
}
