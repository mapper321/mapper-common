package com.mapper.core.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class PageBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//第几页
	@ApiModelProperty(value="起始页",required=false)
	private int page = 0;
	//每页行数
	@ApiModelProperty(value="每页显示数量",required=false)
	private int rows = 10;
	
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
