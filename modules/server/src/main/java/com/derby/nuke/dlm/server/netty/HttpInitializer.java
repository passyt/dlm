package com.derby.nuke.dlm.server.netty;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpInitializer extends ServerInitializer {

	@Override
	protected void init(ChannelPipeline pipeline) {
		pipeline.addLast("codec-http", new HttpServerCodec());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
	}

}
