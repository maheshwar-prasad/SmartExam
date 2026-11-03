package com.smartexam.gradingsystem.service;

import com.smartexam.gradingsystem.dto.GradingSystemRequest;
import com.smartexam.gradingsystem.dto.GradingSystemResponse;

import java.util.List;

public interface GradingSystemService {

    GradingSystemResponse create(GradingSystemRequest request);

    GradingSystemResponse update(Long id, GradingSystemRequest request);

    GradingSystemResponse getById(Long id);

    List<GradingSystemResponse> getAll();

    List<GradingSystemResponse> getByOrganization(Long organizationId);

    List<GradingSystemResponse> getActiveByOrganization(Long organizationId);

    GradingSystemResponse activate(Long id);

    GradingSystemResponse deactivate(Long id);

    void delete(Long id);
}