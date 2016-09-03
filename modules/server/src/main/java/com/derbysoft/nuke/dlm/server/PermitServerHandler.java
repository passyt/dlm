package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.model.Protobuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by passyt on 16-9-2.
 */
public class PermitServerHandler extends ChannelHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(PermitServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Protobuf.AcquireRequest request = (Protobuf.AcquireRequest) msg;
        log.info("Receive new request {} from {}", request, ctx.channel().remoteAddress().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Catch exception", cause);
    }

}
