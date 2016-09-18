package com.derbysoft.nuke.dlm.client.integration.tcp

import com.derbysoft.nuke.dlm.PermitSpec
import com.derbysoft.nuke.dlm.client.tcp.TcpPermitManager
import org.junit.Before
import org.junit.Test

import java.util.concurrent.TimeUnit

/**
 * Created by passyt on 16-9-18.
 */
class TcpPermitTest {

    def TcpPermitManager manager;
    def resourceId = "123";

    @Before
    def void startup() {
        manager = new TcpPermitManager("127.0.0.1", 8081);
    }

    def void shutdown() {
        manager.shutdown();
    }

    @Test
    def void register() {
        println manager.register(resourceId, "LeakyBucketPermit", new PermitSpec("permitsPerSecond=10"))
        println manager.register(resourceId, "LeakyBucketPermit", new PermitSpec("permitsPerSecond=10"))
        println manager.register(resourceId, "LeakyBucketPermit", new PermitSpec("permitsPerSecond=10"))
    }

    @Test
    def void unregister() {
        println manager.unregister(resourceId);
    }

    @Test
    def void isExisting() {
        println manager.isExisting(resourceId);
    }

    @Test
    def void acquire() {
        manager.getPermit(resourceId).acquire();
    }

    @Test
    def void tryAcquire() {
        println manager.getPermit(resourceId).tryAcquire();
    }

    @Test
    def void tryAcquireWithTimeout() {
        println manager.getPermit(resourceId).tryAcquire(100l, TimeUnit.MILLISECONDS);
    }

    @Test
    def void release() {
        manager.getPermit(resourceId).release();
    }

}
