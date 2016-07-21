package com.sldt.activiti.process.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "role_")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role extends BaseEntity {
	
	private String roleName;
	
	/**
	 * 关联属性：角色对应的用户
	 */
	private Set<User> users = new HashSet<User>();
	
	/**
	 * 关联属性:记录角色对应的资源
	 */
	private Set<Resource> resources = new HashSet<Resource>();
	
	private Set<Menu> menus = new HashSet<Menu>();		
	
	@Column(name = "role_name_")
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * JoinTable标签：配置关系表
	 * 		name: 关系表的名字
	 * 	 	joinColumns:当前表在关系表中的外键
	 * 		inverseJoinColumns:	对方在关系表中的外键
	 * @return
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "role_user_", joinColumns = {@JoinColumn(name = "role_id_")}, inverseJoinColumns = {@JoinColumn(name = "user_id_")} ) 
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	/**
	 * # ManyToMany：多对多，相当于xml中<set name="关联属性名"><ManyToMany></set>
	 *    # fetch = FetchType.LAZY：使用延时加载，相当于xml中的lazy=true
	 *    # mappedBy = "resources"，设置当前类是否关联关系
	 *    	# resources：对方的关联属性
	 *      # 相当于xml中inverse=true
	 * # JoninTable：多对多是指定中间表，相当于xml中<set name="关联属性" table="中间表名">
	 *    # name = "role_resource_"：中间表名字
	 *    # joinColumns = {@JoinColumn(name = "role_id_")}：当前表在中间表的外键名
	 *    # inverseJoinColumns = {@JoinColumn(name = "resource_id_")}:对方表在中间表的外键名
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "role_resource_", joinColumns = {@JoinColumn(name = "role_id_")}, inverseJoinColumns = {@JoinColumn(name = "resource_id_")} ) 
	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "role_menu_", joinColumns = {@JoinColumn(name = "role_id_")} , inverseJoinColumns = {@JoinColumn(name =  "menu_id_")} )
	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}		
	
}
