package com.mapper.core.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;


@Configuration
public class JMSConfig {

	@Bean
	public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(true);
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}
	
	/**
	 * 给quene类型添加事务，发生异常时，会回滚。然后会从新接口消息，默认重新发送6次。
	 * @param connectionFactory
	 * @return
	 */
	@Bean
	public JmsListenerContainerFactory<?> queueListenerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(false);
		factory.setSessionTransacted(true);
		factory.setConnectionFactory(connectionFactory);		
		return factory;
	}
	
	/**
	 * 发送消息时添加事务，此处会和数据库事务控制保持一致
	 * jmsTemplate.setSessionTransacted(true);
	 * @param connectionFactory
	 * @return
	 */
	@Bean
	public JmsTemplate initJmsTemplate(ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate=new JmsTemplate();
		jmsTemplate.setSessionTransacted(true);
		jmsTemplate.setConnectionFactory(connectionFactory);
		return jmsTemplate;
	}
	
	
	
	
	/**
	 *  监听示例
	 *  @JmsListener(destination = "oa_todo" ,containerFactory="queueListenerFactory")
	 * 	public void comsumeBpmtodo(BPMTodoPO todo) {
	 *  或者
	 *  public void comsumeBpmtodo(@Payload BPMTodoPO todo) {
	 */
	
	/**
	 * 发送消息示列
	 *  ActiveMQQueue quene = new ActiveMQQueue("mapperQuene");
	 *	ActiveMQTopic topic = new ActiveMQTopic("mapperTopuc");
	 *  boolean sessionTransacted = jmsTemplate.isSessionTransacted();
	 *	jmsTemplate.convertAndSend(quene, "ttttt");
	 */
}
