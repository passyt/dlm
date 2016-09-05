package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-5.
 */
public class ReleaseResponse extends BaseResponse {

    public ReleaseResponse() {
    }

    public ReleaseResponse(String permitId) {
        super(permitId);
    }

    public ReleaseResponse(String permitId, String errorMessage) {
        super(permitId, errorMessage);
    }

}
