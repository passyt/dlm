package com.derbysoft.nuke.dlm.model;

/**
 * Created by passyt on 16-9-3.
 */
public class AcquireResponse extends BaseResponse {
    public AcquireResponse() {
    }

    public AcquireResponse(String permitId) {
        super(permitId);
    }

    public AcquireResponse(String permitId, String errorMessage) {
        super(permitId, errorMessage);
    }
}
