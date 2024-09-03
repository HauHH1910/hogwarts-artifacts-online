package com.hauhh.hogwartsartifactsonline.artifact.conveter;

import com.hauhh.hogwartsartifactsonline.artifact.Artifact;
import com.hauhh.hogwartsartifactsonline.artifact.ArtifactDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class ArtifactDTOTOArtifactConverter implements Converter<ArtifactDTO, Artifact> {

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
