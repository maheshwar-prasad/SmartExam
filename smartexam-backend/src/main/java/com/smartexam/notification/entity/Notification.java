package com.smartexam.notification.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.common.enums.NotificationType;
import com.smartexam.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(
                        name = "idx_notification_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_notification_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_notification_read",
                        columnList = "read_flag"
                ),
                @Index(
                        name = "idx_notification_active",
                        columnList = "active"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

    @Column(
            name = "organization_id",
            nullable = false
    )
    private Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_notification_user"
            )
    )
    private User user;

    @Column(
            nullable = false,
            length = 150
    )
    private String title;

    @Column(
            nullable = false,
            length = 1000
    )
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private NotificationType type;

    @Builder.Default
    @Column(
            name = "read_flag",
            nullable = false
    )
    private Boolean read = false;

    @Builder.Default
    @Column(
            nullable = false
    )
    private Boolean active = true;
}