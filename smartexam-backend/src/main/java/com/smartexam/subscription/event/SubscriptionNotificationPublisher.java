package com.smartexam.subscription.event;

import com.smartexam.common.enums.NotificationType;
import com.smartexam.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionNotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishExpired(Long userId, Long organizationId) {

        NotificationEvent event = NotificationEvent.builder().userId(userId).organizationId(organizationId).title("Subscription Expired").message("Your SmartExam subscription has expired.").type(NotificationType.SUBSCRIPTION).build();

        rabbitTemplate.convertAndSend("notification.exchange", "notification.created", event);
    }
    public void publishCancelled(
            Long userId,
            Long organizationId) {

        NotificationEvent event =
                NotificationEvent.builder()
                        .userId(userId)
                        .organizationId(
                                organizationId
                        )
                        .title(
                                "Subscription Cancelled"
                        )
                        .message(
                                "Your SmartExam subscription has been cancelled."
                        )
                        .type(
                                NotificationType.SUBSCRIPTION
                        )
                        .build();

        rabbitTemplate.convertAndSend(
                "notification.exchange",
                "notification.created",
                event
        );
    }
}