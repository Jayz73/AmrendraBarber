package com.climbingfox.barber.service;

import com.climbingfox.barber.StatusQueueConfig;
import com.climbingfox.barber.WaitingQueueConfig;
import com.climbingfox.barber.dto.StatusResponse;
import com.climbingfox.barber.entity.Chair;
import com.climbingfox.barber.entity.Customer;
import com.climbingfox.barber.entity.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Properties;
import java.util.Set;

@Slf4j
@RabbitListener(queues = WaitingQueueConfig.BARBER_WAITING_CUSTOMERS_QUEUE)
public class BarberChair {
	
	@Autowired
    private RabbitAdmin rabbitAdmin;

	@Autowired
	RabbitTemplate mqTemplate;
	
	@Autowired
	@Qualifier("WAITING_QUEUE")
	private Queue queue;

	@Autowired
	private Set<Customer> customerRegistry;
	
	private Chair chair;
	
	public BarberChair(Chair chair) {
		this.chair = chair;
	}

	@RabbitHandler
	public void cutHair(Customer customer) throws InterruptedException {
		log.info("Started hair cut for customer {} in chair {}", customer.getEmail(), chair);
		StatusResponse resp = new StatusResponse();
		resp.setChair(this.chair);
		resp.setCustomerUUID(customer.getToken());
		postStatusUpdate(resp, EventType.STARTED);

		Thread.sleep(20000);

		Properties properties = rabbitAdmin.getQueueProperties(queue.getName());
		Integer count = ((Integer)properties.get("QUEUE_MESSAGE_COUNT"));

		customerRegistry.remove(customer);
		postStatusUpdate(resp, EventType.COMPLETED);
		log.info("Completed hair cut in chair {}, customers awaiting: {}", chair, count);
	}

	private void postStatusUpdate(StatusResponse resp, EventType eventType) {
		resp.setEventType(eventType);
		mqTemplate.convertAndSend(StatusQueueConfig.BARBER_STATUS_EXCHANGE, StatusQueueConfig.ROUTINGKEY_CUSTOMER_STATUS, resp);
	}

}
