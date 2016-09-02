package com.derbysoft.nuke.dlm;

public interface IPermitResource {

    /**
     * @param permitId        the id of permit in register server
     * @param permitClassName permit class name
     * @param arguments       arguments in constructor
     * @return
     */
    boolean register(String permitId, String permitClassName, Object... arguments);

    /**
     * @param permitId the id of permit in register server
     * @return
     */
    IPermit getPermit(String permitId);

}
