package com.climbingfox.barber;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.climbingfox.barber.entity.Chair;
import com.climbingfox.barber.service.BarberChair;


@Configuration
public class QueueConfig {
	public static final String ROUTINGKEY_NEW_CUSTOMER = "Barber_CustomerKey";
	public static final String BARBER_EXCHANGE = "Barber_Exchange";
	public static final String BARBER_CUSTOMERS_QUEUE = "Barber_CustomersQueue";

	@Bean
	public Queue queue() {
		return new Queue(BARBER_CUSTOMERS_QUEUE);
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
	
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(BARBER_EXCHANGE);
	}
	
	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_NEW_CUSTOMER);
	}
	
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
	
	public AmqpTemplate template(ConnectionFactory conn) {
		RabbitTemplate template = new RabbitTemplate(conn);
		template.setMessageConverter(messageConverter());
		return template;
	}
	
	
}
