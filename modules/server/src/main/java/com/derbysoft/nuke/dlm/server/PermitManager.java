package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.PermitBuilderManager;
import com.derbysoft.nuke.dlm.PermitSpec;
import com.derbysoft.nuke.dlm.standalone.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by passyt on 16-9-2.
 */
@Component
public class PermitManager implements IPermitManager {

    private Logger log = LoggerFactory.getLogger(PermitManager.class);
    private ConcurrentMap<String, IPermit> permits = new ConcurrentHashMap<>();

    static {
        StandalonePermit.init();
    }

    @Override
    public boolean register(String resourceId, String permitName, PermitSpec spec) {
        log.debug("Register permit {} with spec {} by id {}", permitName, spec, resourceId);

        if (permits.putIfAbsent(resourceId, buildPermit(permitName, spec)) != null) {
            log.warn("An existing permit {} with id {}, not allow to register", permits.get(resourceId), resourceId);
            return false;
        }

        return true;
    }

    @Override
    public boolean unregister(String resourceId) {
        permits.remove(resourceId);
        return true;
    }

    @Override
    public boolean isExisting(String resourceId) {
        return permits.containsKey(resourceId);
    }

    @Override
    public IPermit getPermit(String resourceId) {
        return permits.get(resourceId);
    }

    public Map<String, IPermit> permits() {
        return ImmutableMap.copyOf(this.permits);
    }

    protected IPermit buildPermit(String permitName, PermitSpec spec) {
        IPermit permit = PermitBuilderManager.getInstance().buildPermit(permitName, spec);
        if (permit == null) {
            throw new IllegalArgumentException("Permit not found by permit " + permitName + " with spec " + spec);
        }

        return permit;
    }

}
