package com.climbingfox.barber.service;

import java.util.Properties;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;

import com.climbingfox.barber.QueueConfig;
import com.climbingfox.barber.entity.Chair;
import com.climbingfox.barber.entity.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RabbitListener(queues = QueueConfig.BARBER_CUSTOMERS_QUEUE)
public class BarberChair {
	
	@Autowired
    private RabbitAdmin rabbitAdmin;
	
	@Autowired
	private Queue queue;
	
	private Chair chair;
	
	public BarberChair(Chair chair) {
		this.chair = chair;
	}

	@RabbitHandler
	public void cutHair(Customer customer) throws InterruptedException {
		log.info("Started hair cut for customer {} in chair {}", customer.getEmail(), chair);
		Thread.sleep(20000);
		Properties properties = rabbitAdmin.getQueueProperties(queue.getName());
		Integer count = ((Integer)properties.get("QUEUE_MESSAGE_COUNT"));
		log.info("Completed hair cut in chair {}, customers awaiting: {}", chair, count);
	}
}
