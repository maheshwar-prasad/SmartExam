package com.smartexam.institution.program.service.impl;

import com.smartexam.institution.program.dto.ProgramCreateRequest;
import com.smartexam.institution.program.dto.ProgramResponse;
import com.smartexam.institution.program.dto.ProgramUpdateRequest;
import com.smartexam.institution.program.entity.Program;
import com.smartexam.institution.program.exception.ProgramNotFoundException;
import com.smartexam.institution.program.repository.ProgramRepository;
import com.smartexam.institution.program.service.ProgramService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProgramResponse> getAllPrograms(
            Long organizationId
    ) {
        return programRepository
                .findByOrganizationIdOrderByNameAsc(organizationId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramResponse getProgramById(
            Long id
    ) {
        Program program = findProgram(id);

        return toResponse(program);
    }

    @Override
    public ProgramResponse createProgram(
            ProgramCreateRequest request
    ) {
        String code = generateProgramCode(
                request.name()
        );

        code = makeUniqueCode(
                request.organizationId(),
                code
        );

        Program program = Program.builder()
                .organizationId(request.organizationId())
                .departmentId(request.departmentId())
                .name(request.name().trim())
                .code(code)
                .programType(request.programType())
                .duration(request.duration())
                .durationUnit(request.durationUnit())
                .description(request.description())
                .active(true)
                .build();

        Program savedProgram =
                programRepository.save(program);

        return toResponse(savedProgram);
    }

    @Override
    public ProgramResponse updateProgram(
            Long id,
            ProgramUpdateRequest request
    ) {
        Program program = findProgram(id);

        String generatedCode =
                generateProgramCode(request.name());

        String code = generatedCode;

        if (
                !generatedCode.equalsIgnoreCase(
                        program.getCode()
                )
        ) {
            code = makeUniqueCodeForUpdate(
                    request.organizationId(),
                    generatedCode,
                    id
            );
        }

        program.setOrganizationId(
                request.organizationId()
        );

        program.setDepartmentId(
                request.departmentId()
        );

        program.setName(
                request.name().trim()
        );

        program.setCode(code);

        program.setProgramType(
                request.programType()
        );

        program.setDuration(
                request.duration()
        );

        program.setDurationUnit(
                request.durationUnit()
        );

        program.setDescription(
                request.description()
        );

        Program updatedProgram =
                programRepository.save(program);

        return toResponse(updatedProgram);
    }

    @Override
    public void activateProgram(
            Long id
    ) {
        Program program = findProgram(id);

        program.setActive(true);

        programRepository.save(program);
    }

    @Override
    public void deactivateProgram(
            Long id
    ) {
        Program program = findProgram(id);

        program.setActive(false);

        programRepository.save(program);
    }

    @Override
    public void deleteProgram(
            Long id
    ) {
        Program program = findProgram(id);

        programRepository.delete(program);
    }

    private Program findProgram(
            Long id
    ) {
        return programRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProgramNotFoundException(id)
                );
    }

    private ProgramResponse toResponse(
            Program program
    ) {
        return new ProgramResponse(
                program.getId(),
                program.getOrganizationId(),
                program.getDepartmentId(),
                program.getName(),
                program.getCode(),
                program.getProgramType(),
                program.getDuration(),
                program.getDurationUnit(),
                program.getDescription(),
                program.getActive()
        );
    }

    private String generateProgramCode(
            String programName
    ) {
        if (
                programName == null ||
                        programName.isBlank()
        ) {
            return "PROGRAM";
        }

        String normalizedName =
                programName
                        .toUpperCase(Locale.ROOT)
                        .replaceAll("[^A-Z0-9 ]", " ")
                        .replaceAll("\\s+", " ")
                        .trim();

        String[] words =
                normalizedName.split(" ");

        StringBuilder code =
                new StringBuilder();

        for (String word : words) {
            if (!word.isBlank()) {
                code.append(word.charAt(0));
            }
        }

        if (code.length() < 2) {
            String compactCode =
                    normalizedName.replaceAll(
                            "[^A-Z0-9]",
                            ""
                    );

            code = new StringBuilder(
                    compactCode.substring(
                            0,
                            Math.min(
                                    compactCode.length(),
                                    6
                            )
                    )
            );
        }

        if (code.isEmpty()) {
            return "PROGRAM";
        }

        return code
                .toString()
                .substring(
                        0,
                        Math.min(
                                code.length(),
                                20
                        )
                );
    }

    private String makeUniqueCode(
            Long organizationId,
            String baseCode
    ) {
        String code = baseCode;
        int counter = 2;

        while (
                programRepository.existsByOrganizationIdAndCode(
                        organizationId,
                        code
                )
        ) {
            code = baseCode + counter;
            counter++;
        }

        return code;
    }

    private String makeUniqueCodeForUpdate(
            Long organizationId,
            String baseCode,
            Long id
    ) {
        String code = baseCode;
        int counter = 2;

        while (
                programRepository
                        .existsByOrganizationIdAndCodeAndIdNot(
                                organizationId,
                                code,
                                id
                        )
        ) {
            code = baseCode + counter;
            counter++;
        }

        return code;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProgramResponse> getAllPrograms(
            Long organizationId,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection, sortBy);

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        return programRepository
                .findByOrganizationId(
                        organizationId,
                        pageable
                )
                .map(this::mapToResponse);
    }

    private ProgramResponse mapToResponse(Program program) {

        return new ProgramResponse(
                program.getId(),
                program.getOrganizationId(),
                program.getDepartmentId(),
                program.getName(),
                program.getCode(),
                program.getProgramType(),
                program.getDuration(),
                program.getDurationUnit(),
                program.getDescription(),
                program.getActive()
        );
    }
}