package com.smartexam.institution.settings.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "institution_settings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "organization_id",
            nullable = false,
            unique = true
    )
    private Long organizationId;

    @Column(
            name = "institution_name",
            nullable = false,
            length = 150
    )
    private String institutionName;

    @Column(
            name = "institution_code",
            nullable = false,
            unique = true,
            length = 50
    )
    private String institutionCode;

    @Column(
            name = "email",
            length = 150
    )
    private String email;

    @Column(
            name = "phone",
            length = 20
    )
    private String phone;

    @Column(
            name = "website",
            length = 255
    )
    private String website;

    @Column(
            name = "address",
            length = 500
    )
    private String address;

    @Column(
            name = "city",
            length = 100
    )
    private String city;

    @Column(
            name = "state",
            length = 100
    )
    private String state;

    @Column(
            name = "country",
            length = 100
    )
    private String country;

    @Column(
            name = "postal_code",
            length = 20
    )
    private String postalCode;

    @Column(
            name = "logo_url",
            length = 500
    )
    private String logoUrl;

    @Column(
            name = "active",
            nullable = false
    )
    @Builder.Default
    private boolean active = true;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at"
    )
    private LocalDateTime updatedAt;

}