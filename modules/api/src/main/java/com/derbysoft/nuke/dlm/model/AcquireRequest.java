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

    public AcquireRequest(String permitId) {
        super(permitId);
    }

    @Override
    protected AcquireResponse newReponse() {
        return new AcquireResponse();
    }

    @Override
    protected void doExecuteBy(IPermitManager manager, AcquireResponse acquireResponse) {
        IPermit permit = manager.getPermit(getPermitId());
        if (permit == null) {
            throw new PermitNotFoundException(getPermitId());
        }

        permit.acquire();
    }
}
