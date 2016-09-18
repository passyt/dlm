package com.derbysoft.nuke.dlm.model;

/**
 * Created by passyt on 16-9-3.
 */
public interface IPermitResponse {

    void setResourceId(String resourceId);

    void setErrorMessage(String errorMessage);

    String getErrorMessage();

}
