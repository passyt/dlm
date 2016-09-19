package com.derbysoft.nuke.dlm.server.status;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by passyt on 16-9-19.
 */
public class TcpMonitorHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StatsCenter.getInstance().getTcpTrafficStats().increment();
        StatsCenter.getInstance().activeTcp(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        StatsCenter.getInstance().getTcpTrafficStats().decrement();
        StatsCenter.getInstance().inactiveTcp(ctx.channel());
    }

}
