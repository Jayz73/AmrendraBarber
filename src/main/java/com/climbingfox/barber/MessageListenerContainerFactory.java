package com.climbingfox.barber;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListenerContainerFactory {
    @Autowired
    private ConnectionFactory connectionFactory;

    public MessageListenerContainer createMessageListenerContainer(String queueName) {
        SimpleMessageListenerContainer mlc = new SimpleMessageListenerContainer(connectionFactory);
        mlc.addQueueNames(queueName);
        return mlc;
    }
}
