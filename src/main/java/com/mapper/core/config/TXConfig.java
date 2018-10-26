package com.mapper.core.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
@Configuration
public class TXConfig {
	private static final int TX_METHOD_TIMEOUT = 50000;
	private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.mapper..*Controller.*(..))";

	@Autowired
	private PlatformTransactionManager ptx;

	// 事务的实现Advice
	@Bean
	public TransactionInterceptor txAdvice() {
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
		readOnlyTx.setReadOnly(true);
		readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
		RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
		requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		requiredTx.setTimeout(TX_METHOD_TIMEOUT);
		Map<String, TransactionAttribute> txMap = new HashMap<>();
		//增删改
		txMap.put("add*", requiredTx);
		txMap.put("save*", requiredTx);
		txMap.put("insert*", requiredTx);
		txMap.put("update*", requiredTx);
		txMap.put("del*", requiredTx);
		//增删改（流程）
		txMap.put("start*", requiredTx);
		txMap.put("complete*", requiredTx);
		txMap.put("end*", requiredTx);
		//查
		txMap.put("get*", readOnlyTx);
		txMap.put("list*", readOnlyTx);
		txMap.put("query*", readOnlyTx);
		source.setNameMap(txMap);
		TransactionInterceptor txAdvice = new TransactionInterceptor(ptx, source);
		return txAdvice;
	}

	// 切面的定义,pointcut及advice
	@Bean
	public Advisor txAdviceAdvisor(@Qualifier("txAdvice") TransactionInterceptor txAdvice) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
		return new DefaultPointcutAdvisor(pointcut, txAdvice);
	}
}
