package com.derbysoft.nuke.dlm.server.initializer;

import com.derbysoft.nuke.dlm.server.handler.PermitServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by passyt on 16-9-4.
 */
public abstract class PermitServerInitializer extends ChannelInitializer<SocketChannel> {

    public static final String TYPE_TCP = "TCP";
    public static final String TYPE_HTTP = "HTTP";

    protected final PermitServerHandler handler;

    public PermitServerInitializer(PermitServerHandler handler) {
        this.handler = handler;
    }

    public abstract String getType();

    protected abstract void beforeInitChannel(SocketChannel socketChannel) throws Exception;

    protected abstract void afterInitChannel(SocketChannel socketChannel) throws Exception;

    protected final void initChannel(SocketChannel socketChannel) throws Exception {
        beforeInitChannel(socketChannel);
        socketChannel.pipeline().addLast("handler", handler);
        afterInitChannel(socketChannel);
    }

}
