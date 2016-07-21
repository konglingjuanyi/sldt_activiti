package com.sldt.activiti.process.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.sldt.activiti.process.common.PageResult;

public interface IBaseService {
	
	/**
	 * 增加
	 * @param obj
	 * @return
	 */
	public <T> void add(T obj);
	
	/**
	 * 修改
	 * @param obj
	 * @return
	 */
	public <T> void update(T obj);	
	
	/**
	 * 增加，修改
	 * @param obj
	 * @return
	 */
	public <T> void save(T obj);
	
	/**
	 * 修改
	 * @param obj
	 * @return
	 */
	public <T> void delete(T obj);	
	
	/**
	 * 加载
	 * @param obj
	 * @return
	 */
	public <T> T get(Class<T> clz, Serializable id);
	
	/**
	 * 查询所有
	 * @param criteria：离线criteria
	 * @return
	 */
	public List find(DetachedCriteria criteria);
	
	
	/**
	 * 分页查询
	 * @param criteria：离线criteria, 在具体的Action中构件好，处理好条件，传给service，传给dao
	 * @param start：起始下标
	 * @param pageSize：每页行数
	 * @return pageResult，result,一页数据,rowCount：总页数
	 */
	public PageResult findPage(DetachedCriteria detachedCriteria, int start, int pageSize);
}
