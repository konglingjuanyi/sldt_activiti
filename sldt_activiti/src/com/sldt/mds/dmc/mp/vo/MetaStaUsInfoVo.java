package com.sldt.mds.dmc.mp.vo;

import java.io.Serializable;

public class MetaStaUsInfoVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 565997943452668795L;
	
	private int totalUsers;
	private int totalDealTimes;
	private String fstUserId;
	private String fstName;
	private String fsImgUrl;
	private int fstDealTimes;
	public int getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}
	public int getTotalDealTimes() {
		return totalDealTimes;
	}
	public void setTotalDealTimes(int totalDealTimes) {
		this.totalDealTimes = totalDealTimes;
	}
	public String getFstUserId() {
		return fstUserId;
	}
	public void setFstUserId(String fstUserId) {
		this.fstUserId = fstUserId;
	}
	public String getFstName() {
		return fstName;
	}
	public void setFstName(String fstName) {
		this.fstName = fstName;
	}
	public String getFsImgUrl() {
		return fsImgUrl;
	}
	public void setFsImgUrl(String fsImgUrl) {
		this.fsImgUrl = fsImgUrl;
	}
	public int getFstDealTimes() {
		return fstDealTimes;
	}
	public void setFstDealTimes(int fstDealTimes) {
		this.fstDealTimes = fstDealTimes;
	}

}
