package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.exception.PermitNotFoundException;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.concurrent.TimeUnit;

/**
 * Created by passyt on 16-9-3.
 */
public class TryAcquireRequest extends BaseRequest<TryAcquireResponse> {

    private Long timeout;
    private TimeUnit timeUnit;

    public TryAcquireRequest() {
    }

    public TryAcquireRequest(String resourceId) {
        super(resourceId, newHeader());
    }

    public TryAcquireRequest(String resourceId, Header header) {
        super(resourceId, header);
    }

    public TryAcquireRequest(String resourceId, long timeout, TimeUnit unit) {
        this(resourceId, timeout, unit, newHeader());
    }

    public TryAcquireRequest(String resourceId, Long timeout, TimeUnit timeUnit, Header header) {
        super(resourceId, header);
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    public TryAcquireResponse newResponse() {
        return new TryAcquireResponse();
    }

    @Override
    protected void doExecuteBy(IPermitManager manager, TryAcquireResponse tryAcquireResponse) {
        IPermit permit = manager.getPermit(getResourceId());
        if (permit == null) {
            throw new PermitNotFoundException(getResourceId());
        }

        if (getTimeout() == null || getTimeout() == null) {
            tryAcquireResponse.setSuccessful(permit.tryAcquire());
        } else {
            tryAcquireResponse.setSuccessful(permit.tryAcquire(getTimeout(), getTimeUnit()));
        }
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TryAcquireRequest)) return false;
        if (!super.equals(o)) return false;
        TryAcquireRequest that = (TryAcquireRequest) o;
        return Objects.equal(timeout, that.timeout) &&
                timeUnit == that.timeUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), timeout, timeUnit);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("header", header)
                .add("resourceId", resourceId)
                .add("timeout", timeout)
                .add("timeUnit", timeUnit)
                .toString();
    }
}
