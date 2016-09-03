package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public abstract class BaseRequest implements IPermitRequest{

    protected String permitId;

    public BaseRequest() {
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
