package com.derbysoft.nuke.dlm.client.http;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.PermitSpec;

/**
 * Created by suny on 2016-09-06.
 */
public class HttpPermitManager extends AbstractHttpPermitClient implements IPermitManager {

    @Override
    public boolean register(String resourceId, String permitName, PermitSpec spec) {
        return (boolean) execute("/register/" + resourceId + "/permitname/" + permitName + "/spec/" + spec).get("successful");
    }

    @Override
    public boolean unregister(String resourceId) {
        return (boolean) execute("/unregister/" + resourceId).get("successful");
    }

    @Override
    public boolean isExisting(String resourceId) {
        return (boolean) execute("/existing/" + resourceId).get("existing");
    }

    @Override
    public IPermit getPermit(String resourceId) {
        return new HttpPermit(resourceId, this.client, this.serverUrl);
    }
}
