package com.derbysoft.nuke.dlm;

public interface IPermitResource {

    /**
     * @param permitId           the id of permit in register server
     * @param permitResourceName permit resource name(class name or alias name)
     * @param spec               permit spec
     * @return
     */
    boolean register(String permitId, String permitResourceName, PermitSpec spec);

    /**
     * @param permitId the id of permit in register server
     * @return
     */
    IPermit getPermit(String permitId);

}
