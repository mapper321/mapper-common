package com.mapper.core.model;

import java.io.Serializable;

public class PageBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//第几页
	private int page = 0;
	//每页行数
	private int rows = 0;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "PageBean [page=" + page + ", rows=" + rows + "]";
	}
	
	
}
