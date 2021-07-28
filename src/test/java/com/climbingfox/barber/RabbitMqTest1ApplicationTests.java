package com.climbingfox.barber;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitMqTest1ApplicationTests {

	@Autowired
	@Qualifier("WAITING_QUEUE")
	private Queue queue;

	@Test
	void contextLoads() {
		Assertions.assertEquals("Barber_Waiting_CustomersQueue", queue.getName());
	}

}
