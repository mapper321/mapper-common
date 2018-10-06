package com.mapper.core.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebLogAspect {
	public static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
	
	ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Pointcut("execution(* com.mapper..*Controller.*(..))")
	public void webLog() {
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		startTime.set(System.currentTimeMillis());
		// 接收到请求，记录请求内容
		try{
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if(attributes!=null) {
				HttpServletRequest request = attributes.getRequest();
				// 记录下请求内容
				logger.info("============================ : start : ============================");
				logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."+ joinPoint.getSignature().getName());
				logger.info("URL : " + request.getRequestURL().toString());
				logger.info("HTTP_METHOD : " + request.getMethod());
				logger.info("IP : " + request.getRemoteAddr());
				logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(Object ret) throws Throwable {
		// 处理完请求，返回内容
		logger.info("RESPONSE : " + ret);
		logger.info("========================== : end : =================================time:"+(System.currentTimeMillis() - startTime.get()));
	}
}

