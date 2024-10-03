package com.hauhh.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Wizard implements Serializable {

    @Id
    private Integer wizardID;

    private String name;

    /**
     * This line meaning that when saving 1 Wizard then all the Artifact that associated with Wizard will be save as well
     * -> Only need to save the Wizard
    **/
    @OneToMany(
            mappedBy = "owner",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<Artifact> artifacts = new ArrayList<>();

    public void addArtifact(Artifact artifacts) {
        artifacts.setOwner(this);
        this.artifacts.add(artifacts);
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }

    public void removeArtifact(Artifact artifactToBeAssigned) {
        artifactToBeAssigned.setOwner(null);
        this.artifacts.remove(artifactToBeAssigned);
    }

    public void removeAllArtifacts() {
        this.artifacts.forEach(artifact -> artifact.setOwner(null));
        this.artifacts = null;
    }
}
