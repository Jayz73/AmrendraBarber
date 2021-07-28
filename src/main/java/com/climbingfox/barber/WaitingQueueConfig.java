package com.climbingfox.barber;

import com.climbingfox.barber.entity.Chair;
import com.climbingfox.barber.entity.Customer;
import com.climbingfox.barber.service.BarberChair;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;


@Configuration
public class WaitingQueueConfig {
	public static final String ROUTINGKEY_WAITING_NEW_CUSTOMER = "Barber_Waiting_CustomerKey";
	public static final String BARBER_WAITING_EXCHANGE = "Barber_Waiting_Exchange";
	public static final String BARBER_WAITING_CUSTOMERS_QUEUE = "Barber_Waiting_CustomersQueue";

	@Bean(name = "WAITING_QUEUE")
	public Queue queue() {
		return new Queue(BARBER_WAITING_CUSTOMERS_QUEUE);
	}
	
	public static class BarberChairConfig {
		
        @Bean
        public BarberChair barberChair1() {
        	return new BarberChair(Chair.CHAIR1);
        }
        
        @Bean
        public BarberChair barberChair2() {
        	return new BarberChair(Chair.CHAIR2);
        }

    }

	@Bean(name = "WAITING_EXCHANGE")
	public TopicExchange exchange() {
		return new TopicExchange(BARBER_WAITING_EXCHANGE);
	}

	@Bean(name = "WAITING_BINDING")
	public Binding binding(@Qualifier("WAITING_QUEUE") Queue queue, @Qualifier("WAITING_EXCHANGE") TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_WAITING_NEW_CUSTOMER);
	}

	@Bean(name = "WAITING_MSG_CONVERTER")
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

	@Bean
	public AmqpTemplate template(ConnectionFactory conn) {
		RabbitTemplate template = new RabbitTemplate(conn);
		template.setMessageConverter(this.messageConverter());
		return template;
	}

	@Bean
	public ConcurrentSkipListSet<Customer> customerRegistry() {
		return new ConcurrentSkipListSet<>(Comparator.comparing(Customer::getInTime));
	}
}
