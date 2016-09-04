package com.derbysoft.nuke.dlm;

import com.derbysoft.nuke.dlm.model.IPermitRequest;
import com.derbysoft.nuke.dlm.model.IPermitResponse;

/**
 * Created by passyt on 16-9-4.
 */
public interface IPermitService<RS extends IPermitResponse, RQ extends IPermitRequest<RS>> {

    RS execute(RQ request);

}
