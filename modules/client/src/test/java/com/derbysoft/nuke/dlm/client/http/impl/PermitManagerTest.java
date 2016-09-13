package com.derbysoft.nuke.dlm.client.http.impl;

import com.derbysoft.nuke.dlm.PermitSpec;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class PermitManagerTest {

    private PermitManagerImpl permitManager;
    private static final String resourceId = "M21AK47";
    private static final String leakyBucketPermit = "LeakyBucketPermit";
    private static final String reentrantPermit = "ReentrantPermit";
    private static final String semaphorePermit = "SemaphorePermit";
    private static final String standalonePermit = "StandalonePermit";
    private static final String tokenBucketPermit = "TokenBucketPermit";

    @Before
    public void init() {
        permitManager = new PermitManagerImpl();
        permitManager.setServerUrl("http://127.0.0.1:8080");
    }

    @Test
    public void testRegister() {
        System.out.println(permitManager.register(resourceId, semaphorePermit, createPermitSpec()));
    }

    @Test
    public void testUnregister() {
        System.out.println(permitManager.unregister(resourceId));
    }

    @Test
    public void testIsExisting() {
        System.out.println(permitManager.isExisting(resourceId));
    }

    private PermitSpec createPermitSpec() {
        return new PermitSpec("timeOut=3,total=5");
    }

}
