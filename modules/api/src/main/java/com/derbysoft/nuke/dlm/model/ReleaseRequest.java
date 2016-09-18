package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.exception.PermitNotFoundException;

/**
 * Created by passyt on 16-9-5.
 */
public class ReleaseRequest extends BaseRequest<ReleaseResponse> {

    public ReleaseRequest() {
    }

    public ReleaseRequest(String resourceId) {
        super(resourceId);
    }

    @Override
    public ReleaseResponse newResponse() {
        return new ReleaseResponse();
    }

    @Override
    protected void doExecuteBy(IPermitManager manager, ReleaseResponse releaseResponse) {
        IPermit permit = manager.getPermit(getResourceId());
        if (permit == null) {
            throw new PermitNotFoundException(getResourceId());
        }

        permit.release();
    }

}
