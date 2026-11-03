package com.smartexam.studentdocument.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.student.entity.Student;
import com.smartexam.studentdocument.enums.StudentDocumentType;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(
        name = "student_documents",
        indexes = {
                @Index(
                        name = "idx_student_document_student",
                        columnList = "student_id"
                ),
                @Index(
                        name = "idx_student_document_type",
                        columnList = "document_type"
                ),
                @Index(
                        name = "idx_student_document_active",
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
public class StudentDocument extends BaseEntity {

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_student_document_student"
            )
    )
    private Student student;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "document_type",
            nullable = false,
            length = 50
    )
    private StudentDocumentType documentType;


    @Column(
            name = "document_name",
            nullable = false,
            length = 255
    )
    private String documentName;


    @Column(
            name = "original_file_name",
            nullable = false,
            length = 255
    )
    private String originalFileName;


    @Column(
            name = "stored_file_name",
            nullable = false,
            length = 255
    )
    private String storedFileName;


    @Column(
            name = "storage_path",
            nullable = false,
            length = 1000
    )
    private String storagePath;


    @Column(
            name = "content_type",
            length = 100
    )
    private String contentType;


    @Column(
            name = "file_size"
    )
    private Long fileSize;


    @Column(
            name = "document_number",
            length = 100
    )
    private String documentNumber;


    @Column(
            name = "remarks",
            length = 500
    )
    private String remarks;


    @Builder.Default
    @Column(
            nullable = false
    )
    private Boolean active = true;
}