package com.sldt.mds.dmc.mp.vo;

import java.util.HashMap;
import java.util.Map;


public class MetadataDatabaseVO {
	
	private String imageUrl;//图片地址
	private String target;//打开方式，类型：_target，
	private String remark;//系统描述
	private String dbId;//数据库ID
	private String dbChName;//数据库中文名
	private String dept;//所属开发科室
	private String proMFac;//开发商
	private String devloper;//开发负责人
	private String maintainer;//一线维护人员
	private String dba;//DBA
	private String totalSize;//可用总大小
	private String usedSize;//已用大小
	private String tabCnt;//总表数
	private String objCnt;//总对象数
	private String fStartdate;//拉链开始时间
	private String fEnddate;//拉链结束时间
	private boolean isMe;//
	private String title;//
	private String valid;//
	private String dec;//
	private String notDec;//
	private String unc;//
	private String developAltHtml;//
	private String developAlt;//
	private String forwardAlt;//
	private String curVerOptId;//
	private String userName;//
	private String verName;//
	private String userId;//
	private String verDesc;//
	private String namespace;//
	private String lastVerOptId;
	private String altOperDate;
	private String isSnaCode;
	
	public String getIsSnaCode() {
		return isSnaCode;
	}
	public void setIsSnaCode(String isSnaCode) {
		this.isSnaCode = isSnaCode;
	}
	private Map<String,Object> altMap = new HashMap<String, Object>();
	
	public Map<String, Object> getAltMap() {
		return altMap;
	}
	public void setAltMap(Map<String, Object> altMap) {
		this.altMap = altMap;
	}
	public String getAltOperDate() {
		return altOperDate;
	}
	public void setAltOperDate(String altOperDate) {
		this.altOperDate = altOperDate;
	}
	public String getLastVerOptId() {
		return lastVerOptId;
	}
	public void setLastVerOptId(String lastVerOptId) {
		this.lastVerOptId = lastVerOptId;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getVerDesc() {
		return verDesc;
	}
	public void setVerDesc(String verDesc) {
		this.verDesc = verDesc;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getVerName() {
		return verName;
	}
	public void setVerName(String verName) {
		this.verName = verName;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getDec() {
		return dec;
	}
	public void setDec(String dec) {
		this.dec = dec;
	}
	public String getNotDec() {
		return notDec;
	}
	public void setNotDec(String notDec) {
		this.notDec = notDec;
	}
	public String getUnc() {
		return unc;
	}
	public void setUnc(String unc) {
		this.unc = unc;
	}
	public String getDevelopAltHtml() {
		return developAltHtml;
	}
	public void setDevelopAltHtml(String developAltHtml) {
		this.developAltHtml = developAltHtml;
	}
	public String getDevelopAlt() {
		return developAlt;
	}
	public void setDevelopAlt(String developAlt) {
		this.developAlt = developAlt;
	}
	public String getForwardAlt() {
		return forwardAlt;
	}
	public void setForwardAlt(String forwardAlt) {
		this.forwardAlt = forwardAlt;
	}
	public String getCurVerOptId() {
		return curVerOptId;
	}
	public void setCurVerOptId(String curVerOptId) {
		this.curVerOptId = curVerOptId;
	}
	public boolean isMe() {
		return isMe;
	}
	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getfStartdate() {
		return fStartdate;
	}
	public void setfStartdate(String fStartdate) {
		this.fStartdate = fStartdate;
	}
	public String getfEnddate() {
		return fEnddate;
	}
	public void setfEnddate(String fEnddate) {
		this.fEnddate = fEnddate;
	}
	public String getProMFac() {
		return proMFac;
	}
	public void setProMFac(String proMFac) {
		this.proMFac = proMFac;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDbId() {
		return dbId;
	}
	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	public String getDbChName() {
		return dbChName;
	}
	public void setDbChName(String dbChName) {
		this.dbChName = dbChName;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDevloper() {
		return devloper;
	}
	public void setDevloper(String devloper) {
		this.devloper = devloper;
	}
	public String getMaintainer() {
		return maintainer;
	}
	public void setMaintainer(String maintainer) {
		this.maintainer = maintainer;
	}
	public String getDba() {
		return dba;
	}
	public void setDba(String dba) {
		this.dba = dba;
	}
	public String getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
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
	
}
