package com.smartexam.notification.event;

import com.smartexam.notification.config.NotificationRabbitConfig;
import com.smartexam.notification.dto.CreateNotificationRequest;
import com.smartexam.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(
            queues = NotificationRabbitConfig.NOTIFICATION_QUEUE
    )
    public void consume(NotificationEvent event) {

        log.info(
                "========== RABBITMQ MESSAGE RECEIVED =========="
        );

        log.info(
                "userId={}",
                event.getUserId()
        );

        log.info(
                "organizationId={}",
                event.getOrganizationId()
        );

        log.info(
                "title={}",
                event.getTitle()
        );

        log.info(
                "message={}",
                event.getMessage()
        );

        log.info(
                "type={}",
                event.getType()
        );

        CreateNotificationRequest request =
                new CreateNotificationRequest();

        request.setUserId(event.getUserId());
        request.setOrganizationId(event.getOrganizationId());
        request.setTitle(event.getTitle());
        request.setMessage(event.getMessage());
        request.setType(event.getType());

        notificationService.create(request);

        log.info(
                "========== NOTIFICATION CREATED =========="
        );
    }
}