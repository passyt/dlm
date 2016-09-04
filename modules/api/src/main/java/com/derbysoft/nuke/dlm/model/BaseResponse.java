package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public class BaseResponse implements IPermitResponse {

    protected String permitId;
    protected String errorMessage;

    public BaseResponse() {
    }

    public BaseResponse(String permitId) {
        this.permitId = permitId;
    }

    public BaseResponse(String permitId, String errorMessage) {
        this.permitId = permitId;
        this.errorMessage = errorMessage;
    }

    public String getPermitId() {
        return permitId;
    }

    public void setPermitId(String permitId) {
        this.permitId = permitId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseResponse)) return false;
        BaseResponse that = (BaseResponse) o;
        return Objects.equal(permitId, that.permitId) &&
                Objects.equal(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(permitId, errorMessage);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("permitId", permitId)
                .add("errorMessage", errorMessage)
                .toString();
    }
}
