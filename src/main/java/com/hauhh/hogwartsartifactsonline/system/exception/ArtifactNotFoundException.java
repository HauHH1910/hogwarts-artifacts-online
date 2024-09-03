package com.hauhh.hogwartsartifactsonline.system.exception;

public class ArtifactNotFoundException extends RuntimeException {

    public ArtifactNotFoundException(String artifactID) {
        super("Could not find artifact with ID: " + artifactID);
    }

}
