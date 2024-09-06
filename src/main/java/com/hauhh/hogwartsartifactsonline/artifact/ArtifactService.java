package com.hauhh.hogwartsartifactsonline.artifact;

import com.hauhh.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.hauhh.hogwartsartifactsonline.system.utils.IDUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final IDUtil IDUtil;

    public Artifact findById(String artifactID) {
        return this.artifactRepository.findById(artifactID).orElseThrow(() -> new ObjectNotFoundException("artifact",artifactID));
    }

    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    public Artifact addArtifact(Artifact artifact) {
        artifact.setArtifactID(String.valueOf(IDUtil.nextId()));
        return this.artifactRepository.save(artifact);
    }

    public Artifact updateArtifact(String artifactID, Artifact updateArtifact) {
        return this.artifactRepository.findById(artifactID)
                .map(oldArtifact -> {

                    oldArtifact.setName(updateArtifact.getName());
                    oldArtifact.setDescription(updateArtifact.getDescription());
                    oldArtifact.setImageUrl(updateArtifact.getImageUrl());

                    return this.artifactRepository.save(oldArtifact);

                }).orElseThrow(() -> new ObjectNotFoundException("artifact",artifactID));
    }

    public void deleteArtifact(String artifactID) {
        Artifact artifact = this.artifactRepository.findById(artifactID)
                .orElseThrow(() -> new ObjectNotFoundException("artifact",artifactID));
        this.artifactRepository.deleteById(artifact.getArtifactID());
    }
}

