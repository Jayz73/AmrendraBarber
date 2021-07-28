package com.climbingfox.barber.service;

import com.climbingfox.barber.WaitingQueueConfig;
import com.climbingfox.barber.dto.APIException;
import com.climbingfox.barber.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RabbitListener(queues = WaitingQueueConfig.BARBER_WAITING_CUSTOMERS_QUEUE)
public class WelcomeService {
	
	@Autowired
	RabbitTemplate mqTemplate;
	
	@Autowired
	CustomerValidator customerValidator;

	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	public String newCustomer(Customer customer) throws APIException {
		customerValidator.validate();
		customer.setToken(UUID.randomUUID().toString());
		mqTemplate.convertAndSend(WaitingQueueConfig.BARBER_WAITING_EXCHANGE, WaitingQueueConfig.ROUTINGKEY_WAITING_NEW_CUSTOMER, customer);
		return "Thanks for waiting";
	}


}
