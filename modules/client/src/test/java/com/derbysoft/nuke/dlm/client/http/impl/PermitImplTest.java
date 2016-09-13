package com.derbysoft.nuke.dlm.client.http.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by DT219 on 2016-09-06.
 */
public class PermitImplTest {

    private PermitImpl permitImpl;
    private static final String resourceId = "M21fAK47";

    @Before
    public void init() {
        permitImpl = new PermitImpl(resourceId);
        permitImpl.setServerUrl("http://127.0.0.1:8080");
        permitImpl.setTimeOut(100l);
        permitImpl.setTimeUnit(TimeUnit.MILLISECONDS);
    }

    @Test
    public void testAcquire() {
        permitImpl.acquire();
    }

    @Test
    public void testTryAcquire() {
        System.out.println(permitImpl.tryAcquire());
    }

    @Test
    public void testTryAcquireTimeOut() {
        System.out.println(permitImpl.tryAcquire(100l, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testRelease() {
        permitImpl.release();
    }
}
