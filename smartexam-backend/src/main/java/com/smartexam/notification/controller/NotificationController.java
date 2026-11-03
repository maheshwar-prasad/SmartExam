package com.smartexam.notification.controller;

import com.smartexam.common.enums.NotificationType;
import com.smartexam.notification.dto.CreateNotificationRequest;
import com.smartexam.notification.dto.NotificationResponse;
import com.smartexam.notification.event.NotificationEvent;
import com.smartexam.notification.event.NotificationEventPublisher;
import com.smartexam.notification.service.NotificationService;
import com.smartexam.notification.sse.NotificationSseService;
import com.smartexam.user.entity.User;
import com.smartexam.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationSseService notificationSseService;
    private final UserRepository userRepository;
    private final NotificationEventPublisher notificationEventPublisher;


    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody CreateNotificationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.create(request));
    }


    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(Pageable pageable) {

        return ResponseEntity.ok(notificationService.getMyNotifications(pageable));
    }


    @GetMapping("/unread")
    public ResponseEntity<Page<NotificationResponse>> getMyUnreadNotifications(Pageable pageable) {

        return ResponseEntity.ok(notificationService.getMyUnreadNotifications(pageable));
    }


    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount() {

        return ResponseEntity.ok(notificationService.getUnreadCount());
    }


    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {

        notificationService.markAsRead(notificationId);

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {

        notificationService.markAllAsRead();

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter subscribe(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return notificationSseService.subscribe(user.getId());
    }

    @PostMapping("/test-event")
    public ResponseEntity<Void> testEvent() {
        notificationEventPublisher.publish(
                NotificationEvent.builder()
                        .userId(3L)
                        .organizationId(1L)
                        .title("RabbitMQ Test")
                        .message(
                                "This notification was sent through RabbitMQ."
                        )
                        .type(NotificationType.SYSTEM)
                        .build()
        );
        return ResponseEntity.accepted().build();
    }
}