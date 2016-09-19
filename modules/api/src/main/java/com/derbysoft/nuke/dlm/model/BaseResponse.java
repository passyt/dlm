package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public class BaseResponse implements IPermitResponse {

    protected String resourceId;
    protected String errorMessage;
    protected Header header;

    public BaseResponse() {
    }

    public BaseResponse(String resourceId, Header header) {
        this.resourceId = resourceId;
        this.header = header;
    }

    public BaseResponse(String resourceId, String errorMessage, Header header) {
        this.resourceId = resourceId;
        this.errorMessage = errorMessage;
        this.header = header;
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

    public Header getHeader() {
        return header;
    }

    @Override
    public BaseResponse setHeader(Header header) {
        this.header = header;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseResponse)) return false;
        BaseResponse that = (BaseResponse) o;
        return Objects.equal(resourceId, that.resourceId) &&
                Objects.equal(errorMessage, that.errorMessage) &&
                Objects.equal(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resourceId, errorMessage, header);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("header", header)
                .add("resourceId", resourceId)
                .add("errorMessage", errorMessage)
                .toString();
    }
}
