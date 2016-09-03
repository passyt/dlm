package com.derbysoft.nuke.dlm.standalone;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitBuilder;
import com.derbysoft.nuke.dlm.PermitManager;
import com.derbysoft.nuke.dlm.PermitSpec;

/**
 * @author Passyt
 */
public abstract class StandalonePermit implements IPermit {

    static {
        PermitManager.getInstance().registerPermitBuilder((spec) -> {
            return new LeakyBucketPermit(spec.required(true).doubleValueOf("permitsPerSecond"));
        }, LeakyBucketPermit.class.getSimpleName(), LeakyBucketPermit.class.getName());

        PermitManager.getInstance().registerPermitBuilder((spec) -> {
            return new ReentrantPermit();
        }, ReentrantPermit.class.getSimpleName(), ReentrantPermit.class.getName());

        PermitManager.getInstance().registerPermitBuilder((spec) -> {
            return new SemaphorePermit(spec.required(true).intValueOf("total"));
        }, SemaphorePermit.class.getSimpleName(), SemaphorePermit.class.getName());

        PermitManager.getInstance().registerPermitBuilder((spec) -> {
            return new TokenBucketPermit(spec.required(true).doubleValueOf("permitsPerSecond"));
        }, TokenBucketPermit.class.getSimpleName(), TokenBucketPermit.class.getName());
    }

}
