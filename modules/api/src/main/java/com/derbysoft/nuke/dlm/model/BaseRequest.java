package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.exception.PermitException;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public abstract class BaseRequest<RS extends BaseResponse> implements IPermitRequest<RS> {

    protected String resourceId;

    public BaseRequest() {
    }

    protected abstract void doExecuteBy(IPermitManager manager, RS rs);

    @Override
    public RS executeBy(IPermitManager manager) {
        RS rs = newResponse();
        rs.setResourceId(resourceId);
        try {
            doExecuteBy(manager, rs);
        } catch (PermitException e) {
            throw e;
        } catch (Exception e) {
            throw new PermitException(e);
        }
        return rs;
    }

    public BaseRequest(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseRequest)) return false;
        BaseRequest that = (BaseRequest) o;
        return Objects.equal(resourceId, that.resourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resourceId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("resourceId", resourceId)
                .toString();
    }
}
