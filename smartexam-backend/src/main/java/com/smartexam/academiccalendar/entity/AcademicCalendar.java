package com.smartexam.academiccalendar.entity;

import com.smartexam.common.entity.BaseEntity;
import com.smartexam.academic.entity.AcademicSession;
import com.smartexam.organization.entity.Organization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "academic_calendars")
@Getter
@Setter
@NoArgsConstructor
public class AcademicCalendar extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academic_session_id", nullable = false)
    private AcademicSession academicSession;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(length = 1000)
    private String description;

    @Column(length = 200)
    private String location;

    @Column(name = "all_day", nullable = false)
    private Boolean allDay = true;

    @Column(nullable = false)
    private Boolean active = true;
}