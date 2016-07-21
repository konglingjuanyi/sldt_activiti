package com.sldt.activiti.process.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;

import com.sldt.activiti.process.common.PageResult;
import com.sldt.activiti.process.dao.IBaseDao;
import com.sldt.activiti.process.service.IBaseService;

@Service
public class BaseServiceImpl implements IBaseService {
	
	@Resource(name = "myBaseDao")
	private IBaseDao baseDao;
	

	@Override
	public PageResult findPage(DetachedCriteria detachedCriteria, int start,
			int pageSize) {
		return baseDao.findPage(detachedCriteria, start, pageSize);
	}


	@Override
	public <T> void add(T obj) {
		baseDao.add(obj);
	}


	@Override
	public <T> void update(T obj) {
		baseDao.update(obj);
	}


	@Override
	public <T> void save(T obj) {
		baseDao.save(obj);
	}


	@Override
	public <T> void delete(T obj) {
		baseDao.delete(obj);
	}


	@Override
	public <T> T get(Class<T> clz, Serializable id) {
		return baseDao.get(clz, id);
	}


	@Override
	public List find(DetachedCriteria criteria) {
		return baseDao.find(criteria);
	}

}
