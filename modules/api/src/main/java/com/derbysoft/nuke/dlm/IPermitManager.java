package com.derbysoft.nuke.dlm;

public interface IPermitManager {

    /**
     * @param resourceId the id of resource in register server
     * @param permitName permit name(class name or alias)
     * @param spec       permit spec
     * @return
     */
    boolean register(String resourceId, String permitName, PermitSpec spec);

    /**
     * @param resourceId the id of permit in register server
     * @return
     */
    boolean unregister(String resourceId);

    /**
     * @param resourceId
     * @return
     */
    boolean isExisting(String resourceId);

    /**
     * @param resourceId the id of permit in register server
     * @return
     */
    IPermit getPermit(String resourceId);

}
