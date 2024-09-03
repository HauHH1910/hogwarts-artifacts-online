package com.hauhh.hogwartsartifactsonline.wizard;

import lombok.Builder;

@Builder
public record WizardDTO(Integer wizardID,
                        String name,
                        Integer numberOfArtifacts) {



}
