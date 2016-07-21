package com.sldt.mds.dmc.mp.vo;

import java.io.Serializable;

public class MetaUserVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5012703624029778991L;
	
	private String userId;
	private String userName;
	private String imgUrl;
	private String userDesc;
	private int dealTimes;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getUserDesc() {
		return userDesc;
	}
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
	public int getDealTimes() {
		return dealTimes;
	}
	public void setDealTimes(int dealTimes) {
		this.dealTimes = dealTimes;
	}
	
	
}
