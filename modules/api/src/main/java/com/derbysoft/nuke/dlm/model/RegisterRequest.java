package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.PermitSpec;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-3.
 */
public class RegisterRequest extends BaseRequest<RegisterResponse> {

    private String permitResourceName;
    private PermitSpec permitSpec;

    public RegisterRequest() {
    }

    public RegisterRequest(String permitId, String permitResourceName, String permitSpec) {
        super(permitId);
        this.permitResourceName = permitResourceName;
        this.permitSpec = new PermitSpec(permitSpec);
    }

    @Override
    public RegisterResponse newResponse() {
        return new RegisterResponse();
    }

    @Override
    protected void doExecuteBy(IPermitManager manager, RegisterResponse registerResponse) {
        registerResponse.setSuccessful(manager.register(getPermitId(), getPermitResourceName(), getPermitSpec()));
    }

    public String getPermitResourceName() {
        return permitResourceName;
    }

    public void setPermitResourceName(String permitResourceName) {
        this.permitResourceName = permitResourceName;
    }

    public PermitSpec getPermitSpec() {
        return permitSpec;
    }

    public void setPermitSpec(PermitSpec permitSpec) {
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
                .add("permitId", permitId)
                .add("permitResourceName", permitResourceName)
                .add("permitSpec", permitSpec)
                .toString();
    }
}
