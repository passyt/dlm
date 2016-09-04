package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.PermitBuilderManager;
import com.derbysoft.nuke.dlm.PermitSpec;
import com.derbysoft.nuke.dlm.standalone.LeakyBucketPermit;
import com.derbysoft.nuke.dlm.standalone.ReentrantPermit;
import com.derbysoft.nuke.dlm.standalone.SemaphorePermit;
import com.derbysoft.nuke.dlm.standalone.TokenBucketPermit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        PermitBuilderManager.getInstance().registerPermitBuilder((spec) -> {
            return new LeakyBucketPermit(spec.required(true).doubleValueOf("permitsPerSecond"));
        }, LeakyBucketPermit.class.getSimpleName(), LeakyBucketPermit.class.getName());

//        PermitBuilderManager.getInstance().registerPermitBuilder((spec) -> {
//            return new ReentrantPermit();
//        }, ReentrantPermit.class.getSimpleName(), ReentrantPermit.class.getName());

        PermitBuilderManager.getInstance().registerPermitBuilder((spec) -> {
            return new SemaphorePermit(spec.required(true).intValueOf("total"));
        }, SemaphorePermit.class.getSimpleName(), SemaphorePermit.class.getName());

        PermitBuilderManager.getInstance().registerPermitBuilder((spec) -> {
            return new TokenBucketPermit(spec.required(true).doubleValueOf("permitsPerSecond"));
        }, TokenBucketPermit.class.getSimpleName(), TokenBucketPermit.class.getName());
    }

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
