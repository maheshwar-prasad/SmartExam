package com.smartexam.ai.service.impl;

import com.smartexam.ai.service.AiPromptBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AiPromptBuilderImpl implements AiPromptBuilder {

    @Override
    public String build(
            String instruction,
            Map<String, Object> variables) {

        String prompt = instruction;

        if (variables == null || variables.isEmpty()) {
            return prompt;
        }

        for (Map.Entry<String, Object> entry : variables.entrySet()) {

            String placeholder =
                    "{{" + entry.getKey() + "}}";

            String value =
                    entry.getValue() == null
                            ? ""
                            : String.valueOf(entry.getValue());

            prompt = prompt.replace(
                    placeholder,
                    value
            );
        }
        return prompt;
    }
}