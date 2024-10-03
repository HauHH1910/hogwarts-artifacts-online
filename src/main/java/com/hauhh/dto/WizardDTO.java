package com.hauhh.dto;

import lombok.Builder;

@Builder
public record WizardDTO(Integer wizardID,
                        String name,
                        Integer numberOfArtifacts) {



}
