package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public class RegisterRequest extends BaseRequest {

    private String permitResourceName;
    private String permitSpec;

    public RegisterRequest() {
    }

    public RegisterRequest(String permitId, String permitResourceName, String permitSpec) {
        super(permitId);
        this.permitResourceName = permitResourceName;
        this.permitSpec = permitSpec;
    }

    public String getPermitResourceName() {
        return permitResourceName;
    }

    public void setPermitResourceName(String permitResourceName) {
        this.permitResourceName = permitResourceName;
    }

    public String getPermitSpec() {
        return permitSpec;
    }

    public void setPermitSpec(String permitSpec) {
        this.permitSpec = permitSpec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterRequest)) return false;
        if (!super.equals(o)) return false;
        RegisterRequest that = (RegisterRequest) o;
        return Objects.equal(permitResourceName, that.permitResourceName) &&
                Objects.equal(permitSpec, that.permitSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), permitResourceName, permitSpec);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("permitResourceName", permitResourceName)
                .add("permitSpec", permitSpec)
                .toString();
    }
}
