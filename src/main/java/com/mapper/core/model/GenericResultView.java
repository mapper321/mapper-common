package com.mapper.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericResultView<T> extends AbstractData implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<T> rows;
	private Long total;
	private Map<String, Object> kvs;
	
	public GenericResultView() {
		this.rows = new ArrayList<T>();
		this.kvs = new HashMap<String, Object>();
	}
	
	public void appendObject(T obj) {
		assert obj != null : "obj is null";
		if (this.rows == null) {
			this.rows = new ArrayList<T>();
		}
		this.rows.add(obj);
	}
	public void appendRows(List<T> rows) {
		assert rows != null : "obj is null";
		if (this.rows == null) {
			this.rows = new ArrayList<T>();
		}
		this.rows.addAll(rows);
	}
	public void addKV(String key,Object value) {
		if (this.kvs == null) {
			this.kvs = new HashMap<>();
		}
		this.kvs.put(key, value);
	}
	
	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}


	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Map<String, Object> getKvs() {
		return kvs;
	}

	public void setKvs(Map<String, Object> kvs) {
		this.kvs = kvs;
	}

}
