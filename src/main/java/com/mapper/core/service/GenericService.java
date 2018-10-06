package com.mapper.core.service;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mapper.core.model.PageBean;
import com.mapper.core.model.ResultView;
import com.mapper.core.mybatis.IEntityDao;
import com.mapper.core.util.BeanUtils;



public abstract class GenericService<T, PK extends Serializable> {

	protected Logger log = LoggerFactory.getLogger(GenericService.class);

	protected abstract IEntityDao<T, PK> getEntityDao();

	public void add(Object entity) {
		getEntityDao().add(entity);
	}

	public void delById(PK id) {
		getEntityDao().delById(id);
	}

	public void delByIds(PK[] ids) {
		if (BeanUtils.isEmpty(ids))
			return;
		for (PK p : ids)
			delById(p);
	}

	public void update(T entity) {
		getEntityDao().update(entity);
	}

	public T getById(PK id) {
		return getEntityDao().getById(id);
	}

	public List<T> getList(String statatementName, PageBean pb) {
		List list = getEntityDao().getList(statatementName, pb);
		return list;
	}

	public List<T> getAll() {
		return getEntityDao().getAll();
	}

	public ResultView getAll(PageBean pb, Object params) {
		return getEntityDao().getAll(pb, params);
	}
	
	public List getList(Class clz,String sqlKey, Object params) {
		return getEntityDao().getList(clz,sqlKey, params);
	}
	public ResultView getList(Class clz,String sqlKey, Object params,PageBean pb) {
		return getEntityDao().getList(clz,sqlKey, params,pb);
	}
}
