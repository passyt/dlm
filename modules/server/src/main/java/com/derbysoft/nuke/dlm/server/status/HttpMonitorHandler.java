package com.derbysoft.nuke.dlm.server.status;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by passyt on 16-9-19.
 */
public class HttpMonitorHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StatsCenter.getInstance().getHttpTrafficStats().increment();
        StatsCenter.getInstance().activeHttp(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        StatsCenter.getInstance().getHttpTrafficStats().decrement();
        StatsCenter.getInstance().inactiveHttp(ctx.channel());
    }

}