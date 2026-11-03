package com.smartexam.organization.entity;

import com.smartexam.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class Organization extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 20)
    private String mobile;

    @Column(length = 300)
    private String address;

    @Column(name = "logo_path", length = 500)
    private String logoPath;

    @Column(name = "logo_content_type", length = 100)
    private String logoContentType;

    @Column(name = "logo_original_file_name", length = 255)
    private String logoOriginalFileName;
}