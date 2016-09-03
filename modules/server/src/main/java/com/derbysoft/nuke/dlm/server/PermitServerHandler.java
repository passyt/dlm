package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.model.Protobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by passyt on 16-9-2.
 */
public class PermitServerHandler extends SimpleChannelInboundHandler<Protobuf.AcquireRequest> {

    private static Logger log = LoggerFactory.getLogger(PermitServerHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("New connection from {}", ctx.channel().remoteAddress().toString());
    }

    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Protobuf.AcquireRequest acquireRequest) throws Exception {
        System.out.println(acquireRequest.getPermitId());
        channelHandlerContext.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Catch exception", cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("Disconnect from {}", ctx.channel().remoteAddress().toString());
    }
}
