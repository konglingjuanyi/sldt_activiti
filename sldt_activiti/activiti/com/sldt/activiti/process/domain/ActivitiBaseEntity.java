package com.sldt.activiti.process.domain;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * MappedSuperclass：注解可以被继承
 * 
 */
@MappedSuperclass
public class ActivitiBaseEntity extends BaseEntity {
	
	/**
	 * 申请原因标题
	 */
	private String title;
	/**
	 * 申请原因
	 */
	private String reason;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 申请人
	 */
	private User user;
	
	@Column(name = "title_")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Lob
	@Column(name = "reason_")
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Column(name = "status_")
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_")
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
