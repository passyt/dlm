package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.PermitBuilderManager;
import com.derbysoft.nuke.dlm.PermitSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by passyt on 16-9-2.
 */
public class PermitManager implements IPermitManager {

    private Logger log = LoggerFactory.getLogger(PermitManager.class);
    private ConcurrentMap<String, IPermit> permits = new ConcurrentHashMap<>();

    @Override
    public boolean register(String permitId, String resourceName, PermitSpec spec) {
        log.debug("Register permit {} with spec {} by id {}", resourceName, spec, permitId);

        if (permits.putIfAbsent(permitId, buildPermit(resourceName, spec)) != null) {
            log.warn("An existing permit {} with id {}, not allow to register", permits.get(permitId), permitId);
            return false;
        }

        return true;
    }

    @Override
    public boolean unregister(String permitId) {
        permits.remove(permitId);
        return true;
    }

    @Override
    public boolean isExisting(String permitId) {
        return permits.containsKey(permitId);
    }

    @Override
    public IPermit getPermit(String permitId) {
        return permits.get(permitId);
    }

    protected IPermit buildPermit(String resourceName, PermitSpec spec) {
        IPermit permit = PermitBuilderManager.getInstance().buildPermit(resourceName, spec);
        if (permit == null) {
            throw new IllegalArgumentException("Permit not found by resource " + resourceName + " with spec " + spec);
        }

        return permit;
    }

}
