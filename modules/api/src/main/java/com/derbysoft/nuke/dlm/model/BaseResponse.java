package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public class BaseResponse implements IPermitResponse {

    protected String resourceId;
    protected String errorMessage;

    public BaseResponse() {
    }

    public BaseResponse(String resourceId) {
        this.resourceId = resourceId;
    }

    public BaseResponse(String resourceId, String errorMessage) {
        this.resourceId = resourceId;
        this.errorMessage = errorMessage;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
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
        return Objects.equal(resourceId, that.resourceId) &&
                Objects.equal(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resourceId, errorMessage);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("resourceId", resourceId)
                .add("errorMessage", errorMessage)
                .toString();
    }
}
