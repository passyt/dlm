package com.derbysoft.nuke.dlm.client.tcp;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.exception.PermitException;
import com.derbysoft.nuke.dlm.model.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import java.util.concurrent.TimeUnit;

/**
 * Created by DT219 on 2016-09-14.
 */
public class TcpPermit extends AbstractTcpPermitClient implements IPermit {

    private String resourceId;

    protected TcpPermit(String resourceId, ChannelFuture channelFuture, EventLoopGroup group) {
        super(channelFuture, group);
        this.resourceId = resourceId;
    }

    @Override
    public void acquire() {
        execute(new AcquireRequest(resourceId));
    }

    @Override
    public boolean tryAcquire() {
        return execute(new TryAcquireRequest(resourceId)).isSuccessful();
    }

    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return execute(new TryAcquireRequest(resourceId, timeout, unit)).isSuccessful();
    }

    @Override
    public void release() {
        execute(new ReleaseRequest(resourceId));
    }
}
