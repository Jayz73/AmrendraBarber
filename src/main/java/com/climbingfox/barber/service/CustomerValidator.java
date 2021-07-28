package com.climbingfox.barber.service;

import com.climbingfox.barber.dto.APIException;
import com.climbingfox.barber.dto.ErrorResponse;
import com.climbingfox.barber.entity.ErrorLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
public class CustomerValidator {
	
	@Autowired
    private RabbitAdmin rabbitAdmin;
	
	@Autowired
	@Qualifier("WAITING_QUEUE")
	private Queue queue;
	
	public void validate() throws APIException {
		this.validateMaxWaitingLimit();
	}
	
	private void validateMaxWaitingLimit() throws APIException {

		Properties properties = rabbitAdmin.getQueueProperties(queue.getName());
		Integer count = ((Integer)properties.get("QUEUE_MESSAGE_COUNT"));
		log.info("Waiting customers count is: {}", count);
		if (count >= 5) {
			log.warn("Maximum 5 customers can be in waiting at at time");
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setErrorLevel(ErrorLevel.HIGH);
			ErrorResponse.ErrorDetail errorDetail = new ErrorResponse.ErrorDetail("001", "Maximum waiting limit reached");
			errorResponse.getErrorDetails().add(errorDetail);
			throw new APIException(errorResponse);
		}
	}
}
