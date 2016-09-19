package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-5.
 */
public class ReleaseResponse extends BaseResponse {

    public ReleaseResponse() {
    }

    public ReleaseResponse(String resourceId, Header header) {
        super(resourceId, header);
    }

    public ReleaseResponse(String resourceId, String errorMessage, Header header) {
        super(resourceId, errorMessage, header);
    }

}
