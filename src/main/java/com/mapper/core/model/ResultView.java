package com.mapper.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultView extends AbstractData implements Serializable {
	private List rows;
	private Long total;
	private Map<String, Object> kvs;
	
	public ResultView() {
		this.rows = new ArrayList();
		this.kvs = new HashMap();
	}
	
	public void appendObject(Object obj) {
		assert obj != null : "obj is null";
		if (this.rows == null) {
			this.rows = new ArrayList();
		}
		this.rows.add(obj);
	}
	public void appendRows(List rows) {
		assert rows != null : "obj is null";
		if (this.rows == null) {
			this.rows = new ArrayList();
		}
		this.rows.add(rows);
	}
	public void addKV(String key,Object value) {
		if (this.kvs == null) {
			this.kvs = new HashMap<>();
		}
		this.kvs.put(key, value);
	}
	
	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
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

	@Override
	public String toString() {
		return "ResultView [rows=" + rows + ", total=" + total + ", kvs=" + kvs + "]";
	}

}
