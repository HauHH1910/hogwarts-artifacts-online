package com.hauhh.hogwartsartifactsonline.artifact;

import com.hauhh.hogwartsartifactsonline.wizard.WizardDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record ArtifactDTO(String artifactID,
                            @NotEmpty(message = "Name is required") String name,
                            @NotEmpty(message = "Description is required") String description,
                            @NotEmpty(message = "ImageURL is required") String imageUrl,
                            WizardDTO owner) {



}
