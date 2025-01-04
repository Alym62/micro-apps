package com.github.alym62.service.cashback.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    @Value("${app.queue}")
    private String queue;

    @Value("${app.queue-dead-letter}")
    private String queueDeadLetter;

    @Value("${app.queue-parking-lot}")
    private String queueParkingLot;

    @Value("${app.exchange}")
    private String exchange;

    @Value("${app.exchange-dead-letter}")
    private String exchangeDeadLetter;

    @Bean
    public Queue queue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", exchangeDeadLetter);

        return new Queue(queue, true, false, false, arguments);
    }

    @Bean
    public Queue queueDeadLetter() {
        return new Queue(queueDeadLetter);
    }

    @Bean
    public Queue queueParkingLot() {
        return new Queue(queueParkingLot);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(new FanoutExchange(exchange));
    }

    @Bean
    public Binding bindingDeadLetter() {
        return BindingBuilder.bind(queueDeadLetter()).to(new FanoutExchange(exchangeDeadLetter));
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }
}
