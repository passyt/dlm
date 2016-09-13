package com.derbysoft.nuke.dlm.client.http;

import com.derby.nuke.common.ws.client.SimpleClient;
import com.derbysoft.nuke.dlm.IPermit;

import java.util.concurrent.TimeUnit;

/**
 * Created by suny on 2016-09-06.
 */
public class HttpPermit extends AbstractHttpPermitClient implements IPermit {

    private final String resourceId;

    private Long acquireTimeout = 10L;

    protected HttpPermit(String resourceId) {
        this.resourceId = resourceId;
    }

    protected HttpPermit(String resourceId, String serverUrl) {
        this.resourceId = resourceId;
        super.serverUrl = serverUrl;
    }

    protected HttpPermit(String resourceId, SimpleClient client, String serverUrl) {
        this.resourceId = resourceId;
        super.client = client;
        super.serverUrl = serverUrl;
    }

    @Override
    public void acquire() {
        while (true) {
            if (tryAcquire(acquireTimeout, TimeUnit.MILLISECONDS)) {
                break;
            }
        }
    }

    @Override
    public boolean tryAcquire() {
        return (boolean) execute(toUri("/permit/%s/action/tryacquire", resourceId)).get("successful");
    }

    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return (boolean) execute(toUri("/permit/%s/action/tryacquire/acquireTimeout/%s/timeunit/%s", resourceId, timeout, unit)).get("successful");
    }

    @Override
    public void release() {
        execute(toUri("/permit/%s/action/release", resourceId));
    }

    public void setAcquireTimeout(Long acquireTimeout) {
        this.acquireTimeout = acquireTimeout;
    }

}
