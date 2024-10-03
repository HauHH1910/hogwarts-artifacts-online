package com.hauhh.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Artifact implements Serializable {

    @Id
    private String artifactID;

    private String name;

    private String description;

    private String imageUrl;

    @ManyToOne
    private Wizard owner;

}
