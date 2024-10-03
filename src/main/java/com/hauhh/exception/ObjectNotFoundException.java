package com.hauhh.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, String ID) {
        super("Could not find " + objectName + " with ID: " + ID);
    }

    public ObjectNotFoundException(String objectName, Integer ID) {
        super("Could not find " + objectName + " with ID: " + ID);
    }

}
