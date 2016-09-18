package com.derbysoft.nuke.dlm.client.tcp;

/**
 * Created by passyt on 16-9-18.
 */
interface SetEnableResponseFuture<V> extends IResponseFuture<V> {

    void set(V v);

}
