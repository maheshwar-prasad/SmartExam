package com.smartexam.payment.event;

import com.smartexam.common.enums.NotificationType;
import com.smartexam.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentNotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishSubscriptionActivated(
            Long userId,
            Long organizationId,
            String planName) {

        NotificationEvent event =
                NotificationEvent.builder()
                        .userId(userId)
                        .organizationId(organizationId)
                        .title("Subscription Activated")
                        .message(
                                "Your "
                                        + planName
                                        + " subscription has been activated successfully."
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


    public void publishPaymentFailed(
            Long userId,
            Long organizationId) {

        NotificationEvent event =
                NotificationEvent.builder()
                        .userId(userId)
                        .organizationId(organizationId)
                        .title("Payment Failed")
                        .message(
                                "Your subscription payment could not be completed."
                        )
                        .type(
                                NotificationType.PAYMENT
                        )
                        .build();

        rabbitTemplate.convertAndSend(
                "notification.exchange",
                "notification.created",
                event
        );
    }
}