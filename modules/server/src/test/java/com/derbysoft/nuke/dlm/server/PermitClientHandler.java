package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.model.Protobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by passyt on 16-9-2.
 */
public class PermitClientHandler extends SimpleChannelInboundHandler<Protobuf.AcquireRequest> {

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Protobuf.AcquireRequest acquireRequest) throws Exception {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
