package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermitManager;

/**
 * Created by passyt on 16-9-3.
 */
public interface IPermitRequest<RS extends IPermitResponse> {

    RS executeBy(IPermitManager manager);

}
