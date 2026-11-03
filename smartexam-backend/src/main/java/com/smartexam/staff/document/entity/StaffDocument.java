package com.smartexam.staff.document.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.organization.entity.Organization;
import com.smartexam.staff.document.enums.DocumentVerificationStatus;
import com.smartexam.teacher.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "staff_documents",
        indexes = {
                @Index(
                        name = "idx_staff_document_org",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_staff_document_teacher",
                        columnList = "teacher_id"
                ),
                @Index(
                        name = "idx_staff_document_type",
                        columnList = "document_type_id"
                ),
                @Index(
                        name = "idx_staff_document_status",
                        columnList = "verification_status"
                ),
                @Index(
                        name = "idx_staff_document_expiry",
                        columnList = "expiry_date"
                ),
                @Index(
                        name = "idx_staff_document_active",
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
public class StaffDocument extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "organization_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_staff_document_organization"
            )
    )
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "teacher_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_staff_document_teacher"
            )
    )
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "document_type_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_staff_document_type"
            )
    )
    private StaffDocumentType documentType;

    @Column(
            name = "document_number",
            length = 100
    )
    private String documentNumber;

    @Column(
            name = "document_name",
            nullable = false,
            length = 255
    )
    private String documentName;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(
            name = "description",
            length = 1000
    )
    private String description;

    @Column(
            name = "file_name",
            length = 255
    )
    private String fileName;

    @Column(
            name = "stored_file_name",
            length = 255
    )
    private String storedFileName;

    @Column(
            name = "file_path",
            length = 1000
    )
    private String filePath;

    @Column(
            name = "file_type",
            length = 100
    )
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "verification_status",
            nullable = false,
            length = 30
    )
    @Builder.Default
    private DocumentVerificationStatus verificationStatus =
            DocumentVerificationStatus.PENDING;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(
            name = "verification_comments",
            length = 1000
    )
    private String verificationComments;

    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;

    @PrePersist
    protected void onCreate() {

        if (verificationStatus == null) {
            verificationStatus =
                    DocumentVerificationStatus.PENDING;
        }

        if (active == null) {
            active = true;
        }
    }
}