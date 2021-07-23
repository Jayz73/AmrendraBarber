package com.climbingfox.barber.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.climbingfox.barber.QueueConfig;
import com.climbingfox.barber.entity.Customer;

@Service
public class WelcomeService {
	
	@Autowired
	RabbitTemplate mqTemplate;
	
	@Autowired
	CustomerValidator customerValidator;
	
	public String newCustomer(Customer customer) throws Exception {
		customerValidator.validate();
		customer.setToken(UUID.randomUUID().toString());
		mqTemplate.convertAndSend(QueueConfig.BARBER_EXCHANGE, QueueConfig.ROUTINGKEY_NEW_CUSTOMER, customer);
		return "Thanks for waiting";
	}
	

}
