package com.mapper.core.mybatis;

import java.io.Serializable;
import java.util.List;

import com.mapper.core.model.PageBean;
import com.mapper.core.model.ResultView;


public interface IEntityDao <T,PK extends Serializable>{
	/**
	 * 添加对象
	 * @param entity
	 * @return
	 */
	public int add(Object entity);

	/**
	 * 根据主键删除
	 * @param id
	 */
	public int delById(PK id);

	/**
	 * 更新对象
	 * @param entity
	 */
	public int update(T entity);

	/**
	 * 根据主键ID取得对象
	 * @param id
	 * @return
	 */
	public T getById(PK id);

	/**
	 * 根据条件获取列表数据，不分页
	 * @param statementName
	 * @param params
	 * @return
	 */
	public List<T> getList(String sqlKey,Object params);
	
	
	/**
	 * 根据条件获取列表数据，分页
	 * @return
	 */
	public ResultView getList(String sqlKey,Object params,PageBean pb);
	
	/**
	 * 获取该表的所有记录,不分页
	 * @return
	 */
	public List<T> getAll();
	
	
	/**
	 * 分页获取指定条件数据
	 * @param pb
	 * @param params
	 * @return
	 */
	public ResultView getAll(PageBean pb,Object params);
	
	/**
	 * 根据条件获取一个
	 * @param sqlKey
	 * @param params
	 * @return
	 */
	public T getOne(String sqlKey,Object params);
	
	/**
	 * 根据类获取相应一条数据
	 * @param clz
	 * @param sqlKey
	 * @param params
	 * @return
	 */
	public Object getOne(Class clz,String sqlKey,Object params);
	/**
	 * 根据类获取
	 * @param clz
	 * @param sqlKey
	 * @param params
	 * @return
	 */
	public List getList(Class clz,String sqlKey,Object params);
	/**
	 * 根据类获取列表，带分页
	 * @param clz
	 * @param sqlKey
	 * @param params
	 * @param pb
	 * @return
	 */
	public ResultView getList(Class clz,String sqlKey,Object params,PageBean pb);
}
