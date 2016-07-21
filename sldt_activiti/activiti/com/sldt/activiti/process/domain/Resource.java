package com.sldt.activiti.process.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "resource_")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Resource extends BaseEntity {
	
	/**
	 * 资源地址
	 */
	private String url;
	
	/**
	 * 关联属性：记录资源对应的角色
	 *   # 资源已有的角色
	 */
	private Set<Role> roles = new HashSet<Role>();
	
	@Column(name = "url_")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * # ManyToMany：多对多
	 *    # fetch = FetchType.LAZY：使用延时加载，相当于xml中的lazy=true
	 *    # mappedBy = "resources"，设置当前类是否关联关系
	 *    	# resources：对方的关联属性
	 *      # 相当于xml中inverse=true
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "resources")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}
