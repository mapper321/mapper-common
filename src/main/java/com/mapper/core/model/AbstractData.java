package com.mapper.core.model;

public abstract class AbstractData {
private boolean isSucceed;// 是否执行成功
	
	private String message;//消息
	
	public AbstractData()
	{
		isSucceed=true;
		message="";
	}

	public boolean getIsSucceed()
	{
		return isSucceed;
	}

	public void setIsSucceed(boolean isSucceed)
	{
		this.isSucceed = isSucceed;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
