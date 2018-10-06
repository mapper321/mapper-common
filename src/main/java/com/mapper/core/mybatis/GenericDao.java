package com.mapper.core.mybatis;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mapper.core.model.PageBean;
import com.mapper.core.model.ResultView;

abstract class GenericDao<T, PK extends Serializable> implements IEntityDao<T, PK> {
	
	protected final Log log = LogFactory.getLog(getClass());
	@Resource
	protected JdbcTemplate jdbcTemplate;
	@Resource 
	protected SqlSessionTemplate sqlSessionTemplate;
	/**
	 * 获取当前dao 需要访问的model类名，在实现类中实现这个方法。<br>
	 * 主要的功能：用于获取类的完整路径，以提供给mybatis使用。
	 * @return
	 */
	protected abstract Class<T> getEntityClass();
	
	/**
	 * 根据类名判断mybatis的命名空间，方便后续查找sql
	 * @return
	 */
	private String getMybatisMapperNamespace() {
		return getEntityClass().getName();
	}
	
	private String getMybatisMapperName(String sqlKey) {
		sqlKey = getMybatisMapperNamespace() + "." + sqlKey;
		return sqlKey;
	}
	
	@Override
	public int add(Object entity) {
		String statement = getMybatisMapperNamespace()+"."+"add";
		return sqlSessionTemplate.insert(statement, entity);
	}

	@Override
	public int delById(PK id) {
		String statement = getMybatisMapperNamespace()+"."+"delById";
		return sqlSessionTemplate.delete(statement, id);
	}

	@Override
	public int update(T entity) {
		String statement = getMybatisMapperNamespace()+"."+"update";
		return sqlSessionTemplate.update(statement, entity);
	}

	@Override
	public T getById(PK id) {
		String sqlkey = "getById";
		return getOne(sqlkey, id);
	}

	@Override
	public List<T> getList(String sqlKey, Object params) {
		String statement = getMybatisMapperName(sqlKey);
		return sqlSessionTemplate.selectList(statement, params);
	}

	@Override
	public ResultView getList(String sqlKey, Object params, PageBean pb) {
		String statement = getMybatisMapperName(sqlKey);
		//如果rows为0，则不分页返回全部信息
		ResultView rt = new ResultView();
		if(pb.getRows()==0) {
			List<T> selectList = getList(sqlKey,  params);
			rt.setRows(selectList);
			rt.setTotal(Long.valueOf(selectList.size()));
		}else {
			PageHelper.startPage(pb.getPage(), pb.getRows());
			Page<T> selectList = (Page<T>) sqlSessionTemplate.selectList(statement, params);
			rt.setRows(selectList.getResult());
			rt.setTotal(selectList.getTotal());
		}
		return rt;
	}

	@Override
	public List<T> getAll() {
		String statement = getMybatisMapperNamespace()+"."+"getAll";
		return sqlSessionTemplate.selectList(statement);
	}

	@Override
	public ResultView getAll(PageBean pb, Object params) {
		String sqlKey = "getAll";
		return getList(sqlKey, params, pb);
	}

	@Override
	public T getOne(String sqlKey, Object params) {
		String statement = getMybatisMapperName(sqlKey);
		return sqlSessionTemplate.selectOne(statement, params);
	}

	@Override
	public Object getOne(Class clz, String sqlKey, Object params) {
		String statement = getMybatisMapperNamespace()+"."+sqlKey;
		return sqlSessionTemplate.selectOne(statement, params);
	}

	@Override
	public List getList(Class clz, String sqlKey, Object params) {
		String statement = getMybatisMapperNamespace()+"."+sqlKey;
		return sqlSessionTemplate.selectList(statement, params);
	}

	@Override
	public ResultView getList(Class clz, String sqlKey, Object params, PageBean pb) {
		ResultView rt = new ResultView();
		if(pb.getRows()==0) {
			List list = getList(clz, sqlKey, params);
			rt.setRows(list);
			rt.setTotal(Long.valueOf(list.size()));
		}else {
			String statement = getMybatisMapperNamespace()+"."+sqlKey;
			PageHelper.startPage(pb.getPage(), pb.getRows());
			Page selectList = (Page) sqlSessionTemplate.selectList(statement, params);
			rt.setRows(selectList.getResult());
			rt.setTotal(selectList.getTotal());
		}
		return rt;
	}

	
}
