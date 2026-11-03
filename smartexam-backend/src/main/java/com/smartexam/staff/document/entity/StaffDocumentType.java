package com.smartexam.staff.document.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "staff_document_types",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_staff_document_type_code_org",
                        columnNames = {
                                "organization_id",
                                "code"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_staff_document_type_org",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_staff_document_type_active",
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
public class StaffDocumentType extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_staff_document_type_organization"
            )
    )
    private Organization organization;

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;

    @Column(
            name = "code",
            nullable = false,
            length = 50
    )
    private String code;

    @Column(
            name = "description",
            length = 500
    )
    private String description;

    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;
}