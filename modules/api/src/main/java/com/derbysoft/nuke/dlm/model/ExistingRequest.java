package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermitManager;

/**
 * Created by passyt on 16-9-3.
 */
public class ExistingRequest extends BaseRequest<ExistingResponse> {
    public ExistingRequest() {
    }

    public ExistingRequest(String permitId) {
        super(permitId);
    }

    @Override
    protected ExistingResponse newReponse() {
        return new ExistingResponse();
    }

    @Override
    protected void doExecuteBy(IPermitManager manager, ExistingResponse existingResponse) {
        existingResponse.setExisting(manager.isExisting(getPermitId()));
    }
}
