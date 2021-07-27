package com.climbingfox.barber.service;

import com.climbingfox.barber.StatusQueueConfig;
import com.climbingfox.barber.MessageListenerContainerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@Slf4j
public class StatusService {
    @Autowired
    RabbitTemplate mqTemplate;

    @Autowired
    private MessageListenerContainerFactory messageListenerContainerFactory;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private AmqpAdmin amqpAdmin;

    public Flux<?> consumeViaCaller() {
        Queue topicQueue = createTopicQueue();
        String qname = topicQueue.getName();
        MessageListenerContainer mlc = messageListenerContainerFactory
                .createMessageListenerContainer(qname);
        Flux<String> f = Flux.<String> create(emitter -> {
            mlc.setupMessageListener((MessageListener) m -> {
                String payload = new String(m.getBody());
                log.info("Payload: {}", payload);
                emitter.next(payload);
            });
            emitter.onRequest(v -> {
                mlc.start();
            });
            emitter.onDispose(() -> {
                mlc.stop();
            });
        });

        return Flux.interval(Duration.ofSeconds(5))
                .map(v -> "No news is good news")
                .mergeWith(f);
    }

    private Queue createTopicQueue() {

        Exchange ex = ExchangeBuilder
                .topicExchange(StatusQueueConfig.BARBER_STATUS_EXCHANGE)
                .durable(true)
                .build();
        amqpAdmin.declareExchange(ex);
        Queue q = QueueBuilder
                .nonDurable()
                .build();
        amqpAdmin.declareQueue(q);
        Binding b = BindingBuilder.bind(q)
                .to(ex)
                .with(StatusQueueConfig.ROUTINGKEY_CUSTOMER_STATUS)
                .noargs();
        amqpAdmin.declareBinding(b);
        return q;
    }
}
