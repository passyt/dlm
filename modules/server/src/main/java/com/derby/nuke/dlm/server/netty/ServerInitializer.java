package com.derby.nuke.dlm.server.netty;

import com.derby.nuke.dlm.server.dispatcher.HandlerDispatcher;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 
 * @author Passyt
 *
 */
public abstract class ServerInitializer extends ChannelInitializer<SocketChannel> {

	protected int timeout = 3600;
	protected HandlerDispatcher handlerDispatcher;

	public void init() {
		new Thread(this.handlerDispatcher).start();
	}

	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		init(pipeline);
		pipeline.addLast("timeout", new ReadTimeoutHandler(this.timeout));
		pipeline.addLast("handler", new ServerAdapter(this.handlerDispatcher));
	}

	protected abstract void init(ChannelPipeline pipeline);

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

}