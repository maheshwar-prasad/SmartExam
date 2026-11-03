package com.smartexam.institution.settings.service;

import com.smartexam.institution.settings.dto.CreateInstitutionSettingsRequest;
import com.smartexam.institution.settings.dto.InstitutionSettingsResponse;
import com.smartexam.institution.settings.dto.UpdateInstitutionSettingsRequest;

public interface InstitutionSettingsService {

    InstitutionSettingsResponse create(
            CreateInstitutionSettingsRequest request
    );

    InstitutionSettingsResponse getByOrganizationId(
            Long organizationId
    );

    InstitutionSettingsResponse update(
            Long organizationId,
            UpdateInstitutionSettingsRequest request
    );

    void delete(Long organizationId);

    void deactivate(Long organizationId);

    void activate(Long organizationId);
}