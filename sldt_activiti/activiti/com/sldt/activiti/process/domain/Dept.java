package com.sldt.activiti.process.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "dept_")
public class Dept extends BaseEntity {
	
	private String deptName;
	private String address;
	
	/**
	 * 关联属性：部门对应的用户
	 */
	private Set<User> users = new HashSet<User>();
	
	@Column(name = "dept_name_")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "address_")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "depts")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}	

}
