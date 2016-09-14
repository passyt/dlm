package com.derbysoft.nuke.dlm.client.tcp;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.exception.PermitException;
import com.derbysoft.nuke.dlm.model.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by DT219 on 2016-09-14.
 */
public class TcpPermit extends AbstractTcpPermitService implements IPermit {

    private String resourceId;

    public TcpPermit(String resourceId) {
        this.resourceId = resourceId;
    }


    public TcpPermit(String host, int port) throws InterruptedException {
        super(host, port);
    }

    public TcpPermit(String resourceId, String host, int port) throws InterruptedException {
        super(host, port);
        this.resourceId = resourceId;
    }

    @Override
    public void acquire() {
        try {
            AcquireResponse response = this.sendMessage(new AcquireRequest(resourceId));
            if (!("".equals(response.getErrorMessage())) && response.getErrorMessage() != null) {
                throw new PermitException(response.getErrorMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean tryAcquire() {
        try {
            TryAcquireResponse response = this.sendMessage(new TryAcquireRequest(resourceId));
            if (!response.isSuccessful()) {
                throw new PermitException(response.getErrorMessage());
            }
            return response.isSuccessful();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) {
        try {
            TryAcquireResponse response = this.sendMessage(new TryAcquireRequest(resourceId, timeout, unit));
            if (!response.isSuccessful()) {
                throw new PermitException(response.getErrorMessage());
            }
            return response.isSuccessful();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void release() {
        try {
            ReleaseResponse response = this.sendMessage(new ReleaseRequest(resourceId));
            if (!("".equals(response.getErrorMessage())) && response.getErrorMessage() != null) {
                throw new PermitException(response.getErrorMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
