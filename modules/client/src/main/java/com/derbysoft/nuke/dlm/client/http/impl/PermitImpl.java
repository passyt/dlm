package com.derbysoft.nuke.dlm.client.http.impl;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.client.utils.DataUtil;
import com.derbysoft.nuke.dlm.exception.PermitException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by suny on 2016-09-06.
 */
public class PermitImpl extends HttpPermitClient implements IPermit {

    private String resourceId;
    private Long timeOut;
    private TimeUnit timeUnit;

    public PermitImpl(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public void acquire() {
        while (true) {
            if (tryAcquire(timeOut, timeUnit)) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100L);
            } catch (InterruptedException e) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public boolean tryAcquire() {
        Map<String, String> response = DataUtil.transformerResponse(client.get(serverUrl + "/permit/" + resourceId + "/action/tryacquire"));
        if (response.get("errorMessage") != null) {
            throw new PermitException((String) response.get("errorMessage"));
        }
        return Boolean.valueOf(String.valueOf(response.get("successful")));
    }

    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) {
        Map<String, String> response = DataUtil.transformerResponse(client.get(serverUrl + "/permit/" + resourceId + "/action/tryacquire/timeout/" + timeout + "/timeunit/" + unit));
        if (response.get("errorMessage") != null) {
            throw new PermitException((String) response.get("errorMessage"));
        }
        return Boolean.valueOf(String.valueOf(response.get("successful")));
    }

    @Override
    public void release() {
        Map<String, String> response = DataUtil.transformerResponse(client.get(serverUrl + "/permit/" + resourceId + "/action/release"));
        if (response.get("errorMessage") != null) {
            throw new PermitException((String) response.get("errorMessage"));
        }
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
