package com.smartexam.student.entity;

import com.smartexam.classroom.entity.ClassRoom;
import com.smartexam.common.entity.BaseEntity;
import com.smartexam.common.enums.Gender;
import com.smartexam.organization.entity.Organization;
import com.smartexam.section.entity.Section;
import com.smartexam.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(
        name = "students",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_student_code",
                        columnNames = "student_code"
                ),

                @UniqueConstraint(
                        name = "uk_admission_no",
                        columnNames = "admission_no"
                )

        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Student extends BaseEntity {

    @Column(name = "student_code",
            nullable = false,
            length = 30)
    private String studentCode;

    @Column(name = "admission_no",
            nullable = false,
            length = 30)
    private String admissionNo;

    @Column(name = "roll_no",
            length = 30)
    private String rollNo;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "first_name",
            nullable = false,
            length = 100)
    private String firstName;

    @Column(name = "last_name",
            nullable = false,
            length = 100)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender",
            length = 20)
    private Gender gender;

    @Column(name = "blood_group",
            length = 20)
    private String bloodGroup;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "mobile",
            length = 20)
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "address",
            length = 500)
    private String address;

    @Column(name = "photo_url")
    private String photoUrl;

    /**
     * Organization
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "organization_id",
            nullable = false
    )
    private Organization organization;

    /**
     * User Account
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * Class
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "class_id",
            nullable = false
    )
    private ClassRoom classRoom;

    /**
     * Section
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "section_id",
            nullable = false
    )
    private Section section;

}