package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermitManager;

/**
 * Created by passyt on 16-9-3.
 */
public class UnRegisterRequest extends BaseRequest<UnRegisterResponse> {
    public UnRegisterRequest() {
    }

    public UnRegisterRequest(String resourceId) {
        this(resourceId, newHeader());
    }

    public UnRegisterRequest(String resourceId, Header header) {
        super(resourceId, header);
    }

    @Override
    public UnRegisterResponse newResponse() {
        return new UnRegisterResponse();
    }

    @Override
    protected void doExecuteBy(IPermitManager manager, UnRegisterResponse unRegisterResponse) {
        unRegisterResponse.setSuccessful(manager.unregister(getResourceId()));
    }
}
