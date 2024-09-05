package com.hauhh.hogwartsartifactsonline.system.exception;

public class WizardNotFoundException extends RuntimeException{

    public WizardNotFoundException(Integer wizardID) {
        super("Could not find wizard with ID: " + wizardID);
    }
}
