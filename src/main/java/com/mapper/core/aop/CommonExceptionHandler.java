package com.mapper.core.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mapper.core.model.ResultView;


@RestControllerAdvice
public class CommonExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler(DuplicateKeyException.class)
	public ResultView handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return ResultView.error("数据库中已存在该记录");
	}


	@ExceptionHandler(Exception.class)
	public ResultView handleException(Exception e){
		logger.error(e.getMessage(), e);
		return ResultView.error();
	}
}
