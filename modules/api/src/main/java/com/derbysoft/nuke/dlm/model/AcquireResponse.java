package com.derbysoft.nuke.dlm.model;

/**
 * Created by passyt on 16-9-3.
 */
public class AcquireResponse extends BaseResponse {
    public AcquireResponse() {
    }

    public AcquireResponse(String resourceId, Header header) {
        super(resourceId, header);
    }

    public AcquireResponse(String resourceId, String errorMessage, Header header) {
        super(resourceId, errorMessage, header);
    }
}
