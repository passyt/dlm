package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.exception.PermitException;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public abstract class BaseRequest<RS extends BaseResponse> implements IPermitRequest<RS> {

    protected String permitId;

    public BaseRequest() {
    }

    protected abstract void doExecuteBy(IPermitManager manager, RS rs);

    @Override
    public RS executeBy(IPermitManager manager) {
        RS rs = newResponse();
        rs.setPermitId(permitId);
        try {
            doExecuteBy(manager, rs);
        } catch (PermitException e) {
            throw e;
        } catch (Exception e) {
            throw new PermitException(e);
        }
        return rs;
    }

    public BaseRequest(String permitId) {
        this.permitId = permitId;
    }

    public String getPermitId() {
        return permitId;
    }

    public void setPermitId(String permitId) {
        this.permitId = permitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseRequest)) return false;
        BaseRequest that = (BaseRequest) o;
        return Objects.equal(permitId, that.permitId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(permitId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("permitId", permitId)
                .toString();
    }
}
