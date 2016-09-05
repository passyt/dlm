package com.derbysoft.nuke.dlm.exception;

/**
 * Created by passyt on 16-9-4.
 */
public class PermitException extends RuntimeException {
    public PermitException() {
    }

    public PermitException(String message) {
        super(message);
    }

    public PermitException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermitException(Throwable cause) {
        super(cause);
    }
}
