package com.derbysoft.nuke.dlm.exception;

/**
 * Created by passyt on 16-9-4.
 */
public class PermitNotFoundException extends PermitException {

    private String permitId;

    public PermitNotFoundException(String permitId) {
        super("Permit not found by " + permitId);
        this.permitId = permitId;
    }

    public String getPermitId() {
        return permitId;
    }
}
