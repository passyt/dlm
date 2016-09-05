package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public class ExistingResponse extends BaseResponse {

    private boolean existing;

    public ExistingResponse() {
    }

    public ExistingResponse(String permitId, boolean existing) {
        super(permitId);
        this.existing = existing;
    }

    public ExistingResponse(String permitId, String errorMessage, boolean existing) {
        super(permitId, errorMessage);
        this.existing = existing;
    }

    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExistingResponse)) return false;
        if (!super.equals(o)) return false;
        ExistingResponse that = (ExistingResponse) o;
        return existing == that.existing;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), existing);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("permitId", permitId)
                .add("errorMessage", errorMessage)
                .add("existing", existing)
                .toString();
    }
}
