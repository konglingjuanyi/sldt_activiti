package com.sldt.mds.dmc.mp.vo;

public class MyDeclareVO {
	private String rn;
	private String type;
	private String decId;//申报流水号
	private String decName;//申报名称
	private String decDesc;//申报描述
	private String altOperDate;//提交申报时间
	private String altUser;//提交申报用户
	private String altUserName;//提交申报用户名
	private String actId;//流程ID
	private String actSts;//申报单状态
	private String lastModifier;//修改者
	private String lastModifyTime;//修改时间
	private String taskId;
	private String name;
	private String assignee;
	private String decType;//变更申报类型
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public String getDecId() {
		return decId;
	}
	public void setDecId(String decId) {
		this.decId = decId;
	}
	public String getDecName() {
		return decName;
	}
	public void setDecName(String decName) {
		this.decName = decName;
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
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAltUserName() {
		return altUserName;
	}
	public void setAltUserName(String altUserName) {
		this.altUserName = altUserName;
	}
	public String getDecType() {
		return decType;
	}
	public void setDecType(String decType) {
		this.decType = decType;
	}
}
