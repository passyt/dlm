package com.derby.nuke.dlm.server.netty;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class SocketInitalizer extends ServerInitializer {

	@Override
	protected void init(ChannelPipeline pipeline) {
		pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
		pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
	}

}
