package com.derbysoft.nuke.dlm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by passyt on 16-9-3.
 */
public class PermitBuilderManager {

    private static final Logger log = LoggerFactory.getLogger(PermitBuilderManager.class);
    private final ConcurrentMap<String, IPermitBuilder> builders = new ConcurrentHashMap<>();
    private static final PermitBuilderManager INSTANCE = new PermitBuilderManager();

    private PermitBuilderManager() {
    }

    public static PermitBuilderManager getInstance() {
        return INSTANCE;
    }

    public void registerPermitBuilder(IPermitBuilder builder, String... resourceNames) {
        for (String resourceName : resourceNames) {
            if (builders.putIfAbsent(resourceName, builder) != null) {
                log.warn("Existing permit builder by name {} and ignore to register", resourceName);
                return;
            }
        }
    }

    public IPermit getPermit(String resourceName, PermitSpec spec) {
        IPermitBuilder builder = builders.get(resourceName);
        if (builder == null) {
            return null;
        }

        return builder.build(spec);
    }

}
