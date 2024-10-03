package com.hauhh.converter.artifact;

import com.hauhh.model.Artifact;
import com.hauhh.dto.ArtifactDTO;
import com.hauhh.converter.wizard.WizardToWizardDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtifactToArtifactDTOConverter implements Converter<Artifact, ArtifactDTO> {

    private final WizardToWizardDTOConverter wizardDTOConverter;

    @Override
    public ArtifactDTO convert(Artifact source) {
        return ArtifactDTO.builder()
                .artifactID(source.getArtifactID())
                .description(source.getDescription())
                .imageUrl(source.getImageUrl())
                .name(source.getName())
                .owner(source.getOwner() != null
                        ? this.wizardDTOConverter.convert(source.getOwner())
                        : null)
                .build();
    }
}
