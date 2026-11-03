package com.smartexam.notification.event;

import com.smartexam.notification.config.NotificationRabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(NotificationEvent event) {

        log.info(
                "Publishing notification event for userId={}",
                event.getUserId()
        );

        rabbitTemplate.convertAndSend(
                NotificationRabbitConfig.NOTIFICATION_EXCHANGE,
                NotificationRabbitConfig.NOTIFICATION_ROUTING_KEY,
                event
        );
    }
}