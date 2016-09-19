package com.derbysoft.nuke.dlm.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by passyt on 16-9-19.
 */
public class Header {

    private String transactionId;

    public Header() {
    }

    public Header(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Header)) return false;
        Header header = (Header) o;
        return Objects.equal(transactionId, header.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transactionId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("transactionId", transactionId)
                .toString();
    }
}
