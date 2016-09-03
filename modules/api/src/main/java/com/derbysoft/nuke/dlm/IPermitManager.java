package com.derbysoft.nuke.dlm;

public interface IPermitManager {

    /**
     * @param permitId     the id of permit in register server
     * @param resourceName permit resource name(class name or alias name)
     * @param spec         permit spec
     * @return
     */
    boolean register(String permitId, String resourceName, PermitSpec spec);

    /**
     * @param permitId the id of permit in register server
     * @return
     */
    boolean unregister(String permitId);

    /**
     * @param permitId
     * @return
     */
    boolean isExisting(String permitId);

    /**
     * @param permitId the id of permit in register server
     * @return
     */
    IPermit getPermit(String permitId);

}
