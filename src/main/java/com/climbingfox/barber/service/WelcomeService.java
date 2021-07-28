package com.climbingfox.barber.service;

import com.climbingfox.barber.WaitingQueueConfig;
import com.climbingfox.barber.dto.APIException;
import com.climbingfox.barber.entity.Constants;
import com.climbingfox.barber.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@Autowired
	private ConcurrentSkipListSet<Customer> customerRegistry;
	
	public Customer newCustomer(Customer customer) throws APIException {
		customer.setToken(UUID.randomUUID().toString());
		customer.setInTime(new Date());
		customerValidator.validate(customer);
		customerRegistry.add(customer);
		mqTemplate.convertAndSend(WaitingQueueConfig.BARBER_WAITING_EXCHANGE, WaitingQueueConfig.ROUTINGKEY_WAITING_NEW_CUSTOMER, customer);
		log.info(customerRegistry.toString());
		int index = customerRegistry.stream()
				.map(cust -> cust.getEmail())
				.collect(Collectors.toList())
				.indexOf(customer.getEmail());
		customer.setSeqNo(index);

		if(customer.getSeqNo() >= 2) {
			Stream<Customer> stream = customerRegistry.stream();
			/*if(customer.getSeqNo() % 2 != 0) {
				stream.skip(1);
			}*/
			long totalWaiting = Duration.between(
					stream.skip(customer.getSeqNo() % 2).iterator().next().getInTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
					LocalDateTime.now()
			).toSeconds();

			if(customer.getSeqNo() >= 4) {
				totalWaiting = totalWaiting + (((customer.getSeqNo() / 2) - 1) * Constants.PROCESSING_DURATION_SECONDS);
			}

			customer.setEstimatedWaitingTime(totalWaiting);
		}

		return customer;
	}


}
