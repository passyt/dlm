package com.derbysoft.nuke.dlm.model;

import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.exception.PermitException;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.UUID;

/**
 * Created by passyt on 16-9-3.
 */
public abstract class BaseRequest<RS extends BaseResponse> implements IPermitRequest<RS> {

    protected String resourceId;
    protected Header header;

    public BaseRequest() {
    }

    public BaseRequest(String resourceId, Header header) {
        this.resourceId = resourceId;
        this.header = header;
    }

    protected abstract void doExecuteBy(IPermitManager manager, RS rs);

    protected static Header newHeader() {
        return new Header(UUID.randomUUID().toString().replace("-", ""));
    }

    @Override
    public RS executeBy(IPermitManager manager) {
        RS rs = newResponse();
        rs.setResourceId(resourceId);
        rs.setHeader(getHeader());
        try {
            doExecuteBy(manager, rs);
        } catch (PermitException e) {
            throw e;
        } catch (Exception e) {
            throw new PermitException(e);
        }
        return rs;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseRequest)) return false;
        BaseRequest<?> that = (BaseRequest<?>) o;
        return Objects.equal(resourceId, that.resourceId) &&
                Objects.equal(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resourceId, header);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("resourceId", resourceId)
                .add("header", header)
                .toString();
    }

}
