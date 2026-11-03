package com.smartexam.ai.entity;

import com.smartexam.ai.enums.AiPromptType;
import com.smartexam.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "ai_prompts",
        indexes = {
                @Index(name = "idx_ai_prompt_type", columnList = "prompt_type"),
                @Index(name = "idx_ai_prompt_active", columnList = "active")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AiPrompt extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "prompt_type", nullable = false, length = 50)
    private AiPromptType promptType;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "system_prompt", nullable = false, columnDefinition = "TEXT")
    private String systemPrompt;

    @Column(name = "description", length = 500)
    private String description;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;
}