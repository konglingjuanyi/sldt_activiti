package com.sldt.mds.dmc.mp.vo;

public class CheckInfo {
	private String actId;
	private String auditor;//审核人
	private String department;//所在部门
	private String auditTime;//审核时间
	private String auditResult;//审核结果
	private String auditOpinion;//审核意见
	private String node;
	private Integer records;
	private String userName;
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getActId() {
		return actId;
	}
	public void setActId(String actId) {
		this.actId = actId;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	public String getAuditOpinion() {
		return auditOpinion;
	}
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}
	public Integer getRecords() {
		return records;
	}
	public void setRecords(Integer records) {
		this.records = records;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actId == null) ? 0 : actId.hashCode());
		result = prime * result
				+ ((auditOpinion == null) ? 0 : auditOpinion.hashCode());
		result = prime * result
				+ ((auditResult == null) ? 0 : auditResult.hashCode());
		result = prime * result
				+ ((auditTime == null) ? 0 : auditTime.hashCode());
		result = prime * result + ((auditor == null) ? 0 : auditor.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
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
		CheckInfo other = (CheckInfo) obj;
		if (actId == null) {
			if (other.actId != null)
				return false;
		} else if (!actId.equals(other.actId))
			return false;
		if (auditOpinion == null) {
			if (other.auditOpinion != null)
				return false;
		} else if (!auditOpinion.equals(other.auditOpinion))
			return false;
		if (auditResult == null) {
			if (other.auditResult != null)
				return false;
		} else if (!auditResult.equals(other.auditResult))
			return false;
		if (auditTime == null) {
			if (other.auditTime != null)
				return false;
		} else if (!auditTime.equals(other.auditTime))
			return false;
		if (auditor == null) {
			if (other.auditor != null)
				return false;
		} else if (!auditor.equals(other.auditor))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}
}
