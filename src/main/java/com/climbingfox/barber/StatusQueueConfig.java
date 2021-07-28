package com.climbingfox.barber;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


@Configuration
public class StatusQueueConfig {
	public static final String BARBER_STATUS_EXCHANGE = "Barber_Status_Exchange";
	public static final String ROUTINGKEY_CUSTOMER_STATUS = "Barber_Customer_Status_Key";

	@Autowired
	private AmqpAdmin amqpAdmin;

	@PostConstruct
	public void setupTopicDestinations() {
		Exchange ex = ExchangeBuilder
				.topicExchange(BARBER_STATUS_EXCHANGE)
				.durable(true)
				.build();
		amqpAdmin.declareExchange(ex);
	}
}
