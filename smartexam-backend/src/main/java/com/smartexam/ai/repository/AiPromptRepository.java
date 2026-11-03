package com.smartexam.ai.repository;

import com.smartexam.ai.entity.AiPrompt;
import com.smartexam.ai.enums.AiPromptType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AiPromptRepository
        extends JpaRepository<AiPrompt, Long> {

    Optional<AiPrompt> findByIdAndActiveTrue(Long id);

    Optional<AiPrompt> findByPromptTypeAndActiveTrue(
            AiPromptType promptType
    );

    List<AiPrompt> findAllByActiveTrue();

    boolean existsByNameIgnoreCase(String name);
}