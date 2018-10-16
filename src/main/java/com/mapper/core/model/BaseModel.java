package com.mapper.core.model;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;


public abstract class BaseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	//创建时间
	@ApiModelProperty(hidden=true)
	protected Date createtime;
	//创建人
	@ApiModelProperty(hidden=true)
	protected Long createby;
	//更新时间
	@ApiModelProperty(hidden=true)
	protected Date updatetime;
	//更新人
	@ApiModelProperty(hidden=true)
	protected Long updateby;
	
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Long getCreateby() {
		return createby;
	}
	public void setCreateby(Long createby) {
		this.createby = createby;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public Long getUpdateby() {
		return updateby;
	}
	public void setUpdateby(Long updateby) {
		this.updateby = updateby;
	}
	
	
}
