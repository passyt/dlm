package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public class PermitResourceResponse extends BaseResponse {

    private boolean successful;

    public PermitResourceResponse() {
    }

    public PermitResourceResponse(String permitId, boolean successful) {
        super(permitId);
        this.successful = successful;
    }

    public PermitResourceResponse(String permitId, String errorMessage) {
        super(permitId, errorMessage);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermitResourceResponse)) return false;
        if (!super.equals(o)) return false;
        PermitResourceResponse that = (PermitResourceResponse) o;
        return successful == that.successful;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), successful);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("successful", successful)
                .toString();
    }
}
