package com.hauhh.service;

import com.hauhh.model.Artifact;
import com.hauhh.repository.ArtifactRepository;
import com.hauhh.model.Wizard;
import com.hauhh.exception.ObjectNotFoundException;
import com.hauhh.repository.WizardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WizardService {

    private final WizardRepository wizardRepository;
    private final ArtifactRepository artifactRepository;

    public List<Wizard> getAllWizard() {
        return this.wizardRepository.findAll();
    }

    public Wizard getWizardByID(Integer wizardID) {
        return this.wizardRepository.findById(wizardID).orElseThrow(
                () -> new ObjectNotFoundException("wizard", wizardID)
        );
    }

    public Wizard saveWizard(Wizard wizard) {
        return this.wizardRepository.save(wizard);
    }

    public Wizard updateWizard(Integer wizardID, Wizard existingWizard) {
        return this.wizardRepository.findById(wizardID).map(
                wizard -> {
                    wizard.setWizardID(existingWizard.getWizardID());
                    wizard.setName(existingWizard.getName());
                    wizard.setArtifacts(existingWizard.getArtifacts());

                    return this.wizardRepository.save(wizard);
                }
        ).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardID));
    }

    public void deleteWizard(Integer wizardID) {
        this.wizardRepository.findById(wizardID).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardID));
        this.wizardRepository.deleteById(wizardID);
    }

    public void assignArtifactToWizard(Integer wizardID, String artifactID) {
        Wizard wizard = this.wizardRepository.findById(wizardID).orElseThrow(
                () -> new ObjectNotFoundException("wizard", wizardID));
        Artifact artifact = this.artifactRepository.findById(artifactID).orElseThrow(
                () -> new ObjectNotFoundException("artifact", artifactID));

        if (artifact.getOwner() != null) {
            artifact.getOwner().removeArtifact(artifact);
        }
        wizard.addArtifact(artifact);
    }
}
