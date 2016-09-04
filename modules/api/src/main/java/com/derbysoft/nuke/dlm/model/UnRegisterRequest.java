package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermitManager;

/**
 * Created by passyt on 16-9-3.
 */
public class UnRegisterRequest extends BaseRequest<UnRegisterResponse> {
    public UnRegisterRequest() {
    }

    public UnRegisterRequest(String permitId) {
        super(permitId);
    }

    @Override
    protected UnRegisterResponse newReponse() {
        return new UnRegisterResponse();
    }

    @Override
    protected void doExecuteBy(IPermitManager manager, UnRegisterResponse unRegisterResponse) {
        unRegisterResponse.setSuccessful(manager.unregister(getPermitId()));
    }
}
