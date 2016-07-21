package com.sldt.activiti.process.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "job_")
public class Job extends BaseEntity {
	
	private String jobName;
	
	/**
	 * 关联属性：职位对应的用户
	 */
	private Set<User> users = new HashSet<User>();	
	
	@Column(name = "job_name_")
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "jobs")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}	

}
