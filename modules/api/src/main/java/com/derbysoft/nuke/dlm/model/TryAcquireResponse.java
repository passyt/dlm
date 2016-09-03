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

    public TryAcquireResponse(String permitId, boolean successful) {
        super(permitId);
        this.successful = successful;
    }

    public TryAcquireResponse(String permitId, String errorMessage) {
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
                .add("successful", successful)
                .toString();
    }
}
