package com.mapper.core.model;

import java.util.List;

import com.mapper.core.model.constant.StatusCode;

public class ResultView extends GenericResultView<Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
