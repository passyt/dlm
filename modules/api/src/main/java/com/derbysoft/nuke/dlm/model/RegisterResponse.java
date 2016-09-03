package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public class RegisterResponse extends BaseResponse {

    private boolean successful;

    public RegisterResponse() {
    }

    public RegisterResponse(String permitId, boolean successful) {
        super(permitId);
        this.successful = successful;
    }

    public RegisterResponse(String permitId, String errorMessage) {
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
        if (!(o instanceof RegisterResponse)) return false;
        if (!super.equals(o)) return false;
        RegisterResponse that = (RegisterResponse) o;
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
