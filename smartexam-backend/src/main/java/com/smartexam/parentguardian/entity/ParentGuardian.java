package com.smartexam.parentguardian.entity;

import com.smartexam.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "parent_guardians",
        indexes = {
                @Index(
                        name = "idx_parent_guardian_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_parent_guardian_mobile",
                        columnList = "mobile"
                ),
                @Index(
                        name = "idx_parent_guardian_email",
                        columnList = "email"
                ),
                @Index(
                        name = "idx_parent_guardian_active",
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
public class ParentGuardian extends BaseEntity {

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Column(name = "alternate_mobile", length = 20)
    private String alternateMobile;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "occupation", length = 150)
    private String occupation;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}