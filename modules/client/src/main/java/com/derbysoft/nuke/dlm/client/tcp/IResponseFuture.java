package com.derbysoft.nuke.dlm.client.tcp;

import java.util.concurrent.TimeUnit;

/**
 * Created by passyt on 16-9-18.
 */
public interface IResponseFuture<V> {

    /**
     * @return
     * @throws InterruptedException
     */
    V get() throws InterruptedException;

    /**
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    V get(long timeout, TimeUnit unit) throws InterruptedException;

}
