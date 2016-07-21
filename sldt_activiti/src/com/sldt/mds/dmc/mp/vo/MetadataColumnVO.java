package com.sldt.mds.dmc.mp.vo;

public class MetadataColumnVO {
	private String dbId;//数据库ID
	private String schname;//模式名
	private String modName;//模块名
	private String tabName;//表英文名
	private String colName;//字段英文名
	private String colChName;//字段中文名
	private String colType;//字段类型
	private String colSeq;//字段序号
	private String pkFlag;//主键
	private String pdkFlag;//分布分区键
	private String nvlFlag;//是否允许空值
	private String ccolFlag;//是否代码字段
	private String indxFlag;//是否有索引
	private String codeTab;//引用代码表
	private String remark;//字段描述
	private String dsRef;//数据标准引用
	private String fStartDate;//总对象数
	private String fEndDate;//总对象数
	
	public String getDsRef() {
		return dsRef;
	}
	public void setDsRef(String dsRef) {
		this.dsRef = dsRef;
	}
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
	
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColChName() {
		return colChName;
	}
	public void setColChName(String colChName) {
		this.colChName = colChName;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public String getColSeq() {
		return colSeq;
	}
	public void setColSeq(String colSeq) {
		this.colSeq = colSeq;
	}
	public String getPkFlag() {
		return pkFlag;
	}
	public void setPkFlag(String pkFlag) {
		this.pkFlag = pkFlag;
	}
	public String getPdkFlag() {
		return pdkFlag;
	}
	public void setPdkFlag(String pdkFlag) {
		this.pdkFlag = pdkFlag;
	}
	public String getNvlFlag() {
		return nvlFlag;
	}
	public void setNvlFlag(String nvlFlag) {
		this.nvlFlag = nvlFlag;
	}
	public String getCcolFlag() {
		return ccolFlag;
	}
	public void setCcolFlag(String ccolFlag) {
		this.ccolFlag = ccolFlag;
	}
	public String getIndxFlag() {
		return indxFlag;
	}
	public void setIndxFlag(String indxFlag) {
		this.indxFlag = indxFlag;
	}
	public String getCodeTab() {
		return codeTab;
	}
	public void setCodeTab(String codeTab) {
		this.codeTab = codeTab;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
