package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.exception.PermitNotFoundException;

/**
 * Created by passyt on 16-9-3.
 */
public class AcquireRequest extends BaseRequest<AcquireResponse> {

    public AcquireRequest() {
    }

    public AcquireRequest(String resourceId) {
        this(resourceId, newHeader());
    }

    public AcquireRequest(String resourceId, Header header) {
        super(resourceId, header);
    }

    @Override
    public AcquireResponse newResponse() {
        return new AcquireResponse();
    }

    @Override
    protected void doExecuteBy(IPermitManager manager, AcquireResponse acquireResponse) {
        IPermit permit = manager.getPermit(getResourceId());
        if (permit == null) {
            throw new PermitNotFoundException(getResourceId());
        }

        permit.acquire();
    }
}
