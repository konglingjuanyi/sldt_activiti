package com.sldt.mds.dmc.mp.vo;

import java.util.Date;

public class UserVO{
	private String usertype;
	private String usercode;
	
	private String username;
	
	private String validflag;
	
	private String password;
	
	private String sex;
	
	private String phone;
	
	private String email;
	
	private Long resetpwtype;
	
	private Date resetpwtime;
	
	private String usedpword;
	
	private Long userlock;
	
	private String jobnumber;
	
	private Long documenttype;
	
	private String documentnumber;
	
	private String dept;
	
	private boolean isAdmin = false;
	
	private String tUnitid;
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getValidflag() {
		return validflag;
	}

	public void setValidflag(String validflag) {
		this.validflag = validflag;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getResetpwtype() {
		return resetpwtype;
	}

	public void setResetpwtype(Long resetpwtype) {
		this.resetpwtype = resetpwtype;
	}

	public Date getResetpwtime() {
		return resetpwtime;
	}

	public void setResetpwtime(Date resetpwtime) {
		this.resetpwtime = resetpwtime;
	}

	public String getUsedpword() {
		return usedpword;
	}

	public void setUsedpword(String usedpword) {
		this.usedpword = usedpword;
	}

	public Long getUserlock() {
		return userlock;
	}

	public void setUserlock(Long userlock) {
		this.userlock = userlock;
	}

	public String getJobnumber() {
		return jobnumber;
	}

	public void setJobnumber(String jobnumber) {
		this.jobnumber = jobnumber;
	}

	public Long getDocumenttype() {
		return documenttype;
	}

	public void setDocumenttype(Long documenttype) {
		this.documenttype = documenttype;
	}

	public String getDocumentnumber() {
		return documentnumber;
	}

	public void setDocumentnumber(String documentnumber) {
		this.documentnumber = documentnumber;
	}

	public String gettUnitid() {
		return tUnitid;
	}

	public void settUnitid(String tUnitid) {
		this.tUnitid = tUnitid;
	}
	
}
