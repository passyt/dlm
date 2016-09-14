package com.derbysoft.nuke.dlm.client.tcp;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.PermitSpec;
import com.derbysoft.nuke.dlm.exception.PermitException;
import com.derbysoft.nuke.dlm.model.*;

/**
 * Created by DT219 on 2016-09-14.
 */
public class TcpPermitManager extends AbstractTcpPermitService implements IPermitManager {

    public TcpPermitManager(String host, int port) throws InterruptedException {
        super(host, port);
    }

    @Override
    public boolean register(String resourceId, String permitName, PermitSpec spec) {

        try {
            RegisterResponse response = this.sendMessage(new RegisterRequest(resourceId, permitName, spec.getSpecification()));
            if (!response.isSuccessful()) {
                throw new PermitException(response.getErrorMessage());
            }
            return response.isSuccessful();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean unregister(String resourceId) {
        try {
            UnRegisterResponse response = this.sendMessage(new UnRegisterRequest(resourceId));
            if (!response.isSuccessful()) {
                throw new PermitException(response.getErrorMessage());
            }
            return response.isSuccessful();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isExisting(String resourceId) {
        try {
            ExistingResponse response = this.sendMessage(new ExistingRequest(resourceId));
            if (!response.isExisting()) {
                throw new PermitException(response.getErrorMessage());
            }
            return response.isExisting();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IPermit getPermit(String resourceId) {
        return new TcpPermit(resourceId);
    }
}
