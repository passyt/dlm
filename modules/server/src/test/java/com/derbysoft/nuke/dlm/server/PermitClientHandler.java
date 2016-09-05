package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.model.Protobuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by passyt on 16-9-2.
 */
public class PermitClientHandler extends ChannelHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(PermitClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Receive response <<| {}", msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Catch excpetion", cause);
        ctx.close();
    }

}
