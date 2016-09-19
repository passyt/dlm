package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public class TryAcquireResponse extends BaseResponse {

    private boolean successful;

    public TryAcquireResponse() {
    }

    public TryAcquireResponse(String resourceId, boolean successful, Header header) {
        super(resourceId, header);
        this.successful = successful;
    }

    public TryAcquireResponse(String resourceId, String errorMessage, Header header) {
        super(resourceId, errorMessage, header);
    }

    public TryAcquireResponse(String resourceId, String errorMessage, boolean successful, Header header) {
        super(resourceId, errorMessage, header);
        this.successful = successful;
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
        if (!(o instanceof TryAcquireResponse)) return false;
        if (!super.equals(o)) return false;
        TryAcquireResponse that = (TryAcquireResponse) o;
        return successful == that.successful;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), successful);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("header", header)
                .add("resourceId", resourceId)
                .add("errorMessage", errorMessage)
                .add("successful", successful)
                .toString();
    }
}
