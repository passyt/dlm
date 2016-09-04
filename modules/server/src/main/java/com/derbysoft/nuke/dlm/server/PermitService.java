package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.IPermitService;
import com.derbysoft.nuke.dlm.model.IPermitRequest;
import com.derbysoft.nuke.dlm.model.IPermitResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by passyt on 16-9-4.
 */
@Service
public class PermitService<RS extends IPermitResponse, RQ extends IPermitRequest<RS>> implements IPermitService<RS, RQ> {

    private final IPermitManager permitManager;

    @Autowired
    public PermitService(IPermitManager permitManager) {
        this.permitManager = permitManager;
    }

    @Override
    public RS execute(RQ request) {
        return request.executeBy(permitManager);
    }

}
