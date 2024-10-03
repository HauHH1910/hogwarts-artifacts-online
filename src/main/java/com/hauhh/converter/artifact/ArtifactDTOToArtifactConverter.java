package com.hauhh.converter.artifact;

import com.hauhh.model.Artifact;
import com.hauhh.dto.ArtifactDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class ArtifactDTOToArtifactConverter implements Converter<ArtifactDTO, Artifact> {

    @Override
    public Artifact convert(ArtifactDTO source) {
        return Artifact.builder()
                .artifactID(source.artifactID())
                .name(source.name())
                .description(source.description())
                .imageUrl(source.imageUrl())
                .build();
    }
}
