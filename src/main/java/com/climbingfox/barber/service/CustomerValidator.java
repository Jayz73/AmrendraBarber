package com.climbingfox.barber.service;

import java.util.Properties;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerValidator {
	
	@Autowired
    private RabbitAdmin rabbitAdmin;
	
	@Autowired
	private Queue queue;
	
	public void validate() throws Exception {
		this.validateMaxWaitingLimit();
	}
	
	private void validateMaxWaitingLimit() throws Exception {

		Properties properties = rabbitAdmin.getQueueProperties(queue.getName());
		Integer count = ((Integer)properties.get("QUEUE_MESSAGE_COUNT"));
		log.info("Waiting customers count is: {}", count);
		if (count >= 5) {
			log.warn("Maximum 5 customers can be in waiting at at time");
			throw new Exception("Customer waiting limit exceeded");
		}
	}
}
