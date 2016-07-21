package com.sldt.activiti.process.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "user_")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class User extends BaseEntity {
	
	private String userName;
	private String realName;
	private String password;
	
	/**
	 * 用户创建时间
	 */
	private Date createDate;
	
	
	/**
	 * 关联属性：记录用户对应的角色
	 */
	private Set<Role> roles = new HashSet<Role>();
	
	/**
	 * 关联属性:用户的职位
	 */
	private Set<Job> jobs = new HashSet<Job>();
	
	/**
	 * 关联属性：用户的部门
	 */
	private Set<Dept> depts = new HashSet<Dept>();	
	
	/**
	 * 瞬时属性：流程控制时，=ture表示是部门主管
	 */
	private boolean deptManagerSign = false;	

	/**
	 * unique：唯一索引字段
	 * @return
	 */
	@Column(name = "user_name_", unique = true)
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name = "password_")
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "real_name_")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}		

	/**
	 * mapperdBy:制定关联属性，表示关联哪一方维护关联关系,维护关联关系的一方配置JoinTable
	 * @return
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_job_", joinColumns = {@JoinColumn(name = "user_id_")} , inverseJoinColumns = {@JoinColumn(name =  "job_id_")} )
	public Set<Job> getJobs() {
		return jobs;
	}

	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_dept_", joinColumns = {@JoinColumn(name = "user_id_")} , inverseJoinColumns = {@JoinColumn(name =  "dept_id_")} )
	public Set<Dept> getDepts() {
		return depts;
	}

	public void setDepts(Set<Dept> depts) {
		this.depts = depts;
	}

	/**
	 * Temporal：在JPA注解中，映射世间
	 *   * Date：年月日
	 *   * Time：时分秒
	 *   * Timestamp：年月日时分秒毫秒
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "create_date_")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Transient
	public boolean isDeptManagerSign() {
		return deptManagerSign;
	}

	public void setDeptManagerSign(boolean deptManagerSign) {
		this.deptManagerSign = deptManagerSign;
	}	
}
