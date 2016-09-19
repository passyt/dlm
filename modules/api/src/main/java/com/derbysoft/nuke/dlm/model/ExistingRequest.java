package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermitManager;

/**
 * Created by passyt on 16-9-3.
 */
public class ExistingRequest extends BaseRequest<ExistingResponse> {
    public ExistingRequest() {
    }

    public ExistingRequest(String resourceId) {
        this(resourceId, newHeader());
    }

    public ExistingRequest(String resourceId, Header header) {
        super(resourceId, header);
    }

    @Override
    public ExistingResponse newResponse() {
        return new ExistingResponse();
    }

    @Override
    protected void doExecuteBy(IPermitManager manager, ExistingResponse existingResponse) {
        existingResponse.setExisting(manager.isExisting(getResourceId()));
    }
}
