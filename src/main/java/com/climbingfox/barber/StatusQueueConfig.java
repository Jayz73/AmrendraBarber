package com.climbingfox.barber;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


@Configuration
public class StatusQueueConfig {
	public static final String BARBER_STATUS_EXCHANGE = "Barber_Status_Exchange";
	public static final String ROUTINGKEY_CUSTOMER_STATUS = "Barber_Customer_Status_Key";
	/*public static final String BARBER_STATUS_QUEUE = "Barber_Status_Queue";

	@Bean(name = "STATUS_QUEUE")
	public Queue queue() {
		return new Queue(BARBER_STATUS_QUEUE);
	}

	@Bean(name = "STATUS_EXCHANGE")
	public TopicExchange exchange() {
		return new TopicExchange(BARBER_STATUS_EXCHANGE);
	}

	@Bean(name = "STATUS_BINDING")
	public Binding binding(@Qualifier("STATUS_QUEUE") Queue queue, @Qualifier("STATUS_EXCHANGE") TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_CUSTOMER_STATUS);
	}*/

	@Autowired
	private AmqpAdmin amqpAdmin;

	@PostConstruct
	public void setupTopicDestinations() {
		Exchange ex = ExchangeBuilder
				.topicExchange(BARBER_STATUS_EXCHANGE)
				.durable(true)
				.build();
		amqpAdmin.declareExchange(ex);
	};
	
}
