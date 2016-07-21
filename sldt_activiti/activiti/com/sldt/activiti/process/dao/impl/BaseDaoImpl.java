package com.sldt.activiti.process.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import com.sldt.activiti.process.common.PageResult;
import com.sldt.activiti.process.dao.IBaseDao;



@Repository(value = "myBaseDao")
public class BaseDaoImpl implements IBaseDao {
	
	@Resource
	private SessionFactory sessionFactory;

	public PageResult findPage(DetachedCriteria detachedCriteria , int start, int pageSize) {
		PageResult page = new PageResult();
		//通过DetachCriteria得到可执行的criteria
		//currentSession必须要有事务
		Criteria criteria = detachedCriteria.getExecutableCriteria(sessionFactory.getCurrentSession());
		
		//查询总行数
		//设置投影，查询语句select count(*) from Dept where ... 如果有条件		
		criteria.setProjection(Projections.rowCount());
		Long rowCount = (Long) criteria.uniqueResult();
		page.setRowCount(rowCount);
		
		//查询一页数据
		//清楚投影, 相当于from Dept where ... 如果有条件
		criteria.setProjection(null);
		
		//设置返回结果类型，默认是ROOT_ENTITY=创建criteria 的类
		//当存在关联查询时值，回事数组，代码设置返回值为ROOT_ENTITY
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		
		criteria.setFirstResult(start);
		criteria.setMaxResults(pageSize);
		
		//按照id倒序查询
		criteria.addOrder(Order.desc("id"));
		//查询一页数据
		List list = criteria.list();
		page.setResult(list);
		
		return page;
	}



	@Override
	public <T> void add(T obj) {
		sessionFactory.getCurrentSession().save(obj);
	}



	@Override
	public <T> void update(T obj) {
		sessionFactory.getCurrentSession().update(obj);
	}



	@Override
	public <T> void save(T obj) {
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
	}



	@Override
	public <T> void delete(T obj) {
		sessionFactory.getCurrentSession().delete(obj);
	}



	@Override
	public <T> T get(Class<T> clz, Serializable id) {
		return (T) sessionFactory.getCurrentSession().get(clz, id);
	}

	@Override
	public List find(DetachedCriteria criteria) {
		Criteria c = criteria.getExecutableCriteria(sessionFactory.getCurrentSession());
		return c.list();
	}

}
