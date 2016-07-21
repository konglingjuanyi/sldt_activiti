package com.sldt.mds.dmc.mp.vo;

import java.io.Serializable;

public class DeclareVO implements Serializable{
	private static final long serialVersionUID = 4152428972278795561L;
	
	private String type;
	private String decName;
	private String decId;
	private String decDesc;//DEC_DESC;
	private String altOperDate;//ALT_OPER_DATE;
	private String altUser;//ALT_USER;
	private String actId;//ACT_ID;
	private String actSts;//ACT_STS;
	private String lastModifier;//LAST_MODIFIER;
	private String lastModifyTime;//LAST_MODIFY_TIME;
	
	public String getDecName() {
		return decName;
	}
	public void setDecName(String decName) {
		this.decName = decName;
	}
	public String getDecId() {
		return decId;
	}
	public void setDecId(String decId) {
		this.decId = decId;
	}
	public String getDecDesc() {
		return decDesc;
	}
	public void setDecDesc(String decDesc) {
		this.decDesc = decDesc;
	}
	public String getAltOperDate() {
		return altOperDate;
	}
	public void setAltOperDate(String altOperDate) {
		this.altOperDate = altOperDate;
	}
	public String getAltUser() {
		return altUser;
	}
	public void setAltUser(String altUser) {
		this.altUser = altUser;
	}
	public String getActId() {
		return actId;
	}
	public void setActId(String actId) {
		this.actId = actId;
	}
	public String getActSts() {
		return actSts;
	}
	public void setActSts(String actSts) {
		this.actSts = actSts;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public String getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
