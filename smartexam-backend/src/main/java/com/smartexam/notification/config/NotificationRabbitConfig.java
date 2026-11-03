package com.smartexam.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitConfig {

    public static final String NOTIFICATION_QUEUE =
            "user-notification-queue";

    public static final String NOTIFICATION_EXCHANGE =
            "notification-exchange";

    public static final String NOTIFICATION_ROUTING_KEY =
            "notification.user";


    @Bean
    public Queue notificationQueue() {

        return QueueBuilder
                .durable(NOTIFICATION_QUEUE)
                .build();
    }


    @Bean
    public DirectExchange notificationExchange() {

        return new DirectExchange(
                NOTIFICATION_EXCHANGE
        );
    }


    @Bean
    public Binding notificationBinding(
            Queue notificationQueue,
            DirectExchange notificationExchange) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(NOTIFICATION_ROUTING_KEY);
    }


    /**
     * Converts Java objects to/from JSON
     * for RabbitMQ messages.
     */
    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    /**
     * Configure RabbitTemplate to send
     * NotificationEvent as JSON.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter rabbitMessageConverter) {

        RabbitTemplate rabbitTemplate =
                new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(
                rabbitMessageConverter
        );

        return rabbitTemplate;
    }


    /**
     * Configure RabbitMQ listener to convert
     * JSON back into Java objects.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter rabbitMessageConverter) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

        factory.setMessageConverter(
                rabbitMessageConverter
        );

        return factory;
    }
}