package com.hauhh.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record WizardDTO(Integer wizardID,
                        @NotEmpty(message = "Name is required") String name,
                        Integer numberOfArtifacts) {



}
