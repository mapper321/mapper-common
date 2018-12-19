package com.mapper.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mapper.core.model.constant.StatusCode;

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


	public static ResultView ok(){
		return ok("");
	}
	
	public static ResultView ok(String message){
		return ok(StatusCode.OK,message);
	}
	
	public static ResultView ok(int code,String message){
		ResultView rv = new ResultView();
		rv.setMessage(message);
		rv.setCode(code);
		return rv;
	}
	
	public static ResultView ok(Object obj){
		ResultView rv = new ResultView();
		rv.appendObject(obj);
		rv.setTotal((long) rv.getRows().size());
		return rv;
	}
	
	public static ResultView ok(List list){
		ResultView rv = new ResultView();
		rv.appendRows(list);
		rv.setTotal((long) list.size());
		return rv;
	}
	
	
	public static ResultView error() {
		return error("服务内部错误！请联系管理员处理");
	}

	public static ResultView error(String message) {
		return error(StatusCode.ERROR,message);
	}

	public static ResultView error(int code, String message) {
		ResultView rv = new ResultView();
		rv.setSucceed(false);
		rv.setCode(code);
		rv.setMessage(message);
		return rv;
	}
}
